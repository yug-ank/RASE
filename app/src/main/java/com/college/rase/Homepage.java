package com.college.rase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

public class Homepage extends Activity {
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    CircleImageView profileImage;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(Homepage.this , LoginPage.class);
            startActivity(intent);
            finish();
        }
        recyclerView=(RecyclerView)findViewById(R.id.homePageView);
        profileImage=(CircleImageView) findViewById(R.id.profileImage);
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
                        intent = new Intent(Homepage.this, ChatAll.class);
                        startActivity(intent);
                        return true;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(Homepage.this , 2));
        recyclerView.setHasFixedSize(false);
        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Students").whereEqualTo("verified" , true);
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
        HomaPageRecyclerAdapter adapter=new HomaPageRecyclerAdapter(options);
        adapter.setContext(Homepage.this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}