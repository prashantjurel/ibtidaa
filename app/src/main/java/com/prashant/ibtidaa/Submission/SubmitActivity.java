package com.prashant.ibtidaa.Submission;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;
import com.prashant.ibtidaa.HomeActivity;
import com.prashant.ibtidaa.R;
import com.prashant.ibtidaa.common.RealPathUtil;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SubmitActivity extends AppCompatActivity {
    private Button btnSend;
    private ImageView backBtnSubmit;
    private RadioButton reciteYes, reciteNo;
    private TextView uploadAudioTextName;
    private RadioGroup mRadioGroup;
    private EditText title, writtenBy, fullPiece, submitBy, idHandle;
    private LinearLayout frameLayout;
    private AwesomeValidation mAwesomeValidation;
    private String wantToRecite, filePath, fileName;
    private MaterialButton uploadAudioBtn;

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
        setContentView(R.layout.activity_submit);

        //Initialization of Recite string
        wantToRecite = "No";

        //Hooks
        title = findViewById(R.id.title_piece);
        writtenBy = findViewById(R.id.name_author);
        fullPiece = findViewById(R.id.full_piece);
        submitBy = findViewById(R.id.submit_author);
        idHandle = findViewById(R.id.id_handle);
        btnSend = findViewById(R.id.submitData);
        backBtnSubmit = findViewById(R.id.submit_back);
        mRadioGroup = findViewById(R.id.recite_select);
        reciteYes = findViewById(R.id.recite_yes);
        reciteNo = findViewById(R.id.recite_no);
        frameLayout = findViewById(R.id.layout_audio_file_container);
        uploadAudioBtn = findViewById(R.id.browseAudioFile);
        uploadAudioTextName = findViewById(R.id.browseAudioFileName);

        //Initialization Of Validation before Submission
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        //Add Validations for text Fields Before Submission
        mAwesomeValidation.addValidation(this, R.id.title_piece, "[a-zA-Z\\s]+", R.string.err_title_piece);
        mAwesomeValidation.addValidation(this, R.id.name_author, "[a-zA-Z\\s]+", R.string.err_name_author);
        mAwesomeValidation.addValidation(this, R.id.full_piece, "[a-zA-Z\\s]+", R.string.err_full_piece);
        mAwesomeValidation.addValidation(this, R.id.submit_author, "[a-zA-Z\\s]+", R.string.err_submit_author);
        mAwesomeValidation.addValidation(this, R.id.id_handle, "[^ ]", R.string.err_id_handle);


        //Radio Button Action on Recite

        reciteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wantToRecite = "Yes";
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        reciteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.GONE);
            }
        });

        //Animation on Radio Button
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout_recite_radio);


        //Submission Page Back Button Action
        backBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubmitActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        fileName = queryName(getContentResolver(), uri);
                        filePath = RealPathUtil.getRealPath(getApplicationContext(),uri);
                        uploadAudioTextName.setText(fileName);
                        uploadAudioTextName.setVisibility(View.VISIBLE);
                        Log.i("Test.activity", "testing");

                    }
                });

        //Intent to upload audio File
        uploadAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("audio/*");
            }
        });


        //Submit Button (Sending data to mail)
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAwesomeValidation.validate() && (mRadioGroup.getCheckedRadioButtonId() != -1)
                        && /*isNetworkConnected() && isInternetConnected() &&*/ (uploadAudioTextName.getText() != "")) {

                    final String username = "imaginedragonwashere@gmail.com";
                    final String password = "4050pp01";

                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");

                    String msgToSend = "Title: " + title.getText().toString() + "\n\n" +
                            "Written By: " + writtenBy.getText().toString() + "\n\n" +
                            "Full Piece:\n" + fullPiece.getText().toString() + "\n\n" +
                            "Wants To Recite:\n" + wantToRecite + "\n\n" +
                            "Submitted By: " + submitBy.getText().toString() + "\n\n" +
                            "InstaHandle: " + idHandle.getText().toString() + "\n\n" +
                            "Regards, \nIbtidaa App";

                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse("iambenzmarshall@gmail.com"));
                        message.setSubject("New Submission Request On Ibtidaa App");
                        message.setText(msgToSend);

                        MimeBodyPart messageBodyPart = new MimeBodyPart();

                        Multipart multipart = new MimeMultipart();

                        messageBodyPart = new MimeBodyPart();
                        String attachedFileName = fileName;
                        DataSource source = new FileDataSource(filePath);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(attachedFileName);
                        multipart.addBodyPart(messageBodyPart);

                        message.setContent(multipart);

                        Transport.send(message);
                        title.setText(null);
                        writtenBy.setText(null);
                        fullPiece.setText(null);
                        submitBy.setText(null);
                        idHandle.setText(null);
                        uploadAudioTextName.setText(null);
                        mRadioGroup.clearCheck();
                        frameLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Submission Successful", Toast.LENGTH_LONG).show();

                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (!(mAwesomeValidation.validate())) {
                        Toast.makeText(getApplicationContext(), "Please check input data once", Toast.LENGTH_LONG).show();
                    } else if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select any one on recite", Toast.LENGTH_LONG).show();
                    } else if (uploadAudioTextName.getText().length() == 0 || uploadAudioTextName.getText() == null) {
                        Toast.makeText(getApplicationContext(), "Please upload the audio file", Toast.LENGTH_LONG).show();
                    } else if (!(isNetworkConnected()) || !(isInternetConnected())) {
                        Toast.makeText(getApplicationContext(), "Please check your internet Connection", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    //Checking Network Connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) SubmitActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    //Query to get uploaded audio file Name
    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}
