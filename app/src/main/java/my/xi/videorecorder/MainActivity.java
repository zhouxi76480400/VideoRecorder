package my.xi.videorecorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import my.xi.videorecorder.application.MyApplication;
import my.xi.videorecorder.fragment.CameraFragment;
import my.xi.videorecorder.fragment.CameraVideoFragment;
import my.xi.videorecorder.view.AutoFitTextureView;

public class MainActivity extends AppCompatActivity
//        implements MultiplePermissionsListener, PermissionRequestErrorListener

{

//    private AutoFitTextureView textureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
////        getWindow().getDecorView().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////
////            }
////        }, 500);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }

    }

    private void initView() {
        setContentView(R.layout.activity_main);
//        textureView = findViewById(R.id.textureView);
//        loadCameraUI();
        hideSystemUI();
    }


//    private void permissionCheck() {
//        String[] permissions = null;
//        // get permissions from manifest
//        PackageInfo info = null;
//        try {
//            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (info != null) {
//            permissions = info.requestedPermissions;
//        }
//        // run Dexter
//        Dexter.withContext(this).withPermissions(permissions)
//                .withListener(this)
//                .withErrorListener(this)
//                .onSameThread()
//                .check();
//    }
//
//    @Override
//    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//        if (!multiplePermissionsReport.areAllPermissionsGranted()) {
//            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            gotoSettingsForPermissionCheck();
//                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                            MyApplication.getInstance().exit(0);
//                        }
//                    }
//                };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                        .setTitle(getString(R.string.dialog_title_goto_system_permission_settings))
//                        .setMessage(getString(R.string.dialog_message_goto_system_permission_settings))
//                        .setPositiveButton(getString(R.string.dialog_goto_system_settings), onClickListener)
//                        .setNegativeButton(getString(R.string.dialog_exit), onClickListener)
//                        .setCancelable(false);
//                builder.show();
//            } else {
//                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            permissionCheck();
//                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                            MyApplication.getInstance().exit(0);
//                        }
//                    }
//                };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                        .setTitle(getString(R.string.dialog_title_error))
//                        .setMessage(getString(R.string.dialog_message_error))
//                        .setPositiveButton(getString(R.string.dialog_retry), onClickListener)
//                        .setNegativeButton(getString(R.string.dialog_exit), onClickListener)
//                        .setCancelable(false);
//                builder.show();
//            }
//        } else {
//            // next step
//            initCamera();
//        }
//    }
//
//    @Override
//    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//        permissionToken.continuePermissionRequest();
//    }
//
//    @Override
//    public void onError(DexterError dexterError) {
//        Toast.makeText(this,getString(R.string.txt_permission_request_error),
//                Toast.LENGTH_SHORT).show();
//    }
//
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();

        int code = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (decorView.getSystemUiVisibility() != code) {
            decorView.setSystemUiVisibility(code);
        }
    }
