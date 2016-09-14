package com.motiontech.fusecodingtest;

import android.app.Application;

import com.alirezaafkar.json.requester.Requester;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manenga on 2016/09/14.
 */

public class init extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Map<String, String> header = new HashMap<>();
        header.put("charset", "utf-8");

        //set up requester library
        String baseUrl = "https://";
        new Requester.Config(getApplicationContext()).baseUrl(baseUrl).header(header);
    }
}
