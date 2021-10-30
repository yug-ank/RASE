package com.college.rase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class Homepage extends Activity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        bottomNavigationView=findViewById(R.id.materialToolbar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.aboutUs:
                        intent = new Intent(Homepage.this, AboutUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.search:
                        intent = new Intent(Homepage.this, Search.class);
                        startActivity(intent);
                        return true;
                    case R.id.feedback:
                        intent = new Intent(Homepage.this, Feedback.class);
                        startActivity(intent);
                        return true;
                    case R.id.chat:
                        intent = new Intent(Homepage.this, Chat.class);
                        startActivity(intent);
                        return true;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            }
        });
    }

}