package com.college.rase;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class HomaPageRecyclerAdapter extends FirestorePagingAdapter<StudentModel , HomaPageRecyclerAdapter.studentViewHolder> {

    View view;
    Context context;
    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public HomaPageRecyclerAdapter(@NonNull FirestorePagingOptions<StudentModel> options) {
        super(options);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull studentViewHolder holder, int position, @NonNull StudentModel model) {
            holder.studentName.setText(model.getName());
            holder.studentYear.setText(model.getYop());
            holder.studentCurrentCompany.setText(model.getCurrentCompany());
            if(model.getProfilePicture().length()>0){
                Picasso.get().load(model.getProfilePicture()).fit().into(holder.studentImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context , StudentDetail.class);
                    intent.putExtra("ItemId" , model.getItemId());
                    context.startActivity(intent);
                }
            });
    }

    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_cardview , parent , false);
            return new studentViewHolder(view);
    }

    public static class studentViewHolder extends RecyclerView.ViewHolder{
        ImageView studentImage;
        TextView studentName;
        TextView studentYear;
        TextView studentCurrentCompany;
        public studentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentImage=(ImageView)itemView.findViewById(R.id.studentImage);
            studentName=(TextView)itemView.findViewById(R.id.studentName);
            studentYear=(TextView)itemView.findViewById(R.id.studentYear);
            studentCurrentCompany=(TextView)itemView.findViewById(R.id.studentCurrentCompany);
        }
    }
}
