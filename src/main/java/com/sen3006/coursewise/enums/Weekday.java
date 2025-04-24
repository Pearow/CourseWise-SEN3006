package com.sen3006.coursewise.enums;

public enum Weekday {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday;

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