package model;

public enum UserRole {
    USER,
    EMPLOYEE,
    ADMIN;

    public boolean isUser() {
        return this == USER;
    }

    public boolean isEmployee() {
        return this == EMPLOYEE;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
