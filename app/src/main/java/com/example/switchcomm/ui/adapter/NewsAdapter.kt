package com.example.switchcomm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.switchcomm.R
import com.example.switchcomm.data.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun setArticles(list: List<Article>) {
        articles.clear()
        articles.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]

        holder.titleTextView.text = article.title ?: "No Title"

        holder.descTextView.text = article.description ?: "No Description"

        Glide.with(holder.itemView)
            .load(article.urlToImage)
           // .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        val descTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    }
}
