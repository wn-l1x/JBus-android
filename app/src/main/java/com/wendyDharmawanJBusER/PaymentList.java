package com.wendyDharmawanJBusER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.wendyDharmawanJBusER.jbus_android.R;
import com.wendyDharmawanJBusER.model.Bus;
import com.wendyDharmawanJBusER.model.Payment;
import com.wendyDharmawanJBusER.request.BaseApiService;
import com.wendyDharmawanJBusER.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentList extends AppCompatActivity {
    private androidx.appcompat.widget.AppCompatButton userprofile_button = null;
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 12; // kalian dapat bereksperimen dengan field ini
    private int listSize;
    private int noOfPages;
    private List<Payment> listPayment;
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView paymentListView = null;
    private HorizontalScrollView pageScroll = null;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        System.out.println(LoginActivity.loggedAccount.id);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        paymentListView = findViewById(R.id.listView);
        getAllMyPayment();
        paginationFooter();
        goToPage(currentPage);
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });
    }

    protected void getAllMyPayment(){
        mApiService.getmyPaymentBuyer(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                listPayment = response.body();
                System.out.println(listPayment);
                if (!listPayment.isEmpty()) {

                    ArrayList<Payment> setList = new ArrayList<>(listPayment);

                    PaymentArrayAdapter pageList = new PaymentArrayAdapter(mContext, setList);
                    paymentListView.setAdapter(pageList);
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Ada problem pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.app_bar_profile) {
            moveActivity(mContext, AboutMeActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
            // ganti dengan warna yang kalian mau
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_circle_24));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listPayment, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) /
                2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(List<Payment> listPayment, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listPayment.size());
        List<Payment> paginatedList = listPayment.subList(startIndex, endIndex);
        // Tampilkan paginatedList ke listview
        // seperti yang sudah kalian lakukan sebelumnya,
        // menggunakan array adapter.
        PaymentArrayAdapter numbersArrayAdapter = new PaymentArrayAdapter(this, paginatedList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView numbersListView = findViewById(R.id.listView);

        // set the numbersViewAdapter for ListView
        numbersListView.setAdapter(numbersArrayAdapter);
    }
}
