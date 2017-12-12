package com.example.andrew.goodreads;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AllHymns extends AppCompatActivity {

    private ProgressDialog mprogress;

    private DatabaseReference mDatabase;
    private Button addHymn;
    private RecyclerView mHymnsList;
    private String hymnTitle;
    ProgressDialog progressDialog;
    SearchView SV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hymns);


        mprogress=new ProgressDialog(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mHymnsList=(RecyclerView)findViewById(R.id.hymns_list);
        mHymnsList.setHasFixedSize(true);
        mHymnsList.setLayoutManager(layoutManager);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("hymns");

        addHymn=(Button)findViewById(R.id.add_hymn);
        addHymn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddHymn.class));


            }
        });


        SV=(SearchView)findViewById(R.id.search);
        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FirebaseRecyclerAdapter<Hymn,AllHymns.blogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Hymn, AllHymns.blogViewHolder>(
                        Hymn.class,
                        R.layout.pdf_row,
                        AllHymns.blogViewHolder.class,
                        mDatabase.orderByChild("hymnTitle").startAt(s).endAt(s+"\uf8ff")
                ) {
                    @Override
                    protected void populateViewHolder(AllHymns.blogViewHolder viewHolder, Hymn model, int position) {
                        final String Post_key=getRef(position).getKey();
                        viewHolder.setHymnName(model.getHymnTitle());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDatabase.child(Post_key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
//
                                        Intent i=new Intent(getApplicationContext(),viewHymn.class);
                                        i.putExtra("post_key",Post_key);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                    }
                };
                mHymnsList.setAdapter(firebaseRecyclerAdapter);


                return false;
            }
        });



    }
    public static class blogViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public blogViewHolder (View itemView){
            super (itemView);
            mView=itemView;
        }
        public void setHymnName(final String HymnName){
            final TextView textview=(TextView) mView.findViewById(R.id.book_pdf);
            //Picasso.with(ctx).load(image).into(imageview);
            textview.setText(HymnName);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Hymn,AllHymns.blogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Hymn, AllHymns.blogViewHolder>(
                Hymn.class,
                R.layout.pdf_row,
                AllHymns.blogViewHolder.class,
                mDatabase.getRef()
        ) {
            @Override
            protected void populateViewHolder(AllHymns.blogViewHolder viewHolder, Hymn model, int position) {
                final String Post_key=getRef(position).getKey();
                viewHolder.setHymnName(model.getHymnTitle());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child(Post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//
                                Intent i=new Intent(getApplicationContext(),viewHymn.class);
                                i.putExtra("post_key",Post_key);
                                startActivity(i);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        mHymnsList.setAdapter(firebaseRecyclerAdapter);


    }



}
