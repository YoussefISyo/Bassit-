package com.optim.bassit.ui.activities;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.optim.bassit.R;

import java.util.concurrent.TimeUnit;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class CodeActivity extends AppCompatActivity {

    private MaskedEditText edtMaskedCode;
    private RelativeLayout btnVerify;
    private TextView txtCountDown;
    private RelativeLayout layoutCodeContainer;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    String message;

    private FirebaseAuth mAuth;
    private String mVerificationId, mobile;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        init();
        mobile = getIntent().getExtras().getString("mobile");

        sendVerificationCode(mobile);

        edtMaskedCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edtMaskedCode.setBackgroundResource(R.drawable.bg_edt_focused);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnVerify.setOnClickListener(v -> {
            String code = edtMaskedCode.getRawText();
            if (code.isEmpty() || code.length()<6){
                edtMaskedCode.setError(getString(R.string.error_code));
                edtMaskedCode.requestFocus();
                return;
            }
            verifyVerificationCode(code);

        });


    }

    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        edtMaskedCode = findViewById(R.id.edtMaskedCode);
        btnVerify = findViewById(R.id.btnVerify);
        layoutCodeContainer = findViewById(R.id.layout_code_container);
        txtCountDown = findViewById(R.id.txtCountDown);



        cTimer = new CountDownTimer(30000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds =  millisUntilFinished / 1000;
                if(seconds >= 10){
                    txtCountDown.setText("0:" + millisUntilFinished / 1000);
                }else{
                    txtCountDown.setText("0:0" + millisUntilFinished / 1000);
                }

            }

            @Override
            public void onFinish() {
                txtCountDown.setText(R.string.resend);
                txtCountDown.setClickable(true);
            }
        };

        cTimer.start();

        txtCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCountDown.getText().equals(R.string.resend)){
                    resendVerificationCode(mobile, resendingToken);
                }

            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+213"+ mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                edtMaskedCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
            resendingToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        try{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            //signing the user
          //  myDialog.ShowProgressDialog(true);
            signInWithPhoneAuthCredential(credential);

        }catch(Exception e){
            message = getResources().getString(R.string.snackbar_code_error);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_container_code), message, Snackbar.LENGTH_LONG);
            snackbar.setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(CodeActivity.this, RegisterActivity.class);
                            intent.putExtra("mobile", mobile);
                            startActivity(intent);
                        } else {
                            message = getResources().getString(R.string.snackbar_error);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = getResources().getString(R.string.snackbar_code_error);
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.layout_container_code), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+213"+ phoneNumber) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)               // Timeout duration
                        .setActivity(this)               // Activity (for callback binding)
                        .setCallbacks(mCallbacks)         // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)               // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        cTimer.start();
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }
}