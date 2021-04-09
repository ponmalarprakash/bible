package com.bible.core
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

private const val HEADER_AUTH_KEY = "authkey"
private const val HEADER_TOKEN = "token"
private const val HEADER_USER_AUTH = "userAuth"
const val SESSION_AUTH_KEY ="auth_key"
const val SESSION_USER_KEY ="user_key"
const val SESSION_DEVICE_ID ="device_id"

class Base64EncodeRequestInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var builder: Request.Builder = originalRequest.newBuilder()
        if (originalRequest.method.equals("POST", ignoreCase = true)) {
            builder = originalRequest.newBuilder()
                .method(originalRequest.method, originalRequest.body)
        }

       /* builder.addHeader(
            HEADER_AUTH_KEY, SessionManager.getSession(mContext,
                SESSION_AUTH_KEY, ""
            )
        )
        builder.addHeader(
            HEADER_TOKEN, SessionManager.getSession(
                mContext,
                SESSION_DEVICE_ID, ""
            )
        )
        builder.addHeader(
            HEADER_USER_AUTH, SessionManager.getSession(
                mContext,
                SESSION_USER_KEY, ""
            )
        )*/

        val originalHttpUrl = originalRequest.url
        val url = originalHttpUrl.newBuilder()
          /*  .addQueryParameter("dt", "a")
            .addQueryParameter("i", "5")
            .addQueryParameter("pv", "" + BuildConfig.VERSION_CODE)
            .addQueryParameter("k", "")
            .addQueryParameter("s", "0")*/
            .build()

        builder.url(url)
        return chain.proceed(builder.build())
    }

}
