package com.motiontech.fusecodingtest;

/*
    Author: Manenga Mungandi
    Objective: Validate a company exist within Fuse platform
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.androidadvance.topsnackbar.TSnackbar;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load correct xml layout for activity
        setContentView(R.layout.activity_main);

        //bind edit text and image view objects to views in xml
        editText = (EditText) findViewById(R.id.edt_text);
        imageView = (ImageView) findViewById(R.id.imageView);

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

                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.BLACK);
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
                return false;
            }
        });

        //set listener when user retries to search for company
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Clear and hide image view if visible
                if(imageView.isShown()) {
                    imageView.setImageResource(android.R.color.transparent);
                    imageView.setVisibility(View.GONE);
                }
                editText.setBackgroundColor(Color.WHITE);
                editText.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        System.out.println("REQUEST onResponse " + jsonObject);
                        System.out.println("REQUEST requestCode: " + requestCode);
                        dialog.dismiss();

                        //this couldn't use response code in this library so I found another way
                        if(!jsonObject.optString("name").isEmpty()){
                            showAlertWith("Company exists.");

                            String name = jsonObject.optString("name");
                            String logo = jsonObject.optString("logo");
                            String custom_color = jsonObject.optString("custom_color");
                            boolean enabled = jsonObject.optJSONObject("password_changing").optBoolean("enabled");
                            boolean secure = jsonObject.optJSONObject("password_changing").optBoolean("secure_field");

                            Company company = new Company(name, logo, custom_color, enabled, secure);
                            editText.setText(company.name);
                            imageView.setVisibility(View.VISIBLE);

                            editText.setTextColor(Color.WHITE);
                            editText.setBackgroundColor(Color.parseColor("#16CA81"));
                            System.out.println("setBackgroundColor: GREEN");
                            Picasso.with(MainActivity.this).load(company.logo).into(imageView);
                        }
                        else {
                            editText.setTextColor(Color.WHITE);
                            editText.setBackgroundColor(Color.RED);
                            showAlertWith("Company does not exist.");
                        }
                    }

                    @Override
                    public void onErrorResponse(int requestCode, com.android.volley.VolleyError volleyError, @Nullable JSONObject errorObject) {
                        dialog.dismiss();

                        editText.setTextColor(Color.WHITE);
                        editText.setBackgroundColor(Color.RED);
                        showAlertWith("Company does not exist.");
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

    private void showAlertWith(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
