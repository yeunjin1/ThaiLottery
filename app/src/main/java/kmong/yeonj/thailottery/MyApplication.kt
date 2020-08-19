package kmong.yeonj.thailottery

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Retrofit

class MyApplication: Application(){

    companion object{
//        private val SPINNER_COUNT = 30
//        private val ALARM_DAY = 1
        lateinit var realm:Realm
        lateinit var prefs: PreferenceUtil
        lateinit var retrofit: Retrofit
        lateinit var myAPI: RetrofitInterface
//        lateinit var lotteryResult:RealmResults<LotteryInfo>
//        lateinit var prizeInfoResult:RealmResults<PrizeInfo>
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name("appdb.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
        AndroidThreeTen.init(this)
        retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(RetrofitInterface::class.java)
//        lotteryResult = realm.where(LotteryInfo::class.java).findAll().sort("date", Sort.DESCENDING)
//        prizeInfoResult = realm.where(PrizeInfo::class.java).findAll()
//
//        if(lotteryResult.isEmpty()){
//            val today = LocalDate.now()
//            var year = today.year + 543
//            var month = today.monthValue
//            var day = today.dayOfMonth
//            if(day <= 15){
//                day = 1
//            }
//            else{
//                day = 16
//            }
//            for(i in 0 until SPINNER_COUNT){
//                val dateFormat = org.threeten.bp.format.DateTimeFormatter.ofPattern("dMMyyyy")
//                val date = LocalDate.of(year, month, day).format(dateFormat)
//                Log.d("[MyApplication] date : ", date)
//                if(day == 16){
//                    day = 1
//                }
//                else{ //1
//                    day = 16
//                    month--
//                    if(month == 0){
//                        month = 12
//                        year--
//                    }
//                }
//                getLotteryResult(date)
//            }
//        }
//        else{
//            initLastDraw()
//        }
//        setPushAlarm()
    }

//    fun initLastDraw(){
//        Runnable {
//            myAPI.getResult("LastDraw").enqueue(object :Callback<LotteryInfoTemp>{
//                override fun onFailure(call: Call<LotteryInfoTemp>, t: Throwable) {
//                    Log.d("mytag", "[MyApplication] initLastDraw Failure : " + t.message )
//                }
//
//                override fun onResponse(call: Call<LotteryInfoTemp>, response: Response<LotteryInfoTemp>) {
//                    if(response.isSuccessful){
//                        val result = response.body()!!
//                        if(lotteryResult.where().equalTo("drawId", result.Draw_Id).findFirst() == null){
//                            realm.beginTransaction()
//                            var lotteryInfo = realm.createObject(LotteryInfo::class.java, result.Draw_Id)
//                            lotteryInfo.date = result.Date
//                            lotteryInfo.prize1 = getPrizeInfo(result.Prize_1)
//                            lotteryInfo.prize2 = getPrizeInfo(result.Prize_2)
//                            lotteryInfo.prize3 = getPrizeInfo(result.Prize_3)
//                            lotteryInfo.prize4 = getPrizeInfo(result.Prize_4)
//                            lotteryInfo.prize5 = getPrizeInfo(result.Prize_5)
//                            lotteryInfo.prize1Close= getPrizeInfo(result.Prize_1_Close)
//                            lotteryInfo.prizeLast2= getPrizeInfo(result.Prize_Last_2)
//                            lotteryInfo.prizeLast3= getPrizeInfo(result.Prize_Last_3)
//                            lotteryInfo.prizeFirst3= getPrizeInfo(result.Prize_First_3)
//                            realm.commitTransaction()
//                            Log.d("mytag", "[MyApplication] latest data update : " + lotteryInfo.drawId)
//                            //제일 오래된거 삭제
//                            Log.d("mytag", "[MyApplication] oldest data delete : " + lotteryResult[lotteryResult.count() - 1]!!.drawId)
//                            realm.beginTransaction()
//                            lotteryResult[lotteryResult.count() - 1]!!.deleteFromRealm()
//                            realm.commitTransaction()
//                            Log.d("mytag", "[MyApplication] initLastDraw response success : " + response.body())
//                        }
//                    }
//                    else{
//                        Log.d("mytag", "[MyApplication] initLastDraw response error : " + response.code())
//                    }
//                }
//            })
//        }.run()
//    }
//
//    fun getPrizeInfo(resultPrize: String): PrizeInfo{
//        val obj = JSONObject(resultPrize)
//        val numList = RealmList<String>()
//        if(obj["numbers"] is String){
//            numList.add(obj["numbers"] as String)
//        }
//        else{ //array
//            val jsonArray = obj.getJSONArray("numbers")
//            for(i in 0 until jsonArray.length()){
//                numList.add(jsonArray[i].toString())
//            }
//        }
//
//        val prizeInfo = realm.createObject(PrizeInfo::class.java)
//        prizeInfo.numbers = numList
//        prizeInfo.prize = obj.getString("prize")
//        return prizeInfo
//    }
//
//    fun getLotteryResult(value: String){
//            Runnable {
//                myAPI.getResult("FindDraw/" + value).enqueue(object : Callback<LotteryInfoTemp> {
//                    override fun onFailure(call: Call<LotteryInfoTemp>, t: Throwable) {
//                        Log.d("mytag", "[MyApplication] getLotteryResult Failure : " + t.message )
//
//                    }
//
//                    override fun onResponse(call: Call<LotteryInfoTemp>, response: Response<LotteryInfoTemp>) {
//                        if(response.isSuccessful){
//                            val result = response.body()!!
//                            realm.beginTransaction()
//                            var lotteryInfo = realm.createObject(LotteryInfo::class.java, result.Draw_Id)
//                            lotteryInfo.date = result.Date
//                            lotteryInfo.prize1 = getPrizeInfo(result.Prize_1)
//                            lotteryInfo.prize2 = getPrizeInfo(result.Prize_2)
//                            lotteryInfo.prize3 = getPrizeInfo(result.Prize_3)
//                            lotteryInfo.prize4 = getPrizeInfo(result.Prize_4)
//                            lotteryInfo.prize5 = getPrizeInfo(result.Prize_5)
//                            lotteryInfo.prize1Close = getPrizeInfo(result.Prize_1_Close)
//                            lotteryInfo.prizeLast2= getPrizeInfo(result.Prize_Last_2)
//                            lotteryInfo.prizeLast3= getPrizeInfo(result.Prize_Last_3)
//                            lotteryInfo.prizeFirst3= getPrizeInfo(result.Prize_First_3)
//                            realm.commitTransaction()
//                            Log.d("mytag", "[MyApplication] getLotteryResult response : " + response.body())
//                        }
//                        else{
//                            Log.d("mytag", "[MyApplication] getLotteryResult response error : " + response.code())
//                            Log.d("mytag",  "[MyApplication] getLotteryResult response error : " + response.raw().request().url().url())
//                        }
//                    }
//                })
//            }.run()
//    }
//
//    fun setPushAlarm(){
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AlarmReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
//        if(pendingIntent != null){
//            alarmManager.cancel(pendingIntent)
//        }
//        if(prefs.getString("alarm") == ""){
//            val triggerTime = (SystemClock.elapsedRealtime() + 60 * 10 * 24 * ALARM_DAY * 1000)
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
//            Log.d("mytag", "alarm 등록")
//        }
//        else{
//            Log.d("mytag", "alarm 해제")
//        }
//    }

}