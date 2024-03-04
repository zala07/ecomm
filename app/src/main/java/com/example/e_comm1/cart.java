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

public class cart extends Fragment {
    ImageView imgCartImage;
    TextView txtNoDataCart;
    RecyclerView recyclerViewCart;
    favadapter cartadapter;
    ArrayList<z_productmodel> datalist;
    SearchView edtSearchcart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart, container, false);

        imgCartImage=view.findViewById(R.id.imgEmptyCart);
        txtNoDataCart=view.findViewById(R.id.txtEmptyCart);
        edtSearchcart=view.findViewById(R.id.edtSearchcart);
        recyclerViewCart=view.findViewById(R.id.rcyCart);

        senddata();
        search();
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void senddata() {
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireActivity()));
        datalist=fetchdata();
        cartadapter=new favadapter(datalist,requireActivity(),"cart");
        recyclerViewCart.setAdapter(cartadapter);
        cartadapter.notifyDataSetChanged();
    }

    private ArrayList<z_productmodel> fetchdata() {
        ArrayList<z_productmodel> data=new ArrayList<>();

        FirebaseFirestore.getInstance().collection("cart").whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (z_productmodel ds:queryDocumentSnapshots.toObjects(z_productmodel.class)) {
                            data.add(ds);
                            cartadapter.notifyDataSetChanged();
                        }if (queryDocumentSnapshots.isEmpty()){
                            imgCartImage.setVisibility(View.VISIBLE);
                            txtNoDataCart.setVisibility(View.VISIBLE);
                        }else {
                            imgCartImage.setVisibility(View.GONE);
                            txtNoDataCart.setVisibility(View.GONE);
                        }
                    }
                });

        return data;
    }
    private void search() {
        edtSearchcart.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        cartadapter.searchdata(sdata);
    }
}