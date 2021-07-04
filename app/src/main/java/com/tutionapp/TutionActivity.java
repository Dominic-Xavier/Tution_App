package com.tutionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dataHelper.CatcheData;
import com.register_Login.Login;

import java.util.ArrayList;
import java.util.List;

public class TutionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentlist;
    private PagerAdapter pagerAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tution_activity);

        fragmentlist = new ArrayList<>();
        fragmentlist.add(new list1());
        fragmentlist.add(new list2());
        fragmentlist.add(new list3());

        viewPager = findViewById(R.id.slide_view);
        dotsLayout = findViewById(R.id.dots_container);
        pagerAdapter = new SlideViews(getSupportFragmentManager(), fragmentlist);

        dots = new TextView[fragmentlist.size()];

        viewPager.setAdapter(pagerAdapter);

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