package com.sen3006.coursewise.enums;

public enum Weekday {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday;

    public int Weekday() {
        switch (this) {
            case Monday:
                return 0;
            case Tuesday:
                return 1;
            case Wednesday:
                return 2;
            case Thursday:
                return 3;
            case Friday:
                return 4;
            default:
                return -1;
        }
    }

    public static Weekday fromString(String input) {
        for (Weekday day : Weekday.values()) {
            if (day.name().equalsIgnoreCase(input)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid weekday: " + input);
    }
    public static Weekday fromIndex(int index) {
        Weekday[] day = Weekday.values();
        if (index >= 0 && index < day.length) {
            return day[index];
        }
        throw new IllegalArgumentException("Invalid weekday index: " + index);
    }

}