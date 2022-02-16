package com.register_Login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.common.AlertOrToastMsg;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tutionapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class OTPValidation extends AppCompatActivity {

    private TextInputEditText firstNumber, secondNumber, thridNumber, fourthNumber;
    private String str_firstNumber, str_secondNumber, str_thridNumber, str_fourthNumber, phoneNumber;
    private AppCompatButton verify;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private AlertOrToastMsg alertOrToastMsg = new AlertOrToastMsg(this);

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_validation);

        firstNumber = findViewById(R.id.textInputEditText);
        secondNumber = findViewById(R.id.textInputEditText2);
        thridNumber = findViewById(R.id.textInputEditText3);
        fourthNumber = findViewById(R.id.textInputEditText4);
        verify = findViewById(R.id.verify_otp);
        mAuth = FirebaseAuth.getInstance();

        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        //phoneNumber = "+919600066074";

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                alertOrToastMsg.ToastMsg("Verification Completed...!");
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                alertOrToastMsg.showAlert("Error",e.getMessage());
            }
        };

        startPhoneNumberVerification(phoneNumber);
        sendVerificationToUser(phoneNumber);


        firstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    secondNumber.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        secondNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    thridNumber.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        thridNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                    fourthNumber.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        verify.setOnClickListener((v) -> {
            str_firstNumber = firstNumber.getText().toString();
            str_secondNumber = secondNumber.getText().toString();
            str_thridNumber = thridNumber.getText().toString();
            str_fourthNumber = fourthNumber.getText().toString();

            if(!str_firstNumber.isEmpty() && !str_secondNumber.isEmpty() && !str_thridNumber.isEmpty() && !str_fourthNumber.isEmpty()){
                //Code for OTP verification
            }
        });
    }

    private void sendVerificationToUser(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (task)-> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            alertOrToastMsg.ToastMsg("Verification Successfull");
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                });
    }
}
