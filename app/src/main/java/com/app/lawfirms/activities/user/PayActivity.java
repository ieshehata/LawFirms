package com.app.lawfirms.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.lawfirms.R;
import com.app.lawfirms.callback.OrderCallback;
import com.app.lawfirms.controllers.OrderController;
import com.app.lawfirms.models.OrderModel;
import com.app.lawfirms.utils.LoadingHelper;
import com.app.lawfirms.utils.SharedData;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {
    EditText amount, name, no, month, year, cvv, address;
    Spinner type;
    Button pay;
    LoadingHelper loadingHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        amount = findViewById(R.id.amount);
        name = findViewById(R.id.name_on_card);
        no = findViewById(R.id.card_no);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        cvv = findViewById(R.id.cvv);
        address = findViewById(R.id.address);
        loadingHelper = new LoadingHelper(this);
        type = findViewById(R.id.card_type);
        pay = findViewById(R.id.pay);

        amount.setEnabled(false);
        amount.setText(String.format("%.3f", SharedData.currentOrder.getTotalPrice()));
        pay.setOnClickListener(view -> {
            if (checkData()) {
                SharedData.currentOrder.setDescription(address.getText().toString());
                new OrderController().save(SharedData.currentOrder, new OrderCallback() {
                    @Override
                    public void onSuccess(ArrayList<OrderModel> orderCares) {
                        Toast.makeText(PayActivity.this, "Done Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    @Override
                    public void onFail(String error) {
                        Toast.makeText(PayActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });

            }

        });
    }

    private boolean checkData(){
        boolean state = true;

        if(name.getText().toString().isEmpty()){
            state = false;
            name.setError("Required");
        }else{
            name.setError(null);
        }

        if(no.getText().toString().isEmpty()){
            state = false;
            no.setError("Required");
        }else{
            no.setError(null);
        }

        if(month.getText().toString().isEmpty()){
            state = false;
            month.setError("Required");
        }else{
            month.setError(null);
        }

        if(year.getText().toString().isEmpty()){
            state = false;
            year.setError("Required");
        }else{
            year.setError(null);
        }

        if(cvv.getText().toString().isEmpty()){
            state = false;
            cvv.setError("Required");
        }else{
            cvv.setError(null);
        }

        if (address.getText().toString().isEmpty()) {
            state = false;
            address.setError("Required");
        } else {
            address.setError(null);
        }
        return  state;
    }
}