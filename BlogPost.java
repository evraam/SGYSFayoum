package com.example.andrew.goodreads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlogPost extends AppCompatActivity {

    String post_key;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;
    private TextView mPostTitle,mPostDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        mPostTitle=(TextView)findViewById(R.id.post_title);
        mPostDetails=(TextView)findViewById(R.id.post_details);

        Intent i=getIntent();
        post_key=i.getStringExtra("post_key");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("blog");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPostTitle.setText(dataSnapshot.child(post_key).child("postTitle").getValue().toString());
                mPostDetails.setText(dataSnapshot.child(post_key).child("Post").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
