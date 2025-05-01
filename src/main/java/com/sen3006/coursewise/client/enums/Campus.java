package com.sen3006.coursewise.client.enums;

public enum Campus {
    SOUTH,
    NORTH,
    BALMUMCU,
    GALATA,
    GOZTEPE,
    IDEA,
    PERA,
    FUTURE,
    ONLINE;

    public int getIntCampus() {
        switch (this) {
            case SOUTH:
                return 0;
            case NORTH:
                return 1;
            case BALMUMCU:
                return 2;
            case GALATA:
                return 3;
            case GOZTEPE:
                return 4;
            case IDEA:
                return 5;
            case PERA:
                return 6;
            case FUTURE:
                return 7;
            case ONLINE:
                return 8;
            default:
                return -1;
        }
    }

    public static Campus fromString(String input) {
        if (input.equalsIgnoreCase("İSTANBUL-BEŞİKTAŞ")) {
            return Campus.SOUTH;
        } else if (input.equalsIgnoreCase("İSTANBUL-KUZEY")) {
            return Campus.NORTH;
        } else if (input.equalsIgnoreCase("İSTANBUL-GALATA")) {
            return Campus.GALATA;
        } else if (input.equalsIgnoreCase("İSTANBUL-GÖZTEPE")) {
            return Campus.GOZTEPE;
        } else if (input.equalsIgnoreCase("İSTANBUL-PERA")) {
            return Campus.PERA;
        } else if (input.equalsIgnoreCase("UZAKTAN EĞİTİM")) {
            return Campus.ONLINE;
        } else if (input.equalsIgnoreCase("İSTANBUL-BALMUMCU")) {return Campus.BALMUMCU;}

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
