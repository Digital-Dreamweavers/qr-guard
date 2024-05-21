package dev.digitaldreamweavers.qrguard.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import dev.digitaldreamweavers.qrguard.R;

public class ProfileFragment extends Fragment {

    private String TAG = "ProfileFragment";

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
        // fetchAndDisplayUserName();
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
                    Log.i(TAG, "Full name: " + fullName);
                    break;
                }
            }
        }
    }

}