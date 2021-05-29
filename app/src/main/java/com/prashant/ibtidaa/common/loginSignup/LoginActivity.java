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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prashant.ibtidaa.HomeActivity;
import com.prashant.ibtidaa.R;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailAddress, password;
    private TextInputEditText emailAddressText, passwordText;
    private LinearLayout progressBar;
    private MaterialButton loginBtn;
    private AwesomeValidation mAwesomeValidation;
    private TextView forgotPassword,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_login);


        emailAddress = findViewById(R.id.loginEmailAddress);
        emailAddressText = findViewById(R.id.loginEmailAddressInputText);
        password = findViewById(R.id.loginPassword);
        passwordText = findViewById(R.id.loginPasswordInputText);
        progressBar = findViewById(R.id.loginProgressBar);
        loginBtn = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUpForFree);


        mAwesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.error_appearance);

        //Add Validations for text Fields Before Submission
        mAwesomeValidation.addValidation(this, R.id.loginEmailAddress, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        String regexPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        mAwesomeValidation.addValidation(this, R.id.loginPassword, regexPassword, R.string.err_loginPassword);

        //On click function on login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letTheUserLoggedIn();
            }
        });

        //On click function on btns or links
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });

        emailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAwesomeValidation.clear();
            }
        });
        emailAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAwesomeValidation.clear();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAwesomeValidation.clear();
            }
        });

        passwordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAwesomeValidation.clear();
            }
        });
    }

    private void letTheUserLoggedIn() {

        if (!isConnectedToInternet(LoginActivity.this)) {
            showConnectToInternetDialog();
        }
        if (mAwesomeValidation.validate()) {
            progressBar.setVisibility(View.VISIBLE);
            //get data
            String emailAddressInput = emailAddress.getEditText().getText().toString().trim();
            String passwordInput = password.getEditText().getText().toString().trim();
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

                        String systemPassword = snapshot.child(finalEmailAddressInput).child("password").getValue(String.class);
                        if (systemPassword.equals(passwordInput)) {
                            password.setError(null);
                            password.setErrorEnabled(false);
                            progressBar.setVisibility(View.GONE);
                            btnEnabled();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            btnEnabled();
                            Toast.makeText(LoginActivity.this, "Password does not match with our records", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        mAwesomeValidation.clear();
                        btnEnabled();
                        Toast.makeText(LoginActivity.this, "Email is not registered with us.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    mAwesomeValidation.clear();
                    btnEnabled();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void redirectToSignup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpScreenActivity.class);
        startActivity(intent);
    }

    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    /*Checking For Internet Connection*/
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if ((activeNetwork != null) && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE))) {
            return true;
        } else {
            return false;
        }
    }

    private void showConnectToInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.MyDialogTheme);
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

    private void btnEnabled(){
        emailAddress.setEnabled(true);
        password.setEnabled(true);
        forgotPassword.setEnabled(true);
        signUp.setEnabled(true);
    }
    private void btnEDisabled(){
        emailAddress.setEnabled(false);
        password.setEnabled(false);
        forgotPassword.setEnabled(false);
        signUp.setEnabled(false);
    }

}
