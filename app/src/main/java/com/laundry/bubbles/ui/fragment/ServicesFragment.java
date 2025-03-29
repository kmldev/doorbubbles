package com.laundry.bubbles.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laundry.bubbles.ModelClass.PopLaundryDTO;
import com.laundry.bubbles.ModelClass.ShopServicesDTO;
import com.laundry.bubbles.ModelClass.ShopServicesDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.FragmentServicesBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.ui.activity.Schedule_Activity;
import com.laundry.bubbles.ui.activity.ServiceAcitivity;
import com.laundry.bubbles.ui.adapter.OffersAdapter;
import com.laundry.bubbles.ui.adapter.ServiceAdapter;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicesFragment extends Fragment {

    String TAG = ServicesFragment.class.getSimpleName();
    FragmentServicesBinding binding;

    private Bundle bundle;
    PopLaundryDTO popLaundryDTO;
    HashMap<String,String> params=new HashMap<>();
    
    ArrayList<ShopServicesDTO>shopServicesDTOS=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ServiceAdapter serviceAdapter;
    boolean checkClick=true;
    ServiceAcitivity serviceAcitivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding= DataBindingUtil.inflate(inflater,R.layout.fragment_services, container, false);

        bundle = this.getArguments();
        popLaundryDTO = (PopLaundryDTO) bundle.getSerializable(Consts.SHOPDTO);
         getSerivces();

         binding.cvSchedule.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(shopServicesDTOS.size()>0) {
                     if (checkClick) {
                         Intent in = new Intent(getActivity(), Schedule_Activity.class);
                         in.putExtra(Consts.SHOPDTO, popLaundryDTO);
                         startActivity(in);
                         checkClick=false;
                     }
                 }
                 else {
                 }
             }
         });
        return binding.getRoot();
    }




    @Override
    public void onResume() {
        super.onResume();
        checkClick=true;
    }



    private void getSerivces() {

        ProjectUtils.getProgressDialog(getActivity());
        params.put(Consts.SHOP_ID,popLaundryDTO.getShop_id());
        new HttpsRequest(Consts.GETSHOPSERVICES, params,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        shopServicesDTOS = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<ShopServicesDTO>>() {
                        }.getType();
                        shopServicesDTOS = (ArrayList<ShopServicesDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);

                        binding.rvServices.setVisibility(View.VISIBLE);
                        binding.ctvnodata.setVisibility(View.GONE);
                        binding.cvSchedule.setVisibility(View.VISIBLE);
                        setData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    binding.rvServices.setVisibility(View.GONE);
                    binding.ctvnodata.setVisibility(View.VISIBLE);
                    binding.cvSchedule.setVisibility(View.GONE);

                }
            }
        });


    }

    private void setData() {


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvServices.setLayoutManager(linearLayoutManager);
        serviceAdapter = new ServiceAdapter(getActivity(), shopServicesDTOS);
        binding.rvServices.setAdapter(serviceAdapter);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        serviceAcitivity = (ServiceAcitivity) context;
    }


}
