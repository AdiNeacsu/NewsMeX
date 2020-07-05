package com.example.newsme.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsme.data.local.ArticleEntity;
import com.example.newsme.databinding.FragmentArticleItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ArticlesListRecyclerViewAdapter extends RecyclerView.Adapter<ArticlesListRecyclerViewAdapter.ArticlesListViewHolder> {

    private List<ArticleEntity> articleEntityList;
    private OnItemClickListener itemClickListener;

    public ArticlesListRecyclerViewAdapter(){
        this.articleEntityList = new ArrayList<>();//instantiez lista dar nu pun nimic in ea
    }

    public void setArticleEntityList(List<ArticleEntity> articleEntityList, OnItemClickListener itemClickListener) {
        this.articleEntityList = articleEntityList;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticlesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentArticleItemBinding itemBinding = FragmentArticleItemBinding.inflate(layoutInflater,parent,false);
        return new ArticlesListViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesListViewHolder holder, int position) {
        ArticleEntity articleEntity = articleEntityList.get(position);
        holder.bind(articleEntity, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return articleEntityList != null ? articleEntityList.size() : 0;
    }

    static class ArticlesListViewHolder extends RecyclerView.ViewHolder{

        private FragmentArticleItemBinding binding;

        public ArticlesListViewHolder(FragmentArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ArticleEntity articleEntity, OnItemClickListener clickListener){
            binding.setArticleEntity(articleEntity);
            binding.executePendingBindings();
            itemView.setOnClickListener(view -> {
                if (clickListener != null){
                    clickListener.onItemClicked(view, articleEntity);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(View view, ArticleEntity articleEntity);
    }

}