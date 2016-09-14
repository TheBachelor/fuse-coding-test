package com.motiontech.fusecodingtest;

/*
    Author: Manenga Mungandi
    Objective: Validate a company exist within Fuse platform
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.androidadvance.topsnackbar.TSnackbar;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load correct xml layout for activity
        setContentView(R.layout.activity_main);

        //bind edit text object to view in xml
        editText = (EditText) findViewById(R.id.edt_text);

        //set done button
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //set listener for when done is pressed
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView txtView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //validate value of edit text to be more than one, then trim before proceeding
                    if(editText.getText().toString().length() > 1) {
                        performSearch(editText.getText().toString().trim());
                        return true;
                    }
                    else {
                        //Display message if validation fails

                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "Company name must be more than one character.", TSnackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.WHITE);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
                return false;
            }
        });
    }

    private void performSearch(String companyName) {

        //Create progress dialog
        final LovelyProgressDialog dialog = new LovelyProgressDialog(this);
        dialog.setTitle("Company Validation");
        //dialog.setIcon(R.drawable.zzz_account);
        dialog.setTopColor(Color.parseColor("#9ffb99"));
        dialog.setMessage("Validating your company...");
        dialog.show();

        JsonObjectRequester mRequester = new RequestBuilder(this)
                .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                .showError(true) //Show error with toast on Network or Server error
                .shouldCache(true)
                .buildObjectRequester(new Response.ObjectResponse() {

                    @Override
                    public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
                        //this could be response code ????
                        if(requestCode == 200){

                        }
                        else {

                        }
                    }

                    @Override
                    public void onErrorResponse(int requestCode, com.android.volley.VolleyError volleyError, @Nullable JSONObject errorObject) {
                        System.out.println("REQUEST ERROR RESPONSE");
                        System.out.println("REQUEST ERROR: " + errorObject);
                    }

                    @Override
                    public void onFinishResponse(int requestCode, @Nullable com.android.volley.VolleyError volleyError, String message) {
                        dialog.dismiss();

                        System.out.println("REQUEST FINISH RESPONSE");
                        System.out.println("REQUEST RESPONSE: " + message);
                    }

                    @Override
                    public void onRequestStart(int requestCode) {
                        System.out.println("REQUEST STARTED");
                        System.out.println("REQUEST CODE: " + requestCode);
                    }

                    @Override
                    public void onRequestFinish(int requestCode) {
                        System.out.println("REQUEST FINISHED");
                        System.out.println("REQUEST CODE: " + requestCode);
                    }
                });

        JSONObject data = new JSONObject();

        //set up endpoint and make request
        mRequester.request(Methods.GET, companyName + ".fusion-universal.com/api/v1/company.json", data);
    }
}
