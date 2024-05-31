package dev.digitaldreamweavers.qrguard.checker;

import android.location.Location;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    // Total scans
    protected long totalScans = 0;

    public void setTotalScans(long totalScans) {
        this.totalScans = totalScans;
    }

    public long getTotalScans() {
        return totalScans;
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

    public static String hashUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing URL", e);
        }
    }

    private OnReadyListener onReadyListener;

    public void setOnReadyListener(OnReadyListener onReadyListener) {
        this.onReadyListener = onReadyListener;
    }
    protected void notifyOnReady() {
        if (onReadyListener != null) {
            onReadyListener.onReady(this);
        }
    }

    public abstract void check();

    public interface OnReadyListener {
        void onReady(Check check);
    }


}
