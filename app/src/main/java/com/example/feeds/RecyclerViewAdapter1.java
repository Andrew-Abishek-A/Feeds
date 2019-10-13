package com.example.feeds;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> comments = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter1(Context mContext, ArrayList<String> comments){
        this.mContext = mContext;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);
        holder.commentsView.setText(comments.get(position));

        holder.user.setText("user1");

        holder.parent_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" + comments.get(position));
                Toast.makeText(mContext, comments.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView commentsView;
        TextView user;
        RelativeLayout parent_layout1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentsView = itemView.findViewById(R.id.commentview);
            user = itemView.findViewById(R.id.username);
            parent_layout1 = itemView.findViewById(R.id.parent_layout1);
        }
    }
}
