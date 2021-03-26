package com.example.veterineradmin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.veterineradmin.Fragments.HomeFragment;
import com.example.veterineradmin.R;
import com.example.veterineradmin.Utils.ChangeFragments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeFragments changeFragments = new ChangeFragments(MainActivity.this);
        changeFragments.change(new HomeFragment());
    }
}