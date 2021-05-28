package com.prashant.ibtidaa.common.loginSignup;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.prashant.ibtidaa.HomeActivity;
import com.prashant.ibtidaa.R;

import org.json.JSONObject;

import java.util.Arrays;

public class SignUpScreenActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;
    private SignInButton signInButton;
    private AwesomeValidation mAwesomeValidation;
    private TextInputLayout fullName,emailAddress,password,phoneNumber;
    private Button createAccountButton;

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
        setContentView(R.layout.activity_signup);

        TextView t2 = (TextView) findViewById(R.id.term_and_conditions);
        t2.setMovementMethod(LinkMovementMethod.getInstance());


        //Hooks
        fbLoginButton = findViewById(R.id.sign_in_button_fb);
        signInButton = findViewById(R.id.sign_in_button_gmail);
        fullName = findViewById(R.id.signUpFullName);
        emailAddress = findViewById(R.id.signUpEmailAddress);
        password = findViewById(R.id.signUpPassword);
        createAccountButton = findViewById(R.id.createAccount);
        phoneNumber = findViewById(R.id.signUpPhoneNumber);



        //Initialization Of Validation before Submission
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.error_appearance);

        //Add Validations for text Fields Before Submission
        mAwesomeValidation.addValidation(this, R.id.signUpFullName, "[a-zA-Z\\s]+", R.string.err_fullName);
        mAwesomeValidation.addValidation(this, R.id.signUpEmailAddress, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        String phoneNumberValidation = "^[6-9]\\d{9}$";
        mAwesomeValidation.addValidation(this,R.id.signUpPhoneNumber,phoneNumberValidation,R.string.err_phone);
        String regexPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        mAwesomeValidation.addValidation(this, R.id.signUpPassword, regexPassword, R.string.err_Password);


        //<------------------------------------GOOGLE SIGN IN----------------------------------------->
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // Set the dimensions of the sign-in button.

        signInButton.setSize(SignInButton.SIZE_ICON_ONLY);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button_gmail:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        //<------------------------------------FACEBOOK SIGN IN----------------------------------------->

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setPermissions(Arrays.asList("email"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("demo","login successful");
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Log.d("demo","login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("demo","login unsuccessful");
            }
        });

    }

    public void redirectToLogin(View view){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void redirectToOtpVerification(View view){

        if(mAwesomeValidation.validate()){
            Log.d("testing","test");
            Intent intent = new Intent(SignUpScreenActivity.this,OTPVerificationActivity.class);
            intent.putExtra("full_name",fullName.getEditText().getText().toString().trim());
            intent.putExtra("phone_number",phoneNumber.getEditText().getText().toString().trim());
            intent.putExtra("email_address",emailAddress.getEditText().getText().toString().trim());
            intent.putExtra("password",password.getEditText().getText().toString().trim());
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please check input data once", Toast.LENGTH_LONG).show();
        }

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("demo",object.toString());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields","name,first_name,last_name,id");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

        //google login
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
            }
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("signInResult:failed" , e.toString());
        }
    }
}