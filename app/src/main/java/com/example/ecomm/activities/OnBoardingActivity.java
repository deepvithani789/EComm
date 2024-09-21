package com.example.ecomm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.ecomm.R;
import com.example.ecomm.adapters.SliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    AppCompatButton btn;
    Animation alpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.slider);
        btn = findViewById(R.id.get_started_btn);
        alpha = AnimationUtils.loadAnimation(this,R.anim.btn_animation);
        dotsLayout =findViewById(R.id.dots);
        addDots(0);

        viewPager.addOnPageChangeListener(changeListener);

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OnBoardingActivity.this , RegistrationActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void addDots(int position)
    {

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i=0 ; i<dots.length ; i++)
        {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0)
        {

            dots[position].setTextColor(getResources().getColor(R.color.pink));

        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

            if (position == 0)
                btn.setVisibility(View.INVISIBLE);

            else if (position == 1) {

                btn.setVisibility(View.INVISIBLE);

            }
            else {

                btn.startAnimation(alpha);
                btn.setVisibility(View.VISIBLE);

            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}