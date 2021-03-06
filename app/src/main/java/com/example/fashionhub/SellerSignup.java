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

public class SellerSignup extends AppCompatActivity {
    /**
     * signup button
     */
    Button btnsellersignup;
    /**
     * editetxt
     */
    EditText fname, lnumber, email, password, confirmpassword, company;
    TextView login_btn;
    /**
     * firebase auth variable
     */
    FirebaseAuth mFirebaseAuth;
    ProgressDialog pd;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellersingup);
        mFirebaseAuth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.names);
        lnumber = (EditText) findViewById(R.id.numbers);
        email = (EditText) findViewById(R.id.emails);
        password = (EditText) findViewById(R.id.passwords);
        confirmpassword = (EditText) findViewById(R.id.confirmpasswords);
        company = (EditText) findViewById(R.id.company);
        btnsellersignup = findViewById(R.id.sellersignupid);

        login_btn = findViewById(R.id.loginss);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        /**
         * fignup button onclick listener
         */
        sp = getSharedPreferences("Userdata", Context.MODE_PRIVATE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnsellersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = fname.getText().toString();
                String Phone = lnumber.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmpassword.getText().toString();
                String Company = company.getText().toString();

                if (Name.isEmpty() || Phone.isEmpty() || Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty() || Company.isEmpty()) {
                    Toast.makeText(SellerSignup.this, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length() < 6) {

                    Toast.makeText(SellerSignup.this, "Password should be 6 characters or long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Phone.length() < 10) || (Phone.length() > 10)) {
                    Toast.makeText(SellerSignup.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(SellerSignup.this, "Confirm password doesn't match with password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(Email)) {
                    Toast.makeText(SellerSignup.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                pd = new ProgressDialog(SellerSignup.this);
                pd.setMessage("Loading...");
                pd.show();

                final Map<String, Object> usermap = new HashMap<>();
                usermap.put("Name", Name);
                usermap.put("Phone", Phone);
                usermap.put("Email", Email);
                usermap.put("Company", Company);
                usermap.put("Customer", "Seller");
                usermap.put("AdminApprove", "No");
                usermap.put("UserID", "UserId");


                mFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SellerSignup.this, new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(SellerSignup.this, "Email id already Exist", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(SellerSignup.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
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

