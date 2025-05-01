package com.sen3006.coursewise.client.enums;

public enum Role {
    STUDENT,
    ADMIN,
    ACADEMIC;

    public int getIntRole() {
        switch (this) {
            case STUDENT:
                return 0;
            case ADMIN:
                return 1;
            case ACADEMIC:
                return 2;
            default:
                return -1;
        }
    }
}
