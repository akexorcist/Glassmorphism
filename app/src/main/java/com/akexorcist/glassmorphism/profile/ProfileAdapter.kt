package com.akexorcist.glassmorphism.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.glassmorphism.data.Profile
import com.akexorcist.glassmorphism.databinding.ViewEmptyBinding
import com.akexorcist.glassmorphism.databinding.ViewHeaderBinding
import com.akexorcist.glassmorphism.databinding.ViewPostInfoBinding
import com.akexorcist.glassmorphism.databinding.ViewSocialInfoBinding

class ProfileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var profiles: List<Profile> = listOf()
    private var onSocialClick: ((social: Profile.Social) -> Unit)? = null

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_SOCIAL = 1
        const val TYPE_POST = 2
        const val TYPE_EMPTY = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER ->
                HeaderViewHolder(ViewHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_SOCIAL ->
                SocialViewHolder(ViewSocialInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)) {
                    onSocialClick?.invoke(it)
                }
            TYPE_POST ->
                PostViewHolder(ViewPostInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else ->
                EmptyViewHolder(ViewEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (profiles.getOrNull(position)) {
            is Profile.Header -> TYPE_HEADER
            is Profile.Social -> TYPE_SOCIAL
            is Profile.Post -> TYPE_POST
            else -> TYPE_EMPTY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val profile = profiles.getOrNull(position)
        when (holder) {
            is HeaderViewHolder -> (profile as? Profile.Header)?.let { data ->
                holder.bind(data)
            }
            is SocialViewHolder -> (profile as? Profile.Social)?.let { data ->
                holder.bind(data)
            }
            is PostViewHolder -> (profile as? Profile.Post)?.let { data ->
                holder.bind(data)
            }
        }

    }

    fun setOnSocialClickListener(onClick: (social: Profile.Social) -> Unit) {
        onSocialClick = onClick
    }

    fun update(profiles: List<Profile>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }
}
