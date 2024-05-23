package dev.digitaldreamweavers.qrguard.checker;

import android.util.Log;

import java.net.URL;

import dev.digitaldreamweavers.qrguard.R;

public class PhishTankCheck {

    private final String TAG = "Checker (PhishTank)";

    // PhishTank requires a user agent for requests for API use.
    // e.g. phishtank/QR Guard Ver 1.0
    private final String userAgent =
            "phishtank/" +
                    R.string.app_name  + " " +
                    R.string.version_text;


    PhishTankCheck(URL url) {
        String hostname = url.getHost();
        if (hostname == null) {
            Log.w(TAG, "Hostname is null, cannot check.");
            return;
        } else {
            Log.i(TAG, "Checking " + hostname + " with PhishTank.");
        }
    }
}
