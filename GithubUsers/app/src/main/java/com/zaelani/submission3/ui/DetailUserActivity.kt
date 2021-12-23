package com.zaelani.submission3.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.zaelani.submission3.R
import com.zaelani.submission3.SectionsPagerAdapter
import com.zaelani.submission3.database.Favorite
import com.zaelani.submission3.databinding.ActivityDetailUserBinding
import com.zaelani.submission3.model.UserDetailResponse
import com.zaelani.submission3.network.ApiConfig
import com.zaelani.submission3.viewmodel.AddDeleteFavoriteViewModel
import com.zaelani.submission3.viewmodel.FavoriteViewModelFactory
import com.zaelani.submission3.viewmodel.UserDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var addDeleteFavoriteViewModel: AddDeleteFavoriteViewModel
    private var favorite: Favorite? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        val actionbar = supportActionBar
        actionbar!!.title= "Detail User"
        actionbar.setDisplayHomeAsUpEnabled(true)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = Bundle()
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val isFav = intent.getBooleanExtra(EXTRA_ISFAV, false)
        bundle.putString(EXTRA_USERNAME, username)

        addDeleteFavoriteViewModel = obtainViewModel(this)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(UserDetailViewModel::class.java)

        isLoading(true)
        ApiConfig.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                        call: Call<UserDetailResponse>,
                        response: Response<UserDetailResponse>
                ) {
                    isLoading(false)
                    if (response.isSuccessful) {
                        response.body()?.let { viewModel.setUserDetail(it) }
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    isLoading(false)
                    Toast.makeText(this@DetailUserActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                            .load(it.avatar_url)
                            .apply(RequestOptions())
                            .into(imgItemPhoto)
                    tvRepo.text = it.public_repos
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvCompany.text = it.company
                    tvLocation.text = it.location
                    val login = it.login
                    val avatar = it.avatar_url
                    favorite = Favorite()
                    favorite.let {
                        favorite!!.username = login
                        favorite!!.avatar = avatar
                    }
                    if (isFav) {
                        btnFav.setImageResource(R.drawable.ic_delete_24)
                        btnFav.setOnClickListener {
                            try {
                                addDeleteFavoriteViewModel.delete(favorite as Favorite)
                                Snackbar.make(binding.root, "Dihapus dari favorite", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    val intent = intent
                                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
                                    intent.putExtra(DetailUserActivity.EXTRA_ISFAV, false)
                                    finish()
                                    startActivity(intent);
                                }, 2000)
                            } catch (e: Exception) {
                                Snackbar.make(binding.root, "Error: ${e.message}", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show()
                            }
                        }
                    } else {
                        btnFav.setOnClickListener {
                            try {
                                addDeleteFavoriteViewModel.insert(favorite as Favorite)
                                Snackbar.make(binding.root, "Ditambahkan ke favorite", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show()
                                Handler(Looper.getMainLooper()).postDelayed({
                                    val intent = intent
                                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
                                    intent.putExtra(DetailUserActivity.EXTRA_ISFAV, true)
                                    finish()
                                    startActivity(intent);
                                }, 2000)
                            } catch (e: Exception) {
                                Snackbar.make(binding.root, "Error: ${e.message}", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddDeleteFavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AddDeleteFavoriteViewModel::class.java)
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ISFAV = "extra_isfav"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }
}