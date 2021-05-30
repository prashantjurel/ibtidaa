package com.prashant.ibtidaa.common.loginSignup;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prashant.ibtidaa.Database.UserDataHelperClass;
import com.prashant.ibtidaa.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.prashant.ibtidaa.common.loginSignup.LoginActivity.encodeUserEmail;

public class OTPVerificationActivity extends Activity {

    private TextView phoneNumberView;
    private PinView pinview;
    private FirebaseAuth mAuth;
    private String codeBySystem;
    private MaterialButton confirmSignUp;
    private ImageView onCloseButton;
    private LinearLayout otpProgressBtn;
    String phoneNumber, fullName,emailAddress,password,typeOfCall;

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
        setContentView(R.layout.activity_otpverification);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //Hooks
        phoneNumberView = findViewById(R.id.phoneNumberAtOtpVerify);
        pinview = findViewById(R.id.pin_view);
        confirmSignUp = findViewById(R.id.confirmSignup);
        onCloseButton = findViewById(R.id.verificationOtp_back);
        otpProgressBtn = findViewById(R.id.otpProgressBar);

        //get all the data from intent
        phoneNumber = getIntent().getExtras().getString("phone_number");
        fullName = getIntent().getExtras().getString("full_name");
        emailAddress = getIntent().getExtras().getString("email_address");
        password = getIntent().getExtras().getString("password");
        typeOfCall = getIntent().getExtras().getString("typeOfCall");

        phoneNumberView.setText("+91"+phoneNumber);

        sendVerificationCodeToUser("+91"+phoneNumber);

        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(LoginActivity.isConnectedToInternet(OTPVerificationActivity.this))) {
                    showConnectToInternetDialog();
                }
                else {
                    String code = pinview.getText().toString();
                    if (!code.isEmpty()) {
                        otpProgressBtn.setVisibility(View.VISIBLE);
                        onCloseButton.setEnabled(false);
                        pinview.setEnabled(false);
                        confirmSignUp.setEnabled(false);
                        verifyCode(code);
                    }
                    else{
                        Toast.makeText(OTPVerificationActivity.this, "Please enter the one time pin received", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        onCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpScreenActivity.class);
                startActivity(intent);
                OTPVerificationActivity.this.finish();
            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNumberPassed) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumberPassed)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)// Timeout and unit
                        .setActivity(this)             // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                pinview.setText(code);
                onCloseButton.setEnabled(false);
                pinview.setEnabled(false);
                confirmSignUp.setEnabled(true);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
            Toast.makeText(OTPVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
        
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(typeOfCall.equals("updatePassword")){
                                updateOldUserPassword();
                            }
                            else{
                                phoneNumber="+91"+phoneNumber;
                                storeNewUserData();
                                Toast.makeText(OTPVerificationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPVerificationActivity.this, "Verification Not Completed. Try Again!", Toast.LENGTH_SHORT).show();
                            }
                            onCloseButton.setEnabled(true);
                            pinview.setEnabled(true);
                            confirmSignUp.setEnabled(true);
                        }
                    }
                });
    }

    private void updateOldUserPassword() {
        Intent intent = new Intent(getApplicationContext(),SetNewPasswordActivity.class);
        intent.putExtra("email_address",emailAddress);
        startActivity(intent);
        finish();
    }

    private void storeNewUserData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        emailAddress = encodeUserEmail(emailAddress);

        UserDataHelperClass addNewUser = new UserDataHelperClass(fullName, emailAddress, phoneNumber,password);

        databaseReference.child(emailAddress).setValue(addNewUser);

    }

    private void showConnectToInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OTPVerificationActivity.this, R.style.MyDialogTheme);
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
                        startActivity(new Intent(getApplicationContext(), SignUpScreenActivity.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}