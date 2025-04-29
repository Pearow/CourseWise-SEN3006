package com.sen3006.coursewise.enums;

public enum CourseType {
    Synchronous,
    Asynchronous;

    public int getIntType() {
        switch (this) {
            case Synchronous:
                return 0;
            case Asynchronous:
                return 1;
            default:
                return -1;
        }
    }

    public static CourseType fromString(String input) {
        for (CourseType type : CourseType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + input);
    }

    public static CourseType fromIndex(int index) {
        CourseType[] type = CourseType.values();
        if (index >= 0 && index < type.length) {
            return type[index];
        }
        throw new IllegalArgumentException("Invalid type index: " + index);
    }
}
