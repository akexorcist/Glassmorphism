package com.akexorcist.glassmorphism.profile

import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.glassmorphism.data.Profile
import com.akexorcist.glassmorphism.databinding.ViewHeaderBinding

class HeaderViewHolder(private val binding: ViewHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Profile.Header) {
        binding.textViewName.text = data.name
        binding.textViewDescription.text = data.description
    }
}
