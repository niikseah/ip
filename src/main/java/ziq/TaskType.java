package ziq;

import java.util.Arrays;

/**
 * Enumeration of task types with their corresponding codes.
 */
public enum TaskType {
    TODO("T"), DEADLINE("D"), EVENT("E");

    private final String code;

    /**
     * Constructs a TaskType with the given code.
     *
     * @param code the code representing this task type
     */
    TaskType(String code) {
        this.code = code;
    }

    /**
     * Returns the code for this task type.
     *
     * @return the code string
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the TaskType corresponding to the given code.
     *
     * @param code the code to look up
     * @return the corresponding TaskType
     * @throws ZiqException if the code does not match any task type
     */
    public static TaskType fromCode(String code) throws ZiqException {
        return Arrays.stream(TaskType.values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ZiqException("what kind of task is this?"));
    }
}
