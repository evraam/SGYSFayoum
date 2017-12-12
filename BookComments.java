package com.example.andrew.goodreads;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class BookComments extends AppCompatActivity {
    String post_key;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;
    private TextView mComment;
    private ImageButton mSubmitComment;
    private ProgressDialog mProcessDialog;

    private RecyclerView mCommentList;
    private RatingBar rate;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comments);
        rate=(RatingBar)findViewById(R.id.book_rate) ;
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                final int numStars = ratingBar.getNumStars();

                //Toast.makeText(getApplicationContext(),Integer.toString(rating),Toast.LENGTH_SHORT).show();
                setRate(ratingBar.getRating());

            }
        });

        mCommentList=(RecyclerView)findViewById(R.id.comment_list);
        mCommentList.setHasFixedSize(true);
        mCommentList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        bookTitle=(TextView)findViewById(R.id.title);
//        bookDes=(TextView)findViewById(R.id.description) ;
        mProcessDialog=new ProgressDialog(this);
        mProcessDialog.setCanceledOnTouchOutside(false);

        Intent i=getIntent();
        post_key=i.getStringExtra("post_key");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("comment");
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());

        mDatabase.keepSynced(true);
        mComment=(EditText)findViewById(R.id.comment);
        mSubmitComment=(ImageButton)findViewById(R.id.submit_comment);
        mSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProcessDialog.setMessage("wait");
                mProcessDialog.show();
                DatabaseReference mDatabaseAllBookComments=FirebaseDatabase.getInstance().getReference().child("comment").child(post_key);
                final DatabaseReference newComment=mDatabaseAllBookComments.push();
                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newComment.child("userImage").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                        newComment.child("comment").setValue(mComment.getText().toString().trim());
                        newComment.child("user").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mProcessDialog.dismiss();
                                    finish();
                                    startActivity(getIntent());

                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                bookTitle.setText(dataSnapshot.child("bookTitle").getValue().toString());
//                bookDes.setText(dataSnapshot.child("bookDes").getValue().toString());
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }
    public static class commentViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public commentViewHolder (View itemView){
            super (itemView);
            mView=itemView;
        }
        public void setUserName(String user){
            TextView userName=(TextView)mView.findViewById(R.id.user_name);
            userName.setText(user);
        }
        public void setUserComment(String comment){
            TextView userComment=(TextView)mView.findViewById(R.id.user_comment);
            userComment.setText(comment);
        }
        public void setUserImage(final Context ctx, final String userImage){
            final ImageView imageview=(ImageView)mView.findViewById(R.id.userImage);
            //Picasso.with(ctx).load(image).into(imageview);
            Picasso.with(ctx).load(userImage).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(userImage).into(imageview);

                }
            });

        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<comment,commentViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<comment, commentViewHolder>(
                comment.class,
                R.layout.comment_row,
                commentViewHolder.class,
                mDatabase.child(post_key).getRef()
        ) {
            @Override
            protected void populateViewHolder(commentViewHolder viewHolder, comment model, int position) {

                final String Post_key=getRef(position).getKey();

                viewHolder.setUserName(model.getUser());
                viewHolder.setUserComment(model.getComment());
                viewHolder.setUserImage(getApplicationContext(),model.getUserImage());

            }
        };
        mCommentList.setAdapter(firebaseRecyclerAdapter);


    }
    public void setRate(final float rate){
        DatabaseReference DBR= (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("books");
        DBR.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float previousRate= Float.parseFloat(dataSnapshot.child("bookRate").getValue().toString());
                int totalRate=Math.round((previousRate+rate)/2);
                //Toast.makeText(getApplicationContext(),)
                updateRate(Integer.toString(totalRate));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updateRate(final String totalRate) {
        DatabaseReference DBR = (DatabaseReference) FirebaseDatabase.getInstance().getReference()
                .child("books").child(post_key);
                DBR.child("bookRate").setValue(totalRate.toString());
    }
}
