package com.example.newsme.request;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import timber.log.Timber;

public class ViewPagerRequestAdapter extends FragmentStateAdapter {
    public ViewPagerRequestAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    //Atentie! Daca modifici ordinea sau elimini vreun fragment, trebuie modificate variabilele fragmentRequest in mai multe locuri.

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                Timber.d("ajunge in adapter = %s", String.valueOf("in fragmentul History"));
                fragment = RequestHistoryFragment.newInstance("de sters", "de sters");
                break;
            case 1:
                fragment = RequestPredefinedFragment.newInstance("de sters", "de sters");
                break;
            case 2:
                fragment = RequestCustomFragment.newInstance();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;//deocamdata l-am hardcodat; de luat dintr-un loc mai logic
    }
}
