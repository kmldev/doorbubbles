package com.laundry.bubbles.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laundry.bubbles.ModelClass.CurrencyDTO;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.interfaces.Consts;

import java.lang.reflect.Type;

public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static SharedPrefrence myObj;

    private SharedPrefrence() {

    }

    public void clearAllPreferences() {
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return myPrefs.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }


    public String getValue(String Tag) {
        if (Tag.equalsIgnoreCase(Consts.LATITUDE))
            return myPrefs.getString(Tag, "22.7497853");
        else if (Tag.equalsIgnoreCase(Consts.LONGITUDE))
            return myPrefs.getString(Tag, "75.8989044");
        return myPrefs.getString(Tag, "");
    }

    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public void setParentUser(UserDTO userDTO, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public UserDTO getParentUser(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            UserDTO testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }



    public void setSubscription(CurrencyDTO currencyDTO, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(currencyDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public CurrencyDTO getCurrency(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new CurrencyDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<CurrencyDTO>() {
            }.getType();
            CurrencyDTO testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }

}
