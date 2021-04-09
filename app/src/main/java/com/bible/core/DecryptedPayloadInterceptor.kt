package com.bible.core

import android.content.Context
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class DecryptedPayloadInterceptor(private val mContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            val responseBuilder = response.newBuilder()
            var contentType = response.header("Content-Type")
            if (TextUtils.isEmpty(contentType))
                contentType = "application/json"
            val cryptedStream = response.body!!.byteStream()
            var decrypted: String? = null
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int = cryptedStream.read(buffer)
            while (length != -1) {
                result.write(buffer, 0, length)
                length = cryptedStream.read(buffer)
            }
            decrypted = result.toString("UTF-8")
            if (!decrypted.isNullOrEmpty()) {

                try {

                    responseBuilder.body(decrypted.toResponseBody(contentType?.toMediaTypeOrNull()))


                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    cryptedStream.close()
                }
                try {

                   // UpdateToken(JSONObject(decrypted), mContext,refreshTokenInterface).updateRefreshToken()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                return responseBuilder.build()
            }
        }
        return response
    }

}