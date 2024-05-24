package dev.digitaldreamweavers.qrguard.checker;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

public class PhishTankCheck extends Check {

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

    private ExecutorService networkThread = Executors.newSingleThreadExecutor();

    private String URLString;



    public PhishTankCheck(String url) {
        setPhishTank(true);
        setUrl(url);

        // Prepare the URL to be put into the parameter of a POST request.
        String encodedURL = Base64EncodeString(url);
        Log.i(TAG, "Encoded URL: " + encodedURL);
        URLString = (
                    PHISHTANK_ENDPOINT +
                            "?url=" + encodedURL
        );

        Log.i(TAG, "URL to check: " + URLString);
        checkPhishTank(URLString);
    }

    private String Base64EncodeString(String input) {
        return Base64.getUrlEncoder().encodeToString(input.getBytes());
    }

    public void checkPhishTank(String urlToCheck) {
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

        Callable<String> getResponse = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Response res = client.newCall(ptRequest).execute();
                    if (res.code() != 200) {
                        Log.w(TAG, "PhishTank responded wrongly: " + res.code());
                        return "NO_RESPONSE";
                    }
                    String resBody = res.body().string();
                    Log.i(TAG, "PhishTank response: " + resBody);
                    res.close();
                    return resBody;
                } catch (IOException e) {
                    Log.w(TAG, "Could not connect to PhishTank: " + e.getMessage());
                    return "NO_RESPONSE";

                }
            }
        };

        Future<String> response = networkThread.submit(getResponse);

        try {
            String ptResponse = response.get();
            Log.i(TAG, "PhishTank response: " + ptResponse);
            handleResponse(ptResponse);
        } catch (Exception e) {
            Log.e(TAG, "Could not get PhishTank response: " + e.getMessage());
        } finally {
            networkThread.shutdown();
        }

    }

    /*
     *  Handling and XML Parsing of Phishtank response.
     *
     * PhishTank returns a response in XML format, which we need to parse.
     * First, we need to check if <in_database> is true or false.
     * If it's false, the response won't have <valid> or <verified> tags,
     * which can throw a NullPointerException when parsing.
     *
     * If <valid> is true then it is a phishing link.
     *
     * */
    private void handleResponse(String response) {
        Log.i(TAG, "Handling PhishTank response.");
        if (response.equals("NO_RESPONSE")) {
            Log.w(TAG, "PhishTank did not respond.");
            return;
        }

        // Prepare XML Parser
        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder xmlBuilder = xmlFactory.newDocumentBuilder();

            // Put PhishTank's Response into an input stream and feed it to the parser.
            InputSource resInput = new InputSource(new StringReader(response));
            Document xmlResponse = xmlBuilder.parse(resInput);

            // Clean the XML response and check the <in_database> tag.
            xmlResponse.getDocumentElement().normalize();
            Element root = xmlResponse.getDocumentElement();

            // Error checking our way through the XML response.
            // It's important to cover all bases and recover in case PhishTank changes their response format.
            NodeList inDatabase = root.getElementsByTagName("in_database");
            if (inDatabase.getLength() == 0) {
                throw new SAXException("No <in_database> tag found.");
            }
            Node inDatabaseNode = inDatabase.item(0);
            if (inDatabaseNode.getNodeType() != Node.ELEMENT_NODE) {
                throw new SAXException("<in_database> tag is not an element.");
            }

            // Now let's see if the URL is in the database.
            if (inDatabaseNode.getTextContent().equals("true")) {
                Node isValidPhish = root.getElementsByTagName("valid").item(0);
                if (isValidPhish.getTextContent().equals("true")) {
                    Log.i(TAG, "SAFETY REPORT: Phishing link.");
                    setSafetyStatus(SafetyStatus.UNVERIFIED_UNSAFE);
                } else {
                    Log.i(TAG, "SAFETY REPORT: Safe link.");
                    setSafetyStatus(SafetyStatus.UNVERIFIED_SAFE);
                }
            } else {
                Log.i(TAG, "SAFETY REPORT: Unknown, not in PT Database.");
                setSafetyStatus(SafetyStatus.UNKNOWN);
            }



        } catch (ParserConfigurationException e) {
            Log.e(TAG, "XML Config failed: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Could not parse PhishTank response: " + e.getMessage());
        } catch (SAXException e) {
            Log.e(TAG, "PhishTank sent bad XML: " + e.getMessage());
        }

    }
}
