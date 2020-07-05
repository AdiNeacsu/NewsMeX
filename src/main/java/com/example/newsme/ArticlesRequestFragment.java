package com.example.newsme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsme.databinding.FragmentArticlesRequestBinding;
import com.example.newsme.request.ViewPagerRequestAdapter;
import com.example.newsme.viewmodel.ArticleRequestViewModel;
import com.example.newsme.viewmodel.ArticleViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticlesRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticlesRequestFragment extends BaseFragment {

    NavController navController = null;
    FragmentArticlesRequestBinding binding;
    ArticleRequestViewModel articleRequestViewModel;
    private TextView[] dots;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArticlesRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticlesRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticlesRequestFragment newInstance(String param1, String param2) {
        ArticlesRequestFragment fragment = new ArticlesRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        articleRequestViewModel = new ViewModelProvider(this).get(ArticleRequestViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_articles_request,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            if (getArguments().getString("fragmentRequest") != null) {
                //lansez requestul cu textul de cautare predefined category
                Timber.d("ajunge la deschidere = %s", String.valueOf("A"));
                articleRequestViewModel.setFragmentRequest(getArguments().getString("fragmentRequest"));
            } else {
                Timber.d("ajunge la deschidere = %s", String.valueOf("B"));
                articleRequestViewModel.setFragmentRequest("0");
            }
        }

        ViewPagerRequestAdapter viewPagerRequestAdapter = new ViewPagerRequestAdapter(this);
        binding.vp2Request.setAdapter(viewPagerRequestAdapter);
        binding.vp2Request.setPageTransformer(new ZoomOutPageTransformer());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int pagina = Integer.parseInt(articleRequestViewModel.getFragmentRequest());
                binding.vp2Request.setCurrentItem(pagina);
                addBottomDots(pagina);
            }
        });

        binding.vp2Request.registerOnPageChangeCallback(pageChangeCallback);

    }

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            addBottomDots(position);
        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[3];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        binding.layoutDots.removeAllViews();//sterge toate bulinutele si le redeseneaza
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml(getString(R.string.bulinuta)));
            dots[i].setTextSize(60);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            dots[i].setPadding(8,0,8,0);
            dots[i].setGravity(Gravity.CENTER_VERTICAL);
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public boolean onBackPressed() {
        //return super.onBackPressed();
        getActivity().finishAffinity();
        System.exit(0);
        return true;
    }

    private static class ZoomOutPageTransformer implements ViewPager2.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}