package com.example.ecomm.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecomm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationActivity extends AppCompatActivity {

    ProgressBar progressBar;

    EditText name , email , password;

    private FirebaseAuth auth;

    int count = 0;

    Timer timer;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_bar);
        timer = new Timer();

        sharedPreferences = getSharedPreferences("onBoardingScreen" , MODE_PRIVATE);

        boolean isFirstTime = sharedPreferences.getBoolean("firstTime" , true);

        if(isFirstTime)
        {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime" , false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this , OnBoardingActivity.class);
            startActivity(intent);
            finish();

        }

        if(auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(RegistrationActivity.this , MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void signup(View view) {

        progressBar.setVisibility(View.VISIBLE);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                count++;
                progressBar.setProgress(count);
                if (count == 100)
                    timer.cancel();

            }
        };

        timer.schedule(timerTask , 0 , 100 );

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if(TextUtils.isEmpty(userName))
        {
            Toast.makeText(this, "Enter Name !! ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(this, "Enter Email !!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword))
        {
            Toast.makeText(this, "Enter Password !!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.length() < 6)
        {
            Toast.makeText(this, "Enter Password length more than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }




        auth.createUserWithEmailAndPassword(userEmail , userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this , MainActivity.class);
                            startActivity(intent);
                        }

                        else
                        {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed "+task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void signin(View view) {

        Intent intent = new Intent(RegistrationActivity.this , LoginActivity.class);
        startActivity(intent);

    }
}