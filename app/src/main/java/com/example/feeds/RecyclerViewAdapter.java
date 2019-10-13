package com.example.feeds;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> arrayList1 = new ArrayList<>();
    private ArrayList<String> arrayList2 = new ArrayList<>();
    private ArrayList<String> arrayList3 = new ArrayList<>();
    private ArrayList<String> arrayList4 = new ArrayList<>();
    private ArrayList<String> arrayList5 = new ArrayList<>();
    private ArrayList<String> arrayListIDs = new ArrayList<>();

    private Context mContext;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RecyclerViewAdapter(Context mContext, ArrayList<String> arrayList1,
                               ArrayList<String> arrayList2, ArrayList<String> arrayList3,
                               ArrayList<String> arrayList4, ArrayList<String> arrayList5,
                               ArrayList<String> arrayListIDs){
        this.mContext = mContext;
        this.arrayList1 = arrayList1;
        this.arrayList2 = arrayList2;
        this.arrayList3 = arrayList3;
        this.arrayList4 = arrayList4;
        this.arrayList5 = arrayList5;
        this.arrayListIDs = arrayListIDs;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_card_view_fragment, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(arrayList5.get(position))
                .into(holder.image);
        holder.Card.setText(arrayList1.get(position));
        holder.Likes.setText(arrayList2.get(position) + " Likes");
        holder.Tags.setText(arrayList3.get(position));
        holder.Time.setText(arrayList4.get(position));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" + arrayList1.get(position));
                Toast.makeText(mContext, arrayList1.get(position), Toast.LENGTH_LONG).show();
            }
        });

        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> feed = new HashMap<>();
                feed.put("likes", String.valueOf(Integer.parseInt(arrayList2.get(position))+1));
                db.collection("feeds").document(arrayListIDs.get(position))
                        .set(feed, SetOptions.merge());
            }
        });

        holder.Bookmark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Map<String, Object> bookmarked = new HashMap<>();
                bookmarked.put("user", arrayListIDs.get(position));
                db.collection("bookmarks")
                        .add(bookmarked)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(mContext, "Bookmarks Updated", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onSuccess: updated");
                            }
                        });
            }
        });

        holder.Message.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, CommentsActivity.class);
                i.putExtra("ID", arrayListIDs.get(position));
                //mContext.startActivity(new Intent(mContext, CommentsActivity.class));

                mContext.startActivity(i);
            }
        });

        holder.Send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView Card;
        TextView Time;
        TextView Likes;
        TextView Tags;
        ImageButton Like, Send, Message, Bookmark;

        RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image =itemView.findViewById(R.id.image);
            Card = itemView.findViewById(R.id.Title);
            Time = itemView.findViewById(R.id.Time);
            Likes = itemView.findViewById(R.id.likes);
            Tags = itemView.findViewById(R.id.tags);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            Like = itemView.findViewById(R.id.heart);
            Send = itemView.findViewById(R.id.send);
            Message = itemView.findViewById(R.id.message);
            Bookmark = itemView.findViewById(R.id.bookmark);
        }
    }
}
