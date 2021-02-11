package com.example.fashionhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSummary extends AppCompatActivity {
    /**
     * variable declarration for textview
     */
    TextView stotal, Tp;
    /**
     * variable declarration for button
     */
    Button btnCheckout;

    double total;
    String T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        stotal = findViewById(R.id.sTotal);
        Tp = findViewById(R.id.Tp);
        btnCheckout = findViewById(R.id.btnCheckout);
        final Bundle b = getIntent().getExtras();

        final double price = b.getDouble("price");

        stotal.setText("$ " + price);

        total = price;
        total = total + ((price * 15) / 100) + 4;

        Tp.setText("$ " + total);

        String A = Tp.getText().toString();
        T = A.replace("$", "");

        /**
         * got o checkout activity
         */

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Address.class);
                in.putExtras(b);
                in.putExtra("Total", T);
                startActivity(in);

            }
        });


    }
}