package com.wendyDharmawanJBusER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.mainActivity;
import com.wendyDharmawanJBusER.registerActivity;

public class AboutMeActivity extends AppCompatActivity {
    private TextView username = null;
    private TextView email = null;
    private TextView balance = null;
    private Button topUpButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        username = (TextView) findViewById(R.id.fillUsername);
        email = (TextView) findViewById(R.id.fillEmail);
        balance = (TextView) findViewById(R.id.fillBalance);
        topUpButton = findViewById(R.id.Topup_button);
        username.setText("Wendy Dharmawan");
        email.setText("wendydharmawan@gmail.com");
        balance.setText("IDR 1");
        getSupportActionBar().hide();
    }
}
