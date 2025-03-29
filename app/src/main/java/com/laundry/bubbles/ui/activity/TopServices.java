package com.laundry.bubbles.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laundry.bubbles.ModelClass.PopLaundryDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.ActivityTopServicesBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.preferences.SharedPrefrence;
import com.laundry.bubbles.ui.adapter.PopularFullLaundriesAdapter;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopServices extends AppCompatActivity implements View.OnClickListener{

    ActivityTopServicesBinding binding;
    String TAG= TopServices.class.getSimpleName();

    PopularFullLaundriesAdapter popularFullLaundriesAdapter;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;
    SharedPrefrence prefrence;

    RecyclerView.LayoutManager layoutManager;
     Context kContext;
    String serviceID="";
    HashMap<String,String> params=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kContext=TopServices.this;
        binding= DataBindingUtil.setContentView(this,
                R.layout.activity_top_services);
        binding.back.setOnClickListener(this);

        prefrence=SharedPrefrence.getInstance(kContext);


        if(getIntent().hasExtra(Consts.SERVICE_ID)){
            serviceID=getIntent().getStringExtra(Consts.SERVICE_ID);
            params.put(Consts.SERVICE_ID,serviceID);

            getSerivese();
        }else {        setdata();
        }

    }

    private void getSerivese() {


            params.put(Consts.LATITUDE,prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE,prefrence.getValue(Consts.LONGITUDE));

        new HttpsRequest(Consts.GETLAUNDRYBYSERVICE,params ,kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {
                        popLaundryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        popLaundryDTOArrayList = (ArrayList<PopLaundryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(kContext,LinearLayoutManager.VERTICAL,false);

                        binding.recyleService.setLayoutManager(layoutManager);
                        popularFullLaundriesAdapter = new PopularFullLaundriesAdapter(kContext,popLaundryDTOArrayList);
                        binding.recyleService.setAdapter(popularFullLaundriesAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {

                    ProjectUtils.showToast(kContext, msg);
                }
            }
        });



    }


    public  void setdata(){
        params.put(Consts.Count,"20");
        params.put(Consts.LATITUDE,prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE,prefrence.getValue(Consts.LONGITUDE));

        new HttpsRequest(Consts.GETALLLAUNDRYSHOP,params ,kContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {

                    try {
                        popLaundryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<PopLaundryDTO>>() {
                        }.getType();
                        popLaundryDTOArrayList = (ArrayList<PopLaundryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        layoutManager = new LinearLayoutManager(kContext,LinearLayoutManager.VERTICAL,false);

                        binding.recyleService.setLayoutManager(layoutManager);
                        popularFullLaundriesAdapter = new PopularFullLaundriesAdapter(kContext,popLaundryDTOArrayList);
                        binding.recyleService.setAdapter(popularFullLaundriesAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {

                    ProjectUtils.showToast(kContext, msg);
                }
            }
        });






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
        }

    }
}
