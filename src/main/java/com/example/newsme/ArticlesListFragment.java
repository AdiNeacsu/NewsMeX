package com.example.newsme;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsme.adapter.ArticlesListRecyclerViewAdapter;
import com.example.newsme.data.local.ArticleEntity;
import com.example.newsme.databinding.FragmentArticlesListBinding;
import com.example.newsme.viewmodel.ArticlesListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ArticlesListFragment extends BaseFragment implements ArticlesListRecyclerViewAdapter.OnItemClickListener {

    NavController navController;
    FragmentArticlesListBinding binding;
    ArticlesListViewModel articlesListViewModel;

    RecyclerView lsArticles;
    List<ArticleEntity> articleEntityList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticlesListFragment() {
    }

    public static ArticlesListFragment newInstance() {
        return new ArticlesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        articlesListViewModel = new ViewModelProvider(this).get(ArticlesListViewModel.class);
        articleEntityList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.setVariable(BR.viewModel,articlesListViewModel);//bind the viewmodel to the view
        binding.setLifecycleOwner(getViewLifecycleOwner());//asta e necesara doar daca folosesti LiveData

        lsArticles = binding.lsArticles;
        lsArticles.setLayoutManager(new LinearLayoutManager(view.getContext()));//am pus getActivity(), dar de aflat daca e mai bine cu view.getContext()
        ArticlesListRecyclerViewAdapter articlesListRecyclerViewAdapter = new ArticlesListRecyclerViewAdapter();
        lsArticles.setAdapter(articlesListRecyclerViewAdapter);

        articlesListViewModel.getArticleEntityList().observe(getViewLifecycleOwner(), new Observer<List<ArticleEntity>>() {
            @Override
            public void onChanged(List<ArticleEntity> articleEntityList) {
                articlesListRecyclerViewAdapter.setArticleEntityList(articleEntityList, ArticlesListFragment.this);
                //articlesListRecyclerViewAdapter.notifyDataSetChanged();//Nu e necesar aici. L-am pus in adapter.
            }
        });

        if (getArguments() != null) {
            if (getArguments().getString("fragmentRequest") != null) {
                //lansez requestul cu textul de cautare predefined category
                articlesListViewModel.setFragmentRequest(getArguments().getString("fragmentRequest"));
            }
        }

        if (getArguments() != null) {
            if (getArguments().getString("from_my_history") != null){
                //lansez requestul cu textul de cautare predefined category
                articlesListViewModel.requestMyHistory(getArguments().getString("from_my_history"));
            } else if (getArguments().getString("from_predefined_categories") != null){
                Timber.d("ajunge List Fragment = %s", String.valueOf("predefined"));
                //lansez requestul cu textul de cautare predefined category
                articlesListViewModel.requestPredefinedCategories(getArguments().getString("from_predefined_categories"));
            } else if (getArguments().getString("from_custom") != null){
                Timber.d("ajunge List Fragment = %s", String.valueOf("custom"));
                //lansez requestul cu textul de cautare custom
                articlesListViewModel.askResponseRemote(getArguments().getString("from_custom"));
            } else if (getArguments().getString("from_back_article") != null){
                Timber.d("ajunge List Fragment = %s", String.valueOf("back"));
                //cand se intoarce din articolul vizualizat, reincarca lista de articole doar pentru listele neistorice: custom si predefined
                //fragment_requesting_list.equals("from_custom") || fragment_requesting_list.equals("from_predefined_categories")
                switch (getArguments().getString("from_back_article")) {
                    case "from_custom"://"from_back_neistorice"
                    case "from_predefined_categories":
                        Timber.d("ajunge List Fragment = %s", String.valueOf("neistorice"));
                        articlesListViewModel.backFromNonHistoricArticle(getArguments().getString("from_back_article"));
                        break;
                    case "my_to_be_read":
                    case "my_golden":
                    case "my_last_opened":
                        Timber.d("ajunge List Fragment = %s", String.valueOf("istorice"));
                        articlesListViewModel.backFromHistoricArticle(getArguments().getString("from_back_article"));
                        //break;
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClicked(View view, ArticleEntity articleEntity) {
        articlesListViewModel.onItemClick(view,articleEntity);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onBackPressed() {
        //return super.onBackPressed();
        final Bundle bundle = new Bundle();
        bundle.putString("fragmentRequest", articlesListViewModel.getFragmentRequest());
        Navigation.findNavController(getView()).navigate(R.id.action_articlesListFragment_to_articlesRequestFragment, bundle);
        navController.getBackStack().removeLast();
        navController.getBackStack().removeLast();
        return true;
    }
}