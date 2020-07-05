package com.example.newsme.data;

import com.example.newsme.InitApp;
import com.example.newsme.data.local.ArticleEntity;
import com.example.newsme.data.local.LastOpenedEntity;
import com.example.newsme.data.local.NewsMeDatabase;
import com.example.newsme.data.remote.NewsMeWebService;
import com.example.newsme.data.remote.models.ArticleRemote;
import com.example.newsme.data.remote.models.ResponseRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class NewsMeRepository {
    //deocamdata nu are nimic; pun ceva aici cand am prima metoda care comunica date intre UI si local si remote, inclusiv intre local si remote

    private NewsMeWebService newsMeWebService;
    private NewsMeDatabase newsMeDatabase;

    public NewsMeRepository() {
        this.newsMeDatabase = InitApp.getInitApp().getNewsMeDatabase();
        this.newsMeWebService = new NewsMeWebService();
    }

    //Urmeaza diverse cereri adresate bazei de date

    public Observable<List<ArticleEntity>> listenToGetAllArticles() {
        return this.newsMeDatabase.articleDao().getAllNonCollectionsArticles()
                .subscribeOn(Schedulers.io());
    }

    public Observable<ArticleEntity> getArticleByIdUrl(String url) {
        return this.newsMeDatabase.articleDao().getArticleByIdUrl(url)
                .subscribeOn(Schedulers.io());
    }


    //Urmeaza diverse cereri adresate sursei remote

    //sterg din tabelul articles toate articolele care nu sunt in vreo colectie
    private Completable deleteAllArticlesNotInCollections() {
        Timber.d("ajunge = %s", String.valueOf("in delete"));
        return Completable.fromAction(() -> newsMeDatabase.articleDao().deleteAllArticlesNotInCollections());
    }

    private Completable insertAllArticles(Single<ResponseRemote> responseRemoteSingle) {
        List<ArticleEntity> articleEntityList = new ArrayList<>();
        return responseRemoteSingle
                .flatMapCompletable((ResponseRemote responseRemote) -> {
                    List<ArticleRemote> articleRemoteList = new ArrayList<>(responseRemote.getArticles());
                    for (ArticleRemote articleRemote : articleRemoteList) {
                        articleEntityList.add(articleRemote.toEntity());
                    }
                    //return insertAllArticlesCompletable;
                    return newsMeDatabase.articleDao().insertAllArticles(articleEntityList);
                });
    }

    //request predefined categories
    public Single<ResponseRemote> requestPredefinedCategories(String category){
        return insertAllArticles(deleteAllArticlesNotInCollections().andThen(newsMeWebService.fetchTopheadlines(category,"gb")))
                .andThen(newsMeWebService.fetchTopheadlines(category, "gb"))
                .subscribeOn(Schedulers.io());
    }

    //request custom
    public Single<ResponseRemote> askResponseRemote(String query) { //am inlocuit tipul intors Single<ResponseRemote> cu Completable

        return deleteAllArticlesNotInCollections()
                .andThen(insertAllArticles(newsMeWebService.fetchEverything(query,"en")))
                .andThen(newsMeWebService.fetchEverything(query,"en"))       //Asta e doar ca sa trimit ResponseRemote mai departe spre UI
                .subscribeOn(Schedulers.io());
    }

    //retin ultimile articole; urmatoarele metode fac asta impreuna

    private Completable deleteOneByUrlFromLastOpened(String url){
        Timber.d("ajunge deleteOneByUrl = %s", String.valueOf(url));
        return Completable.fromAction(() -> newsMeDatabase.articleDao().deleteOneByUrlFromLastOpened(url));
    }

    private Observable<Integer> getRowCountInLastOpened() {
        Timber.d("ajunge getRowCount = %s", String.valueOf("1"));
        return this.newsMeDatabase.articleDao().getRowCountInLastOpened()
                .subscribeOn(Schedulers.io());
    }

    Disposable disposableCountInLastOpened;
    private Integer getRowCountInLastOpenedInteger(){
        final Integer[] count = new Integer[1];
        //count[0] = 0;
        disposableCountInLastOpened = getRowCountInLastOpened()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> count[0] = value);
        //evit sa intorc valoare null
        Timber.d("ajunge getRowCount Integer = %s", String.valueOf(count[0]));
        if (count[0] == null) return 0;
        return count[0];
    }

    private Completable deleteFirstFromLastOpened(Integer count){
        Timber.d("ajunge deleteFirst = %s", String.valueOf(count));
        //sterg doar daca am 500 de inregistrari
        if (count > 499){//499
            return Completable.fromAction(() -> newsMeDatabase.articleDao().deleteFirstFromLastOpened());
        }

        return Completable.complete();//      nu trebuie sa intoarca null
    }

    private Completable insertOneInLastOpened(LastOpenedEntity lastOpenedEntity){
        Timber.d("ajunge insertOne = %s", String.valueOf(lastOpenedEntity.getUrlLastOpened()));
        if (disposableCountInLastOpened != null){disposableCountInLastOpened.dispose();}
        return Completable.fromAction(() -> newsMeDatabase.articleDao().insertOneInLastOpened(lastOpenedEntity));
    }

    public Completable persistLastOpenedArticles(ArticleEntity articleEntity){
        Timber.d("ajunge persist = %s", String.valueOf(articleEntity.getUrl()));

        return deleteOneByUrlFromLastOpened(articleEntity.getUrl())
                .andThen(deleteFirstFromLastOpened(getRowCountInLastOpenedInteger()))
                .andThen(insertOneInLastOpened(new LastOpenedEntity(articleEntity.getUrl())))
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<ArticleEntity>> getAllLastOpenedArticles() {
        return this.newsMeDatabase.articleDao().getAllLastOpenedArticles()
                //rulam listeningul/interogarea intr-un background thread dedicat I/O
                .subscribeOn(Schedulers.io());
    }

}
