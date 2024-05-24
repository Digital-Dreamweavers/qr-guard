package dev.digitaldreamweavers.qrguard.checker;

import android.location.Location;

import androidx.annotation.Nullable;

import java.net.URL;

public interface CheckInterface {

    // Unverified statuses are community scored checks.
    // Verified statuses are checks from a QR Guard maintainers.
    // (Scored PhishTank checks will always be considered unverified).
    enum SafetyStatus {
        UNKNOWN,
        UNVERIFIED_SAFE,
        UNVERIFIED_UNSAFE,
        VERIFIED_SAFE,
        VERIFIED_UNSAFE
    }

    // PhishTank checks provide less information than Firestore checks,
    // this tells ReportActivity to provide attribution and hide rating stats UI.
    boolean isPhishtankCheck = false;

    // Total ratings
    int totalRatings = 0;

    // Unsafe ratings
    int unsafeRatings = 0;

    // Safe ratings
    int safeRatings = 0;

    // Location of the scan.
    Location location = null;

    // URL to check
    URL url = null;
}
