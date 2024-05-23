package dev.digitaldreamweavers.qrguard.checker;

import java.net.URL;

public interface CheckInterface {
    int safetyStatus = 0;

    // Total ratings
    int totalRatings = 0;

    // Unsafe ratings
    int unsafeRatings = 0;

    // Safe ratings
    int safeRatings = 0;

    // URL to check
    URL url = null;
}
