package com.sen3006.coursewise.client.enums;

public enum Semester {
    Fall,
    Spring;

    public int getIntSemester() {
        switch (this) {
            case Fall:
                return 0;
            case Spring:
                return 1;
            default:
                return -1;
        }
    }

    public static Semester fromString(String input) {
        for (Semester semester : Semester.values()) {
            if (semester.name().equalsIgnoreCase(input)) {
                return semester;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + input);
    }

    public static Semester fromIndex(int index) {
        Semester[] semester = Semester.values();
        if (index >= 0 && index < semester.length) {
            return semester[index];
        }
        throw new IllegalArgumentException("Invalid type index: " + index);
    }
}
