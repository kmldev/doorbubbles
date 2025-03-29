package com.laundry.bubbles.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laundry.bubbles.ModelClass.ChatListDTO;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.https.HttpsRequest;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.interfaces.Helper;
import com.laundry.bubbles.network.NetworkManager;
import com.laundry.bubbles.preferences.SharedPrefrence;
import com.laundry.bubbles.ui.adapter.ChatListAdapter;
import com.laundry.bubbles.utils.CustomTextView;
import com.laundry.bubbles.utils.CustomTextViewBold;
import com.laundry.bubbles.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatList extends AppCompatActivity {
    private String TAG = ChatList.class.getSimpleName();
    private RecyclerView rvChatList;
    private ChatListAdapter chatListAdapter;
    private ArrayList<ChatListDTO> chatList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextView tvNo;
    Context mContext;


    ImageView IVback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mContext=ChatList.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setUiAction();
    }


    public void setUiAction() {
        tvNo = findViewById(R.id.tvNo);
        IVback = findViewById(R.id.IVback);
        rvChatList = findViewById(R.id.rvChatList);

        mLayoutManager = new LinearLayoutManager(mContext);
        rvChatList.setLayoutManager(mLayoutManager);

        chatListAdapter = new ChatListAdapter(mContext, chatList);
        rvChatList.setAdapter(chatListAdapter);

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(mContext)) {
            getChat();

        } else {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
        }
    }

    public void getChat() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GETMESSAGEHISTORY, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    rvChatList.setVisibility(View.VISIBLE);
                    try {
                        chatList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ChatListDTO>>() {
                        }.getType();
                        chatList = (ArrayList<ChatListDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        chatListAdapter = new ChatListAdapter(mContext, chatList);
        rvChatList.setAdapter(chatListAdapter);
    }


}
