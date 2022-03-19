package com.student;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.SlideViews;
import com.dataHelper.CatcheData;
import com.register_Login.Login;
import com.tutionapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class Student extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder alertDialog;
    private ViewPager viewPager;
    private SlideViews slideViews;
    private List<Fragment> fragmentList;
    private TextView[] dots;
    private LinearLayoutCompat dotsLayout;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity);

        fragmentList = new ArrayList<>();
        fragmentList.add(new List1());
        dotsLayout = findViewById(R.id.student_dots);
        viewPager = findViewById(R.id.student_view_pager);

        slideViews = new SlideViews(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(slideViews);

        dots = new TextView[fragmentList.size()];

        dotsIndicator();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void selectedIndicator(int position) {
        for (int i=0;i<dots.length;i++){
            if(i==position)
                dots[i].setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            else
                dots[i].setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
        }
    }

    private void dotsIndicator() {
        for (int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679"));
            dots[i].setTextSize(18);
            dotsLayout.addView(dots[i]);
            dots[0].setTextColor(Color.BLUE);
        }
    }

    @Override
    public void onBackPressed() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialogInterface,i)-> {
                    if(CatcheData.delete_data(getApplicationContext())){
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                })
                .setNegativeButton("No",null);
        dialog = alertDialog.create();
        dialog.show();
    }
}
