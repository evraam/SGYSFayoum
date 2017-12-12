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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddBook extends AppCompatActivity {
    private ImageButton mselectImage;
    private Uri image;
    private EditText mBookTitle;
    private EditText mBookDes;
    private Button mSubmit;
   // String bookTitle_value=null;
    //String bookDes_value=null;

    private StorageReference mStorage;
    private ProgressDialog mprogress;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;


    private static final int Galary_request=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        mprogress=new ProgressDialog(this);

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("books");
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());

        mBookTitle=(EditText)findViewById(R.id.bookName) ;
        mBookDes=(EditText)findViewById(R.id.bookDescription) ;
        
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
        final String bookTitle_value=mBookTitle.getText().toString().trim();
        final String bookDes_value=mBookDes.getText().toString().trim();




        if(!TextUtils.isEmpty(bookTitle_value)  && !TextUtils.isEmpty(bookDes_value) && image!=null){
            StorageReference filepath=mStorage.child("book_images").child(image.getLastPathSegment());
            filepath.putFile(image) .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newbook=mDatabase.push();
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newbook.child("bookTitle").setValue(bookTitle_value);
                            newbook.child("bookDes").setValue(bookDes_value);
                            newbook.child("bookImage").setValue(downloadUrl.toString());
                            newbook.child("userId").setValue(mAuth.getCurrentUser().getUid());
                            newbook.child("userImage").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());

                            //dataSnapshot.child("name").getValue()
                            newbook.child("userName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                    }
                                }
                            });
                            newbook.child("bookRate").setValue("0");

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
