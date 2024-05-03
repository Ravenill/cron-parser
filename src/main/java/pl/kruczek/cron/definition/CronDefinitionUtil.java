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
class CronDefinitionUtil {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum Unit {
        MINUTES(0, 59),
        HOURS(0, 23),
        DAY_OF_MONTH(1, 31),
        MONTHS(1, 12),
        DAY_OF_WEEK(1, 7);

        private final int minAllowed;
        private final int maxAllowed;
    }

    Set<Integer> parseArgs(String args, Unit unit) {
        return Try.of(() -> parseArgsToSet(args, unit)) // Set provide uniqueness
                .filter(
                        parsedValues -> validate(parsedValues, unit),
                        () -> new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name())
                )
                .map(Set::toSortedSet)
                .get();
    }

    private Set<Integer> parseArgsToSet(String args, Unit unit) {
        final int argsLength = args.length();
        final boolean hasOneChar = argsLength == 1;
        final boolean hasMoreThanOneChar = argsLength > 1;

        final String firstChar = String.valueOf(args.charAt(0));
        final boolean isFirstCharANumber = NumberUtils.isParsable(firstChar);

        final boolean hasMoreThanOneGroupInDefinition = args.contains(",");
        final boolean hasFromToValue = args.contains("-");

        if (hasOneChar && !isFirstCharANumber) {
            // single special chars (like *)
            return parseOneSpecialCharacter(firstChar, unit);
        } else if (hasMoreThanOneChar && !isFirstCharANumber) {
            // not allowed (like -21 or aliases @yearly)
            throw new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name());
        } else if (hasMoreThanOneGroupInDefinition) {
            // groups (like 1,2,3,10-20)
            return parseGroups(args, unit);
        } else if (hasFromToValue) {
            // from-to (like 10-20)
            return parseFromToValue(args, unit);
        } else {
            // single (like 2)
            return parseSingleValue(args);
        }
    }

    private Set<Integer> parseOneSpecialCharacter(String specialCharacter, Unit unit) {
        return switch (specialCharacter) {
            case "*" -> Stream.rangeClosed(unit.minAllowed, unit.maxAllowed)
                    .toSet();
            default -> throw new IllegalArgumentException("Not supported char: " + specialCharacter);
        };
    }

    private Set<Integer> parseGroups(String args, Unit unit) {
        final String[] groups = args.split(",");
        if (groups.length <= 1) {
            // not allowed (like , or 3,)
            throw new IllegalArgumentException("Incorrect group definition: " + args);
        }

        return Stream.of(groups)
                .map(group -> parseArgsToSet(group, unit))
                .flatMap(Set::toStream)
                .toSet();
    }

    private Set<Integer> parseFromToValue(String args, Unit unit) {
        final String[] fromAndToValues = args.split("-");
        if (fromAndToValues.length != 2) {
            // not allowed (like 2--3)
            throw new IllegalArgumentException("Incorrect from-to value definition: " + args);
        }

        final int from = Integer.parseInt(fromAndToValues[0]);
        final int to = Integer.parseInt(fromAndToValues[1]);
        return Stream.rangeClosed(from, to)
                .toSet();
    }

    private static Set<Integer> parseSingleValue(String args) {
        return HashSet.of(Integer.parseInt(args));
    }

    private boolean validate(Set<Integer> parsedValues, Unit unit) {
        return parsedValues
                .filter(value -> value < unit.minAllowed || value > unit.maxAllowed)
                .isEmpty();
    }
}
