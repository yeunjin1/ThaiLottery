package kmong.yeonj.thailottery

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitInterface {
@GET()
fun getResult(@Url url:String) : Call<LotteryInfoTemp>
}