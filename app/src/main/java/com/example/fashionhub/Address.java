package com.example.fashionhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionhub.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Address extends AppCompatActivity {
    /**
     * variable declaration for textview
     */
    TextView uname, uapt, uaddress, ucity, upostal;
    /**
     * variable declaration for button
     */
    Button btnNext;
    /**
     * variable declaration db objet
     */
    FirebaseFirestore db;
    /**
     * variable declaration current user object
     */
    private FirebaseUser curUser;
    /**
     * variable declaration
     */
    private FirebaseAuth auth;

    private static final String Tag = "Payment Example";
    public static final String PAYPAL_KEY = "ARfZ6mX_Yns4FORy4xc9iHvEfXNCAk9f2N2r2g8BjuKnqsfWxY1z87V7O5l5NAYNiW5IdnSqmJWCmKHA";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static PayPalConfiguration config;
    PayPalPayment thingsToday;


    private int PAYPAL_REQ_CODE = 12;

    private static PayPalConfiguration paypalconfig = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalClientIDConfigClass.PAYPAL_CLIENT_ID);
    String Total;

    BigDecimal num;
    Double obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        uname = findViewById(R.id.name);
        uapt = findViewById(R.id.aptId);
        uaddress = findViewById(R.id.addressId);
        ucity = findViewById(R.id.city);
        upostal = findViewById(R.id.postalID);
        btnNext = findViewById(R.id.btnNext);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        final Bundle b = getIntent().getExtras();
        final ArrayList<CartModel> list = b.getParcelableArrayList("list");

        Intent in = getIntent();
        Total = in.getStringExtra("Total");

        obj = new Double(Total);
        num = BigDecimal.valueOf(obj);


        Intent intent = new Intent(getApplicationContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalconfig);
        startService(intent);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = uname.getText().toString();
                String apt = uapt.getText().toString();
                String address = uaddress.getText().toString();
                String city = ucity.getText().toString();
                String postal = upostal.getText().toString();

                if (name.isEmpty() || apt.isEmpty() || address.isEmpty() || city.isEmpty() || postal.isEmpty()) {
                    Toast.makeText(Address.this, "Please Fill Address Details", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    savedata(name, apt, address, city, postal, i, list);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                PaypalPaymentsMethod();

            }
        });

    }

    private void PaypalPaymentsMethod() {

        PayPalPayment payment = new PayPalPayment(num,
                "CAD",
                "Test Payment",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalconfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(Address.this, "Payment Successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Address.this, "Payment is Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), PayPalService.class));
        super.onDestroy();
    }

    /**
     * saving address data on db with order details
     *
     * @param name
     * @param apt
     * @param address
     * @param city
     * @param postal
     * @param i
     * @param list
     */
    private void savedata(String name, String apt, String address, String city, String postal, final int i, final ArrayList<CartModel> list) {
        final String userid = String.valueOf(curUser.getUid());
        Date date = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("Apt", apt);
        data.put("Address", address);
        data.put("City", city);
        data.put("Postal", postal);
        data.put("Image", String.valueOf(list.get(i).getImageUrl()));
        data.put("Productid", list.get(i).getProductid());
        data.put("ProductName", list.get(i).getProductName());
        data.put("productQty", list.get(i).getProductQty());
        data.put("price", list.get(i).getProductPrice());
        data.put("Userid", userid);


        db.collection("Orders").document(String.valueOf(date))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Added",
                                Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Error writing document", e);
                    }
                });
        Query productIdRef = db.collection("Cart")
                .whereEqualTo("Productid", list.get(i).getProductid());

        productIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().delete();

                }

            }


//

        });


    }

}


