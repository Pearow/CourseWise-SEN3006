package com.sen3006.coursewise.client.enums;

public enum Weekday {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday, Sunday, Async;

    public int getIntWeekday() {
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
            case Saturday:
                return 5;
            case Sunday:
                return 6;
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
        }else if (index == -1) {
            return Async;
        }else
            throw new IllegalArgumentException("Invalid weekday index: " + index);
    }

}
