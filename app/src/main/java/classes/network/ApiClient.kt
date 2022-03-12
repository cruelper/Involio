package classes.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var baseURL: String = "http://192.168.43.207:8080"

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        var httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    private fun getOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .callTimeout(15, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()


    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .client(getOkHttpClient())
            .build()

    fun getUserService(): UserService =
        getRetrofit().create(UserService::class.java)

}