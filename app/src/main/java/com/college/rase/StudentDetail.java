package com.college.rase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class StudentDetail extends AppCompatActivity {
    private CircleImageView profileImage;
    private EditText name;
    private EditText email;
    private EditText number;
    private EditText yop;
    private EditText branch;
    private EditText currentCompany;
    private EditText currentPosition;
    private EditText collegeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        profileImage=findViewById(R.id.profileImage);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        number=findViewById(R.id.number);
        yop=findViewById(R.id.yop);
        branch=findViewById(R.id.branch);
        currentCompany=findViewById(R.id.currentCompany);
        currentPosition=findViewById(R.id.currentPosition);
        collegeId=findViewById(R.id.collegeId);
        final String ItemId=getIntent().getStringExtra("ItemId");
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        Log.i("rectify" , ItemId);
        db.collection("Students").document(ItemId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value.exists()){
                        if(value.get("profilePicture").toString().length()>0)
                            Picasso.get().load(value.get("profilePicture").toString()).noFade().fit().into(profileImage);
                        name.setText(value.get("name").toString());
                        email.setText(ItemId.toString());
                        number.setText(value.get("number").toString());
                        yop.setText(value.get("yop").toString());
                        branch.setText(value.get("branch").toString());
                        currentCompany.setText(value.get("currentCompany").toString());
                        currentPosition.setText(value.get("currentPosition").toString());
                        collegeId.setText(value.get("collegeId").toString());
                    }
            }
        });
    }
}