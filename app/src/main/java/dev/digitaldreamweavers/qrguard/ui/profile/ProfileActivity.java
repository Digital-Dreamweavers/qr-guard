package dev.digitaldreamweavers.qrguard.ui.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import dev.digitaldreamweavers.qrguard.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Fetch and display user's first and last name
        fetchAndDisplayUserName();
    }

    private void fetchAndDisplayUserName() {
        // Fetch and display user's first and last name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Name can be extracted from Google Sign-In provider
                if (profile.getProviderId().equals("google.com")) {
                    // Cast the UserInfo object to GoogleSignInAccount
                    GoogleSignInAccount googleAccount = (GoogleSignInAccount) profile;
                    String fullName = googleAccount.getDisplayName();
                    // Use fullName as needed
                    break;
                }
            }
        }
    }


}



