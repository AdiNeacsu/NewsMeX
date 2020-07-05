package com.example.newsme.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.newsme.ArticleFragmentDirections;
import com.example.newsme.InitApp;
import com.example.newsme.R;
import com.example.newsme.data.NewsMeRepository;
import com.example.newsme.data.local.ArticleEntity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ArticleViewModel extends ViewModel {

    //de mutat aici unele metode din fragment

    private NewsMeRepository newsMeRepository;
    Disposable articleByIdUrlDisposable;
    private MutableLiveData<ArticleEntity> articleEntity;
    private String fragmentRequest;

    public ArticleViewModel() {
        newsMeRepository = InitApp.getInitApp().getNewsMeRepository();
        this.articleEntity = new MutableLiveData<>();
    }

    public void setUrlArticleEntity(String urlArticleEntity) {
        articleByIdUrlDisposable = newsMeRepository.getArticleByIdUrl(urlArticleEntity)
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(
                                                                this::setArticleEntity,
                                                                throwable -> Timber.e(throwable, "Received error while fetching articles: %s", throwable.getMessage())
                                                        );
    }

    public LiveData<ArticleEntity> getArticleEntity() {
        return articleEntity;
    }

    public void setArticleEntity(ArticleEntity articleEntity) {
        this.articleEntity.setValue(articleEntity);
    }

    public void setFragmentRequest(String fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }

    public String getFragmentRequest() {
        return fragmentRequest;
    }

    // listeneri

    @SuppressLint("RestrictedApi")
    public void onClickSpreRequest(View view){
        NavController navController = Navigation.findNavController(view);
        final Bundle bundle = new Bundle();
        bundle.putString("fragmentRequest", getFragmentRequest());
        navController.navigate(R.id.action_articleFragment_to_articlesRequestFragment, bundle);
        navController.getBackStack().removeLast();
        navController.getBackStack().removeLast();
        navController.getBackStack().removeLast();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (articleByIdUrlDisposable != null){articleByIdUrlDisposable.dispose();}
    }

}
