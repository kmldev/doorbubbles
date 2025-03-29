package com.laundry.bubbles;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.preferences.SharedPrefrence;
import com.laundry.bubbles.ui.activity.Dashboard;
import com.laundry.bubbles.ui.activity.LanguageSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefference = SharedPrefrence.getInstance(MainActivity.this);
        mContext = MainActivity.this;
        
        // Initialize permissions based on Android version
        initializePermissions();
    }
    
    private void initializePermissions() {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.CAMERA);
        
        // Storage permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        
        permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add(Manifest.permission.CALL_PHONE);
        
        permissions = permissionList.toArray(new String[0]);
    }


        private final Runnable mTask = new Runnable() {
            @Override
            public void run() {
                if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
                    Intent in = new Intent(mContext, Dashboard.class);
                    applyLanguage(prefference.getValue(Consts.LANGUAGE));
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent in = new Intent(mContext, LanguageSelection.class);
                    in.putExtra(Consts.TYPE, 1);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }
            }
        };
    
        @Override
        protected void onResume() {
            super.onResume();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!hasPermissions(MainActivity.this, permissions)) {
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    handler.postDelayed(mTask, SPLASH_TIME_OUT);
                }
            } else {
                handler.postDelayed(mTask, SPLASH_TIME_OUT);
            }
        }
    
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                               int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
                boolean allPermissionsGranted = true;
                
                for (int i = 0; i < permissions.length && i < grantResults.length; i++) {
                    boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    
                    if (Manifest.permission.CAMERA.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, isGranted);
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[i]) ||
                               Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i]) ||
                               Manifest.permission.READ_MEDIA_IMAGES.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, isGranted);
                    } else if (Manifest.permission.ACCESS_NETWORK_STATE.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, isGranted);
                    } else if (Manifest.permission.CALL_PHONE.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.CALL_PHONE, isGranted);
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.FINE_LOC, isGranted);
                    } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i])) {
                        prefference.setBooleanValue(Consts.CORAS_LOC, isGranted);
                    }
                    
                    if (!isGranted) {
                        allPermissionsGranted = false;
                    }
                }
                
                // Proceed even if some permissions are denied
                handler.postDelayed(mTask, 0);
            }
        }
    
        public static boolean hasPermissions(Context context, String... permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
    
        public void applyLanguage(String languageCode) {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            
            Resources resources = mContext.getResources();
            Configuration config = new Configuration(resources.getConfiguration());
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }
