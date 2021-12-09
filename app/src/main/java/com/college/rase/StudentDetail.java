package com.college.rase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class StudentDetail extends Activity {
    private CircleImageView profileImage;
    private EditText name;
    private EditText email;
    private EditText number;
    private EditText yop;
    private EditText branch;
    private EditText currentCompany;
    private EditText currentPosition;
    private EditText collegeId;
    HashMap<String , Object> data=new HashMap<>();

    FloatingActionButton verify;
    FloatingActionButton chat;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        user=FirebaseAuth.getInstance().getCurrentUser();
        profileImage=findViewById(R.id.profileImage);
        chat=findViewById(R.id.chat);
        verify=findViewById(R.id.verify);
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
        if(user.getEmail().toString().equals("tpo@tpo.com")){
            verify.setVisibility(View.VISIBLE);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.put("verified" , true);
                    db.collection("Students").document(ItemId).update(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(StudentDetail.this , "Profile verified successfully" , Toast.LENGTH_SHORT).show();
                                    verify.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StudentDetail.this , ""+e , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }else{
            chat.setVisibility(View.VISIBLE);

        }
    }
}