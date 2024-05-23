package dev.digitaldreamweavers.qrguard;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {

    private static final String TAG = "User";

    private String uid;
    private String email;
    private String displayName;
    private Uri photoUrl;
    private int scans;

    public User(FirebaseUser user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.photoUrl = user.getPhotoUrl();
        this.scans = 0; // Initialize scans to 0 or any default value
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public int getScans() {
        return scans;
    }

    public void setScans(int scans) {
        this.scans = scans;
    }

    public void incrementScans() {
        this.scans++;
    }

    @NonNull
    @Override
    public String toString() {
        String photoUrlString = (photoUrl != null) ? photoUrl.toString() : "No photo URL";
        Log.i("User", photoUrlString);
        return "User: " +
                email +
                " (" +
                uid +
                ")" +
                "\n" +
                "Display: " +
                displayName + "\n" +
                "Photo URL: " +
                photoUrlString + "\n" +
                "Scans: " +
                scans;
    }
}

