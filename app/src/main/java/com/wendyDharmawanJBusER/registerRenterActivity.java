package com.wendyDharmawanJBusER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Account;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.model.Renter;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class registerRenterActivity extends AppCompatActivity {
    private EditText companyName = null;
    private EditText Address = null;
    private EditText PhoneNumber = null;
    private Button registerRenterButton = null;
    private Context mContext;
    private BaseApiService mApiService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);
        companyName = findViewById(R.id.companyname);
        Address = findViewById(R.id.address);
        PhoneNumber = findViewById(R.id.phonenumber);
        mApiService = UtilsApi.getApiService();
        registerRenterButton = findViewById(R.id.Register_renter_button);
        registerRenterButton.setOnClickListener(v -> {
           handleRegisterRenter();
        });
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void handleRegisterRenter(){
        String companynameS = companyName.toString();
        String addressS = Address.toString();
        String phonenumberS = PhoneNumber.toString();
        if (companynameS.isEmpty() || addressS.isEmpty() || phonenumberS.isEmpty()){
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(LoginActivity.loggedAccount.id ,companynameS,addressS,phonenumberS).enqueue(new Callback<BaseResponse<Renter>>() {

            @Override
            public void onResponse(Call<BaseResponse<Renter>> call,
                                   Response<BaseResponse<Renter>> response) {
// handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();
// if success finish this activity (back to login activity)
                if (res.success) finish();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                LoginActivity.loggedAccount.company = res.payload;
                moveActivity(mContext, AboutMeActivity.class);
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    }

