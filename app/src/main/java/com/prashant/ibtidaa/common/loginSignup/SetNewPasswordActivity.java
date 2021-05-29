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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prashant.ibtidaa.R;

public class SetNewPasswordActivity extends AppCompatActivity {

    private TextInputLayout newPassword, confirmPassword;
    private MaterialButton nextBtn;
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
        setContentView(R.layout.activity_set_new_password);

        //hooks
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmNewPassword);
        nextBtn = findViewById(R.id.updatePasswordContinueBtn);
        progressBar = findViewById(R.id.setNewPasswordProgressBar);

        //Initialization Of Validation before Submission
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.error_appearance);

        //Add Validations for text Fields Before Submission
        String regexPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        mAwesomeValidation.addValidation(this, R.id.newPassword, regexPassword, R.string.err_Password);
        mAwesomeValidation.addValidation(this,R.id.confirmNewPassword,newPassword.getEditText().getText().toString().trim(),R.string.err_matchPassword);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewPassword(view);
            }
        });
    }

    private void updateNewPassword(View view) {

        if (!isConnectedToInternet()) {
            showConnectToInternetDialog();
        }

        String newPasswordText = newPassword.getEditText().getText().toString().trim();
        String confirmPasswordText = confirmPassword.getEditText().getText().toString().trim();
        String emailAddress = getIntent().getStringExtra("email_address");

        if(mAwesomeValidation.validate()){
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(emailAddress).child("password").setValue(newPasswordText);
            startActivity(new Intent(getApplicationContext(),PasswordUpdatedActivity.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please verify input data once", Toast.LENGTH_LONG).show();
        }

    }

    /*Checking For Internet Connection*/
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SetNewPasswordActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if ((activeNetwork != null) && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE))) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPasswordActivity.this);
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
                        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}