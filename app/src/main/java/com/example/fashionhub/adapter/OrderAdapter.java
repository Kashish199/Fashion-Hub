package com.example.fashionhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionhub.R;
import com.example.fashionhub.model.OrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    Context context;
    List<OrderModel> productsList;

    public OrderAdapter(Context context, List<OrderModel> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Picasso.get().load(productsList.get(position).getDetailmageUrl()).into(holder.prodImage);
        holder.prodName.setText(productsList.get(position).getProductName());
        holder.prodQty.setText("Quantity " + productsList.get(position).getProductQty());
        holder.prodPrice.setText("Price " + productsList.get(position).getProductPrice());
        holder.apt.setText("Apt:- " + productsList.get(position).getflat());
        holder.address.setText("Address:- " + productsList.get(position).getaddress());
        holder.city.setText("City:- " + productsList.get(position).getcity());
        holder.postal.setText("Postal:- " + productsList.get(position).getpostal());

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice, apt, address, city, postal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage = itemView.findViewById(R.id.oimage);
            prodName = itemView.findViewById(R.id.oname);
            prodPrice = itemView.findViewById(R.id.oprice);
            prodQty = itemView.findViewById(R.id.oquantity);
            apt = itemView.findViewById(R.id.oapt);
            address = itemView.findViewById(R.id.oaddress);
            city = itemView.findViewById(R.id.ocity);
            postal = itemView.findViewById(R.id.opostal);


        }
    }
}
