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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laundry.bubbles.ModelClass.OfferDTO;
import com.laundry.bubbles.ModelClass.PopLaundryDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.FragmentOfferShopBinding;
import com.laundry.bubbles.databinding.FragmentOffersBinding;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.ui.activity.Schedule_Activity;
import com.laundry.bubbles.ui.activity.ServiceAcitivity;
import com.laundry.bubbles.ui.adapter.OffersAdapter;
import com.laundry.bubbles.ui.adapter.OffersOtherAdapter;
import com.laundry.bubbles.utils.EndlessRecyclerOnScrollListener;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferShopFragment extends Fragment {

    String TAG = OfferShopFragment.class.getSimpleName();

    FragmentOfferShopBinding binding;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OfferDTO> offerDTOS=new ArrayList<>();
    HashMap<String,String> params=new HashMap<>();
    OffersOtherAdapter offersAdapter;

    private Bundle bundle;
    PopLaundryDTO popLaundryDTO;
    ServiceAcitivity serviceAcitivity;
    boolean checkClick=true;
    private int currentVisibleItemCount = 0;
    int page = 20;
    boolean request = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate( inflater,R.layout.fragment_offer_shop, container, false);


        bundle = this.getArguments();
        popLaundryDTO = (PopLaundryDTO) bundle.getSerializable(Consts.SHOPDTO);
        getOffer();


        binding.cvSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClick) {

                Intent in = new Intent(getActivity(), Schedule_Activity.class);
                in.putExtra(Consts.SHOPDTO,popLaundryDTO);
                startActivity(in);
                    checkClick=false;
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvoffer.setLayoutManager(linearLayoutManager);

/*
        binding.rvoffer.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager)linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page, int totalItemCount) {
                currentVisibleItemCount = totalItemCount;
                if (request) {
                    page = page + 2;


                }else {
                    page = 2;
                    getOffer();

                }

            }
        });
        */
        getOffer();


        return binding.getRoot();


    }

    private void getOffer() {

        ProjectUtils.getProgressDialog(getActivity());
        params.put(Consts.Count, String.valueOf(page));
        new HttpsRequest(Consts.GETALLOFFER, params,getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        offerDTOS = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<OfferDTO>>() {
                        }.getType();
                        offerDTOS = (ArrayList<OfferDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);


                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });


    }

    private void setData() {


        offersAdapter = new OffersOtherAdapter(getActivity(), offerDTOS);
        binding.rvoffer.setAdapter(offersAdapter);
        binding.rvoffer.smoothScrollToPosition(currentVisibleItemCount);
        binding.rvoffer.scrollToPosition(currentVisibleItemCount-1);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        serviceAcitivity = (ServiceAcitivity) context;
    }



    @Override
    public void onResume() {
        super.onResume();
        checkClick=true;
    }


}