//
//    private final int request_permission_code = 1111;
//
//    private void gotoSettingsForPermissionCheck() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivityForResult(intent, request_permission_code);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode == request_permission_code) {
//            permissionCheck();
//        } else
//            super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void loadCameraUI() {
//
//
//
//    }
//
//    private void initCamera() {
//        openCamera(800,600);
//
//
//    }
//
//    private Semaphore cameraOpenCloseLock = new Semaphore(1);
//
//    private Integer sensorOrientation;
//
//    private CaptureRequest.Builder previewBuilder;
//
//    private Size videoSize, previewSize;
//
//    private MediaRecorder mediaRecorder;
//
//    private void openCamera(int width, int height) {
//        Activity activity = this;
//        if(activity == null || activity.isFinishing()) {
//            return;
//        }
//        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            if(!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//
//            int front_facing = CameraCharacteristics.LENS_FACING_FRONT;
//
//            String cameraId = String.valueOf(front_facing);//manager.getCameraIdList()[0];
//
//            CameraCharacteristics cameraCharacteristics =
//                    manager.getCameraCharacteristics(cameraId);
//
//            StreamConfigurationMap map = cameraCharacteristics
//                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//
//            sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//
//            if (map == null) {
//                throw new RuntimeException("Cannot get available preview/video sizes");
//            }
//
//            videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
//            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
//                    width, height, videoSize);
//
//            int orientation = getResources().getConfiguration().orientation;
//
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
//            } else {
//                textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
//            }
//
//            configureTransform(width, height);
//
//            mediaRecorder = new MediaRecorder();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                permissionCheck();
//                return;
//            }
//
//            manager.openCamera(cameraId, stateCallback, null);
//
//
//        } catch (InterruptedException | CameraAccessException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static Size chooseVideoSize(Size[] choices) {
//        for (Size size : choices) {
//            if (1920 == size.getWidth() && 1080 == size.getHeight()) {
//                return size;
//            }
//        }
//        for (Size size : choices) {
//            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
//                return size;
//            }
//        }
//        Log.e("TAG", "Couldn't find any suitable video size");
//        return choices[choices.length - 1];
//    }
//
//    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
//        // Collect the supported resolutions that are at least as big as the preview Surface
//        List<Size> bigEnough = new ArrayList<>();
//        int w = aspectRatio.getWidth();
//        int h = aspectRatio.getHeight();
//        for (Size option : choices) {
//            if (option.getHeight() == option.getWidth() * h / w &&
//                    option.getWidth() >= width && option.getHeight() >= height) {
//                bigEnough.add(option);
//            }
//        }
//        // Pick the smallest of those, assuming we found any
//        if (bigEnough.size() > 0) {
//            return Collections.min(bigEnough, new CompareSizesByArea());
//        } else {
//            Log.e("TAG", "Couldn't find any suitable preview size");
//            return choices[0];
//        }
//    }
//
//    static class CompareSizesByArea implements Comparator<Size> {
//        @Override
//        public int compare(Size lhs, Size rhs) {
//            // We cast here to ensure the multiplications won't overflow
//            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
//                    (long) rhs.getWidth() * rhs.getHeight());
//        }
//    }
//
//    private void configureTransform(int viewWidth, int viewHeight) {
//        Activity activity = this;
//        if (null == textureView || null == previewSize || null == activity) {
//            return;
//        }
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        Matrix matrix = new Matrix();
//        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
//        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
//        float centerX = viewRect.centerX();
//        float centerY = viewRect.centerY();
//        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
//            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
//            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
//            float scale = Math.max(
//                    (float) viewHeight / previewSize.getHeight(),
//                    (float) viewWidth / previewSize.getWidth());
//            matrix.postScale(scale, scale, centerX, centerY);
//            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
//        }
//        textureView.setTransform(matrix);
//    }
//
//    private CameraDevice cameraDevice;
//
//    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
//        @Override
//        public void onOpened(@NonNull CameraDevice camera) {
//            cameraDevice = camera;
//            startPreview();
//            cameraOpenCloseLock.release();
//
//
//        }
//
//        @Override
//        public void onDisconnected(@NonNull CameraDevice camera) {
//            cameraOpenCloseLock.release();
//            cameraDevice.close();
//            cameraDevice = null;
//        }
//
//        @Override
//        public void onError(@NonNull CameraDevice camera, int error) {
//            cameraOpenCloseLock.release();
//            cameraDevice.close();
//            cameraDevice = null;
//            Activity activity = MainActivity.this;
//            if (activity != null) {
//                activity.finish();
//            }
//        }
//    };
//
//    private CameraCaptureSession previewSession;
//
//    private void startPreview() {
//        if (cameraDevice == null || !textureView.isAvailable() || previewSize == null) {
//            return;
//        }
//        try {
//            closePreviewSession();
//            SurfaceTexture texture = textureView.getSurfaceTexture();
//            assert texture != null;
//            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
//            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            Surface previewSurface = new Surface(texture);
//            previewBuilder.addTarget(previewSurface);
//            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
//                    new CameraCaptureSession.StateCallback() {
//                        @Override
//                        public void onConfigured(@NonNull CameraCaptureSession session) {
//                            previewSession = session;
//                            updatePreview();
//                        }
//                        @Override
//                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//                            Log.e("TAG", "onConfigureFailed: Failed ");
//                        }
//                    }, backgroundHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void closePreviewSession() {
//        if (previewSession != null) {
//            previewSession.close();
//            previewSession = null;
//        }
//    }
//
//    private void updatePreview() {
//        if (null == cameraDevice) {
//            return;
//        }
////        try {
////            setUpCaptureRequestBuilder(mPreviewBuilder);
////            HandlerThread thread = new HandlerThread("CameraPreview");
////            thread.start();
////            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
////        } catch (CameraAccessException e) {
////            e.printStackTrace();
////        }
//    }
//
//    private HandlerThread backgroundThread;
//
//    private Handler backgroundHandler;
//
//    private void startBackgroundThread() {
//        backgroundThread = new HandlerThread("CameraBackground");
//        backgroundThread.start();
//        backgroundHandler = new Handler(backgroundThread.getLooper());
//    }
//
//    private void stopBackgroundThread() {
//        backgroundThread.quitSafely();
//        try {
//            backgroundThread.join();
//            backgroundThread = null;
//            backgroundHandler = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startBackgroundThread();
//        permissionCheck();
//    }
//
//    @Override
//    protected void onPause() {
//        closeCamera();
//        stopBackgroundThread();
//        super.onPause();
//    }
//
//    private void closeCamera() {
//        try {
//            cameraOpenCloseLock.acquire();
//            closePreviewSession();
//            if (cameraDevice != null) {
//                cameraDevice.close();
//                cameraDevice = null;
//            }
////            if (null != mMediaRecorder) {
////                mMediaRecorder.release();
////                mMediaRecorder = null;
////            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Interrupted while trying to lock camera closing.");
//        } finally {
//            cameraOpenCloseLock.release();
//        }
//    }

}