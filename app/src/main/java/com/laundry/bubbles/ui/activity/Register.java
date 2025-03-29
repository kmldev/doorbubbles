package com.laundry.bubbles.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.ActivityRegisterBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.network.NetworkManager;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private String TAG = Register.class.getSimpleName();

    Context mContext;
    ActivityRegisterBinding binding;
    boolean doubleClick=true;
    private boolean isHide = false;
    boolean numCheck=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = Register.this;
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_register);

        setUiAction();

    }

    public void setUiAction() {

        binding.cvRegister.setOnClickListener(this);
        binding.loginNow.setOnClickListener(this);
        binding.ivOldPass.setOnClickListener(this);
        binding.ivNewPass.setOnClickListener(this);


        binding.cetNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>9){
                    if(isValidPhoneNumber(String.valueOf(s))){
                        boolean status = validateUsing_libphonenumber(String.valueOf(s));
                        if(status){
                            // Toast.makeText(mContext, "Valid Phone Number (libphonenumber)", Toast.LENGTH_SHORT).show();
                            //  tvIsValidPhone.setText("Valid Phone Number (libphonenumber)");

                            numCheck=true;
                        } else {
/*
                            binding.cetNumber.setError(getResources().getString(R.string.entValNumber));
                            binding.cetNumber.requestFocus();*/
                            numCheck=false;
                            //   tvIsValidPhone.setText("Invalid Phone Number (libphonenumber)");
                        }
                    }
                    else {

                        numCheck=true;
                        // tvIsValidPhone.setText("Invalid Phone Number (isValidPhoneNumber)");
                    }

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_now:
                if(doubleClick){
                    Intent intent_forgot_password = new Intent(mContext, Login.class);
                    startActivity(intent_forgot_password);
                    doubleClick=false;
                }
                break;

                case R.id.cvRegister:
                    if(doubleClick)
                    clickForSubmit();
                break;


            case R.id.ivOldPass:
                if (isHide) {
                    binding.ivOldPass.setImageResource(R.drawable.eye);
                    binding.cetPassword1.setTransformationMethod(null);
                    binding.cetPassword1.setSelection(binding.cetPassword1.getText().length());
                    isHide = false;
                } else {
                    binding.ivOldPass.setImageResource(R.drawable.passwordhide);
                    binding.cetPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.cetPassword1.setSelection(binding.cetPassword1.getText().length());
                    isHide = true;
                }
                break;
            case R.id.ivNewPass:
                if (isHide) {
                    binding.ivNewPass.setImageResource(R.drawable.eye);
                    binding.cetPassword2.setTransformationMethod(null);
                    binding.cetPassword2.setSelection(binding.cetPassword2.getText().length());
                    isHide = false;
                } else {
                    binding.ivNewPass.setImageResource(R.drawable.passwordhide);
                    binding.cetPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.cetPassword2.setSelection(binding.cetPassword2.getText().length());
                    isHide = true;
                }
                break;
        }
    }

    public void clickForSubmit() {
        if(!ProjectUtils.isEditTextFilled(binding.cetName)){
            showSickbar(getResources().getString(R.string.val_name));
        }else if (!ProjectUtils.isEmailValid(   binding.cetEmail.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        }else if (!ProjectUtils.isPhoneNumberValid(binding.cetNumber.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_num));
        }else if (!numCheck) {
            showSickbar(getResources().getString(R.string.val_num));
        } else if (!ProjectUtils.isPasswordValid(binding.cetPassword1.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else if (!ProjectUtils.isPasswordValid(binding.cetPassword2.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else if (ProjectUtils.getEditTextValue(binding.cetPassword1).equals(ProjectUtils.getEditTextValue(binding.cetPassword2))) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    registerUser();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

        }else  showSickbar(getResources().getString(R.string.pass_notmatch));


    }


    public void registerUser() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.SIGNUP, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        doubleClick=false;
                        ProjectUtils.showToast(mContext, msg);
                        Intent in = new Intent(mContext, Login.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    doubleClick=true;
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.cetName));
        parms.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.cetEmail));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.cetPassword2));
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.cetNumber));
        parms.put(Consts.COUNTRY_CODE, "91");
        parms.put(Consts.DEVICE_TYPE, "android");
        parms.put(Consts.DEVICE_TOKEN, "android");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }
    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean validateUsing_libphonenumber(String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(mContext);
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt("+91"));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }
        try{   boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                // Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
                return true;
            } else {
                // Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
                return false;
            }}catch (Exception e){e.printStackTrace();}

        return false;
    }


}
