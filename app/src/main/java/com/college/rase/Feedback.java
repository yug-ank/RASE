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
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Feedback extends Activity {
    BottomNavigationView bottomNavigationView;
    CircleImageView profileImage;
    FirebaseUser user;
    EditText feedback;
    Button Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback=findViewById(R.id.feedbackform);
        Submit=findViewById(R.id.submitFeedback);
        user= FirebaseAuth.getInstance().getCurrentUser();
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        bottomNavigationView = findViewById(R.id.materialToolbar);
        bottomNavigationView.setSelectedItemId(R.id.feedback);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.aboutUs:
                        intent = new Intent(Feedback.this, AboutUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.search:
                        intent = new Intent(Feedback.this, Search.class);
                        startActivity(intent);
                        return true;
                    case R.id.feedback:
                        return true;
                    case R.id.chat:
                        intent = new Intent(Feedback.this, Chat.class);
                        startActivity(intent);
                        return true;
                    case R.id.home:
                        intent = new Intent(Feedback.this, Homepage.class);
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
                Intent intent = new Intent(Feedback.this, Profile.class);
                intent.putExtra("from", "Homepage");
                startActivity(intent);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Feedback.this , "Your feedback is submitted" , Toast.LENGTH_LONG).show();
            }
        });
    }
}