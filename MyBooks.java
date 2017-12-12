package com.example.andrew.goodreads;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBooks.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBooks extends Fragment {

    private StorageReference mStorage;
    private ProgressDialog mprogress;

    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;

    private FirebaseAuth mAuth;
    private Button add;
    private Uri bookUri;
    private RecyclerView mPdfList;
    private static final int READ_REQUEST_CODE = 42;
    private String bookTitle;
    ProgressDialog progressDialog;
    String uPrevilidge;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBooks newInstance(String param1, String param2) {
        MyBooks fragment = new MyBooks();
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
        mprogress=new ProgressDialog(getActivity());
        mprogress.setCanceledOnTouchOutside(getRetainInstance());
        mAuth=FirebaseAuth.getInstance();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        View view = inflater.inflate(R.layout.fragment_my_books, container, false);
        mPdfList=(RecyclerView)view.findViewById(R.id.pdf_list);
        mPdfList.setHasFixedSize(true);
        mPdfList.setLayoutManager(layoutManager);

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("pdf");
        mAuth=FirebaseAuth.getInstance();
        add=(Button)view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        uPrevilidge = dataSnapshot.child("prevLevel").getValue().toString();
                        if(uPrevilidge.equals("two")) {
                            showUploadDialog();
                        }else {
                            Toast.makeText(getActivity(), "you dont have this privilege", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        progressDialog=new ProgressDialog(getActivity());

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==READ_REQUEST_CODE && resultCode==RESULT_OK){
            mprogress.setMessage("uploading");
            mprogress.show();
            bookUri=data.getData();
            startUploading();

        }

    }
    protected void startUploading(){
        StorageReference filepath=mStorage.child("book_pdf").child(bookUri.getLastPathSegment());
        filepath.putFile(bookUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPdf=mDatabase.push();
                newPdf.child("bookName").setValue(bookTitle);
                newPdf.child("bookUri").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"posted",Toast.LENGTH_SHORT).show();
                        mprogress.dismiss();
                    }
                });

            }
        });
    }
    public static class blogViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public blogViewHolder (View itemView){
            super (itemView);
            mView=itemView;
        }

        public void setPdf(final Context ctx, final String pdf){
            //final TextView textview=(TextView) mView.findViewById(R.id.book_pdf);
            //Picasso.with(ctx).load(image).into(imageview);
            //textview.setText(pdf);

        }
        public void setbookName(final String bookName){
            final TextView textview=(TextView) mView.findViewById(R.id.book_pdf);
            //Picasso.with(ctx).load(image).into(imageview);
            textview.setText(bookName);

        }

}

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<pdf,MyBooks.blogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<pdf, MyBooks.blogViewHolder>(
                pdf.class,
                R.layout.pdf_row,
                MyBooks.blogViewHolder.class,
                mDatabase.getRef()
        ) {
            @Override
            protected void populateViewHolder(MyBooks.blogViewHolder viewHolder, pdf model, int position) {
                final String Post_key=getRef(position).getKey();
                viewHolder.setPdf(getActivity(),model.getBookUri());
                viewHolder.setbookName(model.getBookName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child(Post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                StorageReference mStorage2=FirebaseStorage.getInstance().getReferenceFromUrl(dataSnapshot.child("bookUri").getValue().toString());
                                //downloadInMemory(mStorage2,dataSnapshot.child("bookName").getValue().toString());
                                showDownloadDialog(mStorage2,dataSnapshot.child("bookName").getValue().toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        mPdfList.setAdapter(firebaseRecyclerAdapter);


    }
    private void showUploadDialog(){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.text_inpu_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("upload book");
        alertDialogBuilder.setView(dialogView);
        final EditText userInput = (EditText) dialogView.findViewById(R.id.input);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent galaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                        ////galaryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        galaryIntent.setType("*/*");
                        startActivityForResult(galaryIntent,READ_REQUEST_CODE);
                        bookTitle=userInput.getText().toString();


                    }}).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
}
    private void downloadInMemory(StorageReference fileRef, String bookName) {
//        if (fileRef != null) {
//            progressDialog.setTitle("Downloading...");
//            progressDialog.setMessage(null);
//            progressDialog.show();
//
//            final long ONE_MEGABYTE = 1024 * 1024;
//            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(),"done",Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "Upload file before downloading", Toast.LENGTH_LONG).show();
//        }


        File localFile = null;

        File storagePath = new File(Environment.getExternalStorageDirectory(), "FaiGwom");
        // Create direcorty if not exists
        if(!storagePath.exists()) {
            storagePath.mkdirs();
        }
        //Uri x=Uri.parse(fileRef.toString());    //convert StorageReferance to URi
        //Toast.makeText(getActivity(),x.toString(),Toast.LENGTH_SHORT).show();






        final File myFile = new File(storagePath,bookName+".pdf");

        fileRef.getFile(myFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),"File downloaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Download failed. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(getContext(),"Button working",LENGTH_SHORT).show();
    }

    private void showDownloadDialog(final StorageReference fileRef, final String bookName){
        StorageReference SR=fileRef;
        String BN=bookName;
        LayoutInflater li = LayoutInflater.from(getActivity());
        //View dialogView = li.inflate(R.layout.text_inpu_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Download");
        //alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setMessage("Do you want to download  "+bookName);
        //final EditText userInput = (EditText) dialogView.findViewById(R.id.input);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Download",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        downloadInMemory(fileRef,bookName);
                        //ReadHere(fileRef,bookName);


                    }}).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void ReadHere(StorageReference SRef,String BookName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(SRef.toString()), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent newIntent = Intent.createChooser(intent, "Open File");
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }


    }






}
