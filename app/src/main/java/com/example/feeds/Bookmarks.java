package com.example.feeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.feeds.MainActivity.arrayListIDs;

public class Bookmarks extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<String> bookmarks = new ArrayList<>();

    private ArrayList<String> arrayList1 = new ArrayList<>();
    private ArrayList<String> arrayList2 = new ArrayList<>();
    private ArrayList<String> arrayList3 = new ArrayList<>();
    private ArrayList<String> arrayList4 = new ArrayList<>();
    private ArrayList<String> arrayList5 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        getBookmarks();
    }

    public void getBookmarks(){
        db.collection("bookmarks")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Log.d("TAG", "onSuccess: " + documentSnapshot.getId());
                            bookmarks.add(documentSnapshot.get("user").toString());
                        }
                        get_single_bookmarks();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Bookmarks.this, "Sad", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void get_single_bookmarks() {
        for(String S : bookmarks) {
            db.collection("feeds")
                    .document(S)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("TAG", "onSuccess: " + documentSnapshot.getId() + "1");
                            arrayListIDs.add(documentSnapshot.getId());
                            arrayList1.add(documentSnapshot.get("Name").toString());
                            arrayList2.add(documentSnapshot.get("likes").toString());
                            arrayList3.add(documentSnapshot.get("tag").toString());
                            arrayList4.add(documentSnapshot.get("time").toString());
                            arrayList5.add(documentSnapshot.get("link").toString());

                            initRecylerView2();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("main", e.toString());
                        }
                    });
        }
    }

    public void initRecylerView2(){
        Log.d("TAG", "initRecylerView: HERE");

        RecyclerView recyclerView = findViewById(R.id.recycler_view2);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList1, arrayList2,
                arrayList3, arrayList4, arrayList5, arrayListIDs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

    }
}
