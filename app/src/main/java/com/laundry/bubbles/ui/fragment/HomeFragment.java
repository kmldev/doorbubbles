package com.laundry.bubbles.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.laundry.bubbles.ModelClass.GetBannerDTO;
import com.laundry.bubbles.ModelClass.HomeDTO;
import com.laundry.bubbles.ModelClass.NearBYDTO;
import com.laundry.bubbles.ModelClass.PopLaundryDTO;
import com.laundry.bubbles.ModelClass.ServicesDTO;
import com.laundry.bubbles.ModelClass.SpecialOfferPkgDTO;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.HomeFragmentBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.preferences.SharedPrefrence;
import com.laundry.bubbles.ui.activity.AllServices;
import com.laundry.bubbles.ui.activity.Dashboard;
import com.laundry.bubbles.ui.activity.NotificationActivity;
import com.laundry.bubbles.ui.activity.SearchActivity;
import com.laundry.bubbles.ui.activity.TopServices;
import com.laundry.bubbles.ui.adapter.ImageAdapter;
import com.laundry.bubbles.ui.adapter.LaundriesNearAdapter;
import com.laundry.bubbles.ui.adapter.PopularLaundriesAdapter;
import com.laundry.bubbles.ui.adapter.SpecialOffersAdapter;
import com.laundry.bubbles.ui.adapter.TopServiceAdapter;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;


public class HomeFragment extends Fragment implements View.OnClickListener{
    HomeFragmentBinding binding;
    View view;
    ArrayList<GetBannerDTO> imageDTOArrayList;
    private ImageAdapter imageAdapter;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    Dashboard dashBoard;

    String TAG= HomeFragment.class.getSimpleName();

    TopServiceAdapter topServiceAdapter;
    ArrayList<ServicesDTO>servicesDTOArrayList;
    RecyclerView.LayoutManager layoutManagerServ;

    PopularLaundriesAdapter popularLaundriesAdapter;
    ArrayList<PopLaundryDTO>popLaundryDTOArrayList;
    RecyclerView.LayoutManager layoutManagerPop;

    SpecialOffersAdapter specialOffersAdapter;
    ArrayList<SpecialOfferPkgDTO>specialOffersAdapterArrayList;
    RecyclerView.LayoutManager layoutManagerOffer;


    LaundriesNearAdapter laundriesNearAdapter;
    ArrayList<NearBYDTO>nearBYDTOArrayList;
    RecyclerView.LayoutManager layoutManagerNear;

    HashMap<String,String> params=new HashMap<>();
    HomeDTO homeDTO;

    UserDTO userDTO;
    private SharedPrefrence prefrence;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        view = binding.getRoot();
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO=prefrence.getParentUser(Consts.USER_DTO);

        getData();






        return view;
    }


    public void getData(){
        params.put(Consts.LATITUDE,prefrence.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE,prefrence.getValue(Consts.LONGITUDE));
        params.put(Consts.USER_ID,userDTO.getUser_id());



            new HttpsRequest(Consts.GETHOMEDATA,params,getActivity()).stringPost(TAG, new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                    if (flag){

                        try {
                            homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);
                            setupViewPager();

                        }catch (Exception e){
                            e.getMessage();
                        }


                    }

                    else {
                        ProjectUtils.showToast(getActivity(),msg);
                    }
                }
            });

        }





    private void setupViewPager() {

        imageAdapter = new ImageAdapter(homeDTO.getAdvertis(), getActivity());
        binding.viewpager.setAdapter(imageAdapter);
        binding.tabDots.setViewPager(binding.viewpager);




   layoutManagerServ=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyleService.setLayoutManager(layoutManagerServ);
        topServiceAdapter=new TopServiceAdapter(getActivity(),homeDTO.getService());
        binding.recyleService.setAdapter(topServiceAdapter);



        layoutManagerPop=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyleLaundriesPop.setLayoutManager(layoutManagerPop);
        popularLaundriesAdapter=new PopularLaundriesAdapter(getActivity(),homeDTO.getLaundry());
        binding.recyleLaundriesPop.setAdapter(popularLaundriesAdapter);


        layoutManagerOffer=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyleOffers.setLayoutManager(layoutManagerOffer);
        specialOffersAdapter=new SpecialOffersAdapter(getActivity(),homeDTO.getOffer());
        binding.recyleOffers.setAdapter(specialOffersAdapter);



        layoutManagerNear=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyleNear.setLayoutManager(layoutManagerNear);
        laundriesNearAdapter=new LaundriesNearAdapter(getActivity(),homeDTO.getNear_by());
        binding.recyleNear.setAdapter(laundriesNearAdapter);



        binding.ctvbTopService.setOnClickListener(this);
        binding.ctvbPopularLaundries.setOnClickListener(this);
        binding.svLaundry.setOnClickListener(this);
        binding.ivNotification.setOnClickListener(this);



    }



    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onPause() {
      //  stopLocationUpdates();
        super.onPause();
    }









    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dashBoard = (Dashboard) context;
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.ctvbTopService:
            Intent in = new Intent(getActivity(), AllServices.class);
            startActivity(in);
            break;
        case R.id.ctvbPopularLaundries:
            Intent in1 = new Intent(getActivity(), TopServices.class);
            startActivity(in1);
            break;
        case R.id.svLaundry:
            Intent in2 = new Intent(getActivity(), SearchActivity.class);
            startActivity(in2);
            break;
        case R.id.ivNotification:
            Intent in3 = new Intent(getActivity(), NotificationActivity.class);
            startActivity(in3);
            break;
    }
    }




}
