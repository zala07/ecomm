package com.example.e_comm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class favorite extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView txtEmptyFav;
    private ImageView imgEmptyFav;
    private favadapter adapterfav;

    SearchView edtSearchfav;
    private ArrayList<z_productmodel> datalist;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerView=findViewById(R.id.rcyFav);
        txtEmptyFav=findViewById(R.id.txtEmptyFav);
        imgEmptyFav=findViewById(R.id.imgEmptyFav);
        edtSearchfav=findViewById(R.id.edtSearchfav);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist=fetchdata();
        adapterfav=new favadapter(datalist,favorite.this,"fav");
        recyclerView.setAdapter(adapterfav);
        adapterfav.notifyDataSetChanged();
        search();

    }


    private ArrayList<z_productmodel> fetchdata() {
        ArrayList<z_productmodel> data=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("fav").whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (z_productmodel ds:queryDocumentSnapshots.toObjects(z_productmodel.class)) {
                            data.add(ds);
                            adapterfav.notifyDataSetChanged();
                        }if (queryDocumentSnapshots.isEmpty()){
                            imgEmptyFav.setVisibility(View.VISIBLE);
                            txtEmptyFav.setVisibility(View.VISIBLE);
                        }else {
                            imgEmptyFav.setVisibility(View.GONE);
                            txtEmptyFav.setVisibility(View.GONE);
                        }
                    }
                });
        return data;
    }

    private void search() {
        edtSearchfav.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        adapterfav.searchdata(sdata);
    }
}