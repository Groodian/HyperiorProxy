package de.groodian.hyperiorproxy.user;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.postgresql.util.PGInterval;

public class UserHistory {

    private final UUID id;
    private final UUID target;
    private final UUID createdBy;
    private final UserHistoryType type;
    private final String reason;
    private final OffsetDateTime createdAt;
    private final PGInterval duration;

    public UserHistory(UUID target, UUID createdBy, UserHistoryType type, String reason, PGInterval duration) {
        this.id = UUID.randomUUID();
        this.target = target;
        this.createdBy = createdBy;
        this.type = type;
        this.reason = reason;
        this.createdAt = OffsetDateTime.now();
        this.duration = duration;
    }

    public UserHistory(UUID id, UUID target, UUID createdBy, UserHistoryType type, String reason, OffsetDateTime createdAt,
                       PGInterval duration) {
        this.id = id;
        this.target = target;
        this.createdBy = createdBy;
        this.type = type;
        this.reason = reason;
        this.createdAt = createdAt;
        this.duration = duration;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UserHistoryType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public PGInterval getDuration() {
        return duration;
    }

}
