package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    Button signup;

    EditText fname, lnumber, email, password, confirmpassword;
    TextView login_btn1;
    private FirebaseAuth mFirebaseAuth;
    ProgressDialog pd;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = (Button) findViewById(R.id.signup);
        mFirebaseAuth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.fname);
        lnumber = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.semail);
        password = (EditText) findViewById(R.id.spassword);
        confirmpassword = (EditText) findViewById(R.id.sconfirmpassword);

        login_btn1 = findViewById(R.id.loginbb);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("Userdata", Context.MODE_PRIVATE);

        login_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = fname.getText().toString();
                String Phone = lnumber.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmpassword.getText().toString();

                if (Name.isEmpty() || Phone.isEmpty() || Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                    Toast.makeText(Signup.this, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length() < 6) {

                    Toast.makeText(Signup.this, "Password should be 6 characters or long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Phone.length() < 10) || (Phone.length() > 10)) {
                    Toast.makeText(Signup.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(Signup.this, "Confirm password doesn't match with password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(Email)) {
                    Toast.makeText(Signup.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USEREmailID", Email);
                editor.putString("USERPassword", ConfirmPassword);
                editor.commit();
                pd = new ProgressDialog(Signup.this);
                pd.setMessage("Loading...");
                pd.show();
//                FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                String UserId = user.getUid();

                final Map<String, Object> usermap = new HashMap<>();
                usermap.put("Name", Name);
                usermap.put("Phone", Phone);
                usermap.put("Email", Email);
                usermap.put("Customer", "Buyer");
                usermap.put("AdminApprove", "Yes");
                usermap.put("UserID", "UserId");

                mFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            db.collection("User").document(mFirebaseAuth.getCurrentUser().getUid()).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String newData = mFirebaseAuth.getCurrentUser().getUid();

                                        Map<String, Object> usermap1 = new HashMap<>();
                                        usermap1.put("UserID", newData);
                                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                                        fstore.collection("User").document(newData).update("UserID", newData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("tagvv", "ITS WORKING");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                            }
                                        });
                                        Toast.makeText(getApplicationContext().getApplicationContext(), "Register Success!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        pd.dismiss();
                                    }
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Signup.this, "Email id already Exist", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Signup.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });
            }
        });

    }

    /**
     * email validity function
     *
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}