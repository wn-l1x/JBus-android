package com.wendyDharmawanJBusER;

import static com.wendyDharmawanJBusER.LoginActivity.currentBus;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Facility;
import com.wendyDharmawanJBusER.model.Schedule;
import com.wendyDharmawanJBusER.model.Station;

import java.util.ArrayList;
import java.util.List;


public class BusDetailActivity extends AppCompatActivity {

    private TextView priceText = null;
    private TextView busCapacity = null;
    private TextView busType = null;
    private TextView busDeparture = null;
    private TextView busArrival = null;
    private TextView busAc = null;
    private TextView busWiFi = null;
    private TextView busToilet = null;
    private TextView busLcd = null;
    private TextView busCoolbox = null;
    private TextView busLunch = null;
    private TextView busBaggage = null;
    private TextView busSocket = null;
    private TextView busname = null;
    private Button scheduleButton = null;
    private Spinner spinner = null;
    public static Schedule currentSchedule;

    Context mContext;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        mContext = this;
        busname = (TextView) findViewById(R.id.busName);
        priceText =  (TextView) findViewById(R.id.fillPrice);
        busCapacity = (TextView) findViewById(R.id.fillCapacity);
        busType = (TextView) findViewById(R.id.fillType);
        busDeparture = (TextView) findViewById(R.id.fillDeparture);
        busArrival = (TextView) findViewById(R.id.fillArrival);
        busAc = (TextView) findViewById(R.id.facility_AC);
        busWiFi = (TextView) findViewById(R.id.facility_WiFi);
        busToilet = (TextView) findViewById(R.id.facility_toilet);
        busLcd = (TextView) findViewById(R.id.facility_lcd);
        busCoolbox = (TextView) findViewById(R.id.facility_coolbox);
        busLunch = (TextView) findViewById(R.id.facility_lunch);
        busBaggage = (TextView) findViewById(R.id.facility_largebaggage);
        busSocket = (TextView) findViewById(R.id.facility_electricsocket);
        scheduleButton = (Button) findViewById(R.id.Booking_button);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapterSchedule = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, currentBus.schedules);
        adapterSchedule.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSchedule);
        spinner.setOnItemSelectedListener(setSchedule());

        List<Schedule> schedules = currentBus.schedules;
        busname.setText(currentBus.name);
        priceText.setText(String.valueOf(currentBus.price.price));
        busCapacity.setText(String.valueOf(currentBus.capacity));
        busType.setText(""+currentBus.busType);
        busDeparture.setText(""+currentBus.departure);
        busArrival.setText(""+currentBus.arrival);
        scheduleButton.setOnClickListener(v-> moveActivity(this,MakeBookingActivity.class));
        setFacilities();
    }
    protected AdapterView.OnItemSelectedListener setSchedule(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSchedule = (Schedule) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }
        String setFacilities(){
            String memek = "";
            if (currentBus.facilities.contains(Facility.AC)){
            memek = memek + "AC ";
            }
            if (!currentBus.facilities.contains(Facility.WIFI)){
                busWiFi.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.TOILET)){
                busToilet.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.LCD_TV)){
                busLcd.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.COOL_BOX)){
                busCoolbox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.LUNCH)){
                busLunch.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.LARGE_BAGGAGE)){
                busBaggage.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (!currentBus.facilities.contains(Facility.ELECTRIC_SOCKET)){
                busSocket.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return memek;
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

}
