package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UpdateProduct extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload1;
    ImageView[] image;
    FirebaseStorage storage;
    Boolean changed = false;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_product_name, et_des, et_amt, et_category, et_event, et_size, et_color, et_qty;
    int photos = 0, internetPhotos = 0;
    String pid;
    String producid;
    Button status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        et_product_name = findViewById(R.id.p_nameUpdate);
        et_des = findViewById(R.id.desUpdate);
        et_amt = findViewById(R.id.amountUpdate);
        et_category = findViewById(R.id.category1Update);
        et_event = findViewById(R.id.event1Update);
        et_size = findViewById(R.id.size1Update);
        et_color = findViewById(R.id.colorUpdate);
        et_qty = findViewById(R.id.qtyUpdate);
        upload1 = findViewById(R.id.uploadImageUpdate);
        status = findViewById(R.id.status);
        image = new ImageView[]{upload1, selectedImage1, selectedImage2, selectedImage3};

        Intent i = getIntent();
        pid = i.getStringExtra("id");

        Button btn_post_product = findViewById(R.id.post_productUpdate);

        String[] categories = new String[]{"Girl", "Boy", "Men", "Women"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                UpdateProduct.this,
                R.layout.dropdown_item,
                categories
        );

        AutoCompleteTextView category = findViewById(R.id.categoryUpdate);
        category.setAdapter(adapter1);

        String[] events = new String[]{"Thanks Giving", "Boxing day", "Black Friday", "No Events"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                UpdateProduct.this,
                R.layout.dropdown_item,
                events
        );
        AutoCompleteTextView event = findViewById(R.id.eventUpdate);

        event.setAdapter(adapter2);

        String[] sizes = new String[]{"XS", "S", "M", "L", "XL", "XXL", "XXXL"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                UpdateProduct.this,
                R.layout.dropdown_item,
                sizes
        );
        AutoCompleteTextView size = findViewById(R.id.sizeUpdate);
        size.setAdapter(adapter3);

        getdata();
        upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fstore = FirebaseFirestore.getInstance();
                DocumentReference docRef = fstore.collection("Products").document(pid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data1 = document.getData();
                                String Status = data1.get("Status").toString();
                                if (Status.equals("Active")) {
                                    unavailable();
                                } else {
                                    available();
                                }

                                Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("tagvv", "No such document");
                            }
                        } else {
                            Log.d("tagvv", "get failed with ", task.getException());
                        }
                    }
                });

            }
        });

        btn_post_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fstore = FirebaseFirestore.getInstance();
                final String Product_Name = et_product_name.getEditText().getText().toString().trim();
                final String Description = et_des.getEditText().getText().toString().trim();
                final String Amount = et_amt.getEditText().getText().toString().trim();
                String Category = et_category.getEditText().getText().toString().trim();
                String Event = et_event.getEditText().getText().toString().trim();
                String Size = et_size.getEditText().getText().toString().trim();
                String Color = et_color.getEditText().getText().toString().trim();
                String Qty = et_qty.getEditText().getText().toString().trim();


                if (Product_Name.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (Product_Name.length() > 65) {
                    Toast.makeText(UpdateProduct.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.length() > 10000) {
                    Toast.makeText(UpdateProduct.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please enter Amount ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(UpdateProduct.this, "Please Enter Amount in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (Category.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please select category", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Category.equals("Girl") || Category.equals("Boy") || Category.equals("Men") || Category.equals("Women"))) {
                    Toast.makeText(UpdateProduct.this, "Please Select category from DropDown", Toast.LENGTH_LONG).show();
                    et_category.getEditText().getText().clear();
                    return;
                } else if (Event.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please select Event", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Event.equals("Thanks Giving") || Event.equals("Boxing day") || Event.equals("Black Friday") || Event.equals("No Events"))) {
                    Toast.makeText(UpdateProduct.this, "Please Select Event from DropDown", Toast.LENGTH_LONG).show();
                    et_event.getEditText().getText().clear();
                    return;
                } else if (Size.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please select Size", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Size.equals("XS") || Size.equals("S") || Size.equals("M") || Size.equals("L") || Size.equals("XL") || Size.equals("XXL") || Size.equals("XXXL"))) {
                    Toast.makeText(UpdateProduct.this, "Please Select Size from DropDown", Toast.LENGTH_LONG).show();
                    et_category.getEditText().getText().clear();
                    return;
                } else if (Color.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please select color", Toast.LENGTH_LONG).show();
                    return;
                } else if (Qty.isEmpty()) {
                    Toast.makeText(UpdateProduct.this, "Please select Qty", Toast.LENGTH_LONG).show();
                    return;
                } else if (photos < 1) {
                    Toast.makeText(UpdateProduct.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(UpdateProduct.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();


                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Name", Product_Name);
                    userMap.put("Description", Description);
                    userMap.put("Price", Amount);
                    userMap.put("Category", Category);
                    userMap.put("Event", Event);
                    userMap.put("Size", Size);
                    userMap.put("Color", Color);
                    userMap.put("Qty", Qty);


                    fstore.collection("Products").document(pid)
                            .update(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (changed) {
                                        uploadImage((pid));
                                    }
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    SystemClock.sleep(3000);
                                    Toast.makeText(UpdateProduct.this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });


                }
            }
        });
    }


    private void getdata() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Products").document(pid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();
                        // data1 has all the data of particular product id to get particular field from this data
                        producid = data1.get("ProductID").toString();
                        String Name1 = data1.get("Name").toString();
                        String Description1 = data1.get("Description").toString();
                        String Price1 = data1.get("Price").toString();
                        String Category1 = data1.get("Category").toString();
                        String Event1 = data1.get("Event").toString();
                        String Size1 = data1.get("Size").toString();
                        String Detail_image1 = data1.get("Detail_image").toString();
                        String Image1 = data1.get("Image").toString();
                        String Color1 = data1.get("Color").toString();
                        String Qty1 = data1.get("Qty").toString();
                        String Status = data1.get("Status").toString();

                        et_product_name.getEditText().setText(Name1);
                        et_des.getEditText().setText(Description1);
                        et_amt.getEditText().setText(Price1);
                        et_category.getEditText().setText(Category1);
                        et_event.getEditText().setText(Event1);
                        et_size.getEditText().setText(Size1);
                        et_color.getEditText().setText(Color1);
                        et_qty.getEditText().setText(Qty1);
                        getProfileImage(producid);

                        if (Status.equals("Active")) {
                            status.setText("Mark Unavailable");
                        } else {
                            status.setText("Mark Available");
                        }

                        Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getProfileImage(String producid) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(producid + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().into(upload1);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void unavailable() {
        final CharSequence[] options = {"Yes", "No"};
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(UpdateProduct.this);
        builder1.setTitle("Is the product Unavailable ?");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes")) {
                    fstore = FirebaseFirestore.getInstance();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Status", "Inactive");
                    fstore.collection("Products").document(pid)
                            .update(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(UpdateProduct.this, "Product Unavailable", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });

                } else if (options[item].equals("NO")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();

    }

    private void available() {
        final CharSequence[] options = {"Yes", "No"};
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(UpdateProduct.this);
        builder1.setTitle("Is the Product  Available?");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes")) {
                    fstore = FirebaseFirestore.getInstance();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Status", "Active");
                    fstore.collection("Products").document(pid)
                            .update(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(UpdateProduct.this, "Product Available", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });

                } else if (options[item].equals("NO")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();

    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateProduct.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {

                    upload1.setImageResource(R.drawable.logo);
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
                contenturi.clear();
                deleteImages();
                //  Toast.makeText(this, "" + internetPhotos, Toast.LENGTH_SHORT).show();

                if (clipdata != null) {
                    changed = true;
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 1) {
                        Toast.makeText(this, "please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }

                } else {
                    changed = true;
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }

    }

    private void deleteImages() {

        storageReference = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
        StorageReference desertRef = storageReference.child(pid + ".png");
// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

    }

    private void uploadImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int j = 0; j < contenturi.size(); j++) {
            StorageReference ref = storageReference.child(id + ".png");
            ;
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(UpdateProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.updateproduct, menu);
        return true;

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
            case R.id.delete:


                final CharSequence[] options = {"Delete", "Cancel"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateProduct.this);
                builder1.setTitle("Delete Post");
                builder1.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Delete")) {
                            delete();
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder1.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void delete() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Products").document(pid);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteImages();
                Log.d("tagvv", "DocumentSnapshot successfully deleted!");
                Toast.makeText(UpdateProduct.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tagvv", "Error deleting document", e);
                    }
                });

    }


}