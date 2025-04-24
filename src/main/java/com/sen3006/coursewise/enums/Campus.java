package com.sen3006.coursewise.enums;

public enum Campus {
    SOUTH,
    NORTH,
    BALMUMCU,
    GALATA,
    GOZTEPE,
    IDEA,
    PERA,
    FUTURE;

    public static Campus fromString(String input) {
        for (Campus campus : Campus.values()) {
            if (campus.name().equalsIgnoreCase(input)) {
                return campus;
            }
        }
        throw new IllegalArgumentException("Invalid Campus: " + input);

    }
    public static Campus fromIndex(int index) {
        Campus[] campus = Campus.values();
        if (index >= 0 && index < campus.length) {
            return campus[index];
        }
        throw new IllegalArgumentException("Invalid Campus index: " + index);
    }
}
