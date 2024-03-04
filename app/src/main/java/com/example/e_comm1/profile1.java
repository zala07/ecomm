package com.example.e_comm1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class profile1 extends Fragment {
    private ImageView btnProfileUpdate;
    private z_profilimodel profilimodel;
    private ImageView imgProfileDispImage;
    private TextView txtProfileDispName,txtProfileDipsAddress,txtProfileDispPhone,txtLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile1, container, false);
        btnProfileUpdate=view.findViewById(R.id.btnProfileUpdate);
        imgProfileDispImage=view.findViewById(R.id.imgProfileDispImage);
        txtLogout=view.findViewById(R.id.txtLogout);
        txtProfileDipsAddress=view.findViewById(R.id.txtProfileDispAddress);
        txtProfileDispName=view.findViewById(R.id.txtProfileDispName);
        txtProfileDispPhone=view.findViewById(R.id.txtProfileDispNum);

        showdata();
        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),updateuserdata.class);
                intent.putExtra("pdata",profilimodel);
                intent.putExtra("mode","update");
                startActivity(intent);
            }
        });
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
                builder.setTitle("LOG_OUT");
                builder.setCancelable(false);
                builder.setMessage("ARE YOU WANT TO LOG OUT!!");
                builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.setOnDismissListener(DialogInterface::dismiss);
                    }
                });
                builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        builder.setOnDismissListener(DialogInterface::dismiss);
                        Intent intent=new Intent(requireActivity(), login.class);
                        requireActivity().startActivity(intent);
                    }
                });builder.show();
            }
        });
        return view;
    }

    private void showdata() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        int index=email.indexOf("@");
        String trm_email=email.substring(0,index);
        FirebaseFirestore.getInstance().collection(trm_email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> data=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:data){
                            profilimodel=ds.toObject(z_profilimodel.class);
                            txtProfileDispName.setText(profilimodel.getName());
                            txtProfileDipsAddress.setText(profilimodel.getAddress());
                            txtProfileDispPhone.setText("+91"+profilimodel.getPhone());
                            if (isAdded()){
                                Glide.with(requireActivity()).load(profilimodel.getImageUri()).into(imgProfileDispImage);
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        showdata();
    }

}