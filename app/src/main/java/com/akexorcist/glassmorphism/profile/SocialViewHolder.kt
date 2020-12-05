package com.akexorcist.glassmorphism.profile

import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.glassmorphism.data.Profile
import com.akexorcist.glassmorphism.databinding.ViewSocialInfoBinding

class SocialViewHolder(
    private val binding: ViewSocialInfoBinding,
    private val onClick: (Profile.Social) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Profile.Social) {
        binding.textViewName.text = data.name
        binding.imageViewIcon.setImageResource(data.icon)
        binding.root.setOnClickListener {
            onClick(data)
        }
    }
}
