package com.laundry.bubbles.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.ActivityManageProfileBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.network.NetworkManager;
import com.laundry.bubbles.preferences.SharedPrefrence;
import com.laundry.bubbles.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ManageProfile extends AppCompatActivity implements View.OnClickListener {
    String TAG = ManageProfile.class.getSimpleName();


    ActivityManageProfileBinding binding;
    Context mContext;
    ProjectUtils projectUtils;
    NetworkManager networkManager;
    SharedPrefrence prefrence;
    UserDTO userDTO;
    HashMap<String, String> params = new HashMap<>();
    public boolean checkAdd=true,doubleCheck=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_profile);
        mContext = ManageProfile.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.USER_ID,userDTO.getUser_id());

        setdata();
        binding.cvUpdate.setOnClickListener(this);
        binding.cetAddress.setOnClickListener(this);
        binding.profileback.setOnClickListener(this);

    }

    private void setdata() {
        Log.e(TAG, "setdata: " + userDTO.getUser_id());

        binding.cetName.setText(userDTO.getName());
        binding.cetNumber.setText(userDTO.getMobile());
        binding.cetEmail.setText(userDTO.getEmail());

        if(userDTO.getAddress().equalsIgnoreCase(""))
        {}else {

            binding.cetAddress.setText(userDTO.getAddress());
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


                case R.id.cvUpdate:

                    if(!ProjectUtils.isEditTextFilled(binding.cetName)){
                        Toast.makeText(mContext,R.string.addname, Toast.LENGTH_SHORT).show();
                    }else if(!ProjectUtils.isEditTextFilled(binding.cetAddress)){
                        Toast.makeText(mContext,R.string.addAddress, Toast.LENGTH_SHORT).show();
                    }else if (!ProjectUtils.isPhoneNumberValid(binding.cetNumber.getText().toString().trim())) {
                        Toast.makeText(mContext,R.string.val_num, Toast.LENGTH_SHORT).show();
                    }else {
                        if(checkAdd) {
                            params.put(Consts.LATITUDE, userDTO.getLatitude());
                            params.put(Consts.LONGITUDE, userDTO.getLatitude());
                        }
                        params.put(Consts.ADDRESS, ProjectUtils.getEditTextValue(binding.cetAddress));

                        getParams();
                        updateProfile();
                    }
                break;
                case R.id.cetAddress:
                    if(doubleCheck){
                        checkAdd=false;
                        findPlace();
                    doubleCheck=false;}

                break;
                case R.id.profileback:
                    onBackPressed();
                break;


        }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    getAddress(data.getDoubleExtra(Consts.LATITUDE, 0.0), data.getDoubleExtra(Consts.LONGITUDE, 0.0));


                } catch (Exception e) {

                }
            }
        }


    }
    public void updateProfile(){
        ProjectUtils.showProgressDialog(mContext,true,getResources().getString(R.string.please_wait));

        new HttpsRequest(Consts.USERUPDATE,params,mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
               ProjectUtils.pauseProgressDialog();
                if (flag){
                    try {
                        Log.e(TAG, "backResponse: "+response.toString());
                        projectUtils.showToast(mContext,msg);
                        userDTO=new Gson().fromJson(response.getJSONObject("data").toString(),UserDTO.class);
                        prefrence.setParentUser(userDTO,Consts.USER_DTO);
                        prefrence.setBooleanValue(Consts.IS_REGISTERED,true);
                        Intent in=new Intent(mContext,Dashboard.class);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    projectUtils.showToast(mContext,msg);
                }

            }
        });
    }

    private HashMap<String, String> getParams() {
        if (!ProjectUtils.isEditTextFilled(binding.cetName))
        params.put(Consts.NAME,ProjectUtils.getEditTextValue(binding.cetName));
        else
            Toast.makeText(mContext,R.string.addname, Toast.LENGTH_SHORT).show();

        params.put(Consts.NAME,ProjectUtils.getEditTextValue(binding.cetName));
        params.put(Consts.MOBILE,ProjectUtils.getEditTextValue(binding.cetNumber));
        params.put(Consts.COUNTRY_CODE,"91");
        params.put(Consts.DEVICE_TYPE,"Android");
        params.put(Consts.DEVICE_TOKEN, Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID));


        return params;
    }




    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
            binding.cetAddress.setText(obj.getAddressLine(0));


            params.put(Consts.ADDRESS, obj.getAddressLine(0));
            params.put(Consts.LATITUDE, String.valueOf(obj.getLatitude()));
            params.put(Consts.LONGITUDE, String.valueOf(obj.getLongitude()));


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        checkAdd=true;

    }





}
