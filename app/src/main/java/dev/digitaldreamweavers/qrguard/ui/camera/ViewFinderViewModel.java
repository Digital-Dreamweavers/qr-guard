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




}