package com.example.newsme.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.newsme.ArticlesListFragmentDirections;
import com.example.newsme.InitApp;
import com.example.newsme.R;
import com.example.newsme.data.NewsMeRepository;
import com.example.newsme.data.local.ArticleEntity;
import com.example.newsme.data.local.SourceLocal;
import com.example.newsme.data.remote.models.ResponseRemote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ArticlesListViewModel extends ViewModel {

    private NewsMeRepository newsMeRepository;
    Disposable articlesLocalDisposable;
    Disposable responseRemoteDisposable;
    Disposable persistArticleDisposable;
    Disposable articlesLastOpenedDisposable;
    private MutableLiveData<List<ArticleEntity>> articleEntityList;
    private MutableLiveData<ResponseRemote> responseRemoteX;
    private String fragment_requesting_list;//din ce fragment request (ce lista s-a cerut) am ajuns in aceasta lista
    private String fragmentRequest;

    public ArticlesListViewModel() {
        newsMeRepository = InitApp.getInitApp().getNewsMeRepository();
        this.articleEntityList = new MutableLiveData<>();
        this.responseRemoteX = new MutableLiveData<>();
    }

    public LiveData<List<ArticleEntity>> getArticleEntityList() {
        return articleEntityList;
    }

    // interogari spre sursa de date remote

    public LiveData<ResponseRemote> getResponseRemote() {
        return responseRemoteX;
    }

    public void requestMyHistory(String from_my_history) {
        fragment_requesting_list = from_my_history;
        switch (from_my_history){
            case "my_to_be_read":
                // ------------------------------ de facut
                break;
            case "my_golden":
                // ------------------------------ de facut
                break;
            case "my_last_opened":
                articlesLastOpenedDisposable = newsMeRepository.getAllLastOpenedArticles()//este spre baza Room
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(articlesLocal -> {
                                        //Daca, cum e la inceputul folosirii aplicatiei, nu e niciun articol in lista, creez un obiect ArticleEntity doar pentru
                                        //  a afisa un mesaj.
                                    if (articlesLocal.size() == 0){
                                        articlesLocal.add(new ArticleEntity(new SourceLocal("",""), "", "No Articles Visited Yet", "The list would be populated starting with first opened to read article.", "", "", "", ""));
                                        articleEntityList.setValue(articlesLocal);
                                    } else {
                                        //inversez ordinea ca cel mai curand deschis articol sa apara primul
                                        List<ArticleEntity> reverseArticlesLocal = new ArrayList<>();
                                        for (int i=articlesLocal.size()-1;i>=0;i--){
                                            reverseArticlesLocal.add(articlesLocal.get(i));
                                        }
                                        articleEntityList.setValue(reverseArticlesLocal);
                                    }
                                },
                                throwable -> Timber.e(throwable, "4 Received error while fetching articles: %s", throwable.getMessage()));
                break;
        }
    }

    public void requestPredefinedCategories(String category){//este spre news api
        fragment_requesting_list = "from_predefined_categories";
        responseRemoteDisposable = newsMeRepository.requestPredefinedCategories(category)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseRemote -> responseRemoteX.setValue(responseRemote),
                        throwable -> Timber.e(throwable, "3 Received error while fetching articles: %s", throwable.getMessage())
                );
        //activez listeningul spre baza locala Room
        listenToGetAllArticles();
    }

    //este pentru custom request spre news api
    public void askResponseRemote(String stringSearch){
        fragment_requesting_list = "from_custom";
        responseRemoteDisposable = newsMeRepository.askResponseRemote(stringSearch)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseRemote -> responseRemoteX.setValue(responseRemote),
                        throwable -> Timber.e(throwable, "2 Received error while fetching articles: %s", throwable.getMessage())
                );
        //activez listeningul spre baza locala Room
        listenToGetAllArticles();
    }

    private void listenToGetAllArticles(){

        articlesLocalDisposable = newsMeRepository.listenToGetAllArticles()
                //cand vin rezultatele, prelucrarile de aici incolo le facem in main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        articlesLocal -> {
                            //setez lista de articole care ajunge in recyclerview si notific recyclerview
                            articleEntityList.setValue(articlesLocal);//Folosesc setValue penru ca folosesc LiveData
                            //articlesListRecyclerViewAdapter.notifyDataSetChanged();//Nu e necesar aici. L-am pus in adapter.
                        },
                        throwable -> Timber.e(throwable, "1 Received error while fetching articles: %s", throwable.getMessage())
                );
    }

    private void persistLastOpenedArticles(ArticleEntity articleEntity){
        persistArticleDisposable = newsMeRepository.persistLastOpenedArticles(articleEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (articlesLocalDisposable != null){articlesLocalDisposable.dispose();}
        if (responseRemoteDisposable != null){responseRemoteDisposable.dispose();}
        if (persistArticleDisposable != null){persistArticleDisposable.dispose();}
        if (articlesLastOpenedDisposable != null){articlesLastOpenedDisposable.dispose();}
    }

    public void setFragmentRequest(String fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }

    public String getFragmentRequest() {
        return fragmentRequest;
    }

    public void onItemClick(View view, ArticleEntity articleEntity) {
        //Inainte sa deschid articolul in fragment doar cu acel articol, il retin intre ultimile articole deschise.
        //Nu fac asta pentru listele istorice pentru ca articolele sunt deja persistate
        if (fragment_requesting_list.equals("from_predefined_categories") || fragment_requesting_list.equals("from_custom")){
            persistLastOpenedArticles(articleEntity);
        }

        final Bundle bundle = new Bundle();
        bundle.putString("fragment_requesting_list", fragment_requesting_list);
        bundle.putString("fragmentRequest", fragmentRequest);
        String urlArticleEntity = articleEntity.getUrl();
        bundle.putString("urlArticleEntity", urlArticleEntity);
        Navigation.findNavController(view).navigate(R.id.action_articlesListFragment_to_articleFragment,bundle);
    }

    public void backFromNonHistoricArticle(String from_neistorice) {
        fragment_requesting_list = from_neistorice;
        listenToGetAllArticles();
    }

    public void backFromHistoricArticle(String from_istorice) {
        fragment_requesting_list = from_istorice;
        requestMyHistory(from_istorice);
    }
}
