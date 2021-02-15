package com.example.fashionhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.fashionhub.UpdateAd;
import com.example.fashionhub.UpdateProduct;
import com.example.fashionhub.model.PostListModel;
import com.example.fashionhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostListViewHolder> {
    Context context;
    List<PostListModel> postlist;
    FirebaseFirestore db;
    FirebaseUser curUser;
    FirebaseAuth auth;
    String userid = null;


    public PostListAdapter(Context context, List<PostListModel> postlist) {
        this.context = context;
        this.postlist = postlist;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            userid = curUser.getUid();
        }

    }

    @NonNull
    @Override
    public PostListAdapter.PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postlist_item, parent, false);

        return new PostListAdapter.PostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostListAdapter.PostListViewHolder holder, final int position) {
        Picasso.get().load(postlist.get(position).getImage()).fit().into(holder.aptimage);
        holder.price.setText("Price:- " + postlist.get(position).getPrice() + "$");
        holder.category.setText("Category:- " + postlist.get(position).getCategory());
        holder.size.setText("Size:- " + postlist.get(position).getSize());
        holder.title.setText(postlist.get(position).getTitle());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateProduct.class);
                i.putExtra("id", postlist.get(position).getProductId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }


    public static final class PostListViewHolder extends RecyclerView.ViewHolder {

        ImageView aptimage;
        TextView price, category, size, title;
        View item;

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
            aptimage = itemView.findViewById(R.id.postlist_image);
            price = itemView.findViewById(R.id.postlist_price);
            category = itemView.findViewById(R.id.postlist_bedroom);
            size = itemView.findViewById(R.id.postlist_type);
            title = itemView.findViewById(R.id.postlist_title);
            item = itemView;


        }
    }
}

