package com.example.newsme;

import androidx.fragment.app.Fragment;

import timber.log.Timber;

public class BaseFragment extends Fragment {

    public boolean onBackPressed() {
        return false;
    }

}
