package dev.digitaldreamweavers.qrguard.checker;

import android.location.Location;

import java.net.URL;

/**
 * Check.java
 * Abstract Class for standardising checks in QR Guard.
 */

public abstract class Check {

    // Unverified statuses are community scored checks.
    // Verified statuses are checks from a QR Guard maintainers.
    // (Scored PhishTank checks will always be considered unverified).
    public enum SafetyStatus {
        UNKNOWN,
        UNVERIFIED_SAFE,
        UNVERIFIED_UNSAFE,
        VERIFIED_SAFE,
        VERIFIED_UNSAFE
    }

    protected SafetyStatus safetyStatus = SafetyStatus.UNKNOWN;

    public void setSafetyStatus(SafetyStatus status) {
        this.safetyStatus = status;
    }

    public SafetyStatus getSafetyStatus() {
        return safetyStatus;
    }

    protected boolean isPhishTank;

    public void setPhishTank(boolean isPhishTank) {
        this.isPhishTank = isPhishTank;
    }

    public boolean isPhishTank() {
        return isPhishTank;
    }

    // Total ratings
    protected int totalRatings = 0;

    // Unsafe ratings
    protected int unsafeRatings = 0;

    // Safe ratings
    protected int safeRatings = 0;

    public void setRatings(int safe, int unsafe) {
        this.safeRatings = safe;
        this.unsafeRatings = unsafe;
        this.totalRatings = safe + unsafe;
    }

    public int getSafeRatings() {
        return safeRatings;
    }

    public int getUnsafeRatings() {
        return unsafeRatings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    // Location of the scan.
    protected Location location = null;

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    // URL to check
    protected String url = null;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


}
