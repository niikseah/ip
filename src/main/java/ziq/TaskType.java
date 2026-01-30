package ziq;

/**
 * Enumeration of task types with their corresponding codes.
 */
public enum TaskType {
    TODO("T"), DEADLINE("D"), EVENT("E");

    private final String code;

    /**
     * Constructs a TaskType with the given code.
     */
    TaskType(String code) {
        this.code = code;
    }

    /**
     * Returns the code for this task type.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the TaskType corresponding to the given code.
     */
    public static TaskType fromCode(String code) throws ZiqException {
        for (TaskType t : TaskType.values()) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        throw new ZiqException("what kind of task is this?");
    }
}
