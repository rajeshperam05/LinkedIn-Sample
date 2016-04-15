package com.rajesh.linking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajesh on 14/4/16.
 */
public class ShareData extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "ShareData";
    public static final String MY_PROFILE_URL = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";
    public static final String SHARE_URL = "https://api.linkedin.com/v1/people/~/shares";
    private Button btGet;
    private TextView tvName;

    private EditText etTitle;
    private EditText etComment;
    private EditText etDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_data);

        btGet = (Button) findViewById(R.id.btPostRequest);
        btGet.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.tvName);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etComment = (EditText) findViewById(R.id.etComment);
        etDescription = (EditText) findViewById(R.id.etDescription);

        // To load current login user details.
        getProfile();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btPostRequest:

                // Share ur data to current logged user
                shareDataToLinkedIn();

                break;
        }
    }

    private void getProfile(){

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, MY_PROFILE_URL, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                Log.e(TAG, apiResponse.toString());

                JSONObject fullObject = apiResponse.getResponseDataAsJson();

                try {
                    String firstName = fullObject.getString("firstName");
                    String lastName = fullObject.getString("lastName");
                    tvName.setText(firstName + " " + lastName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                Log.e(TAG, liApiError.toString());

            }
        });

    }

    private void shareDataToLinkedIn(){

        String comment = etComment.getText().toString();
        String title = etTitle.getText().toString();
        String desc = etDescription.getText().toString();

        try {
            JSONObject body = new JSONObject("{" +
                    "\"comment\": \"" + comment + "\"," +
                    "\"visibility\": { \"code\": \"anyone\" }," +
                    "\"content\": { " +
                    "\"title\": \"" + title + "\"," +
                    "\"description\": \"" + desc + "\"," +
                    "\"submitted-url\": \"http://www.linkedin.com/\"," +
                    "\"submitted-image-url\": \"http://www.example.com/pic.jpg\"" +
                    "}" +
                    "}");

            APIHelper.getInstance(this).postRequest(this, SHARE_URL, body, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.e(TAG, apiResponse.toString());

                    if (apiResponse.getStatusCode() == 201){
                        Toast.makeText(getApplicationContext(), "Content sharing is done", Toast.LENGTH_SHORT).show();

                        etComment.setText("");
                        etTitle.setText("");
                        etDescription.setText("");
                    }
                }

                @Override
                public void onApiError(LIApiError LIApiError) {

                    Log.e(TAG, LIApiError.toString());

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
