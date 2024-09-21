package com.example.ecomm.activities;

import static com.example.ecomm.R.id.my_rating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ecomm.R;
import com.example.ecomm.models.NewProductsModel;
import com.example.ecomm.models.PopularProductsModel;
import com.example.ecomm.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {


    ImageView detailedImg;
    TextView rating , name , description , price , quantity;
    AppCompatButton addToCart , buyNow;
    ImageView addItems , removeItems;

    Toolbar toolbar;

    int totalQuantity = 1;
    int totalPrice = 0;

    //ShowAll
    ShowAllModel showAllModel = null;

    //PopularProducts
    PopularProductsModel popularProductsModel = null;

    //NewProducts
    NewProductsModel newProductsModel = null;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();



        //NewProducts
        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel)
        {

            newProductsModel = (NewProductsModel) obj;

        } else if (obj instanceof PopularProductsModel) {

            popularProductsModel = (PopularProductsModel) obj;

        } else if (obj instanceof ShowAllModel){

            showAllModel = (ShowAllModel) obj;

        }


        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        detailedImg = findViewById(R.id.detailed_img);

        detailedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailedActivity.this , FullImageActivity.class);
                if (newProductsModel != null)
                {

                    intent.putExtra("image_url",newProductsModel.getImg_url());

                }

                if (popularProductsModel != null)
                {

                    intent.putExtra("image_url",popularProductsModel.getImg_url());

                }

                if (showAllModel != null)
                {

                    intent.putExtra("image_url",showAllModel.getImg_url());

                }

                startActivity(intent);

            }
        });

        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_description);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //NewProducts
        if (newProductsModel != null)
        {

            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));

            totalPrice = newProductsModel.getPrice() * totalQuantity;

        }

        //PopularProducts
        if (popularProductsModel != null)
        {

            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            description.setText(popularProductsModel.getDescription());
            rating.setText(popularProductsModel.getRating());
            price.setText(String.valueOf(popularProductsModel.getPrice()));

            totalPrice = popularProductsModel.getPrice() * totalQuantity;

        }

        //ShowAll
        if (showAllModel != null)
        {

            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            rating.setText(showAllModel.getRating());
            price.setText(String.valueOf(showAllModel.getPrice()));

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }

        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailedActivity.this , AddressActivity.class);

                if (newProductsModel != null)
                {

                    intent.putExtra("item" , newProductsModel);

                }

                if (popularProductsModel != null)
                {

                    intent.putExtra("item" , popularProductsModel);

                }

                if (showAllModel != null)
                {

                    intent.putExtra("item" , showAllModel);

                }
                startActivity(intent);

            }
        });

        //Add to Cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCart();

            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity < 10)
                {

                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null)
                    {

                        totalPrice = newProductsModel.getPrice() * totalQuantity;

                    }

                    if (popularProductsModel != null)
                    {

                        totalPrice = popularProductsModel.getPrice() * totalQuantity;

                    }

                    if (showAllModel != null)
                    {

                        totalPrice = showAllModel.getPrice() * totalQuantity;

                    }

                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity  > 1)
                {

                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                }

            }
        });

    }

    private void addToCart()
    {

        String saveCurrentTime , saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String , Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailedActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailedActivity.this, "Failed to add to cart: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}