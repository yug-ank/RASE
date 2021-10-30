package com.college.rase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.TargetOrBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    private CircleImageView profileImage;
    private FloatingActionButton editProfileImage;
    private FloatingActionButton saveProfile;
    private FloatingActionButton editProfile;
    private EditText name;
    private EditText email;
    private EditText number;
    private EditText yop;
    private EditText branch;
    private EditText currentCompany;
    private EditText currentPosition;
    private EditText collegeId;
    private FirebaseAuth mauth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    String emailregex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Pattern emailPattern = Pattern.compile(emailregex);
    HashMap<String , Object> data=new HashMap<>();
    private  StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage=findViewById(R.id.profileImage);
        editProfileImage=findViewById(R.id.editProfileImage);
        saveProfile=findViewById(R.id.saveProfile);
        editProfile=findViewById(R.id.editProfile);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        number=findViewById(R.id.number);
        yop=findViewById(R.id.yop);
        branch=findViewById(R.id.branch);
        currentCompany=findViewById(R.id.currentCompany);
        currentPosition=findViewById(R.id.currentPosition);
        collegeId=findViewById(R.id.collegeId);
        storageReference= FirebaseStorage.getInstance().getReference("profilePictures");
        db=FirebaseFirestore.getInstance();
        if(getIntent().getStringExtra("from").toString().equals("signUp")){
            editProfileImage.setVisibility(View.VISIBLE);
            name.setEnabled(true);
            email.setEnabled(true);
            number.setEnabled(true);
            yop.setEnabled(true);
            branch.setEnabled(true);
            currentCompany.setEnabled(true);
            currentPosition.setEnabled(true);
            collegeId.setEnabled(true);
            saveProfile.setVisibility(View.VISIBLE);
            editProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CropImage.activity().setAllowRotation(true).setAllowFlipping(true)
                            .setAspectRatio(1 , 1).setFixAspectRatio(true)
                            .setRequestedSize(300 , 300).setCropShape(CropImageView.CropShape.OVAL)
                            .start(Profile.this);
                }
            });
            String error="";
            saveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.put("name" , name.getText().toString().trim());
                    data.put("number" , number.getText());
                    data.put("yop" , yop.getText());
                    data.put("branch" , branch.getText().toString());
                    data.put("currentCompany" , currentCompany.getText().toString());
                    data.put("currentPosition" ,currentPosition.getText().toString());
                    data.put("collegeId" , collegeId.getText().toString());
                    data.put("verified" , false);
                    db.collection("Students").document(user.getEmail().toString()).update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Profile.this , "Profile created successfully" +
                                            "\nWait for verification tby TPO agent" , Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Profile.this , LoginPage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //startActivity(intent);
                                    //finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Profile.this , ""+e , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Toast.makeText(Profile.this , ""+result.getError() , Toast.LENGTH_SHORT).show();
                }else{
                    Uri imageUri=result.getUri();
                    user=FirebaseAuth.getInstance().getCurrentUser();
                    String name=""+user.getEmail()+getExtension(imageUri);
                    StorageReference imgRef=storageReference.child(name);
                    UploadTask uploadTask=imgRef.putFile(imageUri);
                    uploadTask.continueWithTask(task -> {
                         if(!task.isSuccessful()){
                                throw task.getException();
                         }
                         return imgRef.getDownloadUrl();
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Picasso.get().load(task.getResult().toString()).noFade().fit().into(profileImage,
                                        new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                    Toast.makeText(Profile.this ,""+e , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                }
        }
    }
    private String getExtension(Uri uri){
        try{
            ContentResolver contentResolver=getContentResolver();
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        catch (Exception e){
            Toast.makeText(Profile.this , ""+e , Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}