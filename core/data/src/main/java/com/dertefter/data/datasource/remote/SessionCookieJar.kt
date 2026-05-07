package com.dertefter.data.datasource.remote

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class SessionCookieJar () : CookieJar {

    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    private var NSTUPHPSESSID: String = ""

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        synchronized(this) {
            val hostCookies = cookieStore.getOrPut(url.host) { mutableListOf() }

            cookies.forEach { newCookie ->
                hostCookies.removeAll { it.name == newCookie.name }
                hostCookies.add(newCookie)

                if (newCookie.name == "NSTUPHPSESSID") {
                    NSTUPHPSESSID = newCookie.value
                }
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        synchronized(this) {
            val hostCookies = cookieStore.getOrPut(url.host) { mutableListOf() }
            hostCookies.removeAll { it.name == "NSTUPHPSESSID" }
            if (NSTUPHPSESSID.isNotEmpty()) {
                val newCookie = Cookie.Builder()
                    .name("NSTUPHPSESSID")
                    .value(NSTUPHPSESSID)
                    .domain(url.host)
                    .path("/")
                    .build()
                hostCookies.add(newCookie)
            }
            return ArrayList(hostCookies)
        }
    }

    fun cleanUp() {
        synchronized(this) {
            NSTUPHPSESSID = ""
            cookieStore.clear()
        }
    }

}