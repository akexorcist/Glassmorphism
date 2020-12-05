package com.akexorcist.glassmorphism

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.glassmorphism.data.DataProvider
import com.akexorcist.glassmorphism.data.Profile
import com.akexorcist.glassmorphism.databinding.ActivityProfileBinding
import com.akexorcist.glassmorphism.profile.ProfileAdapter
import com.akexorcist.glassmorphism.profile.ProfileSpanSizeLookup


class ProfileActivity : AppCompatActivity() {
    private val dataProvider: DataProvider by lazy { DataProvider() }
    private val binding: ActivityProfileBinding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    private lateinit var adapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initView() {
        adapter = ProfileAdapter()
        adapter.setOnSocialClickListener { social -> openUrl(social.url) }
        binding.recyclerViewProfile.adapter = adapter
        binding.recyclerViewProfile.layoutManager = GridLayoutManager(this, 3).apply {
            spanSizeLookup = ProfileSpanSizeLookup(adapter)
        }
        binding.recyclerViewProfile.addOnScrollListener(onPostScrollListener)
        binding.root.doOnGlobalLayout { binding.glassContainerLayout.glassify() }
    }

    private fun initData() {
        val header = dataProvider.getHeader()
        val socials = dataProvider.getSocials()
        val posts = dataProvider.getPosts()
        val profiles = arrayListOf<Profile>().apply {
            add(header)
            addAll(socials)
            addAll(posts)
        }
        adapter.update(profiles)
    }

    private fun openUrl(url: String?) {
        if (url.isNullOrEmpty()) return
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerViewProfile.removeOnScrollListener(onPostScrollListener)
    }

    private val onPostScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.post {
                binding.glassContainerLayout.glassify()
            }
        }
    }
}







