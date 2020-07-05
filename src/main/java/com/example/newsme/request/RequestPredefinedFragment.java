package com.example.newsme.request;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsme.BR;
import com.example.newsme.R;
import com.example.newsme.databinding.FragmentRequestPredefinedBinding;
import com.example.newsme.viewmodel.ArticleRequestViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestPredefinedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestPredefinedFragment extends Fragment {

    FragmentRequestPredefinedBinding binding;
    ArticleRequestViewModel articleRequestViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // TODO: Rename and change types of parameters
/*    private String mParam1;
    private String mParam2;*/

    public RequestPredefinedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestPredefinedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestPredefinedFragment newInstance(String param1, String param2) {
        RequestPredefinedFragment fragment = new RequestPredefinedFragment();
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

        articleRequestViewModel = new ViewModelProvider(this).get(ArticleRequestViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_predefined, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(BR.requestViewModel, articleRequestViewModel);
    }
}