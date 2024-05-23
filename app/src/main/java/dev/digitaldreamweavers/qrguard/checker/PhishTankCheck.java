package dev.digitaldreamweavers.qrguard.checker;

import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import dev.digitaldreamweavers.qrguard.R;

/**
 * PhishTankCheck.java
 * This class is responsible for checking a URL against the PhishTank database.
 * PhishTank is a community-driven database of known phishing URLs, and is a fallback database for
 * when Firestore does not have a safety report for a provided URL.
 *
 * This class is a part of the QR Guard project.
 *
 * @version 1.0
 * @since 1.0
 */

public class PhishTankCheck implements CheckInterface {

    private final String TAG = "Checker (PhishTank)";

    // PhishTank requires a user agent for requests for API use.
    // e.g. phishtank/QR Guard Ver 1.0
    private final String USERAGENT =
            "phishtank/" +
                    R.string.app_name  + " " +
                    R.string.version_text;

    // API Endpoint for PhishTank, can be seen here: https://www.phishtank.com/api_info.php
    private final String PHISHTANK_ENDPOINT = "https://checkurl.phishtank.com/checkurl/";

    // Not initialised yet so that we can gracefully disable PT checks if the URL is invalid.
    private URL ptURL;

    // Interface implementation.
    public boolean isPhishtankCheck = true;
    public Location location = null;


    PhishTankCheck(URL url) {

        // Validate PhishTank URL.
        try {
            ptURL = new URL(PHISHTANK_ENDPOINT);
        } catch (MalformedURLException e) {
            Log.e(TAG, "PhishTank URL is malformed! No PT checks will work: " + e.getMessage());
            Log.i(TAG, "PT check will be aborted.");
            return;
        }

        // Don't open a connection if there is no valid URL.
        if (url == null) {
            Log.w(TAG, "URL is null, check aborted.");
        } else {
            Log.i(TAG, "Opening connection to PhishTank...");
            try {
                URLConnection connection =  (HttpsURLConnection) ptURL.openConnection();
                checkPhishTank(connection);
            }
            catch (IOException e) {
                Log.e(TAG, "Could not open connection: " + e.getMessage());
            }
        }
    }

    private void checkPhishTank(URLConnection phishtankConnection) {
        phishtankConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        phishtankConnection.setRequestProperty("User-Agent", USERAGENT);
    }
}
