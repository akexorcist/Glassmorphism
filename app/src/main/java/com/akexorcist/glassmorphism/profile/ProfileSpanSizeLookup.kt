package com.akexorcist.glassmorphism.profile

import androidx.recyclerview.widget.GridLayoutManager

class ProfileSpanSizeLookup(val adapter: ProfileAdapter) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return when (adapter.getItemViewType(position)) {
            ProfileAdapter.TYPE_HEADER -> 3
            ProfileAdapter.TYPE_SOCIAL -> 1
            ProfileAdapter.TYPE_POST -> 3
            else -> 0
        }
    }
}