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
import android.widget.ImageView;
import android.widget.SearchView;
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    @Override
    public void applyTexts(String Revent, String Rsize, String Rcolor) {

        if (!(Revent.equals("Null")) && !(Rsize.equals("Null")) && !(Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Event", Revent)
                    .whereEqualTo("Size", Rsize)
                    .whereEqualTo("Color", Rcolor)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if ((Revent.equals("Null")) && !(Rsize.equals("Null")) && !(Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Size", Rsize)
                    .whereEqualTo("Color", Rcolor)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        } else if ((Revent.equals("Null")) && (Rsize.equals("Null")) && !(Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Color", Rcolor)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        } else if ((Revent.equals("Null")) && !(Rsize.equals("Null")) && (Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Size", Rsize)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        } else if (!(Revent.equals("Null")) && (Rsize.equals("Null")) && (Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Event", Revent)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        } else if ((Revent.equals("Null")) && (Rsize.equals("Null")) && (Rcolor.equals("Null"))) {
// toast
            Toast.makeText(Home.this, "Please select some data to filter", Toast.LENGTH_SHORT).show();

        } else if (!(Revent.equals("Null")) && !(Rsize.equals("Null")) && (Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Event", Revent)
                    .whereEqualTo("Size", Rsize)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        } else if (!(Revent.equals("Null")) && (Rsize.equals("Null")) && !(Rcolor.equals("Null"))) {
            final List<Products> productsList = new ArrayList<>();
            db.collection("Products")
                    .whereEqualTo("Event", Revent)
                    .whereEqualTo("Color", Rcolor)
                    .whereEqualTo("Status", "Active")
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
                                    String id = (String) (document.getId());
                                    String image = (String) document.getData().get("Image");
                                    String detail_image = (String) document.getData().get("Detail_image");
                                    String event = (String) document.getData().get("Event");
                                    String colorP = (String) document.getData().get("Color");
                                    String q = (String) document.getData().get("Qty");
                                    String subbrand = (String) document.getData().get("Category");
                                    int qty = Integer.parseInt(q.toString());

                                    getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                                }

                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }

                        }
                    });
        }
    }

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
    TextView girl, boy, men, women, hname, eve;

    ImageView pro, search;
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
    RecyclerView event_name;
    String Customer, Total;
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
        hname = (TextView) findViewById(R.id.hname);
        pro = (ImageView) findViewById(R.id.pro);
        eve = (TextView) findViewById(R.id.cat_name);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getUserData();
        Intent i = getIntent();
        Customer = i.getStringExtra("Customer");


        List<ProductCategory> productCategoryList = new ArrayList<>();
        final List<Products> productsList = new ArrayList<>();
        productCategoryList.add(new ProductCategory("1", "NOEvent"));
        setdata(productsList, "Boy");

        final int white = Color.parseColor("#060001");
        final int yellow = Color.parseColor("#ffc107");
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                women.setTextColor(white);
                boy.setTextColor(white);
                men.setTextColor(white);
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick) {
                    girl.setTextColor(yellow);
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
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick) {
                    boy.setTextColor(yellow);
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
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick) {
                    men.setTextColor(yellow);
                } else {
                    men.setTextColor(white);
                }
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
                boolean isSelectedAfterClick = !view.isSelected();
                view.setSelected(isSelectedAfterClick);
                if (isSelectedAfterClick) {
                    women.setTextColor(yellow);
                } else {
                    women.setTextColor(white);
                }
                removeitem(productsList);
                setdata(productsList, "Women");

            }
        });

    }


    /**
     * this method is use to set data from database to display products
     *
     * @param productsList
     * @param subbrand
     */
    private void setdata(final List<Products> productsList, final String subbrand) {

        db.collection("Products")
                .whereEqualTo("Category", subbrand)
                .whereEqualTo("Status", "Active")
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
                                String id = (String) (document.getId());
                                String image = (String) document.getData().get("Image");
                                String detail_image = (String) document.getData().get("Detail_image");
                                String event = (String) document.getData().get("Event");
                                String colorP = (String) document.getData().get("Color");
                                String q = (String) document.getData().get("Qty");
                                int qty = Integer.parseInt(q.toString());

                                getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);
                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getUserData() {
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final String id = firebaseUser.getUid();
        Log.v("tagvv", " " + id);
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data2 = document.getData();
                        String Name = data2.get("Name").toString();
                        Log.d("tagvv", "DocumentSnapshot data: " + data2);
                        hname.setText("Hello " + Name + "!!");
                        getProfileImage(id);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void getProfileImage(String id) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/Profile/" + id + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).fit().into(pro);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                int errorCode = ((StorageException) exception).getErrorCode();
                String errorMessage = exception.getMessage();
                //       Picasso.get().load(uri).fit().into(pro);
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
    private void getImage(final String id, final String image, final String name, final String event, final String colorP, final String description, final String size, final String price, final List<Products> productsList, final String category, final String detail_image, final int qty) {
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

                        productsList.add(new Products(id, name, event, colorP, size, price, uri, description, category, detail_image, qty));
                        setProdItemRecycler(productsList);
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

    }

    /**
     * set item for product category
     *
     * @param productCategoryList
     */

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
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchdata(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem filter = menu.findItem(R.id.filter);

        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                opendialog();
                return false;
            }
        });

        if (Customer.equals("Seller")) {
            MenuItem profile = menu.findItem(R.id.Profile);
            profile.setVisible(true);//
            MenuItem addProduct = menu.findItem(R.id.addProduct);
            addProduct.setVisible(true);
            MenuItem ManageProduct = menu.findItem(R.id.manageProduct);
            ManageProduct.setVisible(true);
            return true;
        } else {
            MenuItem profile = menu.findItem(R.id.Profile);
            profile.setVisible(true);//
            MenuItem addProduct = menu.findItem(R.id.addProduct);
            addProduct.setVisible(false);
            MenuItem ManageProduct = menu.findItem(R.id.manageProduct);
            ManageProduct.setVisible(false);
            return true;
        }

    }

    private void opendialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");

    }

    private void searchdata(final String query) {
        final String q = query.toUpperCase();
        System.out.println("QUERY VALUE" + q);
        final List<Products> productsList = new ArrayList<>();
        db.collection("Products")
                .whereEqualTo("Name", q)
                .whereEqualTo("Status", "Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String name;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                name = (String) document.getData().get("Name");
                                System.out.println("QUERY VALUE" + q);
                                String description = (String) document.getData().get("Description");
                                String size = (String) document.getData().get("Size");
                                String price = (String) document.getData().get("Price");
                                String id = (String) (document.getId());
                                String image = (String) document.getData().get("Image");
                                String detail_image = (String) document.getData().get("Detail_image");
                                String event = (String) document.getData().get("Event");
                                String colorP = (String) document.getData().get("Color");
                                String q = (String) document.getData().get("Qty");
                                String subbrand = (String) document.getData().get("Category");
                                int qty = Integer.parseInt(q.toString());

                                getImage(id, image, name, event, colorP, description, size, price, productsList, subbrand, detail_image, qty);


                            }


                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }

                    }
                });
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

            case R.id.Profile:
                Intent pr = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(pr);
                return true;
            case R.id.addProduct:
                Intent p = new Intent(getApplicationContext(), AddProduct.class);
                startActivity(p);
                return true;
            case R.id.manageProduct:
                Intent ma = new Intent(getApplicationContext(), PostList.class);
                startActivity(ma);
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