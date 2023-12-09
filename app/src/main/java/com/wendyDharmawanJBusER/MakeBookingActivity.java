package com.wendyDharmawanJBusER;

import android.content.Context;
import android.os.Bundle;
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
    private TextView sched = null;
    private TextView cap = null;
    private TextView ccap = null;
    private TextView pps = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        setContentView(R.layout.activity_make_booking);
        makeBookingButton = findViewById(R.id.MakeBookingButton);
        sched = findViewById(R.id.M);

        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();

        int row = gridLayout.getRowCount();
        int column = gridLayout.getColumnCount();

        for (i = 0; i < currentBus.capacity; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Seat " + (i));

            // Add the checkbox to the existing GridLayout
            gridLayout.addView(checkBox);

            // Set an OnCheckedChangeListener for each checkbox

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Your logic when the checkbox is checked or unchecked
                    if (isChecked) {
                        orderedSeat.add("ER"+String.format("%02d",i));
                        System.out.println(orderedSeat);
                        showToast("Seat " + "ER"+ String.format("%02d",i) + " selected");
                    } else {
                        orderedSeat.remove("ER"+String.format("%02d",i));
                        System.out.println(orderedSeat);
                        showToast("Seat " + "ER"+ String.format("%02d",i) + " unselected");
                    }
                }
            });
        }
        makeBookingButton.setOnClickListener(v->handleMakeBook());
    }
    private void handleMakeBook(){
        int buyerId = loggedAccount.id;
        int renterId = currentBus.accountId;
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

            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}