package org.example.model;

import java.time.LocalDateTime;

public abstract class BaseEntity {

    protected LocalDateTime deletedDateTime = null;
    protected LocalDateTime createdDateTime = LocalDateTime.now();
    protected LocalDateTime updatedDateTime = LocalDateTime.now();

    public LocalDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public boolean isDeleted() {
        return this.deletedDateTime != null;
    }

    public void restore() {
        this.deletedDateTime = null;
    }
}

