package com.rajesh.linking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;

public class ProfileLinking extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "ProfileLinking";

    private Button btMyProfile;
    private Button btOtherProfile;

    private EditText etProfileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_linking);

        btMyProfile = (Button) findViewById(R.id.btMyProfile);
        btOtherProfile = (Button) findViewById(R.id.btOtherProfile);

        btMyProfile.setOnClickListener(this);
        btOtherProfile.setOnClickListener(this);

        etProfileID = (EditText) findViewById(R.id.etProfileID);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btMyProfile:

                // It opens current login user profile in LinkedIn Application
                openMyProfile();

                break;

            case R.id.btOtherProfile:

                // It opens other profile based on entered profileID in Linked Application
                openOtherProfile();

                break;
        }
    }

    private void openMyProfile(){

        DeepLinkHelper.getInstance().openCurrentProfile(this, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {

                Log.e(TAG, "Moved to Current Profile in LinkedIn app");
            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {

                Log.e(TAG, error.toString());

            }
        });
    }

    private void openOtherProfile(){

        String profileID = etProfileID.getText().toString();

        DeepLinkHelper.getInstance().openOtherProfile(this, profileID, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {

                Log.e(TAG, "Moved to Other Profile in LinkedIn app");


            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {

                Log.e(TAG, error.toString());

            }
        });
    }
}
