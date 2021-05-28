package com.prashant.ibtidaa.common.loginSignup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prashant.ibtidaa.R;

import org.jetbrains.annotations.NotNull;

import static com.prashant.ibtidaa.common.loginSignup.LoginActivity.encodeUserEmail;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailAddress;
    private Button nextBtn;
    private AwesomeValidation mAwesomeValidation;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_forgot_password);

        //hooks
        emailAddress = findViewById(R.id.forgotPasswordEmail);
        nextBtn = findViewById(R.id.forgotPasswordNextBtn);
        progressBar = findViewById(R.id.forgotPasswordProgressBar);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.error_appearance);

        String phoneNumberValidation = "^[6-9]\\d{9}$";
        mAwesomeValidation.addValidation(this, R.id.loginEmailAddress, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmailAddress(view);
            }
        });

    }

    private void verifyEmailAddress(View view) {

        if (!isConnectedToInternet()) {
            showConnectToInternetDialog();
        }

        if(mAwesomeValidation.validate()){
            progressBar.setVisibility(View.VISIBLE);
            //get data
            String emailAddressInput = emailAddress.getEditText().getText().toString().trim();
            emailAddressInput = encodeUserEmail(emailAddressInput);

            //set data in database
            Query checkUserExists = FirebaseDatabase.getInstance().getReference("Users").orderByChild("emailAddress").equalTo(emailAddressInput);
            String finalEmailAddressInput = emailAddressInput;
            checkUserExists.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        emailAddress.setError(null);
                        emailAddress.setErrorEnabled(false);
                        String phoneNumber = snapshot.child(finalEmailAddressInput).child("phoneNumber").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),OTPVerificationActivity.class);
                        intent.putExtra("phone_number",phoneNumber);
                        intent.putExtra("email_address",finalEmailAddressInput);
                        intent.putExtra("typeOfCall","updatePassword");
                        startActivity(intent);
                        finish();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPasswordActivity.this, "Email is not registered with us.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    /*Checking For Internet Connection*/
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ForgotPasswordActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if ((activeNetwork != null) && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE))) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        builder.setMessage("Please connect to internet to proceed further.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}