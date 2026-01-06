package com.mypensamiento.mypensamiento.domain.model;

import java.time.LocalDateTime;

public class PasswordResetCode {
    private Long id;
    private String userEmail;
    private String hashedCode;
    private LocalDateTime expiryDate;
    private int attempts;
    private boolean used;
    private static final int MAX_ATTEMPTS = 3;

    public PasswordResetCode(){}

    public PasswordResetCode(
            Long id,
            String userEmail,
            String hashedCode,
            LocalDateTime expiryDate,
            int attempts,
            boolean used
    ) {
        this.id = id;
        this.userEmail = userEmail;
        this.hashedCode = hashedCode;
        this.expiryDate = expiryDate;
        this.attempts = attempts;
        this.used = used;
    }

    public PasswordResetCode(
            String userEmail,
            String hashedCode,
            int expirationMinutes

    ) {
        this.userEmail = userEmail;
        this.hashedCode = hashedCode;
        this.expiryDate = LocalDateTime.now().plusMinutes(expirationMinutes);
        this.attempts = 0;
        this.used = false;
    }

    public boolean canTryAgain() {
        return !used && !isExpired() && attempts < MAX_ATTEMPTS;
    }


    public void registerFailedAttempt() {
        attempts++;

        if (attempts >= MAX_ATTEMPTS) {
            markAsUsed();
        }
    }

    public void markAsUsed() {
        this.used = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public boolean isBlocked() {
        return attempts >= MAX_ATTEMPTS;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getHashedCode() {
        return hashedCode;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isUsed() {
        return used;
    }
}