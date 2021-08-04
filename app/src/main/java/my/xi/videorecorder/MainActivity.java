package my.xi.videorecorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MultiplePermissionsListener, PermissionRequestErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                permissionCheck();
            }
        },1000);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        hideSystemUI();

    }


    private void permissionCheck() {
        String[] permissions = null;
        // get permissions from manifest
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(info != null) {
            permissions = info.requestedPermissions;
        }
        // run Dexter
        Dexter.withContext(this).withPermissions(permissions)
                .withListener(this)
                .withErrorListener(this)
                .onSameThread()
                .check();
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        if(!multiplePermissionsReport.areAllPermissionsGranted()) {
            if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {

                Log.e("test", "彈系統重來");
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };



            } else {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            permissionCheck();
                        } else if(which == DialogInterface.BUTTON_NEGATIVE) {
                            System.exit(0);
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title_error))
                        .setMessage(getString(R.string.dialog_message_error))
                        .setPositiveButton(getString(R.string.dialog_retry),onClickListener)
                        .setNegativeButton(getString(R.string.dialog_exit),onClickListener)
                        .setCancelable(false);
                builder.show();
            }
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

        Log.e("test", "onPermissionRationaleShouldBeShown");
        permissionToken.continuePermissionRequest();
    }

    @Override
    public void onError(DexterError dexterError) {
        Log.e("test", "onError");

    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();

        int code = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(decorView.getSystemUiVisibility() != code) {
            decorView.setSystemUiVisibility(code);
        }
    }
}