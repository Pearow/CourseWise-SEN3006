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
}
