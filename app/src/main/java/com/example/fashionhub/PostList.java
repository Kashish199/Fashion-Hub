package com.example.fashionhub;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionhub.adapter.PostListAdapter;
import com.example.fashionhub.model.PostListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostList extends AppCompatActivity {
    RecyclerView PostListRecycler;

    PostListAdapter postlistAdapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    /**
     * variable declaration authenication object
     */
    FirebaseFirestore db;
    private FirebaseAuth auth;
    /**
     * variable declarationfor current user
     */
    private FirebaseUser curUser;
    String userId = null;
    ArrayList<PostListModel> postlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        getProductList();
    }

    private void getProductList() {
        db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .whereEqualTo("UserID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PostList", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                String title = (String) document.getData().get("Name");
                                String price = (String) document.getData().get("Price");
                                String category = (String) document.getData().get("Category");
                                String size = (String) document.getData().get("Size");
                                getImage(id, title, price, category, size);
                            }
                            setPostListRecycler();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getImage(final String id, final String title, final String price, final String category, final String size) {
        storageReference = FirebaseStorage.getInstance().getReference().child(id + ".png");
        Log.d("PostList",  " => " + storageReference);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                postlist.add(new PostListModel(id, title, price, category, size, uri));
                postlistAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


    private void setPostListRecycler() {
        PostListRecycler = findViewById(R.id.postlist_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        PostListRecycler.setLayoutManager(layoutManager);
        postlistAdapter = new PostListAdapter(this, postlist);
        PostListRecycler.setAdapter(postlistAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        postlist.clear();
        postlistAdapter.notifyDataSetChanged();
        getProductList();
    }
}