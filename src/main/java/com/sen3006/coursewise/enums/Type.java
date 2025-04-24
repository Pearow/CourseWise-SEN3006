package com.sen3006.coursewise.enums;

public enum Type {
    FaceToFace,
    Online,
    Asynchronous;

    public int Type(Type type) {
        switch (type) {
            case FaceToFace:
                return 0;
            case Online:
                return 1;
            case Asynchronous:
                return 2;
            default:
                return -1;
        }
    }

    public static Type fromString(String input) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + input);
    }

    public static Type fromIndex(int index) {
        Type[] type = Type.values();
        if (index >= 0 && index < type.length) {
            return type[index];
        }
        throw new IllegalArgumentException("Invalid type index: " + index);
    }

}

