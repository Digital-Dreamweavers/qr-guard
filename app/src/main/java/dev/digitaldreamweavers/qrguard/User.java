package dev.digitaldreamweavers.qrguard;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

public class User {
    private String Uid;

    private String email;
    private String displayName;
    private Uri photoUrl;

    private int scans;

    public User(FirebaseUser user) {
        this.Uid = user.getUid();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.photoUrl = user.getPhotoUrl();
    }

    public String getUid() {
        return this.Uid;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Uri getPhotoUrl() {
        return this.photoUrl;
    }

    public int getScans() {
        return this.scans;
    }

    @NonNull
    @Override
    public String toString() {
        Log.i("User" , this.photoUrl.toString());
        return "User: " +
                this.email +
                " (" +
                this.Uid +
                ")" +
                "\n" +
                "Display: " +
                this.displayName + "\n";
    }
}

