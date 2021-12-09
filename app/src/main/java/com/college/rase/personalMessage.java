package com.college.rase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class personalMessage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private personalMessageAdapter adapter;
    private ArrayList<MessageObject> chat;
    private Intent intent;
    private String chatId;
    private EditText editText;
    private DatabaseReference databaseReference;
    private TextView textView;
    private String secondaryUser;
    private String primaryUser;
    private long curTime;
    private long prevTime=0;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message);
        recyclerView = findViewById(R.id.personal_message_recyclerView);
        editText = findViewById(R.id.personal_message_editText);
        textView = findViewById(R.id.personal_message_name);

        intent = getIntent();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        primaryUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        secondaryUser = intent.getStringExtra("foundUser");
        chatId = intent.getStringExtra("chatroom");

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("chatroom")
                .child(chatId);
        chat = new ArrayList<>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        adapter = new personalMessageAdapter(chat, this , databaseReference , width , height);
        recyclerView.setAdapter(adapter);
        textView.setText(secondaryUser);
        getChat();
    }

    public void sendMessage(View view) {
        Map<String , Object> msg = new HashMap<>();
        String time = SimpleDateFormat.getDateInstance().format(new Date());
        msg.put("text" , editText.getText().toString());
        msg.put("sender", primaryUser);
        msg.put("time", time);
        databaseReference.push().updateChildren(msg);
        editText.setText(null, null);
    }

    private void getChat() {
        Log.i("yash :", "getChat: came");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    MessageObject msg = new MessageObject(
                            snapshot.child("text").getValue().toString(),
                            snapshot.child("time").getValue().toString(),
                            //use username for the value of sender ////done
                            snapshot.child("sender").getValue().toString(),
                            (Boolean) snapshot.child("flag").getValue(),
                            snapshot.getKey()
                    );
                    Log.i("yash :", "onChildAdded: " + msg.text);
                    chat.add(msg);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}