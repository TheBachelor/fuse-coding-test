package com.motiontech.fusecodingtest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Manenga on 2016/09/14.
 */

public class Company extends Object {

    String name  = "";
    String logo = "";
    String custom_color = "#ffffff";
    JSONObject password_changing = new JSONObject();

    public Company(String name, String logo, String custom_color, boolean enabled, boolean secure){
        this.name = name;
        this.logo = logo;
        this.custom_color = custom_color;

        try {
            this.password_changing.put("enabled", enabled);
            this.password_changing.put("secure_field", secure);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
