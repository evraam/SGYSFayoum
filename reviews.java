package com.example.andrew.goodreads;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reviews extends Fragment {
    private RecyclerView mBookList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private static ProgressDialog mProcessDialog;
    private FirebaseAuth mAuth;
    //private FloatingActionButton mFloating;
    String uPrevilidge;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public reviews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static reviews newInstance(String param1, String param2) {
        reviews fragment = new reviews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDatabase= FirebaseDatabase.getInstance().getReference().child("books");
        mAuth=FirebaseAuth.getInstance();
        //mUserDatabase=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());

        mDatabase.keepSynced(true);
        mProcessDialog=new ProgressDialog(getActivity());
        mProcessDialog.setCanceledOnTouchOutside(getRetainInstance());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        View view=inflater.inflate(R.layout.fragment_reviews, container, false);
        mBookList=(RecyclerView)view.findViewById(R.id.book_list);
//        mFloating=(FloatingActionButton)view.findViewById(R.id.add);
//        mFloating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(view.getContext(),AddBook.class));
//            }
//        });
        mBookList.setHasFixedSize(true);
        mBookList.setLayoutManager(layoutManager);
        mProcessDialog.setMessage("loading...");
        mProcessDialog.show();






        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class blogViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public blogViewHolder (View itemView){
            super (itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView postTitle=(TextView)mView.findViewById(R.id.textView4);
            postTitle.setText(title);
        }
        public void setDes(String des){
            TextView postDes=(TextView)mView.findViewById(R.id.textView5);
            postDes.setText(des);
        }
        public void setImage(final Context ctx, final String image){
            final ImageView imageview=(ImageView)mView.findViewById(R.id.post_image);
            //Picasso.with(ctx).load(image).into(imageview);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(imageview);

                }
            });



            mProcessDialog.dismiss();
        }
        public void setUser(String user){
            TextView postUserName=(TextView)mView.findViewById(R.id.userName);
            postUserName.setText(user);
        }
        public void setUserImage(final Context ctx, final String userImage){
            final ImageView imageview=(ImageView)mView.findViewById(R.id.user_image);
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
        public void setRate(String rate){
            RatingBar postRate=(RatingBar) mView.findViewById(R.id.ratingBar);
            postRate.setRating(Float.parseFloat(rate));
            LayerDrawable stars = (LayerDrawable) postRate.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
            //Drawable drawable = postRate.getProgressDrawable();
            //DrawableCompat.setTint(drawable, android.R.color.holo_orange_light);
            //drawable.setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_ATOP);

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<book,blogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<book, blogViewHolder>(
                book.class,
                R.layout.reviews_row,
                blogViewHolder.class,
                mDatabase.getRef()
        ) {
            @Override
            protected void populateViewHolder(blogViewHolder viewHolder, book model, int position) {

                final String Post_key=getRef(position).getKey();

                viewHolder.setTitle(model.getbookTitle());
                viewHolder.setDes(model.getbookDes());
                viewHolder.setImage(getActivity(),model.getbookImage());
                viewHolder.setUserImage(getActivity(),model.getUserImage());
                viewHolder.setUser(model.getUserName());
                viewHolder.setRate(model.getBookRate());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(),BookComments.class);
                        i.putExtra("post_key",Post_key);
                        startActivity(i);

                    }
                });
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        return true;
                    }
                });
            }
        };
        mBookList.setAdapter(firebaseRecyclerAdapter);


    }
//    public void Add(View view){
//
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_reviews, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_add:
                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        uPrevilidge = dataSnapshot.child("prevLevel").getValue().toString();
                        if(uPrevilidge.equals("two")) {
                            startActivity(new Intent(getActivity(), AddBook.class));
                        }else {
                            Toast.makeText(getActivity(), "you dont have this privilege", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break;

        }
        return true;

    }


}
