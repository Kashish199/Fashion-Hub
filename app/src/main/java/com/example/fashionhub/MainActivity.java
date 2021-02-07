package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;


public class MainActivity extends AppCompatActivity {
    /**
     * variable declaration for button
     */
    Button login;
    /**
     * variable declaration for textview
     */
    TextView useroption , forgotpass;
    /**
     * variable declaration for authentication object
     */
    private FirebaseAuth auth;
    /**
     * variable declaration for current user
     */
    private FirebaseUser curUser;
    /**
     * variable declaration edittext
     */
    EditText email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);
        useroption = (TextView) findViewById(R.id.useroption);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        forgotpass = findViewById(R.id.forgot);

        /**
         * login functionality
         */

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String lemail = email.getText().toString();
                String lpass = pass.getText().toString();
                final String admin = "admin@fashionhub.com";
                if (lemail.isEmpty() || lpass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill the form", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(lemail, lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            curUser = auth.getCurrentUser();
                            if (admin.equals(lemail)) {
                                Toast.makeText(getApplicationContext(), "Admin Login Success!", Toast.LENGTH_LONG).show();
                                Intent a = new Intent(getApplicationContext(), AdminRole.class);
                                startActivity(a);
                            } else {

                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Query document = db.collection("User").whereEqualTo("Email", lemail);
                                Log.d("TAG", "value => " + document);

                                document.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("TAG", document.getId() + " => " + document.getData());
                                                if (document.getData().containsValue("Yes")) {
                                                    String Customer =  (String) document.getData().get("Customer");
                                                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(getApplicationContext(), Home.class);
                                                    i.putExtra("Customer", Customer);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "You are not approved by admin", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Log.w("TAG", "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                            }
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(getApplicationContext(), "Email not exist!", Toast.LENGTH_LONG).show();
                                email.getText().clear();
                                pass.getText().clear();
                                email.setError("Email not exist!");
                                email.requestFocus();
                                return;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_LONG).show();
                                pass.getText().clear();
                                pass.setError("Wrong Password!");
                                pass.requestFocus();
                                return;
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        /**
         *  go to signup activity
         */
        useroption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u = new Intent(getApplicationContext(), User.class);
                startActivity(u);

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        curUser=auth.getCurrentUser();
//        if(curUser!=null){
//            Intent i = new Intent(getApplicationContext(),Home.class);
//            startActivity(i);
//
//        }
//    }
}
