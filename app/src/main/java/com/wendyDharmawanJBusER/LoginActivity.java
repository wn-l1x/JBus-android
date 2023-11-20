package com.wendyDharmawanJBusER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wendyDharmawanJBusER.jbus_android.R;

public class LoginActivity extends AppCompatActivity {
    private TextView registerNow = null;
    private Button loginNow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerNow = findViewById(R.id.Register_button);
        loginNow = findViewById(R.id.login_button);
        registerNow.setOnClickListener(v -> {
            moveActivity(this, registerActivity.class);
            viewToast(this, "Move to register");
        });
        loginNow.setOnClickListener(v -> {
            moveActivity(this, mainActivity.class);
            viewToast(this, "Move to login");
        });
        try {
            getSupportActionBar();
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
}

