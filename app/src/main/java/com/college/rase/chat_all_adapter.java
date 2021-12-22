package com.college.rase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_all_adapter extends RecyclerView.Adapter<chat_all_adapter.viewHolder> {
    private ArrayList<userObject> chatList;
    private Context context;
    private int width;
    private int height;

    public chat_all_adapter(ArrayList<userObject> chatList, Context context, int width, int height) {
        this.chatList = chatList;
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filteredList(ArrayList<userObject> filterList){
        this.chatList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_chat_all, null);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width , height/10);
        view.setLayoutParams(layoutParams);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.name.setText(chatList.get(position).getUserName());
        if(!chatList.get(position).getProfilePicture().isEmpty())
            Picasso.get().load(chatList.get(position).getProfilePicture()).into(holder.profilePic);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), personalMessage.class);
                intent.putExtra("name", chatList.get(position).getUserName());
                intent.putExtra("profilePicture", chatList.get(position).getProfilePicture());
                intent.putExtra("chatroom", chatList.get(position).getChatroomId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private CircleImageView profilePic;
        private CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.activityChatCard_name);
            profilePic = itemView.findViewById(R.id.activityChatCard_profilePicture);
            cardView = itemView.findViewById(R.id.activityChatCard_cardView);

        }
    }
}