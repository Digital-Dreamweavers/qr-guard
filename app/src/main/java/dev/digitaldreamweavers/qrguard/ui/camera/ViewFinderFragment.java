package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.digitaldreamweavers.qrguard.R;

public class ViewFinderFragment extends Fragment {

    private ViewFinderViewModel mViewModel;
    private PreviewView qrViewFinder;

    // Use provided MLKit BarcodeScanner
    private BarcodeScanner qrScanner;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ImageCapture imageCapture;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private String TAG = "ViewFinderFragment";

    public static ViewFinderFragment newInstance() {
        return new ViewFinderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        qrViewFinder = new PreviewView(getContext());
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity());

        // Initialise Barcode Scanner
        qrScanner = BarcodeScanning.getClient();
        startCamera();

        return inflater.inflate(R.layout.fragment_view_finder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ViewFinderViewModel.class);
        // Initialise camera provider
        Log.w(TAG, "Starting Camera...");
        mViewModel.initCameraProviderFuture(cameraProviderFuture);
        // TODO: Use the ViewModel
    }

    private void startCamera() {

        mViewModel.getCameraProviderFuture().observe(requireActivity(), cameraProviderFuture -> {
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
                            //updateFAB(0);
                        } else {
                            for (Barcode barcode : barcodes) {

                                try {
                                    URL barcodeURL = new URL(barcode.getRawValue());
                                    Log.i(TAG, "Valid URL detected, now scanning: " + barcodeURL.toString());
                                    //updateFAB(2);
                                } catch (Exception e) {
                                    Log.i(TAG, "Not a valid URL: " + barcode.getRawValue());
                                    //updateFAB(1);
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


}