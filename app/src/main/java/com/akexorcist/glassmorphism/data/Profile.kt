package com.akexorcist.glassmorphism.data

sealed class Profile {
    data class Header(
        val name: String?,
        val description: String?
    ) : Profile()

    data class Social(
        val icon: Int,
        val name: String?,
        val url: String?
    ) : Profile()

    data class Post(
        val title: String?,
        val description: String?
    ) : Profile()
}
