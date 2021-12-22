package com.college.rase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class personalMessageAdapter extends RecyclerView.Adapter<personalMessageAdapter.viewHolder> {

    private final DatabaseReference databaseReference;
    ArrayList<MessageObject> chat;
    Context context;
    int width;
    int height;
    String primaryUser;
    personalMessageAdapter(ArrayList<MessageObject> chat , Context context , DatabaseReference databaseReference , int width , int height){
        this.chat = chat;
        this.context = context;
        this.databaseReference = databaseReference;
        this.width = width;
        this.height = height;
        primaryUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_view, null);
        ConstraintLayout.LayoutParams params =
                new ConstraintLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(15 , 10 , 15 , 0);
        view.setLayoutParams(params);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if( !chat.get(position).sender.equals(primaryUser) ){
            holder.sendmsg.setVisibility(View.VISIBLE);
            holder.sendmsg.setText(chat.get(position).text);
        }
        else{
            holder.recivemsg.setVisibility(View.VISIBLE);
            holder.recivemsg.setText(chat.get(position).text);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        public TextView sendmsg , recivemsg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            sendmsg = itemView.findViewById(R.id.chatSentMessageTextView);
            recivemsg = itemView.findViewById(R.id.chatRecievedMessageTextView);
        }
    }
}