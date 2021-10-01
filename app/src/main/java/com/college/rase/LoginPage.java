package com.college.rase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {


    private EditText eMail;
    private EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        eMail = findViewById(R.id.email);
        pass = findViewById(R.id.password);
    }

    public void Login(View view) {
        if(eMail.equals("root") && pass.equals("1234")){
            startActivity(new Intent(this, Profile.class));
        }
    }
}