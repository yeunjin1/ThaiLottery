package kmong.yeonj.thailottery

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    fun getInstance(): Retrofit{
        if(instance == null){
            val client = OkHttpClient.Builder().addInterceptor {
                val newRequest = it.request().newBuilder().addHeader("x-rapidapi-host", "limestudio-thailottery.p.rapidapi.com" )
                    .addHeader("x-rapidapi-key",  "5f5aad9099mshf121239791c4281p135d06jsn109889ef1091").build()
                return@addInterceptor it.proceed(newRequest)
            }.build()
            instance = Retrofit.Builder()
                .client(client)
                .baseUrl("https://limestudio-thailottery.p.rapidapi.com/v1/Results/ThaiLotto/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

}