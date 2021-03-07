package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductDetails extends AppCompatActivity {

    /**
     * variable declaration for imageview
     */
    ImageView back;
    /**
     * variable declaration button
     */
    Button addtocart;
    /**
     * variable declaration for textview
     */
    TextView description, price, quantity, name, category, color, event, stock;
    /**
     * variable declaration for imageview
     */
    ImageView remove, add, productimage;
    /**
     * variable declaration for db object
     */
    FirebaseFirestore db;
    /**
     * variable declaration
     */
    private FirebaseUser curUser;
    /**
     * variable declaration for authentication object
     */
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
//        back = findViewById(R.id.back);
        addtocart = findViewById(R.id.addtocart);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        productimage = findViewById(R.id.productimage);
        color = findViewById(R.id.color);
        event = findViewById(R.id.event);
        stock = findViewById(R.id.stock);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final String pid;
        final Bundle b = getIntent().getExtras();


        String id = b.getString("id");
        pid = id;
        String p_description = b.getString("Description");
        final String p_price = b.getString("Price");
        final String p_name = b.getString("Name");
        String p_category = b.getString("Category");
        String p_event = b.getString("Event");
        String p_color = b.getString("Color");
        final String p_image = b.getString("Image");
        curUser = auth.getCurrentUser();
        final String userid = String.valueOf(curUser.getUid());

        getStockData(pid);

        /**
         * go back function
         */
        /**
         * add to cart functionality
         */
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle b = getIntent().getExtras();
                int p_qty = b.getInt("Qty");
                int qty = p_qty;
                final int p_quantity = Integer.parseInt(quantity.getText().toString());
                double price = p_quantity * Integer.parseInt(p_price);
                Date date = new Date();

                if (!(p_quantity == 0) && p_quantity <= qty) {

                    Map<String, Object> data = new HashMap<>();
                    data.put("id", UUID.randomUUID().toString());
                    data.put("Userid", userid);
                    data.put("Quantity", p_quantity);
                    data.put("Productid", pid);
                    data.put("Name", p_name);
                    data.put("Price", price);
                    data.put("Image", p_image);

                    db.collection("Cart").document(String.valueOf(date))
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("", "DocumentSnapshot successfully written!");
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Added",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent i = new Intent(getApplicationContext(), Cart.class);
                                    startActivity(i);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("", "Error writing document", e);
                                }
                            });

                    db.collection("Products")
                            .whereEqualTo("ProductID", pid)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("", document.getId() + " => " + document.getData());
                                            System.out.println(document.getId() + " => " + document.getData());
                                            String q = (String) document.getData().get("Qty");
                                            int qty = Integer.parseInt(q.toString());
                                            int newData = qty - p_quantity;
                                            String newData1 = String.valueOf(newData);
                                            Map<String, Object> usermap1 = new HashMap<>();
                                            usermap1.put("UserID", newData1);
                                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                                            fstore.collection("Products").document(pid).update("Qty", newData1)
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

                                        }

                                    } else {
                                        Log.d("", "Error getting documents: ", task.getException());
                                    }
                                }
                            });


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "This much quantity is not available in stock",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        /**
         * increase the quantity
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p_quantity = Integer.parseInt(quantity.getText().toString());
                p_quantity = p_quantity + 1;
                quantity.setText("" + p_quantity);

            }
        });

        /**
         * decrease the quantity
         */
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p_quantity = Integer.parseInt(quantity.getText().toString());
                if (p_quantity > 0) {
                    p_quantity = p_quantity - 1;
                    quantity.setText("" + p_quantity);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Quantity can not be negative",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        description.setText("" + p_description);
        category.setText("" + p_category);
        price.setText("$" + p_price);
        name.setText("" + p_name);
        event.setText("" + p_event);
        color.setText("" + p_color);

        Picasso
                .get()
                .load(p_image)
                .noFade()
                .into(productimage);

    }

    private void getStockData(String pid) {

        db.collection("Products")
                .whereEqualTo("ProductID", pid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String qty = (String) document.getData().get("Qty");
                                stock.setText(qty);
                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}