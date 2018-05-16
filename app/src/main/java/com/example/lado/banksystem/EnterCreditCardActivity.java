package com.example.lado.banksystem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;


/**
 * Created by lado on 28/3/18.
 */

public class EnterCreditCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard);

        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        TextView txtDocs = (TextView) findViewById(R.id.payment_amount);
        Button btnpay = (Button) findViewById(R.id.btn_pay);
        txtDocs.setText("500$");
        btnpay.setText(String.format("Payer %s", txtDocs.getText()));

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(EnterCreditCardActivity.this, "Name: " + card.getName() + " | Last 4 digits: " + card.getLast4() +
                        " | CVC: " + card.getCVC() + String.format("Exp: %d/%d", card.getExpMonth(), card.getExpYear()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
