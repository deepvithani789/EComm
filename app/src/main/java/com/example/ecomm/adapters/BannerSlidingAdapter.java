package com.example.ecomm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.ecomm.R;

public class BannerSlidingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public BannerSlidingAdapter(Context context)
    {

        this.context = context;

    }

    int imagesArray[] = {

            R.drawable.banner1 ,
            R.drawable.banner2 ,
            R.drawable.banner3

    };

    @Override
    public int getCount() {
        return imagesArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_sliding_layout , container ,false);

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageResource(imagesArray[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);

    }
}
