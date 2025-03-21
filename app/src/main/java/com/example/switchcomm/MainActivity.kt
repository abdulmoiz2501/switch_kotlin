package com.example.switchcomm

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.switchcomm.repository.NewsRepository
import com.example.switchcomm.ui.adapter.NewsAdapter
import com.example.switchcomm.ui.viewmodel.NewsViewModel
import com.example.switchcomm.ui.viewmodel.NewsViewModelFactory
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(NewsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBar = findViewById(R.id.progressBar)

        setSupportActionBar(toolbar)

        val adapter = NewsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.articles.observe(this) { articles ->
            adapter.setArticles(articles)
        }

        viewModel.loading.observe(this) { isLoading ->
            progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.fetchNews()
            swipeRefresh.isRefreshing = false
        }

        viewModel.fetchNews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    viewModel.searchNews(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: live filtering
                return false
            }
        })
        return true
    }
}
