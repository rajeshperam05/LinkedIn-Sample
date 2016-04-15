package com.rajesh.linking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "LinkedIn";
    public static final String keyhash = "Uu6gZoHTCqrzNvJPb9qkrCRAO30=";

    private Button btLogin;
    private Button btApi;
    private Button btLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogin = (Button) findViewById(R.id.btLogin);
        btApi = (Button) findViewById(R.id.btApi);
        btLink = (Button) findViewById(R.id.btLink);

        btLogin.setOnClickListener(this);
        btApi.setOnClickListener(this);
        btLink.setOnClickListener(this);


    }

    private static Scope getScope(){
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

        Log.e(TAG, "on Activity Result");

    }

    private void checkSession(){

        Boolean liSessionIsValid = LISessionManager.getInstance(this).getSession().isValid();

            btApi.setEnabled(liSessionIsValid);
            btLink.setEnabled(liSessionIsValid);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btLogin:

                LISessionManager.getInstance(MainActivity.this).init(MainActivity.this, getScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Log.e(TAG, "Authentication success");

                        // Check is token is expired on not.
                        checkSession();

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Log.e(TAG, "Authentication failed " + error.toString());

                        Toast.makeText(getApplicationContext(), "Somthing went wrong try again.", Toast.LENGTH_SHORT).show();

                    }
                }, true);

                break;

            case R.id.btApi:

                startActivity(new Intent(this, ShareData.class));

                break;

            case R.id.btLink:

                startActivity(new Intent(this, ProfileLinking.class));

                break;

        }

    }
}
