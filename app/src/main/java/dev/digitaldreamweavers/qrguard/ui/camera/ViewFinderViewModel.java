package dev.digitaldreamweavers.qrguard.ui.camera;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;

/*
*
*   ViewFinderViewModel.java
*   The ViewFinderViewModel class provides mutable data for the camera provider and Barcode URl.
*
*   The ViewModel is observed by the FloatingScanButton to update the UI based on Barcode scanning status.
*
 */

public class ViewFinderViewModel extends ViewModel {
    private final MutableLiveData<ListenableFuture<ProcessCameraProvider>> cameraProviderFuture = new MutableLiveData<>();

    private final MutableLiveData<Integer> fabStatus = new MutableLiveData<>();

    // Scanning statuses.
    private String TAG = "ViewFinderViewModel";




    public LiveData<ListenableFuture<ProcessCameraProvider>> getCameraProviderFuture() {
        return cameraProviderFuture;
    }

    public void initCameraProviderFuture(ListenableFuture<ProcessCameraProvider> future) {
        cameraProviderFuture.setValue(future);
    }




}