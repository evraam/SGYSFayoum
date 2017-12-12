package com.example.andrew.goodreads;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private static ProgressDialog mProcessDialog;
    private FirebaseAuth mAuth;

    Button addPost;
    String postDate;
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

    public Home() {
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
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        mDatabase= FirebaseDatabase.getInstance().getReference().child("blog");
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

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mBlogList=(RecyclerView)view.findViewById(R.id.blog_list);
//        mFloating=(FloatingActionButton)view.findViewById(R.id.add);
//        mFloating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(view.getContext(),AddBook.class));
//            }
//        });
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(layoutManager);
        mProcessDialog.setMessage("loading...");
        mProcessDialog.show();

        addPost=(Button)view.findViewById(R.id.add);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddBlogPost.class));

            }
        });






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

        public void setPostTitle(String des){
            TextView postDes=(TextView)mView.findViewById(R.id.postTitle);
            postDes.setText(des);
            if(des=="123"){
                postDes.setHighlightColor(Color.parseColor("#ffd700"));
            }
        }
        public void setPostTime(String time){
            TextView postTime=(TextView)mView.findViewById(R.id.postTime);
            //postTime.setText(time);
            String postDate=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            if(time.equals(postDate)){
                postTime.setText("Today");
            }else{
                postTime.setText(time);
            }

        }

        public void setPostImage(final Context ctx, final String image){
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

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<blog,blogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<blog, blogViewHolder>(
                blog.class,
                R.layout.blog_row,
                blogViewHolder.class,
                mDatabase.getRef()
        ) {
            @Override
            protected void populateViewHolder(blogViewHolder viewHolder, blog model, int position) {

                final String Post_key=getRef(position).getKey();

                viewHolder.setPostTitle(model.getPostTitle());
                viewHolder.setPostImage(getActivity(),model.getPostImage());
                viewHolder.setPostTime(model.getPostTime());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(),BlogPost.class);
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
        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }
//    public void Add(View view){
//
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }
}
