package com.zaelani.submission3.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaelani.submission3.R
import com.zaelani.submission3.adapter.ListUsersAdapter
import com.zaelani.submission3.databinding.FragmentFollowBinding
import com.zaelani.submission3.model.User
import com.zaelani.submission3.network.ApiConfig
import com.zaelani.submission3.viewmodel.FollowingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingFragment : Fragment(R.layout.fragment_follow) {
    private var _binding: FragmentFollowBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: ListUsersAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = ListUsersAdapter()

        binding.apply {
            rvListFollowers.setHasFixedSize(true)
            rvListFollowers.layoutManager = LinearLayoutManager(activity)
            rvListFollowers.adapter = adapter
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        isLoading(true)
        ApiConfig.apiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    isLoading(false)
                    if (response.isSuccessful) {
                        response.body()?.let { viewModel.setListFollowing(it) }
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    isLoading(false)
                    Toast.makeText(view.context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        viewModel.getListFollowing().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setList(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}