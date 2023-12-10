package com.wendyDharmawanJBusER;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Bus;
import com.wendyDharmawanJBusER.model.Payment;

import java.util.List;

public class PaymentArrayAdapter extends ArrayAdapter<Payment> {



    // invoke the suitable constructor of the ArrayAdapter class
    public PaymentArrayAdapter(@NonNull Context context, List <Payment> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.payment_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Payment currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        assert currentNumberPosition != null;

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.textView1);
        textView1.setText(currentNumberPosition.toString());

        Button mybutton = currentItemView.findViewById(R.id.detailButton);
        // then return the recyclable view
        mybutton.setOnClickListener(v -> {
            LoginActivity.currentPayment = currentNumberPosition;
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            getContext().startActivity(intent);
        });




        return currentItemView;
    }

}


