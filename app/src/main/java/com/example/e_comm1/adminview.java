package com.example.e_comm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class adminview extends AppCompatActivity {
    private RecyclerView adminrecyclerView;
    private ArrayList<z_productmodel> datalist;
    private adminadapter adminadapter;
    FloatingActionButton AddPFloating;
    SearchView edtSearchadmin;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminview);
        getSupportActionBar().setTitle("ALL PRODUCT");

        adminrecyclerView=findViewById(R.id.rcyAdminView);
        AddPFloating=findViewById(R.id.AddPFloating);
        edtSearchadmin=findViewById(R.id.edtSearchadmin);

        AddPFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(adminview.this, addproduct.class);
                intent.putExtra("mode","add");
                startActivity(intent);
            }
        });
        senddata();
        search();
    }


    private void senddata() {
        adminrecyclerView.setLayoutManager(new GridLayoutManager(adminview.this,2));
        datalist=fetchdata();
        adminadapter=new adminadapter(this,datalist);
        adminrecyclerView.setAdapter(adminadapter);
        adminadapter.notifyDataSetChanged();
    }

    private ArrayList<z_productmodel> fetchdata() {
        ArrayList<z_productmodel> data1=new ArrayList<>();

        FirebaseFirestore.getInstance().collection("product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> data=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:data){
                            z_productmodel datamod=ds.toObject(z_productmodel.class);
                            data1.add(datamod);
                            adminadapter.notifyDataSetChanged();
                        }
                    }
                });

        return data1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        senddata();
    }
    private void search() {
        edtSearchadmin.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                proccesssearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                proccesssearch(s);
                return false;
            }
        });
    }

    private void proccesssearch(String s) {
        ArrayList<z_productmodel> sdata=new ArrayList<>();
        for (z_productmodel data:datalist){
            if (data.getName().toLowerCase().contains(s.toLowerCase())){
                sdata.add(data);
            }
        }
        adminadapter.searchdata(sdata);
    }
}