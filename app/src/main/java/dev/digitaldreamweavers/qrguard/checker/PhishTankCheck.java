package dev.digitaldreamweavers.qrguard.checker;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import dev.digitaldreamweavers.qrguard.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private final String PHISHTANK_ENDPOINT = "https://checkurl.phishtank.com/checkurl/index.php";

    // Not initialised yet so that we can gracefully disable PT checks if the URL is invalid.
    private URL ptURL;

    // Interface implementation.
    public boolean isPhishtankCheck = true;


    PhishTankCheck(URL url) {

        // Prepare the URL to be put into the parameter of a POST request.
        String encodedURL = Base64EncodeString(url.toString());
        String urlString = (
                    PHISHTANK_ENDPOINT +
                            "?url=" + encodedURL
        );

        Log.i(TAG, "Checking URL: " + urlString);
        checkPhishTank(urlString);

    }

    private String Base64EncodeString(String input) {
        return Base64.getUrlEncoder().encodeToString(input.getBytes());
    }

    private void checkPhishTank(String urlToCheck) {
        // Initialise the OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Create dummy body
        RequestBody body = RequestBody.create("{}", MediaType.parse("application/json"));

        // Create a request to the PhishTank API
        Request ptRequest = new Request.Builder()
                .url(urlToCheck)
                .addHeader("User-Agent", USERAGENT)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .method("POST", body)
                .build();

        try {
            Response res = client.newCall(ptRequest).execute();
            if (res.code() != 200) {
                Log.w(TAG, "PhishTank responded wrongly: " + res.code());
                return;
            }
            String resBody = res.body().string();
            Log.i(TAG, "PhishTank response: " + resBody);
            res.close();
        } catch (IOException e) {
            Log.w(TAG, "Could not connect to PhishTank: " + e.getMessage());
        }

    }
}
