package com.example.newsme.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.newsme.ArticlesRequestFragmentDirections;
import com.example.newsme.R;

import timber.log.Timber;

public class ArticleRequestViewModel extends AndroidViewModel {

    private MutableLiveData<String> etCustomSearch;
    private MutableLiveData<String> mesajNevalidare;
    private String categoria_predefinita;
    private String my_history;
    private String fragmentRequest;

    public ArticleRequestViewModel(@NonNull Application application) {
        super(application);
        this.etCustomSearch = new MutableLiveData<>();
        this.mesajNevalidare = new MutableLiveData<>();
    }

    public void onClickMy(View view){
        switch (view.getId()){
            case R.id.btCategory1:
                my_history = "my_to_be_read";
                break;
            case R.id.btCategory2:
                my_history = "my_golden";
                break;
            case R.id.btCategory3:
                my_history = "my_last_opened";
                break;
        }

        final Bundle bundle = new Bundle();
        bundle.putString("from_my_history", my_history);
        fragmentRequest = "0";//history
        bundle.putString("fragmentRequest", fragmentRequest);
        Navigation.findNavController(view).navigate(R.id.action_requestHistoryFragment_to_articlesListFragment,bundle);
    }

    public void onClickCategory(View view){
        switch (view.getId()){
            case R.id.btCategory1:
                categoria_predefinita = "general";
                break;
            case R.id.btCategory2:
                categoria_predefinita = "business";
                break;
            case R.id.btCategory3:
                categoria_predefinita = "entertainment";
                break;
            case R.id.btCategory4:
                categoria_predefinita = "health";
                break;
            case R.id.btCategory5:
                categoria_predefinita = "sports";
                break;
            case R.id.btCategory6:
                categoria_predefinita = "science";
                break;
            case R.id.btCategory7:
                categoria_predefinita = "technology";
                break;
        }

        final Bundle bundle = new Bundle();
        bundle.putString("from_predefined_categories", categoria_predefinita);
        fragmentRequest = "1";//predefined
        bundle.putString("fragmentRequest", fragmentRequest);
        Navigation.findNavController(view).navigate(R.id.action_requestPredefinedFragment_to_articlesListFragment,bundle);
    }

    public void onClickInCustom(View view){
        String etCustomSearchValidat = getEtCustomSearchValidat(getEtCustomSearch().getValue());
        //daca intoarce un string gol, inseamna ca textul de cautare nu a fost validat
        if (!etCustomSearchValidat.equals("")){//daca a fost validat, da drumul la cautare

            //daca tastatura este inca afisata, o dezafiseaza
            hideSoftKeyboard(view.getRootView());

            final Bundle bundle = new Bundle();
            bundle.putString("from_custom", etCustomSearchValidat);
            fragmentRequest = "2";//custom
            bundle.putString("fragmentRequest", fragmentRequest);
            Timber.d("ajunge Request trimite fragmentRequest = %s", String.valueOf(fragmentRequest));
            Navigation.findNavController(view).navigate(R.id.action_requestCustomFragment_to_articlesListFragment,bundle);

        }
    }

    private String getEtCustomSearchValidat(String etCustomSearchNevalidat){
        //Aici aplic o validare pe rezultatul precedenteia.

        //Deocamdata vor fi cateva validari de bun simt.
        //Elimin spatiul sau spatiile (pot fi mai multe consecutive, dar trim() face si asta) de la inceput si de la sfarsitul expresiei.
        //Transform mai multe spatii consecutive in unul singur.
        etCustomSearchNevalidat = etCustomSearchNevalidat.trim().replaceAll("\\s+", " ");

        //Sa ramana cel putin 2 caractere
        if (etCustomSearchNevalidat.length() < 2){
            //In orice situatie de nevalidare, daca intre ce a tastat userul si expresia rezultata dupa validare exista diferente, atunci ultima va fi pusa
            //  in EditText.
            if (!etCustomSearchNevalidat.equals(etCustomSearch.getValue())){
                etCustomSearch.setValue(etCustomSearchNevalidat);
            }
            setMesajNevalidare(getApplication().getResources().getString(R.string.at_least_2_char));
            etCustomSearchNevalidat = "";
        }
        return etCustomSearchNevalidat;
    }

    public MutableLiveData<String> getEtCustomSearch() {
        return etCustomSearch;
    }

    // !!!! deocamdata nu am o sursa de date ca sa setez edit textul in sensul one-way, dar va fi in viitor
    public void setEtCustomSearch(String etCustomSearchNonLiveData) {
        this.etCustomSearch.setValue(etCustomSearchNonLiveData);
    }

    public MutableLiveData<String> getMesajNevalidare() {
        return mesajNevalidare;
    }

    public void setMesajNevalidare(String mesajNevalidare) {
        this.mesajNevalidare.setValue(mesajNevalidare);
    }

    public void hideSoftKeyboard(View view){
        //Pot fi mai multe situatii cand e necesara dezafisarea:
        //  - cand treci in ecranul cu lista de articole
        //  - cand atingi alta zona a ecranului care nu e de tip EditText

        //nu e complet - de facut sa functioneze pentru orice activitate sau fragment din aplicatie, adica metoda trebuie pusa in alta parte
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setFragmentRequest(String fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }

    public String getFragmentRequest() {
        return fragmentRequest;
    }
}
