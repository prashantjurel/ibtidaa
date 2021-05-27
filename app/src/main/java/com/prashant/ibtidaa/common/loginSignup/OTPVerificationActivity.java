package com.prashant.ibtidaa.common.loginSignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prashant.ibtidaa.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class OTPVerificationActivity extends Activity {

    private TextView phoneNumber;
    private PinView pinview;
    private FirebaseAuth mAuth;
    private String codeBySystem;
    private Button confirmSignUp;
    private ImageView onCloseButton;

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
        phoneNumber = findViewById(R.id.phoneNumberAtOtpVerify);
        pinview = findViewById(R.id.pin_view);
        confirmSignUp = findViewById(R.id.confirmSignup);
        onCloseButton = findViewById(R.id.verificationOtp_back);

        String phoneNumberPassed = "+91"+getIntent().getExtras().getString("phone_number");
        String firstNamePassed = getIntent().getExtras().getString("first_name");
        String lastNamePassed = getIntent().getExtras().getString("last_name");
        String emailAddressPassed = getIntent().getExtras().getString("email_address");
        String passwordPassed = getIntent().getExtras().getString("password");

        phoneNumber.setText(phoneNumberPassed);

        sendVerificationCodeToUser(phoneNumberPassed);

        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinview.getText().toString();
                if(!code.isEmpty()){
                    verifyCode(code);
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(OTPVerificationActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPVerificationActivity.this, "Verification Not Completed. Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}