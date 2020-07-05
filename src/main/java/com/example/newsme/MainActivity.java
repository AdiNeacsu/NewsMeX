package com.example.newsme;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
        List<Fragment> fragmentList = null;
        if (navHostFragment != null) {
            fragmentList = navHostFragment.getChildFragmentManager().getFragments();
        }

        boolean handled = false;
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof BaseFragment) {
                handled = ((BaseFragment) fragment).onBackPressed();
                if (handled) {
                    //hm
                    break;
                }
            }
        }
        if (!handled){
            super.onBackPressed();
        }
    }
}