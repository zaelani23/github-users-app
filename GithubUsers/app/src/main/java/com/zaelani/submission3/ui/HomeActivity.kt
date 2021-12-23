package com.zaelani.submission3.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zaelani.submission3.R
import com.zaelani.submission3.adapter.ListUsersAdapter
import com.zaelani.submission3.databinding.ActivityHomeBinding
import com.zaelani.submission3.model.User
import com.zaelani.submission3.model.UserSearchResponse
import com.zaelani.submission3.network.ApiConfig
import com.zaelani.submission3.viewmodel.UsersViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: UsersViewModel
    private lateinit var adapter: ListUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val actionbar = supportActionBar
        actionbar!!.title= "Home"

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUsersAdapter()
        adapter.notifyDataSetChanged()

        Snackbar.make(binding.root, "Klik tombol search diatas!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        adapter.setOnItemClickCallback(object : ListUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@HomeActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(UsersViewModel::class.java)

        binding.apply {
            rvSearchResults.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvSearchResults.setHasFixedSize(true)
            rvSearchResults.adapter = adapter
        }

        viewModel.getUser().observe(this, {
            if (it != null) {
                adapter.setList(it)
                isLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                Intent(this@HomeActivity, SettingActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }

            R.id.favorite -> {
                Intent(this@HomeActivity, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }
            else -> return true
        }
    }

    private fun searchUsers(query: String) {
        if (query.isEmpty()) return
        isLoading(true)
        ApiConfig.apiInstance
                .getUser(query)
                .enqueue(object : Callback<UserSearchResponse> {
                    override fun onResponse(
                            call: Call<UserSearchResponse>,
                            response: Response<UserSearchResponse>
                    ) {
                        isLoading(false)
                        if (response.isSuccessful) {
                            response.body()?.items?.let {
                                if (it.size == 0) {
                                    Toast.makeText(this@HomeActivity, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.setUser(it)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                        isLoading(false)
                        Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}