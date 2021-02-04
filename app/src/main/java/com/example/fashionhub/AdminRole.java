package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fashionhub.adapter.ApproveAdapter;
import com.example.fashionhub.model.ApproveModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRole extends AppCompatActivity {

    RecyclerView admin_recycler;
    ApproveAdapter adapter;

    Button refresh, logout;
    List<ApproveModel> approve_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_role);

        refresh = findViewById(R.id.refresh);
        logout = findViewById(R.id.logout_admin);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Admin Logout Successfully!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        setRecyclerview(approve_list);
        getdata();
    }

    private void getdata() {
        String data222 = "No";
        String customer = "Seller";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("User").whereEqualTo("AdminApprove", data222).whereEqualTo("Customer", customer)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String Id = (String) document.getData().get("UserID");
                                String Name = (String) document.getData().get("Name");
                                String Company = String.valueOf(document.getData().get("Company"));

//                        for (DocumentSnapshot querySnapshot : task.getResult()) {
//                            ApproveModel user = new ApproveModel(querySnapshot.getString("Name"), querySnapshot.getString("Company"));
//                            approve_list.add(user);
//                            Log.d("DATADATATDATDA", "" + user);

                                approve_list.add(new ApproveModel(Id, Name, Company));
                            }

                            setRecyclerview(approve_list);
//                            adapter = new ApproveAdapter(AdminRole.this, approve_list);
//                            admin_recycler.setAdapter(adapter);
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "ERROR WHILE FETCHING!", Toast.LENGTH_LONG).show();
//                    }
                });

    }


    private void setRecyclerview(List<ApproveModel> approve_list) {
        admin_recycler = findViewById(R.id.admin_recycler);
        admin_recycler.setHasFixedSize(true);
        admin_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ApproveAdapter(this, approve_list);
        admin_recycler.setAdapter(adapter);
    }

//    private List<ApproveModel> getList() {
//         List<ApproveModel> approve_list = new ArrayList<>();
//         approve_list.add(new ApproveModel("Viraj", "virajcompany"));
//         return approve_list;
//    }
}