package pl.kruczek.cron.definition;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;

@UtilityClass
class ParserUtil {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum Unit {
        MINUTES(0, 60),
        HOURS(0, 23),
        DAY_OF_MONTH(1, 31),
        MONTHS(1, 12),
        DAY_OF_WEEK(1, 7);

        private final int minAllowed;
        private final int maxAllowed;
    }

    Set<Integer> parse(String args, Unit unit) {
        return Try.of(() -> parseDefinitionToSet(args, unit))
                .filter(
                        parsedValues -> validate(parsedValues, unit),
                        () -> new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name())
                ).get();
    }

    private static Set<Integer> parseDefinitionToSet(String args, Unit unit) {
        final int argsLength = args.length();
        final boolean hasOneChar = argsLength == 1;
        final boolean hasMoreThanOneChar = argsLength > 1;

        final String firstChar = String.valueOf(args.charAt(0));
        final boolean isFirstCharANumber = NumberUtils.isParsable(firstChar);

        final boolean hasMoreThanOneGroupInDefinition = args.contains(",");

        if (hasOneChar && !isFirstCharANumber) {
            // single special chars (like *)
            return parseOneSpecialCharacter(firstChar, unit);
        } else if (hasMoreThanOneChar && !isFirstCharANumber) {
            // not allowed (like -21 or aliases @yearly)
            throw new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name());
        } else if (hasMoreThanOneGroupInDefinition) {
            // groups
            return parseGroups(args, unit);
        } else {
            // from-to or single
            return parseFromToValueOrSingleValue(args, unit);
        }
    }

    private static Set<Integer> parseOneSpecialCharacter(String specialCharacter, Unit unit) {
        return switch (specialCharacter) {
            case "*" -> Stream.range(unit.minAllowed, unit.maxAllowed)
                    .toSet();
            default -> throw new IllegalArgumentException("Not supported char: " + specialCharacter);
        };
    }

    private static Set<Integer> parseGroups(String args, Unit unit) {
        final String[] groups = args.split("-");
        return Stream.of(groups)
                .map(group -> parse(group, unit))
                .flatMap(Set::toStream)
                .toSet();
    }

    private static Set<Integer> parseFromToValueOrSingleValue(String args, Unit unit) {
        final boolean hasFromToValue = args.contains("-");

        if (hasFromToValue) {
            final String[] fromAndToValues = args.split("-");
            if (fromAndToValues.length != 2) {
                throw new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name());
            }

            final int from = Integer.parseInt(fromAndToValues[0]);
            final int to = Integer.parseInt(fromAndToValues[1]);
            return Stream.range(from, to)
                    .toSet();
        } else {
            return HashSet.of(Integer.parseInt(args));
        }
    }

    private static boolean validate(Set<Integer> parsedValues, Unit unit) {
        return parsedValues
                .filter(value -> value < unit.minAllowed || value > unit.maxAllowed)
                .isEmpty();
    }
}
