package dev.digitaldreamweavers.qrguard;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

public class User {
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
    }

    public User(String uid, String email, String displayName, Uri photoUrl) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
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

    @NonNull
    @Override
    public String toString() {
        Log.i("User", photoUrl.toString());
        return "User: " +
                email +
                " (" +
                uid +
                ")" +
                "\n" +
                "Display: " +
                displayName + "\n";
    }
}


