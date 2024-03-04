package com.example.e_comm1;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class order extends Fragment {

    ImageView imgOrderImage;
    TextView txtNoDataOrder;
    RecyclerView recyclerViewOrder;
    private favadapter orderadapter;
    SearchView searchView;
    ArrayList<z_productmodel> datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        imgOrderImage=view.findViewById(R.id.imgEmptyOrder);
        txtNoDataOrder=view.findViewById(R.id.txtEmptyOrder);
        searchView=view.findViewById(R.id.edtSearchorder);
        recyclerViewOrder=view.findViewById(R.id.rcyOrder);
        senddata();
        search();
        return view;
    }
    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                proccess(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                proccess(s);
                return false;
            }
        });
    }
    private void proccess(String s) {
        ArrayList<z_productmodel> sdata=new ArrayList<>();
        for (z_productmodel data:datalist){
            if (data.getName().toLowerCase().contains(s.toLowerCase())){
                sdata.add(data);
            }
        }
        orderadapter.searchdata(sdata);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void senddata() {
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        datalist=fetchdata();
        orderadapter=new favadapter(datalist,requireActivity(),"order");
        recyclerViewOrder.setAdapter(orderadapter);
        orderadapter.notifyDataSetChanged();
    }

    private ArrayList<z_productmodel> fetchdata() {
        ArrayList<z_productmodel> data=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("order").whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (z_productmodel ds:queryDocumentSnapshots.toObjects(z_productmodel.class)) {
                            data.add(ds);
                            orderadapter.notifyDataSetChanged();
                        }if (queryDocumentSnapshots.isEmpty()){
                            imgOrderImage.setVisibility(View.VISIBLE);
                            txtNoDataOrder.setVisibility(View.VISIBLE);
                        }else {
                            imgOrderImage.setVisibility(View.GONE);
                            txtNoDataOrder.setVisibility(View.GONE);
                        }
                    }
                });
        return data;
    }
}