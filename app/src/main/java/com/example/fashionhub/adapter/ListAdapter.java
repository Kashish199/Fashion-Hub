package com.example.fashionhub.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fashionhub.R;
import com.example.fashionhub.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    FirebaseFirestore db;

    Context context;
    List<CartModel> productsList;

    public ListAdapter(Context context, List<CartModel> productsList) {
        this.context = context;
        this.productsList = productsList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_list_item, parent, false);
        return new ListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, final int position) {
        Picasso.get().load(productsList.get(position).getImageUrl()).into(holder.prodImage);
        holder.prodName.setText(productsList.get(position).getProductName());
        holder.prodQty.setText("Quantity " + productsList.get(position).getProductQty());
        holder.prodPrice.setText("Price " + productsList.get(position).getProductPrice());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.Cancel) {

                    String pid = productsList.get(position).getProductid();
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

                                            String qty1 = productsList.get(position).getProductQty();
                                            SystemClock.sleep(3000);
                                            String pid = productsList.get(position).getProductid();

                                            int p_quantity = Integer.parseInt(qty1);

                                            int newData = qty + p_quantity;
                                            String newData2 = String.valueOf(newData);
                                            Map<String, Object> usermap1 = new HashMap<>();
                                            usermap1.put("UserID", newData2);
                                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                                            fstore.collection("Products").document(pid).update("Qty", newData2)
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

                    Query productIdRef = db.collection("Cart")
                            .whereEqualTo("id", productsList.get(position).getId());

                    productIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();

                            }
                            productsList.remove(position);
                            notifyDataSetChanged();


                        }


//

                    });

                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public static final class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice;
        Button cancel;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage = itemView.findViewById(R.id.cimage);
            prodName = itemView.findViewById(R.id.cname);
            prodPrice = itemView.findViewById(R.id.cprice);
            prodQty = itemView.findViewById(R.id.cquantity);
            cancel = itemView.findViewById(R.id.Cancel);
        }
    }

}