package com.example.fashionhub.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionhub.R;
import com.example.fashionhub.model.ApproveModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.ViewHolder> {

    Context context;
    List<ApproveModel> approve_list;
    String Status;

    public ApproveAdapter(Context context, List<ApproveModel> approve_list) {
        this.context = context;
        this.approve_list = approve_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_admin_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (approve_list != null && approve_list.size() > 0) {
            ApproveModel model = approve_list.get(position);
            holder.sellname.setText(model.getName());
            holder.sellcompany.setText(model.getCompany());

            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    final FirebaseFirestore fstore = FirebaseFirestore.getInstance();


                    Query productIdRef = fstore.collection("User").whereEqualTo("UserID", approve_list.get(position).getId());

                    productIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data1 = document.getData();
                                    String Name = data1.get("Name").toString();
                                    String idUser = data1.get("UserID").toString();
                                    Status = data1.get("AdminApprove").toString();
                                    if (Status.equals("No")) {
                                        DocumentReference washingtonRef = fstore.collection("User").document(idUser);
                                        washingtonRef.update("AdminApprove", "Yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("TAG", "Error updating document", e);
                                                    }
                                                });

//                                        Map<String, Object> userMap = new HashMap<>();
//                                        userMap.put("AdminApprove", "Yes");
//                                        fstore.collection("User").document(idUser).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d("tagvv", "ITS WORKING");
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Log.w("TAG", "Error writing document", e);
//                                                    }
//                                                });
                                    }
                                }
                            } else {
                                Log.d("log", "Error getting documents: ", task.getException());
                            }

                        }
                    });
                }
//                    if(Status.equals("No")) {
////                                        Map<String, Object> userMap = new HashMap<>();
////                                        userMap.put("AdminApprove", "Yes");
////                                        fstore.collection("User").whereEqualTo("Name", Name)
////                                                .set(userMap)
////                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                                    @Override
////                                                    public void onSuccess(Void aVoid) {
////                                                        Log.d("tagvv", "ITS WORKING");
////                                                    }
////                                                })
////                                                .addOnFailureListener(new OnFailureListener() {
////                                                    @Override
////                                                    public void onFailure(@NonNull Exception e) {
////                                                        Log.w("TAG", "Error writing document", e);
////                                                    }
////                                                });
////                                    }
////                                    } else{
////                                        Log.d("tagvv", "No SUCH DATA");
////                                    }
            });

        } else {
            return;
        }

    }

    // productIdRef.get().addOnSuccessListener(new)

////                    Query fdata = fstore.collection("User").whereEqualTo("Name", approve_list.get(position).getName());
//
//                  productIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//
//                            //Toast.makeText(getAp, " Post added Successfully ", Toast.LENGTH_SHORT).show();
//                            //finish();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                            String Error = e.getMessage();
//                            //Toast.makeText(ApproveAdapter.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
//                        }
    // });


    @Override
    public int getItemCount() {
        return approve_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sellname, sellcompany;
        Button approve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sellname = itemView.findViewById(R.id.sellname);
            sellcompany = itemView.findViewById(R.id.sellcompany);
            approve = itemView.findViewById(R.id.approve);
        }
    }
}
