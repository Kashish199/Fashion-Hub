package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionhub.adapter.ProductAdapter;
import com.example.fashionhub.adapter.ProductCategoryAdapter;
import com.example.fashionhub.model.ProductCategory;
import com.example.fashionhub.model.Products;
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
import java.util.List;


public class Home extends AppCompatActivity {

    /**
     * variable declaration for adapter
     */

    ProductCategoryAdapter productCategoryAdapter;
    /**
     * variable declaration for recyclerview
     */
    RecyclerView productCatRecycler, prodItemRecycler;
    /**
     * variable declaration for prodcut adapter
     */
    ProductAdapter productAdapter;
    /**
     * variable declaration for textview
     */
    TextView girl, boy, men, women;
    /**
     * variable declaration
     */
    Button cart;
    /**
     * variable declaration for authentication oobject
     */
    private FirebaseAuth auth;
    /**
     * variable declaration for current user
     */
    private FirebaseUser curUser;
    /**
     * variable declaration
     */
    Object doc;

    String Customer , Total;
    /**
     * variable declaration
     */
    FirebaseFirestore db;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        girl = (TextView) findViewById(R.id.girl);
        boy = (TextView) findViewById(R.id.boy);
        men = (TextView) findViewById(R.id.men);
        women = (TextView) findViewById(R.id.women);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        Customer = i.getStringExtra("Customer");


        List<ProductCategory> productCategoryList = new ArrayList<>();
        final List<Products> productsList = new ArrayList<>();
        productCategoryList.add(new ProductCategory("se", "Events"));
        productCategoryList.add(new ProductCategory("dd", "event"));
//        productCategoryList.add(new ProductCategory(3, "All Body Products"));
//        productCategoryList.add(new ProductCategory(4, "Skin Care"));
//        productCategoryList.add(new ProductCategory(5, "Hair Care"));
//        productCategoryList.add(new ProductCategory(6, "Make Up"));
//        productCategoryList.add(new ProductCategory(7, "Fragrance"));
        setProductRecycler(productCategoryList);
        setdata(productsList, "Boy");


//    final Typeface typeface = g(R.font.rubik_bold_italic);
//        final Typeface typeface1 = getResources().getFont(R.font.rubik);
////            (R.font.rubik_bold_italic);

        final int white = Color.parseColor("#060001");
        final int yellow = Color.parseColor("#ffc107");
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                women.setTextColor(white);
                boy.setTextColor(white);
                men.setTextColor(white);
//                men.setTypeface(typeface1);
//                boy.setTypeface(typeface1);
//                women.setTypeface(typeface1);

                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick){
                    girl.setTextColor(yellow);
//                       girl.setTypeface(typeface);
                } else {
                    girl.setTextColor(white);
                }

                removeitem(productsList);
                setdata(productsList, "Girl");
            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girl.setTextColor(white);
                men.setTextColor(white);
                women.setTextColor(white);
//                men.setTypeface(typeface1);
//                girl.setTypeface(typeface1);
//                women.setTypeface(typeface1);
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick){
                    boy.setTextColor(yellow);
//                    boy.setTypeface(typeface);
                } else {
                    boy.setTextColor(white);
                }
                removeitem(productsList);
                setdata(productsList, "Boy");
            }
        });

        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girl.setTextColor(white);
                boy.setTextColor(white);
                women.setTextColor(white);
//                girl.setTypeface(typeface1);
//                boy.setTypeface(typeface1);
//                women.setTypeface(typeface1);
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick){
                    men.setTextColor(yellow);
//                    men.setTypeface(typeface);
                } else {
                    men.setTextColor(white);
                }
                // men.setHighlightColor(R.font.rubik_bold_italic);
                removeitem(productsList);
                setdata(productsList, "Men");

            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girl.setTextColor(white);
                boy.setTextColor(white);
                men.setTextColor(white);
//                men.setTypeface(typeface1);
//                boy.setTypeface(typeface1);
//                girl.setTypeface(typeface1);
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick){
                    women.setTextColor(yellow);
//                    women.setTypeface(typeface);
                } else {
                    women.setTextColor(white);
                }

                removeitem(productsList);
                setdata(productsList, "Women");


            }
        });


    }

    /**
     * this method is use to set data
     *
     * @param productsList
     * @param subbrand
     */
    private void setdata(final List<Products> productsList, final String subbrand) {


        db.collection("Products")
                .whereEqualTo("Category", subbrand)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String name = (String) document.getData().get("Name");
                                String description = (String) document.getData().get("Description");
                                String size = (String) document.getData().get("Size");
                                String price = (String) document.getData().get("Price");
                                String id = (String)(document.getId());
                                String image = (String) document.getData().get("Image");
                                String detail_image = (String) document.getData().get("Detail_image");
                                getImage(id, image, name, description, size, price, productsList, subbrand, detail_image);
                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * remover item from productlist
     *
     * @param productsList
     */
    private void removeitem(List<Products> productsList) {
        int size = productsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                productsList.remove(0);
                productAdapter.notifyItemRemoved(i);
                productAdapter.notifyItemRangeChanged(i, productsList.size());
            }
        }
    }


    /**
     * get image from firestore
     *
     * @param id
     * @param image
     * @param name
     * @param description
     * @param size
     * @param price
     * @param productsList
     * @param category
     * @param detail_image
     */
    private void getImage(final String id, final String image, final String name, final String description, final String size, final String price, final List<Products> productsList, final String category, final String detail_image) {
        Log.d("", description);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ;

        final StorageReference storageRef = storage.getReference();
        storageRef.child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                storageRef.child(detail_image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri detail_image) {
                        productsList.add(new Products(id, name, size, price, uri, description, category, detail_image));
                        setProdItemRecycler(productsList);

                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        Toast.makeText(this, "Testing Toast", Toast.LENGTH_SHORT).show();

    }

    /**
     * set item for product category
     *
     * @param productCategoryList
     */
    private void setProductRecycler(List<ProductCategory> productCategoryList) {

        productCatRecycler = findViewById(R.id.cat_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);

    }

    /**
     * set item for product list
     *
     * @param productsList
     */
    private void setProdItemRecycler(List<Products> productsList) {
        prodItemRecycler = findViewById(R.id.product_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        prodItemRecycler.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this, productsList);
        prodItemRecycler.setAdapter(productAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (Customer.equals("Seller")) {
            MenuItem profile = menu.findItem(R.id.Profile);
            profile.setVisible(true);//
            MenuItem addProduct = menu.findItem(R.id.addProduct);
            addProduct.setVisible(true);
            return true;
        } else {
            MenuItem profile = menu.findItem(R.id.Profile);
            profile.setVisible(true);//
            MenuItem addProduct = menu.findItem(R.id.addProduct);
            addProduct.setVisible(false);
            return true;
        }
    }

    /**
     * switch case for options menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.addProduct:
                if (Customer.equals("Seller")) {
                    Intent p = new Intent(getApplicationContext(), AddProduct.class);
                    startActivity(p);
                } else {
                    Toast.makeText(getApplicationContext(), "Too Upload Products You need to be register as a Seller", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.cart:
                Intent i = new Intent(getApplicationContext(), Cart.class);
                startActivity(i);
                return true;
            case R.id.orders:
                Intent inte = new Intent(getApplicationContext(), Orders.class);
                startActivity(inte);
                return true;
            case R.id.logout:
                auth.signOut();
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}