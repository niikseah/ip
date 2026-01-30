public enum TaskType {
    TODO("T"), DEADLINE("D"), EVENT("E");

    private final String code;

    TaskType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TaskType fromCode(String code) throws ZiqException {
        for (TaskType t : TaskType.values()) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        throw new ZiqException("Unknown task type in file.");
    }
}