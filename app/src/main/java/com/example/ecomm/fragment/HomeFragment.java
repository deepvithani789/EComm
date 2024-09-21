package com.example.ecomm.fragment;

import static androidx.viewpager.widget.ViewPager.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomm.R;
import com.example.ecomm.activities.ShowAllActivity;
import com.example.ecomm.adapters.BannerSlidingAdapter;
import com.example.ecomm.adapters.CategoryAdapter;
import com.example.ecomm.adapters.NewProductsAdapter;
import com.example.ecomm.adapters.PopularProductsAdapter;
import com.example.ecomm.models.CategoryModel;
import com.example.ecomm.models.NewProductsModel;
import com.example.ecomm.models.PopularProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    TextView catShowAll , popularShowAll , newProductShowAll;

    ProgressDialog progressDialog;

    LinearLayout linearLayout;
    ViewPager viewPager;
    LinearLayout dotsLayout;
    TextView[] dots;
    BannerSlidingAdapter bannerSlidingAdapter;

    RecyclerView catRecyclerview , newProductRecyclerView , popularRecyclerView;
    List<NewProductsModel> newProductsModelList;
    NewProductsAdapter newProductsAdapter;

    List<PopularProductsModel> popularProductsModelList;
    PopularProductsAdapter popularProductsAdapter;

    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    FirebaseFirestore db;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        catShowAll = root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll = root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ShowAllActivity.class);
                startActivity(intent);
            }
        });

        popularShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductRecyclerView = root.findViewById(R.id.new_product_rec);

        popularRecyclerView = root.findViewById(R.id.popular_rec);

        progressDialog = new ProgressDialog(getActivity());

        catRecyclerview = root.findViewById(R.id.rec_category);



        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(GONE);

        viewPager = root.findViewById(R.id.image_slider);
        dotsLayout = root.findViewById(R.id.dots);
        addDots(0);

        bannerSlidingAdapter = new BannerSlidingAdapter(getActivity().getApplicationContext());
        viewPager.setAdapter(bannerSlidingAdapter);
        viewPager.addOnPageChangeListener(changeListener);

        progressDialog.setTitle("Welcome To My E-Commerce App ");
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(R.drawable.icon);
        progressDialog.show();

        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL , false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext() , categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);



        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(VISIBLE);
                                progressDialog.dismiss();
                            }

                        }

                        else {



                        }

                    }
                });

        //New Product
        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL , false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext() , newProductsModelList);
        newProductRecyclerView.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();

                            }

                        }

                        else {

                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

        //Popular Product
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.HORIZONTAL , false));
        popularProductsModelList = new ArrayList<>();
        popularProductsAdapter = new PopularProductsAdapter(getContext() , popularProductsModelList);
        popularRecyclerView.setAdapter(popularProductsAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful())
                        {

                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                PopularProductsModel popularProductsModel = document.toObject(PopularProductsModel.class);
                                popularProductsModelList.add(popularProductsModel);
                                popularProductsAdapter.notifyDataSetChanged();

                            }

                        }

                        else {

                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

        return root;
    }

    private void addDots(int position)
    {

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i=0 ; i<dots.length ; i++)
        {

            dots[i] = new TextView(getActivity().getApplicationContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(25);
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0)
        {

            dots[position].setTextColor(getResources().getColor(R.color.pink));

        }

    }

    ViewPager.OnPageChangeListener changeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}