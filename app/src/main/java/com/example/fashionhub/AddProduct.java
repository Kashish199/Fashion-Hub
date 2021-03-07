package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {


    public static final int GALLERY_REQUEST_CODE = 105;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload;
    ImageView[] image;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_product_name, et_des, et_amt, et_category, et_event, et_size, et_color, et_qty;
    int photos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        et_product_name = findViewById(R.id.p_name);
        et_des = findViewById(R.id.des);
        et_amt = findViewById(R.id.amount);
        et_category = findViewById(R.id.category1);
        et_event = findViewById(R.id.event1);
        et_size = findViewById(R.id.size1);
        et_color = findViewById(R.id.color);
        et_qty = findViewById(R.id.qty);
        upload = findViewById(R.id.uploadImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};


        Button btn_post_product = findViewById(R.id.post_product);

        String[] categories = new String[]{"Girl", "Boy", "Men", "Women"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                AddProduct.this,
                R.layout.dropdown_item,
                categories
        );

        AutoCompleteTextView category = findViewById(R.id.category);
        category.setAdapter(adapter1);

        String[] events = new String[]{"Thanks Giving", "Boxing day", "Black Friday", "No Events"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                AddProduct.this,
                R.layout.dropdown_item,
                events
        );
        AutoCompleteTextView event = findViewById(R.id.event);

        event.setAdapter(adapter2);

        String[] sizes = new String[]{"XS", "S", "M", "L", "XL", "XXL", "XXXL"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                AddProduct.this,
                R.layout.dropdown_item,
                sizes
        );
        AutoCompleteTextView size = findViewById(R.id.size);
        size.setAdapter(adapter3);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        btn_post_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fstore = FirebaseFirestore.getInstance();
                final String Product_Name = et_product_name.getEditText().getText().toString().trim().toUpperCase();
                final String Description = et_des.getEditText().getText().toString().trim();
                final String Amount = et_amt.getEditText().getText().toString().trim();
                String Category = et_category.getEditText().getText().toString().trim();
                String Event = et_event.getEditText().getText().toString().trim();
                String Size = et_size.getEditText().getText().toString().trim();
                String Color = et_color.getEditText().getText().toString().trim().toUpperCase();
                String Qty = et_qty.getEditText().getText().toString().trim();


                if (Product_Name.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (Product_Name.length() > 65) {
                    Toast.makeText(AddProduct.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.length() > 10000) {
                    Toast.makeText(AddProduct.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please enter Amount ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(AddProduct.this, "Please Enter Amount in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (Category.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please select category", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Category.equals("Girl") || Category.equals("Boy") || Category.equals("Men") || Category.equals("Women"))) {
                    Toast.makeText(AddProduct.this, "Please Select category from DropDown", Toast.LENGTH_LONG).show();
                    et_category.getEditText().getText().clear();
                    return;
                } else if (Event.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please select Event", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Event.equals("Thanks Giving") || Event.equals("Boxing day") || Event.equals("Black Friday") || Event.equals("No Events"))) {
                    Toast.makeText(AddProduct.this, "Please Select Event from DropDown", Toast.LENGTH_LONG).show();
                    et_event.getEditText().getText().clear();
                    return;
                } else if (Size.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please select Size", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Size.equals("XS") || Size.equals("S") || Size.equals("M") || Size.equals("L") || Size.equals("XL") || Size.equals("XXL") || Size.equals("XXXL"))) {
                    Toast.makeText(AddProduct.this, "Please Select Size from DropDown", Toast.LENGTH_LONG).show();
                    et_category.getEditText().getText().clear();
                    return;
                } else if (Color.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please select color", Toast.LENGTH_LONG).show();
                    return;
                } else if (Qty.isEmpty()) {
                    Toast.makeText(AddProduct.this, "Please select Qty", Toast.LENGTH_LONG).show();
                    return;
                } else if (photos < 1) {
                    Toast.makeText(AddProduct.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(AddProduct.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.v("tagvv", " " + uid);

                    Map<String, Object> userMap = new HashMap<>();
                    // userMap(Key - means field of table in database, value)
                    userMap.put("UserID", uid);
                    userMap.put("Name", Product_Name);
                    userMap.put("Description", Description);
                    userMap.put("Price", Amount);
                    userMap.put("Category", Category);
                    userMap.put("Event", Event);
                    userMap.put("Size", Size);
                    userMap.put("Detail_image", Event);
                    userMap.put("Image", "Image");
                    userMap.put("Color", Color);
                    userMap.put("Qty", Qty);
                    userMap.put("ProductID", "ProductID");
                    userMap.put("Status", "Active");

                    fstore.collection("Products").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(final DocumentReference documentReference) {
                            String Productid = documentReference.getId();
                            Map<String, Object> usermap1 = new HashMap<>();
                            usermap1.put("ProductID", Productid);
                            usermap1.put("Detail_image", Productid + ".png");
                            usermap1.put("Image", Productid + ".png");
                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                            fstore.collection("Products").document(Productid).update(usermap1)
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

                            uploadImage(Productid);
                            Toast.makeText(AddProduct.this, " Product added Successfully ", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String Error = e.getMessage();
                            Toast.makeText(AddProduct.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProduct.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    contenturi.clear();
                    upload.setImageResource(R.drawable.logo);
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, ""), GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ClipData clipdata = data.getClipData();

                if (clipdata != null) {
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 1) {
                        Toast.makeText(AddProduct.this, "Please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }
                } else {
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }
    }


    private void uploadImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int j = 0; j < contenturi.size(); j++) {
            StorageReference ref = storageReference.child(id + ".png");
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

}