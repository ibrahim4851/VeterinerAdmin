package com.example.veterineradmin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.veterineradmin.Fragments.HomeFragment;
import com.example.veterineradmin.R;
import com.example.veterineradmin.Utils.ChangeFragments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        change();
    }

    public void change()
    {
        Fragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrameLayout, newFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

}