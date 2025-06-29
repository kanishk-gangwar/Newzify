package com.kanishk.newsapp.ui

sealed class Screen(val route: String) {
    object News : Screen("news")
    object Article : Screen("article/{title}/{imageUrl}/{description}") {
        fun createRoute(title: String, imageUrl: String, description: String): String {
            return "article/${encode(title)}/${encode(imageUrl)}/${encode(description)}"
        }

        private fun encode(value: String): String =
            java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8.toString())
    }
}
