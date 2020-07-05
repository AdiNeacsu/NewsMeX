package com.example.newsme;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.webkit.WebViewClientCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.newsme.data.local.ArticleEntity;
import com.example.newsme.databinding.FragmentArticleBinding;
import com.example.newsme.viewmodel.ArticleViewModel;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class ArticleFragment extends BaseFragment {

    NavController navController = null;
    FragmentArticleBinding binding;
    ArticleViewModel articleViewModel;
    WebView wvArticle;
    private String fragment_requesting_list;//din ce fragment request (ce lista s-a cerut) s-a generat lista din care am deschis acest articol

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance() {
        return new ArticleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false);

        wvArticle = binding.wvArticle;
        wvArticle.setWebViewClient(new CustomBrowser());

        WebSettings webSettingsCompat = wvArticle.getSettings();//nu ma lasa cu WebSettingsCompat
        webSettingsCompat.setJavaScriptEnabled(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.setVariable(BR.articleViewModel,articleViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());//adaug LiveData

        articleViewModel.getArticleEntity().observe(getViewLifecycleOwner(), new Observer<ArticleEntity>() {
            @Override
            public void onChanged(ArticleEntity articleEntity) {
                wvArticle.loadUrl(articleEntity.getUrl());
                binding.executePendingBindings();
            }
        });

        if (getArguments() != null) {
            Timber.d("ajunge Article fragmentRequest = %s", String.valueOf(getArguments().getString("fragmentRequest")));
            if (getArguments().getString("fragmentRequest") != null) {
                //lansez requestul cu textul de cautare predefined category
                articleViewModel.setFragmentRequest(getArguments().getString("fragmentRequest"));
            }
            if (getArguments().getString("urlArticleEntity") != null){
                articleViewModel.setUrlArticleEntity(getArguments().getString("urlArticleEntity"));
            }
            if (getArguments().getString("fragment_requesting_list") != null){
                fragment_requesting_list = getArguments().getString("fragment_requesting_list");
                /*switch (getArguments().getString("fragment_requesting_list")) {
                    case "from_custom":
                        fragment_requesting_list = "from_custom";//"from_back_neistorice";
                        break;
                    case "from_predefined_categories":
                        fragment_requesting_list = "from_predefined_categories";//"from_back_neistorice";
                        break;
                    case "from_my_history":
                        fragment_requesting_list = "from_my_history";//"from_back_istorice";
                        //break;
                }*/
            }
        }

    }

    public static class CustomBrowser extends WebViewClientCompat {

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){//pare ca blocarea vulnerabilitatii se face doar de la LOLLIPOP in sus ???
                if (!request.hasGesture()){
                    return false;
                }
                view.loadUrl(request.getUrl().toString());
            } else {
                view.loadUrl(request.toString());
            }
            return true;
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onBackPressed() {

        Timber.d("ajunge Article Fragment = %s", String.valueOf("click pe back"));
        boolean in_webview = false;
        if (in_webview) {
            return false;
        } else {
            Timber.d("ajunge Article Fragment = %s", String.valueOf(fragment_requesting_list));
            if (fragment_requesting_list != null){
            //if (fragment_requesting_list.equals("from_custom") || fragment_requesting_list.equals("from_predefined_categories")) {//"from_back_neistorice"
                //trimit argumentele spre fragmentul lista
                final Bundle bundle = new Bundle();
                bundle.putString("from_back_article", fragment_requesting_list);
                bundle.putString("fragmentRequest", articleViewModel.getFragmentRequest());
                Navigation.findNavController(getView()).navigate(R.id.action_articleFragment_to_articlesListFragment,bundle);

                navController.getBackStack().removeLast();//am sters list
                navController.getBackStack().removeLast();//am sters art
                return true;
            } else {
                return false;
            }
        }
    }
}