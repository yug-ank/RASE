package com.college.rase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AboutUs extends Activity {
    BottomNavigationView bottomNavigationView;
    CircleImageView profileImage;
    FirebaseUser user;
    TextView aboutUsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        user= FirebaseAuth.getInstance().getCurrentUser();
        aboutUsText=(TextView)findViewById(R.id.aboutUsText);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        bottomNavigationView = findViewById(R.id.materialToolbar);
        bottomNavigationView.setSelectedItemId(R.id.aboutUs);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.aboutUs:
                        return true;
                    case R.id.search:
                        intent = new Intent(AboutUs.this, Search.class);
                        startActivity(intent);
                        return true;
                    case R.id.feedback:
                        intent = new Intent(AboutUs.this, Feedback.class);
                        startActivity(intent);
                        return true;
                    case R.id.chat:
                        intent = new Intent(AboutUs.this, Chat.class);
                        startActivity(intent);
                        return true;
                    case R.id.home:
                        intent = new Intent(AboutUs.this, Homepage.class);
                        startActivity(intent);
                        return true;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Students").document(user.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.get("profilePicture").toString().length() > 0) {
                        Picasso.get().load(value.get("profilePicture").toString()).noFade().fit().into(profileImage);
                    }
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUs.this, Profile.class);
                intent.putExtra("from", "Homepage");
                startActivity(intent);
            }
        });
        aboutUsText.setText("RASE is an interactive where students and alumni of a college can interact with each other and " +
                "can help each other to achieve higher goals with proper guidance and knowledge of seniors");
    }
}