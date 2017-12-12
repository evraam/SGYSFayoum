package com.example.andrew.goodreads;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddHymn extends AppCompatActivity {

    private EditText mHymnTitle;
    private EditText mHymn;
    private Button mSubmit;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hymn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("hymns");

        mHymnTitle = (EditText) findViewById(R.id.hymn_name);
        mHymn = (EditText) findViewById(R.id.hymn);
        mSubmit = (Button) findViewById(R.id.add_hymn);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }


        });
    }

    private void startPosting() {
        // mprogress.setMessage("posting ...");
        // mprogress.show();
        final String hymnTitle_value = mHymnTitle.getText().toString().trim();
        final String hymn_value = mHymn.getText().toString().trim();


        if (!TextUtils.isEmpty(hymn_value) && !TextUtils.isEmpty(hymnTitle_value)) {
            final DatabaseReference newHymn=mDatabase.push();
            newHymn.child("hymnTitle").setValue(hymnTitle_value);
            newHymn.child("hymn").setValue(hymn_value);
        }

    }
}
