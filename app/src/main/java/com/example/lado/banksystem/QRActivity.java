package com.example.lado.banksystem;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by lado on 21/3/18.
 */

public class QRActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    EditText editText;
    String EditTextValue ;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    PackageInfo info;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqr);

        imageView = (ImageView)findViewById(R.id.imageView);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditTextValue = editText.getText().toString();
                try {
                    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

                    byte[] randBytes = new byte[128];
                    secureRandom.nextBytes(randBytes);
                    int seedbyCount = 5;
                    byte[] seed = secureRandom.generateSeed(seedbyCount);

                    SecureRandom secureRandom1 = SecureRandom.getInstance("SHA1PRNG");
                    secureRandom1.setSeed(seed);
                    String bigInteger = new BigInteger(130, secureRandom1).toString(32);
                    try {
                        bitmap = TextToImageEncode(bigInteger);
                        imageView.setImageBitmap(bitmap);
                    }
                    catch (WriterException e) {
                        e.printStackTrace();
                    }
                    Log.e("hash ", secureRandom.toString());
                    Log.e("hash1 ", secureRandom1.toString());
                    Log.e("BigInt ", bigInteger);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

//                try {
//                    bitmap = TextToImageEncode(EditTextValue);
//
//                    imageView.setImageBitmap(bitmap);
//
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRcodeWidth, QRcodeWidth, null);

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
