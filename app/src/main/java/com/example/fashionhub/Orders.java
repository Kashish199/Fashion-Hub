package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.fashionhub.adapter.OrderAdapter;
import com.example.fashionhub.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {
    /**
     * variable declarration for db object
     */
    FirebaseFirestore db;
    /**
     * variable declarration for authentication object
     */
    private FirebaseAuth auth;
    /**
     * variable declarration for current user
     */
    private FirebaseUser curUser;
    /**
     * variable declarration fot  order recyclerview
     */
    RecyclerView orderItemRecycler;
    /**
     * variable declarration for order adapter
     */
    OrderAdapter orderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        final ArrayList<OrderModel> productsList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        final String userid = String.valueOf(curUser.getUid());


        db.collection("Orders")
                .whereEqualTo("Userid", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String name = (String) document.getData().get("ProductName");
                                String quantity = String.valueOf(document.getData().get("productQty"));
                                String apt = (String) document.getData().get("Apt");
                                String image = (String) document.getData().get("Image");
                                String address = (String) document.getData().get("Address");
                                String city = (String) document.getData().get("City");
                                String postal = (String) document.getData().get("Postal");
                                String price = String.valueOf(document.getData().get("price"));
                                Uri myUri = Uri.parse(image);
                                productsList.add(new OrderModel(name, quantity, price, myUri, apt, postal, city, address));

                            }
                            setProdItemRecycler(productsList);


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * function to set item on recyclerview
     *
     * @param productsList
     */
    public void setProdItemRecycler(List<OrderModel> productsList) {

        orderItemRecycler = findViewById(R.id.order_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        orderItemRecycler.setLayoutManager(layoutManager);
        orderAdapter = new OrderAdapter(this, productsList);
        orderItemRecycler.setAdapter(orderAdapter);


    }
}