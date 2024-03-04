package com.example.e_comm1;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment {
    private ImageView ProfileIcon,imgFavorite;

    private TextView txtDisplayName,txtCatAll,txtCatMobile,txtCatFootwear,
            txtCatToys,txtCatWatches,txtCatFashion,txtCatBooks;
    private z_profilimodel profilimodel;
    private z_productmodel productmodel;
    private String trm_email;
    private productadapter adapter;
    private productadapter2 adapter2;
    private ArrayList<z_productmodel> datalist,datalist2;
    private RecyclerView recyclerView,recyclerView2;

    private String cat="all";

    SearchView searchView;

    //must find ids
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        imgFavorite=view.findViewById(R.id.imgFavorite);
        ProfileIcon=view.findViewById(R.id.ProfileIcon);
        txtDisplayName=view.findViewById(R.id.txtNameDisplay);
        recyclerView=view.findViewById(R.id.rcyMain);
        recyclerView2=view.findViewById(R.id.rcylSecond);
        searchView=view.findViewById(R.id.edtSearch);

        catidfind(view);

        senddata1();
        senddata2();
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireActivity(), favorite.class);
                requireActivity().startActivity(intent);
            }
        });

        settingUserInfo();
        search();
        return view;
    }



    @SuppressLint("NotifyDataSetChanged")
    private void senddata1() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false));
        datalist=fetchdata1();
        adapter=new productadapter(requireActivity(),datalist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private ArrayList<z_productmodel> fetchdata1(){
        ArrayList<z_productmodel> data1=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("product").whereEqualTo("category","new").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> data=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:data){
                            productmodel=ds.toObject(z_productmodel.class);
                            data1.add(productmodel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        return data1;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void senddata2() {
        recyclerView2.setLayoutManager(new GridLayoutManager(requireActivity(),2));
        datalist2=fetchdata2(cat);
        adapter2=new productadapter2(requireActivity(),datalist2,cat);
        recyclerView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    private ArrayList<z_productmodel> fetchdata2(String cat) {
        Task<QuerySnapshot> ref=null;
        if (cat.equals("all")){
            ref=FirebaseFirestore.getInstance().collection("product").whereNotEqualTo("category","new").get();
        }else {
            ref=FirebaseFirestore.getInstance().collection("product").whereNotEqualTo("category","new").whereEqualTo("category",cat).get();
        }
        ArrayList<z_productmodel> data1=new ArrayList<>();
        ref.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> data=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:data){
                            productmodel=ds.toObject(z_productmodel.class);
                            data1.add(productmodel);
                            adapter2.notifyDataSetChanged();
                        }
                    }
                });
        return data1;
    }

    private void settingUserInfo() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        int index=email.indexOf("@");
        trm_email=email.substring(0,index);
        FirebaseFirestore.getInstance().collection(trm_email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> data=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:data){
                            profilimodel=ds.toObject(z_profilimodel.class);
                            txtDisplayName.setText(profilimodel.getName());
                            if (isAdded()){
                                Glide.with(requireActivity()).load(profilimodel.getImageUri()).into(ProfileIcon);
                            }
                        }
                    }
                });
    }

    private void catidfind(View view) {
        //textView
        txtCatAll=view.findViewById(R.id.txtCatAll);
        txtCatMobile=view.findViewById(R.id.txtCatMobile);
        txtCatFootwear=view.findViewById(R.id.txtCatFootwear);
        txtCatToys=view.findViewById(R.id.txtCatToys);
        txtCatWatches=view.findViewById(R.id.txtCatWatches);
        txtCatFashion=view.findViewById(R.id.txtCatFashion);
        txtCatBooks=view.findViewById(R.id.txtCatBooks);

        txtCatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatAll);
                cat="all";
                senddata2();
            }
        });
        txtCatMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatMobile);
                cat="mobile";
                senddata2();
            }
        });
        txtCatFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatFootwear);
                cat="footwear";
                senddata2();
            }
        });
        txtCatFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatFashion);
                cat="fashion";
                senddata2();
            }
        });
        txtCatToys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatToys);
                cat="toy";
                senddata2();
            }
        });
        txtCatWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatWatches);
                cat="watches";
                senddata2();
            }
        });
        txtCatBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changetheme(txtCatBooks);
                cat="book";
                senddata2();
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changetheme(TextView cattextview) {
        txtCatAll.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatMobile.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatFashion.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatFootwear.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatToys.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatWatches.setBackground(getResources().getDrawable(R.drawable.whiteroundback));
        txtCatBooks.setBackground(getResources().getDrawable(R.drawable.whiteroundback));


        txtCatAll.setTextColor(getResources().getColor(R.color.green));
        txtCatMobile.setTextColor(getResources().getColor(R.color.green));
        txtCatFashion.setTextColor(getResources().getColor(R.color.green));
        txtCatFootwear.setTextColor(getResources().getColor(R.color.green));
        txtCatToys.setTextColor(getResources().getColor(R.color.green));
        txtCatBooks.setTextColor(getResources().getColor(R.color.green));
        txtCatWatches.setTextColor(getResources().getColor(R.color.green));

        cattextview.setTextColor(getResources().getColor(R.color.white));
        cattextview.setBackground(getResources().getDrawable(R.drawable.edtback));
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                proccesssearch(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                proccesssearch(s);
                return true;
            }
        });
    }

    private void proccesssearch(String s) {
        ArrayList<z_productmodel> sdata=new ArrayList<>();
        for (z_productmodel data:datalist2){
            if (data.getName().toLowerCase().contains(s.toLowerCase())){
                sdata.add(data);
            }
        }
        adapter2.searchdata(sdata);
    }

}























