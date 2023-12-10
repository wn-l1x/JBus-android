package com.wendyDharmawanJBusER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Account;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.model.Bus;
import com.wendyDharmawanJBusER.model.Payment;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView registerNow = null;
    private Button loginNow = null;
    public static Account loggedAccount= null;
    public static Bus currentBus = null;
    public static Payment currentPayment = null;
    private EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerNow = findViewById(R.id.Register_button);
        loginNow = findViewById(R.id.login_button);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        registerNow.setOnClickListener(v -> {
            moveActivity(this, registerActivity.class);
            viewToast(this, "Move to register");
        });
        loginNow.setOnClickListener(v -> {
            handleLogin();
        });
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }
    }


    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }
    protected void handleLogin() {
// handling empty field
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {

            @Override
            public void onResponse(Call<BaseResponse<Account>> call,
                                   Response<BaseResponse<Account>> response) {
// handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
// if success finish this activity (back to login activity)
                if (res.success) finish();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                loggedAccount = res.payload;
                if (loggedAccount != null){
                    moveActivity(mContext, mainActivity.class);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

