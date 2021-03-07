package com.example.fashionhub;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    RadioGroup rgsize, rgevents, rgcolor;
    private RadioButton btn_eve, btn_siz, btn_col;
    private ExampleDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        builder1.setView(view).setTitle("Filter").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final String Rsize;
                final String Revent;
                final String Rcolor;
                int selectedId1 = rgevents.getCheckedRadioButtonId();
                btn_eve = view.findViewById(selectedId1);
                if (btn_eve == null) {
                    Revent = "Null";
                    Log.v("tagvv", " " + Revent);
                } else {
                    String r = btn_eve.getText().toString().trim();
                    Revent = r;
                    Log.v("tagvv", " " + Revent);
                }


                int selectedId2 = rgsize.getCheckedRadioButtonId();
                btn_siz = view.findViewById(selectedId2);
                if (btn_siz == null) {
                    Rsize = "Null";
                    Log.v("tagvv", " " + Rsize);
                } else {
                    String s = btn_siz.getText().toString().trim();
                    Rsize = s;
                    Log.v("tagvv", " " + Rsize);
                }

                int selectedId3 = rgcolor.getCheckedRadioButtonId();
                btn_col = view.findViewById(selectedId3);
                if (btn_col == null) {
                    Rcolor = "Null";
                    Log.v("tagvv", " " + Rcolor);
                } else {
                    String c = btn_col.getText().toString().trim();
                    Rcolor = c;
                    Log.v("tagvv", " " + Rcolor);
                }

                listener.applyTexts(Revent, Rsize, Rcolor);

            }
        });

        rgevents = view.findViewById(R.id.rbevent);
        rgsize = view.findViewById(R.id.rbsize);
        rgcolor = view.findViewById(R.id.rbcolor);


        return builder1.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String Revent, String Rsize, String Rcolor);
    }
}
