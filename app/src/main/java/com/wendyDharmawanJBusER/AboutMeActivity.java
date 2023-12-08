package com.wendyDharmawanJBusER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.mainActivity;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.registerActivity;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private TextView username = null;
    private TextView email = null;
    private TextView balance = null;
    private Button topUpButton = null;
    private Button renterButton = null;
    private EditText topUpAmount = null;
    private Context mContext = null;
    private TextView Rentertext = null;
    private TextView notRentertext = null;
    private TextView notRenterClick = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        username = (TextView) findViewById(R.id.fillUsername);
        email = (TextView) findViewById(R.id.fillEmail);
        balance = (TextView) findViewById(R.id.fillBalance);
        topUpButton = findViewById(R.id.Topup_button);
        topUpAmount = findViewById(R.id.TopUp);
        username.setText(LoginActivity.loggedAccount.name);
        email.setText(LoginActivity.loggedAccount.email);
        balance.setText(String.valueOf(LoginActivity.loggedAccount.balance));
        topUpButton.setOnClickListener(v -> handleTopUp());
        Rentertext = (TextView) findViewById((R.id.rtext));
        renterButton = findViewById((R.id.renter_button));
        notRentertext = (TextView) findViewById(R.id.nrtext);
        notRenterClick = (TextView) findViewById(R.id.nrt_click);
        notRenterClick.setOnClickListener(v -> moveActivity(this,registerRenterActivity.class));
        renterButton.setOnClickListener(v -> moveActivity(this,ManageBusActivity.class));
        if (LoginActivity.loggedAccount.company != null){
        notRentertext.setVisibility(View.GONE);
        notRenterClick.setVisibility(View.GONE);
        }
        else {
        Rentertext.setVisibility((View.GONE));
        renterButton.setVisibility((View.GONE));
        }
        getSupportActionBar().hide();
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleTopUp() {
        String topUpString = topUpAmount.getText().toString();

        if (topUpString.isEmpty()) {
            Toast.makeText(mContext, "kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!topUpString.matches("[0-9]+")) {
            Toast.makeText(mContext, "masukkan angka", Toast.LENGTH_SHORT).show();
            return;
        }

        double topUpValue = Double.valueOf(topUpString);

        mApiService.topUp(LoginActivity.loggedAccount.id, topUpValue).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Double> res = response.body();
                if (res.success) {
                    finish();
                    overridePendingTransition(0, 0);

                    LoginActivity.loggedAccount.balance = LoginActivity.loggedAccount.balance +  res.payload.doubleValue();

                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}