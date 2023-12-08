package com.wendyDharmawanJBusER;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.BaseResponse;
import com.wendyDharmawanJBusER.model.Bus;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusScheduleActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, btnComplete;
    TextView txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_schedule);
        mApiService= UtilsApi.getApiService();
        mContext = this;
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btnComplete = (Button) findViewById(R.id.btn_add);
        txtDate = (TextView) findViewById(R.id.in_date);
        txtTime = (TextView) findViewById(R.id.in_time);
        btnDatePicker.setOnClickListener(v -> handleCalendar());
        btnTimePicker.setOnClickListener(v -> handleTime());
        btnComplete.setOnClickListener(v->handleAdd());

        //String dateTimeString = date + " " + time;

    }
    void handleAdd() {
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(mContext, "Please select both date and time", Toast.LENGTH_SHORT).show();
            return;
        }
        String dateTimeString = date + " " + time;
        System.out.println(dateTimeString);
        mApiService.addSchedule(LoginActivity.currentBus.id,dateTimeString).enqueue(new Callback<BaseResponse<Bus>>() {
        @Override
        public void onResponse(Call<BaseResponse< Bus >> call, Response<BaseResponse<Bus>> response) {
            if(!response.isSuccessful()){
                viewToast(mContext, "Application error " + response.code());
                return;
            }

            BaseResponse<Bus> res = response.body();
            if(res.success) finish();
            viewToast(mContext, res.message);
        }

        @Override
        public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
            t.printStackTrace();
            viewToast(mContext,"Server Problem");
        }
    });

    }
    private void viewToast (Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    void handleCalendar() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    void handleTime() {
        final Calendar c = Calendar.getInstance();
        // Get Current Time

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute + ":" + "00");
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}

