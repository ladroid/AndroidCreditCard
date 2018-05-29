package com.example.lado.banksystem;

import android.os.Bundle;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Customer;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.oauth.InvalidGrantException;
import com.stripe.model.Charge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;

import static java.security.AccessController.getContext;

/**
 * Created by lado on 31/3/18.
 */

public class PayPalWallet extends AppCompatActivity {
    //CardInputWidget mCardInputWidget;
    CardMultilineWidget mCardInputWidget;
    Button press;
    TextView textId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //mCardInputWidget = (CardInputWidget) findViewById(R.id.cc);

        mCardInputWidget = (CardMultilineWidget) findViewById(R.id.cc);
        textId = (TextView) findViewById(R.id.textId);
        press = (Button) findViewById(R.id.pay);

        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = mCardInputWidget.getCard();
                //card = new Card(cardNumber, cardExpMonth, cardExpYear, cardCVC);
                card.validateNumber();
                card.validateCVC();
                if(!card.validateCard()) {
                    Toast.makeText(PayPalWallet.this, "Error of validation", Toast.LENGTH_LONG).show();
                    Log.e("Error -> ","Error of validation");
                }
                else {
                    Toast.makeText(PayPalWallet.this, "Saving process...", Toast.LENGTH_LONG).show();
                    Log.e("Saving -> ","Saving process...");
                    String cardNumber = card.getNumber();
                    Integer cardExpMonth = card.getExpMonth();
                    Integer cardExpYear = card.getExpYear();
                    String cardCVC = card.getCVC();
                    card.setNumber(cardNumber);
                    card.setExpMonth(cardExpMonth);
                    card.setExpYear(cardExpYear);
                    card.setCVC(cardCVC);

                    Map<String, Object> tokensParameters = new HashMap<String, Object>();
                    tokensParameters.put("cardNumber", cardNumber);
                    tokensParameters.put("cardCVC", cardCVC);

                    Map<String, Object> users = new HashMap<String, Object>();
                    users.put("email", "qwerty@gmail.com");
                    users.put("source", "tok_visa");

                    Map<String, Object> bankParam = new HashMap<String, Object>();
                    bankParam.put("token", tokensParameters);

                    if(card == null) {
                        Toast.makeText(PayPalWallet.this, "Invalid Card Data", Toast.LENGTH_LONG).show();
                        Log.e("Invalid -> ","Invalid Card Data");
                    }
                    else {
                        Toast.makeText(PayPalWallet.this, "Validation sucessfully...", Toast.LENGTH_LONG).show();
                        Log.e("Validation -> ","Validation sucessfully...");
                        Log.e("cardNumber ", cardNumber);
                        final Stripe stripe = new Stripe(PayPalWallet.this, "YOUR PUBLIC KEY");
                        stripe.createToken(card, new TokenCallback() {
                            @Override
                            public void onError(Exception error) {
                                Toast.makeText(PayPalWallet.this, "Error", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(Token token) {
                                Toast.makeText(PayPalWallet.this, "Generate successfully token...", Toast.LENGTH_LONG).show();
                                textId.setText(token.getId());
                                Log.e("Token is ", token.getId());
                                Log.e("DatabaseTask", "DatabaseTask!!!!!!!!!");
                                //DataBaseTask db = new DataBaseTask("SANDTOKEN", token);
                                try{
                                    DataBaseTaskListener listener = new DataBaseTaskListener() {
                                        @Override
                                        public void onCompletedSendData(String result) {
                                            //Do what you need with the data
                                        }
                                    };
                                    DataBaseTask c = new DataBaseTask(PayPalWallet.this, "SANDTOKEN", token, listener);
                                    c.execute();
                                }
                                catch (Exception ex){
                                    Log.e("LISTENER ", ex.getMessage());
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}
