package com.example.newsme.request;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newsme.BaseFragment;
import com.example.newsme.InitApp;
import com.example.newsme.R;
import com.example.newsme.databinding.FragmentRequestCustomBinding;
import com.example.newsme.viewmodel.ArticleRequestViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestCustomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestCustomFragment extends BaseFragment {

    FragmentRequestCustomBinding binding;
    ArticleRequestViewModel articleRequestViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
/*    private String mParam1;
    private String mParam2;*/

    public RequestCustomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * return A new instance of fragment RequestCustomFragment.
     */
    // TODO: Rename and change types and number of parameters
    //deocamdata nu folosesc niciun parametru
    //public static RequestCustomFragment newInstance(String param1, String param2) {
    public static RequestCustomFragment newInstance() {
        RequestCustomFragment fragment = new RequestCustomFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        articleRequestViewModel = new ViewModelProvider(this).get(ArticleRequestViewModel.class);//pentru ViewModel
        //articleRequestViewModel = new ArticleRequestViewModel(InitApp.getInitApp());//pentru AndroidViewModel
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_request_custom, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_custom, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(BR.requestViewModel, articleRequestViewModel);

        binding.setLifecycleOwner(getViewLifecycleOwner());

        articleRequestViewModel.getEtCustomSearch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.executePendingBindings();//cand se modifica fieldul, se updateaza atributul text
            }
        });

        //acest LiveData e pentru Toast
        articleRequestViewModel.getMesajNevalidare().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}