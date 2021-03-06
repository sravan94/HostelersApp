package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.backend.HostelSignUpResult;
import com.example.hostelers.backend.RetrofitInterface;
import com.example.hostelers.R;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HostelSignUpActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_sign_up);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        final EditText hostelName = findViewById(R.id.hostel_name), hostelLocation = findViewById(R.id.location), hostelWarden = findViewById(R.id.warden_name);
        final EditText emailWarden = findViewById(R.id.warden_email_hostelSignUp), mobile = findViewById(R.id.warden_mobile_hostelSignup);
        Button submit = findViewById(R.id.hostel_signUp_submit);
        MyEditTextListener myTextListener = new MyEditTextListener();
        hostelName.setOnEditorActionListener(myTextListener);
        hostelLocation.setOnEditorActionListener(myTextListener);
        hostelWarden.setOnEditorActionListener(myTextListener);
        emailWarden.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String email = emailWarden.getText().toString();
                if(email.isEmpty()){
                    emailWarden.setError("Mandatory: Can't be Empty.");
                }else{
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@[\\w.]{3,10}",email)){
                        emailWarden.setError("Mandatory: email invalid");
                    }
                }
                return true;
            }
        });
        mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String mobile_num = mobile.getText().toString();
                if(mobile_num.isEmpty()){
                    mobile.setError("Mandatory: Can't be Empty.");
                }else{
                    if (mobile_num.length() != 10){
                        mobile.setError("Mandatory: should have 10 digits");
                    }
                }
                return true;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                String hostelName_text = hostelName.getText().toString(), hostelLocation_text = hostelLocation.getText().toString(), wardenName = hostelWarden.getText().toString(), email = emailWarden.getText().toString(), mobile_num = mobile.getText().toString();
                if (hostelName_text.isEmpty()) {
                    hostelName.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (hostelLocation_text.isEmpty()) {
                    hostelLocation.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (wardenName.isEmpty()) {
                    hostelWarden.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (email.isEmpty()) {
                    emailWarden.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@[\\w.]{4,20}",email)){
                        emailWarden.setError("Mandatory: email invalid");
                    }
                }
                if (mobile_num.isEmpty()) {
                    mobile.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if (mobile_num.length() != 10){
                        mobile.setError("Mandatory: should have 10 digits");
                    }
                }
                if (flag) {
                    HashMap<String, String> details = new HashMap<>();
                    details.put("hostelName",hostelName_text);
                    details.put("hostelLocation", hostelLocation_text);
                    details.put("wardenName", wardenName);
                    details.put("wardenEmail", email);
                    details.put("wardenNumber", mobile_num);
                    Call<HostelSignUpResult> hostelDetails = retrofitInterface.executeHostelSignUp(details);
                    hostelDetails.enqueue(new Callback<HostelSignUpResult>() {
                        @Override
                        public void onResponse(Call<HostelSignUpResult> call, Response<HostelSignUpResult> response) {
                            int response_code = response.code();
                            if(response_code == 200){
                                HostelSignUpResult result = response.body();
                                Toast.makeText(getApplicationContext(), "SignUp Successful!\n Please check your mail for credentials.", Toast.LENGTH_LONG).show();
                                sendMail(result.getEmail(),result.getWardenId(), result.getWardenKey());
                                finish();
                            }
                            else if(response_code == 409){
                                openAlertDialog("User Exists!");
                            }
                        }

                        @Override
                        public void onFailure(Call<HostelSignUpResult> call, Throwable t) {
                            openAlertDialog("Request Error! Try Again.");
                        }
                    });
                }
            }
        });

    }

    private void openAlertDialog(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        }).show();
    }

    private void sendMail(String email, String id, String key){
        String message = "Welcome to Hostelers!\nYour Warden ID: "+id+"\nYour Password: "+key+"\n Thank You for being a part of Hostelers!.";
        String emails[] = {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hostelers Credentials");
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
