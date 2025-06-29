package com.kanishk.newsapp.ui

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object KtorClient {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
    }
}
