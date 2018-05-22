package com.example.lado.banksystem;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.text.Html.fromHtml;

/**
 * Created by lado on 21/3/18.
 */

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;

    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    Button buttonLogin;

    DBHandler dbHandler;

    public String Email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHandler = new DBHandler(this, null, null, 1);
        inCreateAccountTextView();
        initViews();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                    User currentUser = dbHandler.Authonticate(new User(null, null, Email, Password));
                    if(currentUser != null) {
                        Snackbar.make(buttonLogin, "Successfully Login", Snackbar.LENGTH_LONG).show();

                        //send name
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("Email", Email);
                        startActivity(intent);
                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        Snackbar.make(buttonLogin, "Failed Login", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = false;
        Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }
        if(Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password");
        }
        else {
            if(Password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            }
            else {
                valid = false;
                textInputLayoutPassword.setError("Password is short");
            }
        }
        return valid;
    }

    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    private void inCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#0c0099'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
