package util;

import java.time.OffsetDateTime;

public class AnnouncementFilter {

    private OffsetDateTime from;
    private OffsetDateTime to;
    private String authorLogin;

    public AnnouncementFilter() { }

    public AnnouncementFilter from(OffsetDateTime from) {
        this.from = from;
        return this;
    }

    public AnnouncementFilter to(OffsetDateTime to) {
        this.to = to;
        return this;
    }

    public AnnouncementFilter authorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
        return this;
    }

    public OffsetDateTime getFrom()          { return from; }
    public OffsetDateTime getTo()            { return to; }
    public String         getAuthorLogin()   { return authorLogin; }

    public boolean hasDateRange() {
        return from != null || to != null;
    }

    public boolean hasAuthor() {
        return authorLogin != null && !authorLogin.isBlank();
    }

    public boolean isEmpty() {
        return !hasDateRange() && !hasAuthor();
    }
}