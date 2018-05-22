package com.example.sherlock.fixmyeyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

/**
 * Created by root on 29/10/17.
 */

public class signIn extends AppCompatActivity{
    private LoginButton fbloginButton;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login);
        progressDialog = new ProgressDialog(this);
        callbackManager = CallbackManager.Factory.create();
        fbloginButton = findViewById(R.id.login_button);
        fbloginButton.setReadPermissions("public_profile", "email","user_birthday","user_relationships","user_photos");
        fbloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbloginButton.registerCallback(callbackManager, mCallBack);
                progressDialog.setMessage("Wait, Signing In...");
                progressDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            Log.e("success","LOGIN");
            progressDialog.dismiss();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            response.toString();
                            try {
                                String id = object.getString("id");
                                String first_name = object.getString("first_name");
                                String last_name = object.getString("last_name");
                                String profilePicUrl = "https://graph.facebook.com/"+id+"/picture?width=200&height=200";
                                SharedPreferences mSharedPreferences = getSharedPreferences("mySharedPreferences",MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putBoolean("LOGGED_IN",true);
                                editor.putString("LOGIN_MODE","FACEBOOK_LOGIN");
                                editor.putString("USER_NAME",first_name + " " + last_name);
                                editor.putString("USER_ID",id);
                                editor.putString("PROFILE_PIC_URL",profilePicUrl);
                                editor.apply();
                                Log.e("DATA",id + " " + first_name + " "+ last_name + " " + profilePicUrl);
                                Intent intent = new Intent(signIn.this, askforpic.class);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.getClass().getSimpleName();
                                e.printStackTrace();
                                Log.e("Getting","Exception");
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,birthday," +
                    "relationship_status,first_name,last_name");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.e("ERORO","oncancel");
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("FB","ERROR HAPPEDNED");
            progressDialog.dismiss();
        }
    };
}
