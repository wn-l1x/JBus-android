package com.wendyDharmawanJBusER;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.StrikethroughSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Account;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.model.Payment;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import static com.wendyDharmawanJBusER.BusDetailActivity.currentSchedule;
import static com.wendyDharmawanJBusER.LoginActivity.currentBus;
import static com.wendyDharmawanJBusER.LoginActivity.loggedAccount;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeBookingActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button makeBookingButton = null;
    int i;
    private BaseApiService mApiService;
    private Context mContext;
    private List<String> orderedSeat = new ArrayList<>();
    private TextView name = null;
    private TextView sched = null;
    private TextView cap = null;
    private TextView ccap = null;
    private TextView pps = null;
    private TextView costpay = null;
    int amount = 0;
    int current_capacity;
    int amount_paid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);
        mContext = this;
        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        name = (TextView) findViewById(R.id.busName);
        sched = (TextView) findViewById(R.id.fillSched);
        cap =(TextView)  findViewById(R.id.fillCapacity);
        ccap = (TextView) findViewById(R.id.fillCurrent);
        pps = (TextView) findViewById(R.id.fillPPS);
        costpay = (TextView) findViewById(R.id.TotalCost);
        current_capacity = currentBus.capacity;
        mApiService = UtilsApi.getApiService();
        makeBookingButton = findViewById(R.id.MakeBookingButton);
        amount_paid = (int) (currentBus.price.price * amount);
        name.setText(currentBus.name);
        sched.setText(""+currentSchedule);
        cap.setText(String.valueOf(currentBus.capacity));

        pps.setText(String.valueOf(currentBus.price.price));
        costpay.setText(String.valueOf(amount_paid));


        int row = gridLayout.getRowCount();
        int column = gridLayout.getColumnCount();
        System.out.println(currentBus);

        for (i = 0; i < currentBus.capacity; i++) {
            final int seatIndex = i;  // Create a final variable to capture the current value of i
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Seat " + (i+1));
            String check = ("ER" +String.format("%02d", i+1));
            if (Boolean.FALSE.equals(currentSchedule.seatAvailability.get(check))){
                checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setEnabled(false);
                current_capacity = current_capacity - 1;
                ccap.setText(String.valueOf(current_capacity));
            }

            // Add the checkbox to the existing GridLayout
            gridLayout.addView(checkBox);

            // Set an OnCheckedChangeListener for each checkbox
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Your logic when the checkbox is checked or unchecked
                    if (isChecked) {
                        orderedSeat.add("ER" + String.format("%02d", seatIndex+1));
                        System.out.println(orderedSeat);
                        current_capacity = current_capacity-1;
                        amount = amount + 1;
                        amount_paid = amount * (int) currentBus.price.price;
                        showToast("Seat " + "ER" + String.format("%02d", seatIndex+1) + " selected");
                        ccap.setText(String.valueOf(current_capacity));
                        costpay.setText(String.valueOf(amount_paid));
                    } else {
                        orderedSeat.remove("ER" + String.format("%02d", seatIndex+1));
                        System.out.println(orderedSeat);
                        amount = amount - 1;
                        current_capacity = current_capacity+1;
                        amount_paid = amount * (int) currentBus.price.price;
                        showToast("Seat " + "ER" + String.format("%02d", seatIndex+1) + " unselected");
                        ccap.setText(String.valueOf(current_capacity));
                        costpay.setText(String.valueOf(amount_paid));
                    }
                }
            });
        }
        makeBookingButton.setOnClickListener(v->handleMakeBook());
    }
    private void handleMakeBook(){
        int buyerId = loggedAccount.id;
        int renterId = 0;
        int busId = currentBus.id;
        String departureDate = ""+currentSchedule;
        mApiService.makeBooking(buyerId,renterId,busId,orderedSeat,departureDate).enqueue(new Callback<BaseResponse<Payment>>(){
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call,
                                   Response<BaseResponse<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                if (res.success) finish();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t){
                Toast.makeText(mContext, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}