package com.college.rase;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatAll extends AppCompatActivity {
    ArrayList<userObject> chatList;
    RecyclerView recyclerView;
    ImageView searchIcon;
    SearchView searchView;
    String TAG = "TAG";
    public chat_all_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "call1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_all);
        Log.i("TAG", "call2");
        searchIcon = findViewById(R.id.activityChatAll_searchIcon);
        searchView = findViewById(R.id.activityChatAll_searchView);
        recyclerView = (RecyclerView) findViewById(R.id.activityChatAll_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        Log.i("TAG", "call3");
        DisplayMetrics displayMetrics= new DisplayMetrics();
        Log.i("TAG", "call4");
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;
        Log.i("TAG", "before get chats");
        getCHatList();
        Log.i("TAG", "after get chats");
        adapter = new chat_all_adapter(chatList , this , width , height);
        Log.i("TAG", "after create adapter");
        recyclerView.setAdapter(adapter);
        Log.i("TAG", "after set adapter");

        ///search
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIcon.setVisibility(View.GONE);
                findViewById(R.id.activityChatAll_textViewMessage).setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setVisibility(View.GONE);
                searchIcon.setVisibility(View.VISIBLE);
                findViewById(R.id.activityChatAll_textViewMessage).setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    private void filter(String newText) {
        ArrayList<userObject> filterList = new ArrayList<>();
        for(userObject temp : chatList){
            if(temp.getUserName().toLowerCase().contains(newText.toLowerCase()))
                filterList.add(temp);
        }
        adapter.filteredList(filterList);
    }


    void getCHatList(){
        chatList = new ArrayList<>();
// DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DatabaseReference db = FirebaseDatabase.getInstance("https://rase-ba33b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("users").child("yuganksharma012");
//        Map<String, Object> t1 = new HashMap<>();
//        t1.put("aaaaaa", "dfag@gma.com");
// db.setValue(t1);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("TAG", "in listener"+snapshot.getKey());
// if(snapshot.exists()){
                Log.i("TAG", snapshot.getKey()+"before");
                for(DataSnapshot childrens : snapshot.getChildren()){
                    String temp = childrens.getKey();
                    Log.i("TAG", ""+temp);
                    final userObject obj = new userObject( temp , childrens.getValue().toString());

                    FirebaseFirestore.getInstance().collection("Students").document(obj.getUserEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value.exists()){
                                if(value.get("name") != null)
                                    obj.setUserName(value.get("name").toString());
                                if(value.get("profilePicture") != null)
                                    obj.setProfilePicture(value.get("profilePicture").toString());
                                chatList.add(obj);
                                Log.i(TAG, "onEvent: "+obj.getUserName());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
// }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "something went wrong : "+error.toString());
            }
        });
    }
}