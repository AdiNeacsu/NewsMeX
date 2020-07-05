package com.example.newsme.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface ArticleDao {

    @Query("SELECT source, author, title, description, url, urlToImage, publishedAt, content FROM articles LEFT JOIN last_opened ON url=urlLastOpened WHERE urlLastOpened IS NULL")
    Observable<List<ArticleEntity>> getAllNonCollectionsArticles();

    //obtin un singur articol dupa id, care este url
    @Query("SELECT * FROM articles WHERE url = :url")
    Observable<ArticleEntity> getArticleByIdUrl(String url);

    @Query("DELETE FROM articles WHERE url IN (SELECT url FROM articles LEFT JOIN last_opened ON url=urlLastOpened WHERE urlLastOpened IS NULL)")
    void deleteAllArticlesNotInCollections();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllArticles(List<ArticleEntity> articleEntityList);

    //operatii pe "last_opened"

    //sterg o inregistrare din "last_opened" identificata dupa id
    @Query("DELETE FROM last_opened WHERE urlLastOpened = :url")
    void deleteOneByUrlFromLastOpened(String url);

    //cate inregistrari sunt in "last_opened"
    @Query("SELECT COUNT(urlLastOpened) FROM last_opened")
    Observable<Integer> getRowCountInLastOpened();

    //sterg prima inregistrare din "last_opened"
    @Query("DELETE FROM last_opened WHERE urlLastOpened IN (SELECT urlLastOpened FROM last_opened LIMIT 1 OFFSET 0)")
    void deleteFirstFromLastOpened();

    //adaug o inregistrare in "last_opened"
    @Insert
    void insertOneInLastOpened(LastOpenedEntity lastOpenedEntity);

    //obtin toate articolele "ultime"
    @Query("SELECT source, author, title, description, url, urlToImage, publishedAt, content FROM articles LEFT JOIN last_opened ON url=urlLastOpened WHERE urlLastOpened NOT NULL")
    Observable<List<ArticleEntity>> getAllLastOpenedArticles();
}
