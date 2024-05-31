package dev.digitaldreamweavers.qrguard.checker;

import android.util.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class PhishTankCheckTest {

    String url = "https://example.com/";

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    /**
     * Test the handleResponse method with a NO_RESPONSE response.
     */
    @Test
    public void testHandleResponseNoResponse() {
        PhishTankCheck phishTankCheck = new PhishTankCheck(url);
        phishTankCheck.handleResponse("NO_RESPONSE");
        assertEquals(Check.SafetyStatus.UNKNOWN, phishTankCheck.getSafetyStatus());
    }

    /**
     * Test the handleResponse method with a valid phish response.
     */
    @Test
    public void testHandleResponseValidPhish() {
        PhishTankCheck phishTankCheck = new PhishTankCheck(url);
        String response = "<response><in_database>true</in_database><valid>true</valid></response>";
        phishTankCheck.handleResponse(response);
        assertEquals(Check.SafetyStatus.UNVERIFIED_UNSAFE, phishTankCheck.getSafetyStatus());
    }

    /**
     * Test the handleResponse method with a invalid phish response (safe).
     */
    @Test
    public void testHandleResponseInvalidPhish() {
        PhishTankCheck phishTankCheck = new PhishTankCheck(url);
        String response = "<response><in_database>true</in_database><valid>false</valid></response>";
        phishTankCheck.handleResponse(response);
        assertEquals(Check.SafetyStatus.UNVERIFIED_SAFE, phishTankCheck.getSafetyStatus());
    }

    /**
     * Test the handleResponse method when PhishTank doesn't have the URL in the database.
     */
    @Test
    public void testHandleResponseNotInDatabase() {
        PhishTankCheck phishTankCheck = new PhishTankCheck(url);
        String response = "<response><in_database>false</in_database></response>";
        phishTankCheck.handleResponse(response);
        assertEquals(Check.SafetyStatus.UNKNOWN, phishTankCheck.getSafetyStatus());
    }

    /**
     * Test the handleResponse method with a bad XML response.
     */
    @Test
    public void testHandleResponseBadXml() {
        PhishTankCheck phishTankCheck = new PhishTankCheck(url);
        String response = "<response><in_database>true</in_database></response>"; // Missing <valid> tag
        phishTankCheck.handleResponse(response);
        assertEquals(Check.SafetyStatus.UNKNOWN, phishTankCheck.getSafetyStatus());
    }
}