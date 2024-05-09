package dev.digitaldreamweavers.qrguard;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM message received
        // This method will be called when your app receives a notification from FCM
        // You can handle the notification here
    }

    @Override
    public void onNewToken(String token) {
        // Handle token refresh
        // This method will be called when a new FCM token is generated
        // You can use the token to send notifications to this device
    }
}
