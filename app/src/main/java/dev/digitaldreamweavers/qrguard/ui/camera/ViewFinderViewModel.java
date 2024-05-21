package dev.digitaldreamweavers.qrguard.ui.camera;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.digitaldreamweavers.qrguard.R;

public class ViewFinderViewModel extends ViewModel {
    private final MutableLiveData<ListenableFuture<ProcessCameraProvider>> cameraProviderFuture = new MutableLiveData<>();

    private final MutableLiveData<Integer> fabStatus = new MutableLiveData<>();

    // Scanning statuses.
    public static final int FAB_STATUS_WAITING = 0; // No QR Code has been found yet.
    public static final int FAB_STATUS_INVALID = 1; // A QR Code has been found, but it isn't a URL.
    public static final int FAB_STATUS_VALID = 2; // A QR Code has been found, and it is a URL.

    private PreviewView qrViewFinder;

    private String TAG = "ViewFinderViewModel";

    // Use provided MLKit BarcodeScanner
    private BarcodeScanner qrScanner;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ImageCapture imageCapture;

    public LiveData<Integer> getFabStatus() {
        return fabStatus;
    }

    public void setFabStatus(int status) {
        Integer statusInteger = status;
        if (!statusInteger.equals(fabStatus.getValue())) {
            fabStatus.setValue(status);
        }
    }

    public LiveData<ListenableFuture<ProcessCameraProvider>> getCameraProviderFuture() {
        return cameraProviderFuture;
    }

    public void initCameraProviderFuture(ListenableFuture<ProcessCameraProvider> future) {
        cameraProviderFuture.setValue(future);
    }


    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
                .build();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                //.requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Log.w(TAG, "Setting Surface Provider for camera...");
        preview.setSurfaceProvider(qrViewFinder.getSurfaceProvider());

        ImageAnalysis imageAnalyser = buildBarcodeScanner();
        Log.i(TAG, "Image Analyser attached.");


        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyser);
    }


    // buildBarcodeScanner: Returns an image analyser to be bind to a camera.
    @OptIn(markerClass = ExperimentalGetImage.class)
    private ImageAnalysis buildBarcodeScanner() {
        Log.i(TAG, "Building analyser...");
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        Log.i(TAG, "Setting analyser...");
        imageAnalysis.setAnalyzer(executor, image -> {
            // Log.i(TAG, "Analysing...");
            InputImage inputImage = InputImage.fromMediaImage(Objects.requireNonNull(image.getImage()), image.getImageInfo().getRotationDegrees());
            // Use BarcodeScanner to scan the image
            qrScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        if (barcodes.isEmpty()) {
                            //Log.i(TAG, "No barcode detected.");
                            updateFAB(0);
                        } else {
                            for (Barcode barcode : barcodes) {

                                try {
                                    URL barcodeURL = new URL(barcode.getRawValue());
                                    Log.i(TAG, "Valid URL detected, now scanning: " + barcodeURL.toString());
                                    updateFAB(2);
                                } catch (Exception e) {
                                    Log.i(TAG, "Not a valid URL: " + barcode.getRawValue());
                                    updateFAB(1);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Barcode scanning failed: ", e);
                    })
                    .addOnCompleteListener(result -> {
                        image.close();
                        //Log.i(TAG, "Image closed.");
                    });
        });

        return imageAnalysis;
    }

    private void startCamera() {

        this.getCameraProviderFuture().observe(this, cameraProviderFuture -> {
            if (cameraProviderFuture != null) {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Log.w(TAG, "Could not add Camera Listener: ", e);
                }
            } else {
                Log.w(TAG, "Camera Provider Future is null");
            }
        });

    }


}