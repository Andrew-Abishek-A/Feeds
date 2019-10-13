package com.example.feeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String newString;
    //private ArrayList<String> comments = new ArrayList<>();
    private HashSet<String> comments = new HashSet<>();
    private String currentdocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("ID");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("ID");
        }
        Log.d("TAG", "onCreate: " + newString);
        get_comments();

        ImageButton button = findViewById(R.id.commentsend);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = findViewById(R.id.new_comment);
                String st = et.getText().toString();

                Map<String, Object> comment = new HashMap<>();
                comment.put("1", st);

                db.collection("feeds")
                        .document(newString)
                        .collection("comments")
                        .document()
                        .set(comment, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CommentsActivity.this, "comment uploaded",
                                        Toast.LENGTH_SHORT).show();
                                et.setText("");
                                get_comments();
                            }
                        });
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        db.collection("feeds")
//                .document(newString)
//                .collection("comments")
//                .wh
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w("TAG", "Listen failed.", e);
//                            return;
//                        }
//
//                        if (documentSnapshot != null && documentSnapshot.exists()) {
//                            Log.d("TAG", "Current data: " + documentSnapshot.getData());
//                        } else {
//                            Log.d("TAG", "Current data: null");
//                        }
//                    }
//                });
//    }

    public void get_comments(){
        db.collection("feeds")
                .document(newString)
                .collection("comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.d("TAG", "onSuccess: " +documentSnapshot.getId());
                            currentdocument = documentSnapshot.getId();
                            comments.add(documentSnapshot.get("1").toString());
                        }
                        initRecylerView1();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: damn that sucks");
                    }
                });
    }

    public void initRecylerView1(){
        Log.d("TAG", "initRecylerView: HERE");

        RecyclerView recyclerView = findViewById(R.id.recycler_view1);
        RecyclerViewAdapter1 adapter = new RecyclerViewAdapter1(this, new ArrayList<String>(comments));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
    }
}
