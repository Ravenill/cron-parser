package pl.kruczek.cron.definition;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import static io.vavr.API.$;
import static io.vavr.API.Case;

@UtilityClass
class CronDefinitionUtil {

    public static final String[] EMPTY_FROM_TO_VALUES = {"", ""};

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

    // suppress unchecked coz vavr Api.Case
    @SuppressWarnings("unchecked")
    Set<Integer> parseArgs(String args, Unit unit) {
        return Try.of(() -> parseArgsToSet(args, unit))   // Set provide uniqueness
                .mapFailure(
                        Case(
                                $(NumberFormatException.class::isInstance),
                                () -> new IllegalArgumentException("Incorrect format: " + args + " for unit " + unit.name())
                        ),
                        Case($(), ex -> ex)
                )
                .filter(
                        parsedValues -> validate(parsedValues, unit),
                        () -> new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name())
                )
                .map(Set::toSortedSet)
                .get();
    }

    private Set<Integer> parseArgsToSet(String args, Unit unit) {
        if (StringUtils.isBlank(args)) {
            throw new IllegalArgumentException("Value or part of group cannot be empty. Unit: " + unit.name());
        }

        final int argsLength = args.length();
        final boolean hasOneChar = argsLength == 1;
        final boolean hasMoreThanOneChar = argsLength > 1;

        final String firstChar = String.valueOf(args.charAt(0));
        final boolean isFirstCharANumber = NumberUtils.isParsable(firstChar);

        final boolean hasMoreThanOneGroupInArgs = args.contains(",");
        final boolean hasStepValue = args.contains("/");
        final boolean hasFromToValue = args.contains("-");

        if (hasMoreThanOneGroupInArgs) {
            // groups (like 1,2,3,10-20,14-20/2)
            return parseGroups(args, unit);
        } else if (hasOneChar && !isFirstCharANumber) {
            // single special chars (like *)
            return parseOneSpecialCharacter(firstChar, unit);
        } else if (hasStepValue) {
            // step like (*/15)
            return parseStepValue(args, unit);
        } else if (hasMoreThanOneChar && !isFirstCharANumber) {
            // not allowed (like -21 or aliases @yearly)
            throw new IllegalArgumentException("Value: " + args + " not supported for unit: " + unit.name());
        } else if (hasFromToValue) {
            // from-to (like 10-20)
            return parseFromToValue(args);
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

    private Set<Integer> parseStepValue(String args, Unit unit) {
        final String[] stepDefinition = args.split("/");
        if (stepDefinition.length != 2) {
            // not allowed (like 3//4)
            throw new IllegalArgumentException("Incorrect step value definition: " + args);
        }

        final String fromToAsString = stepDefinition[0];
        final boolean isFromToValueAsAllValues = fromToAsString.length() == 1 && fromToAsString.contains("*");

        final String[] fromToValues = !isFromToValueAsAllValues ? fromToAsString.split("-") : EMPTY_FROM_TO_VALUES;
        if (fromToValues.length != 2) {
            // not allowed (like 2--3)
            throw new IllegalArgumentException("Incorrect from-to value definition: " + args);
        }

        final int from = isFromToValueAsAllValues
                ? unit.minAllowed
                : Integer.parseInt(fromToAsString.split("-")[0]);

        final int to = isFromToValueAsAllValues
                ? unit.maxAllowed
                : Integer.parseInt(fromToAsString.split("-")[1]);

        final int step = Integer.parseInt(stepDefinition[1]);
        return Stream.rangeClosedBy(from, to, step)
                .toSet();
    }

    private Set<Integer> parseFromToValue(String args) {
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
