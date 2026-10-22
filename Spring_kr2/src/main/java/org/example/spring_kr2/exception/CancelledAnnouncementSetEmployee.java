package org.example.spring_kr2.exception;

public class CancelledAnnouncementSetEmployee extends RuntimeException {
    public CancelledAnnouncementSetEmployee(String message) {
        super(message);
    }
}
