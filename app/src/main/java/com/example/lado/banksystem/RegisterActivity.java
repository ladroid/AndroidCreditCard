package com.example.lado.banksystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.text.Html.fromHtml;

/**
 * Created by lado on 21/3/18.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;

    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    Button buttonRegister;

    DBHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHandler = new DBHandler(this, null, null, 1);
        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                    User usr = new User(null,null, null, null);
                    if(!dbHandler.isEmailExists(Email)) {
                        dbHandler.addUser(new User(null, UserName, Email, Password));
                        Snackbar.make(buttonRegister, "Successfully registerd", Snackbar.LENGTH_LONG).show();
                        //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_SHORT);
                    }
                    else {
                        Snackbar.make(buttonRegister, "Failed registered! Account is exsisted", Snackbar.LENGTH_LONG);
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = false;
        String UserName = editTextUserName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();
        //email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }
        //username
        if(UserName.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid username");
        }
        else {
            if(UserName.length() > 5) {
                valid = true;
                textInputLayoutUserName.setError(null);
            }
            else {
                valid = false;
                textInputLayoutUserName.setError("Password is short");
            }
        }
        //password
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
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
