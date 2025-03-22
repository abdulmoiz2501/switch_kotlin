package com.example.switchcomm

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.switchcomm.repository.NewsRepository
import com.example.switchcomm.ui.adapter.NewsAdapter
import com.example.switchcomm.ui.viewmodel.NewsViewModel
import com.example.switchcomm.ui.viewmodel.NewsViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

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
        tvEmpty = findViewById(R.id.tvEmpty)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "NewsApp"

        val adapter = NewsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.articles.observe(this) { articles ->
            adapter.setArticles(articles)
            tvEmpty.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(this) { isLoading ->
            progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(it)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.fetchNews()
                    }
                    .show()
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

        val searchEditText = searchView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        searchEditText?.setTextColor(ContextCompat.getColor(this, android.R.color.black))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchNews(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.fetchNews()
                }
                return false
            }
        })
        return true
    }
}
