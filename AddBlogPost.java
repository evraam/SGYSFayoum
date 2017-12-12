package com.example.andrew.goodreads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBlogPost extends AppCompatActivity {
    private ImageButton mselectImage;
    private Uri image;
    private EditText mPostTitle;
    private EditText mPost;
    private EditText mPostTime;
    private Button mSubmit;
    // String bookTitle_value=null;
    //String bookDes_value=null;

    private StorageReference mStorage;
    private ProgressDialog mprogress;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;
    String date;


    private static final int Galary_request=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog_post);
        mprogress=new ProgressDialog(this);

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("blog");
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());


        mPostTitle=(EditText)findViewById(R.id.post_title) ;
        mPost=(EditText)findViewById(R.id.post_details);
         date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



        mselectImage=(ImageButton)findViewById(R.id.imageSelect);
        mselectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent,Galary_request);
            }
        });

        mSubmit=(Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }


        });

    }

    private void startPosting() {
        mprogress.setMessage("posting ...");
        mprogress.show();
        //final String bookTitle_value=mBookTitle.getText().toString().trim();
        final String PostTitle_value=mPostTitle.getText().toString().trim();
        final String PostDetails_value=mPost.getText().toString().trim();




        if(!TextUtils.isEmpty(PostDetails_value)&&!TextUtils.isEmpty(PostTitle_value) && image!=null){
            StorageReference filepath=mStorage.child("post_images").child(image.getLastPathSegment());
            filepath.putFile(image) .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newbook=mDatabase.push();
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newbook.child("postTitle").setValue(PostTitle_value);
                            newbook.child("Post").setValue(PostDetails_value);
                            newbook.child("postImage").setValue(downloadUrl.toString());
                            newbook.child("postTime").setValue(date);

                            //dataSnapshot.child("name").getValue()
                            
                            /*newbook.child("userName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                    }
                                }
                            });*/


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mprogress.dismiss();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            mprogress.setMessage("error");
                            mprogress.show();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Galary_request && resultCode==RESULT_OK){
            image=data.getData();
            mselectImage.setImageURI(image);
        }
    }
}
