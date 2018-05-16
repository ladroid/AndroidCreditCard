package com.example.lado.banksystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Arrays;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by lado on 29/3/18.
 */

public class ScanCreditCardActivity extends AppCompatActivity {
    private static final int REQUEST_SCAN = 101;
    private static final int REQUEST_AUTOTEST = 200;
    private DBCreditCard db;
    CreditCard scanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcardscan);

        Button btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanCreditCardActivity.this, CardIOActivity.class)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                        .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true)
                        .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en")
                        .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
                        .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null &&
                data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            ((TextView) findViewById(R.id.tvCardDetail)).setVisibility(View.VISIBLE);
            String resultDisplayStr;
            if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                if(scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }
                if(scanResult.cvv != null) {
                    resultDisplayStr += "CVV has: " + scanResult.cvv.length() + " digits.\n";
                }
                if(scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
                if(scanResult.cardholderName != null) {
                    resultDisplayStr += "Credit Name: " + scanResult.cardholderName + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceld.";
            }
            ((TextView) findViewById(R.id.tvCardDetail)).setText(resultDisplayStr);
        }
        db = new DBCreditCard(ScanCreditCardActivity.this, null, null, 1);
        CreditInfo creditInfo = new CreditInfo(1, scanResult.cardNumber, scanResult.cvv.length(), scanResult.expiryMonth);
        db.addcredit(creditInfo);
        List<CreditInfo> creditInfos = db.allCredits();
        if(creditInfos != null) {
            String[] itemsCredits = new String[creditInfos.size()];
            for(int i=0; i<creditInfos.size(); i++) {
                itemsCredits[i] = creditInfos.get(i).toString();
            }
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsCredits));
            Log.e("Show me ", Arrays.toString(itemsCredits));
            Log.e("KUKU ", creditInfos.toString());
        }
        db.Delete();
    }
}
