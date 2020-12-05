package com.akexorcist.glassmorphism.profile

import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.glassmorphism.data.Profile
import com.akexorcist.glassmorphism.databinding.ViewPostInfoBinding

class PostViewHolder(private val binding: ViewPostInfoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Profile.Post) {
        binding.textViewTitle.text = data.title
        binding.textViewDescription.text = data.description
    }
}
