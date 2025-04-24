package com.sen3006.coursewise.enums;

public enum Type {
    FacetoFace,
    Online,
    Asynchronous;

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

