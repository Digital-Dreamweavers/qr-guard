package dev.digitaldreamweavers.qrguard.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import dev.digitaldreamweavers.qrguard.MainActivity;
import dev.digitaldreamweavers.qrguard.R;
import dev.digitaldreamweavers.qrguard.User;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;

    private User localUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // Set click listener for Google Sign In button
        findViewById(R.id.button_connect_google).setOnClickListener(view -> signIn());
    }

    // Start the Google Sign In activity
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Handle the Google Sign In result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI accordingly
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    // Authenticate with Firebase using the Google token
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        if (user != null) {
                            // Add user data to Firestore
                            createUserInFirestore(user);

                            // Start MainActivity
                            startMainActivity();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    // Start MainActivity
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish LoginActivity to prevent the user from going back to it after logging in
    }

    // Update UI based on FirebaseUser
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, you can redirect to another activity or perform other actions
            Toast.makeText(this, "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            // User is signed out
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        }
    }

    // Add user data to Firestore
    private void createUserInFirestore(FirebaseUser user) {
        localUser = new User(user);
        String uid = localUser.getUid();
        String email = localUser.getEmail();

        // Log the UID and email address received
        Log.d(TAG, "UID received: " + uid);
        Log.d(TAG, "Email address received: " + email);

        // Create a new user with email address
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        // Use the UID as the document ID
        db.collection("users").document(uid)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written with UID: " + uid))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    // Fetch entire collection from Firestore (for testing)
    private void getEntireCollection() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Log the email address
                            String email = document.getString("email");
                            Log.d(TAG, document.getId() + " => Email: " + email);
                            // You can log other fields as well if needed
                            // Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}




