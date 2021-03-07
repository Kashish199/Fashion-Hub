package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    TextView signup;
    EditText email;
    Button reset;
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.resetemail);
        reset = findViewById(R.id.resetpassword);
        signup = findViewById(R.id.signup_option);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u = new Intent(getApplicationContext(), User.class);
                startActivity(u);

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress = email.getText().toString();

                if (!isEmailValid(emailAddress)) {
                    Toast.makeText(ForgotPassword.this, "Please enter valid email ", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    System.out.println(emailAddress);
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassword.this, "Reset Password Link has been sent to given registered email", Toast.LENGTH_SHORT).show();
                                        Log.d("", "Email sent.");
                                    } else {
                                        try {
                                            throw task.getException();

                                        } catch (FirebaseAuthInvalidUserException e) {
                                            Toast.makeText(ForgotPassword.this, "This Email is not registered", Toast.LENGTH_SHORT).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                }
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}