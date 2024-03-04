package com.example.e_comm1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout llHome,llCart,llOrder,llProfile,layout;
    ImageView imgHome,imgCart,imgOrder,imgProfile,image;
    TextView txtHome,txtCart,txtOrder,txtProfile,textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flMain, new home());
        ft.commit();

        idsofbottumlayout();

        layoutclick();
    }

    private void idsofbottumlayout() {
        llHome=findViewById(R.id.llHome);
        llProfile=findViewById(R.id.llProfile);
        llCart=findViewById(R.id.llCart);
        llOrder=findViewById(R.id.llOrder);

        imgHome=findViewById(R.id.imgHome);
        imgProfile=findViewById(R.id.imgProfile);
        imgCart=findViewById(R.id.imgCart);
        imgOrder=findViewById(R.id.imgOrder);

        txtHome=findViewById(R.id.txtHome);
        txtProfile=findViewById(R.id.txtProfile);
        txtCart=findViewById(R.id.txtCart);
        txtOrder=findViewById(R.id.txtOrder);

    }

    private void layoutclick() {
        llHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                layout=llHome;
                image=imgHome;
                textview=txtHome;

                setlayaout(layout,image,textview);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flMain, new home());
                ft.commit();
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                layout=llProfile;
                image=imgProfile;
                textview=txtProfile;

                setlayaout(layout,image,textview);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flMain, new profile1());
                ft.commit();              }
        });

        llCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                layout=llCart;
                image=imgCart;
                textview=txtCart;

                setlayaout(layout,image,textview);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flMain, new cart());
                ft.commit();
            }
        });

        llOrder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                layout=llOrder;
                image=imgOrder;
                textview=txtOrder;

                setlayaout(layout,image,textview);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.flMain, new order());
                ft.commit();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setlayaout(LinearLayout layout, ImageView image, TextView textview) {
        //bg on layout
        llProfile.setBackgroundColor(getResources().getColor(R.color.white));
        llCart.setBackgroundColor(getResources().getColor(R.color.white));
        llOrder.setBackgroundColor(getResources().getColor(R.color.white));
        llHome.setBackgroundColor(getResources().getColor(R.color.white));

        //change  tint on image view
        ImageViewCompat.setImageTintList(imgProfile,getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(imgCart,getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(imgOrder,getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(imgHome,getColorStateList(R.color.black));

        //visiblity gone on tex
        txtProfile.setVisibility(View.GONE);
        txtCart.setVisibility(View.GONE);
        txtOrder.setVisibility(View.GONE);
        txtHome.setVisibility(View.GONE);


        //seting up selected layout
        layout.setBackground(getResources().getDrawable(R.drawable.bottomshape));
        ImageViewCompat.setImageTintList(image,getColorStateList(R.color.green));
        textview.setVisibility(View.VISIBLE);

        //animation
        ScaleAnimation animation=new ScaleAnimation(0.8f,1.0f,1f,1f,
                Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        layout.startAnimation(animation);
    }

}