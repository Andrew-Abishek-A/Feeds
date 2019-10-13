package com.example.feeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<String> arrayList1 = new ArrayList<>();
    private ArrayList<String> arrayList2 = new ArrayList<>();
    private ArrayList<String> arrayList3 = new ArrayList<>();
    private ArrayList<String> arrayList4 = new ArrayList<>();
    private ArrayList<String> arrayList5 = new ArrayList<>();

    public static ArrayList<String> arrayListIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_values();

        ImageButton button = findViewById(R.id.menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Bookmarks.class));
            }
        });

        ImageButton button1 = findViewById(R.id.Status);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Show Status Update", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton button2 = findViewById(R.id.chat);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
    }

    public void get_values(){
        db.collection("feeds")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d("TAG", "onSuccess: " + documentSnapshot.getId());
                            arrayListIDs.add(documentSnapshot.getId());
                            arrayList1.add(documentSnapshot.get("Name").toString());
                            arrayList2.add(documentSnapshot.get("likes").toString());
                            arrayList3.add(documentSnapshot.get("tag").toString());
                            arrayList4.add(documentSnapshot.get("time").toString());
                            arrayList5.add(documentSnapshot.get("link").toString());
                        }


                        initRecylerView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("main", e.toString());
                    }
                });

//        arrayList1.add("Daily Quoutes");
//        arrayList2.add("2");
//        arrayList3.add("Today's Quote");
//        arrayList4.add("6:00:00");
//        arrayList5.add("");
    }

    public void initRecylerView(){
        Log.d("TAG", "initRecylerView: HERE");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList1, arrayList2,
                arrayList3, arrayList4, arrayList5, arrayListIDs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

    }
}
