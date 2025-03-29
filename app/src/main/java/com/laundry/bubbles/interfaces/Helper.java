package com.laundry.bubbles.interfaces;

import org.json.JSONException;
import org.json.JSONObject;


public interface Helper {
    public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException;
}
