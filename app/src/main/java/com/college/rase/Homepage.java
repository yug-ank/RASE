package com.college.rase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.ProcessingInstruction;

public class Homepage extends Activity {
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    CircleImageView profileImage;
    FirebaseUser user;
    Button LogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        bottomNavigationView = findViewById(R.id.materialToolbar);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        LogOut=findViewById(R.id.LogOut);
        if (user.getEmail().toString().equals("tpo@tpo.com")) {
            bottomNavigationView.setVisibility(View.GONE);
            profileImage.setVisibility(View.GONE);
            LogOut.setVisibility(View.VISIBLE);
            LogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(Homepage.this , LoginPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.homePageView);
            recyclerView.setLayoutManager(new GridLayoutManager(Homepage.this, 2));
            recyclerView.setHasFixedSize(false);
            final PagedList.Config config = new PagedList.Config.Builder()
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build();
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Query query = firebaseFirestore.collection("Students").whereEqualTo("verified", false);
            FirestorePagingOptions<StudentModel> options = new FirestorePagingOptions.Builder<StudentModel>()
                    .setQuery(query, config, new SnapshotParser<StudentModel>() {
                        @NonNull
                        @Override
                        public StudentModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            StudentModel hostelCardviewModel = snapshot.toObject(StudentModel.class);
                            hostelCardviewModel.setItemId(snapshot.getId());
                            return hostelCardviewModel;
                        }
                    })
                    .build();
            HomaPageRecyclerAdapter adapter = new HomaPageRecyclerAdapter(options);
            adapter.setContext(Homepage.this);
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        } else if(user!=null){
            recyclerView = (RecyclerView) findViewById(R.id.homePageView);
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
                    Intent intent = new Intent(Homepage.this, Profile.class);
                    intent.putExtra("from", "Homepage");
                    startActivity(intent);
                }
            });
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
                        case R.id.home:
                            return true;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }
                }
            });

            recyclerView.setLayoutManager(new GridLayoutManager(Homepage.this, 2));
            recyclerView.setHasFixedSize(false);
            final PagedList.Config config = new PagedList.Config.Builder()
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build();
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Query query = firebaseFirestore.collection("Students").whereEqualTo("verified", true);
            FirestorePagingOptions<StudentModel> options = new FirestorePagingOptions.Builder<StudentModel>()
                    .setQuery(query, config, new SnapshotParser<StudentModel>() {
                        @NonNull
                        @Override
                        public StudentModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            StudentModel hostelCardviewModel = snapshot.toObject(StudentModel.class);
                            hostelCardviewModel.setItemId(snapshot.getId());
                            return hostelCardviewModel;
                        }
                    })
                    .build();
            HomaPageRecyclerAdapter adapter = new HomaPageRecyclerAdapter(options);
            adapter.setContext(Homepage.this);
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        }
    }
}