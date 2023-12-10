package com.wendyDharmawanJBusER;

import static com.wendyDharmawanJBusER.LoginActivity.currentBus;
import static com.wendyDharmawanJBusER.LoginActivity.currentPayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.model.Bus;
import com.wendyDharmawanJBusER.model.Payment;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private int currentBusId = currentPayment.getBusId();
    private BaseApiService mApiService;
    private Context mContext;
    private TextView departuretime, orderedseats, timeordered, amounttopay, paymentstatus;
    private TextView busname, bustype, busdeparture, busarrival;
    private Button deny, confirm;
    String listSeats;
    int amount = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mApiService = UtilsApi.getApiService();
        mContext = this;


        for (String s : currentPayment.busSeats) {
            amount = amount + 1;
            s = s.replaceFirst("^null", "");
            listSeats = (listSeats + s + " ");
        }
        departuretime = findViewById(R.id.fillDTime);
        orderedseats = findViewById(R.id.fillSeats);
        timeordered = findViewById(R.id.fillTimeOrdered);
        amounttopay = findViewById(R.id.fillAmountPay);
        paymentstatus = findViewById(R.id.fillStatus);
        confirm = findViewById(R.id.Confirm);
        deny = findViewById(R.id.Deny);


        busname = findViewById(R.id.fillBusName);
        bustype = findViewById(R.id.fillBusType);
        busdeparture = findViewById(R.id.fillDepartureStation);
        busarrival = findViewById(R.id.fillArrivalStation);

        departuretime.setText("" + currentPayment.departureDate);
        orderedseats.setText(listSeats);
        timeordered.setText("" + currentPayment.time);
        paymentstatus.setText("" + currentPayment.status);
        System.out.println(currentPayment.getBusId());
        getBus();
        confirm.setOnClickListener(v->handleAcceptPayment(currentPayment.id));



    }

    void getBus() {
        mApiService.getBusById(currentPayment.getBusId()).enqueue(new Callback<Bus>() {
         @Override
         public void onResponse(Call<Bus> call, Response<Bus> response) {
             if (!response.isSuccessful()) {
                 return;
             }
             currentBus = response.body();
             busname.setText(currentBus.name);
             bustype.setText(""+currentBus.busType);
             busarrival.setText(""+currentBus.arrival);
             busdeparture.setText(""+currentBus.departure);
             amounttopay.setText(""+(currentBus.price.price * currentPayment.busSeats.size()));
         }

        @Override
         public void onFailure(Call<Bus> call, Throwable t) {
         }
     }
    );
    }
    private void handleAcceptPayment(int paymentId){
        mApiService.accept(paymentId).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Payment> res = response.body();
                if(res.success) {
                    LoginActivity.loggedAccount.balance += res.payload.busSeats.size() * currentBus.price.price;
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
