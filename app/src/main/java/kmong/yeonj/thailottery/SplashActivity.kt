package kmong.yeonj.thailottery

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import org.json.JSONObject
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private val SPINNER_COUNT = 30
    private val ALARM_DAY = 1
    companion object{
        lateinit var lotteryResult: RealmResults<LotteryInfo>
        lateinit var prizeInfoResult: RealmResults<PrizeInfo>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
        Thread.sleep(1500)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun init(){
        lotteryResult = MyApplication.realm.where(LotteryInfo::class.java).findAll().sort("date", Sort.DESCENDING)
        prizeInfoResult = MyApplication.realm.where(PrizeInfo::class.java).findAll()

        if(lotteryResult.isEmpty()){
            val today = LocalDate.now()
            var year = today.year + 543
            var month = today.monthValue
            var day = today.dayOfMonth
            if(day <= 15){
                day = 1
            }
            else{
                day = 16
            }
            for(i in 0 until SPINNER_COUNT){
                val dateFormat = org.threeten.bp.format.DateTimeFormatter.ofPattern("dMMyyyy")
                val date = LocalDate.of(year, month, day).format(dateFormat)
                val dateBefore = LocalDate.of(year, month, day).minusDays(1).format(dateFormat)
                val dateAfter = LocalDate.of(year, month, day).plusDays(1).format(dateFormat)
                Log.d("[MyApplication] date : ", date)
                if(day == 16){
                    day = 1
                }
                else{ //1
                    day = 16
                    month--
                    if(month == 0){
                        month = 12
                        year--
                    }
                }
                getLotteryResult(date, dateBefore, dateAfter)
            }
        }
        else{
            initLastDraw()
        }
        setPushAlarm()
    }
    fun initLastDraw(){
        Runnable {
            MyApplication.myAPI.getResult("LastDraw").enqueue(object : Callback<LotteryInfoTemp> {
                override fun onFailure(call: Call<LotteryInfoTemp>, t: Throwable) {
                    Log.d("mytag", "[MyApplication] initLastDraw Failure : " + t.message )
                }

                override fun onResponse(call: Call<LotteryInfoTemp>, response: Response<LotteryInfoTemp>) {
                    if(response.isSuccessful){
                        val result = response.body()!!
                        if(lotteryResult.where().equalTo("drawId", result.Draw_Id).findFirst() == null){
                            MyApplication.realm.beginTransaction()
                            var lotteryInfo = MyApplication.realm.createObject(LotteryInfo::class.java, result.Draw_Id)
                            lotteryInfo.date = result.Date
                            lotteryInfo.prize1 = getPrizeInfo(result.Prize_1)
                            lotteryInfo.prize2 = getPrizeInfo(result.Prize_2)
                            lotteryInfo.prize3 = getPrizeInfo(result.Prize_3)
                            lotteryInfo.prize4 = getPrizeInfo(result.Prize_4)
                            lotteryInfo.prize5 = getPrizeInfo(result.Prize_5)
                            lotteryInfo.prize1Close= getPrizeInfo(result.Prize_1_Close)
                            lotteryInfo.prizeLast2= getPrizeInfo(result.Prize_Last_2)
                            lotteryInfo.prizeLast3= getPrizeInfo(result.Prize_Last_3)
                            lotteryInfo.prizeFirst3= getPrizeInfo(result.Prize_First_3)
                            MyApplication.realm.commitTransaction()
                            Log.d("mytag", "[MyApplication] latest data update : " + lotteryInfo.drawId)
                            //제일 오래된거 삭제
                            Log.d("mytag", "[MyApplication] oldest data delete : " + lotteryResult[lotteryResult.count() - 1]!!.drawId)
                            MyApplication.realm.beginTransaction()
                            lotteryResult[lotteryResult.count() - 1]!!.deleteFromRealm()
                            MyApplication.realm.commitTransaction()
                            Log.d("mytag", "[MyApplication] initLastDraw response success : " + response.body())
                        }
                    }
                    else{
                        Log.d("mytag", "[MyApplication] initLastDraw response error : " + response.code())
                    }
                }
            })
        }.run()
    }

    fun getPrizeInfo(resultPrize: String): PrizeInfo{
        val obj = JSONObject(resultPrize)
        val numList = RealmList<String>()
        if(obj["numbers"] is String){
            numList.add(obj["numbers"] as String)
        }
        else{ //array
            val jsonArray = obj.getJSONArray("numbers")
            for(i in 0 until jsonArray.length()){
                numList.add(jsonArray[i].toString())
            }
        }

        val prizeInfo = MyApplication.realm.createObject(PrizeInfo::class.java)
        prizeInfo.numbers = numList
        prizeInfo.prize = obj.getString("prize")
        return prizeInfo
    }

    fun getLotteryResult(value: String, valueB: String, valueA:String) {
        Runnable {
            MyApplication.myAPI.getResult("FindDraw/" + value).enqueue(object : Callback<LotteryInfoTemp> {
                override fun onFailure(call: Call<LotteryInfoTemp>, t: Throwable) {
                    Log.d("mytag", "[MyApplication] getLotteryResult Failure : " + t.message )

                }

                override fun onResponse(call: Call<LotteryInfoTemp>, response: Response<LotteryInfoTemp>) {
                    if(response.isSuccessful){
                        val result = response.body()!!
                        MyApplication.realm.beginTransaction()
                        var lotteryInfo = MyApplication.realm.createObject(LotteryInfo::class.java, result.Draw_Id)
                        lotteryInfo.date = result.Date
                        lotteryInfo.prize1 = getPrizeInfo(result.Prize_1)
                        lotteryInfo.prize2 = getPrizeInfo(result.Prize_2)
                        lotteryInfo.prize3 = getPrizeInfo(result.Prize_3)
                        lotteryInfo.prize4 = getPrizeInfo(result.Prize_4)
                        lotteryInfo.prize5 = getPrizeInfo(result.Prize_5)
                        lotteryInfo.prize1Close = getPrizeInfo(result.Prize_1_Close)
                        lotteryInfo.prizeLast2= getPrizeInfo(result.Prize_Last_2)
                        lotteryInfo.prizeLast3= getPrizeInfo(result.Prize_Last_3)
                        lotteryInfo.prizeFirst3= getPrizeInfo(result.Prize_First_3)
                        MyApplication.realm.commitTransaction()
                        Log.d("mytag", "[MyApplication] getLotteryResult response : " + response.body())
                    }
                    else{
                        if(valueA != "" && valueB != ""){
                            getLotteryResult(valueA, "", "")
                            getLotteryResult(valueB, "", "")
                        }
                        Log.d("mytag", "[MyApplication] getLotteryResult response error : " + response.code())
                        Log.d("mytag",  "[MyApplication] getLotteryResult response error : " + response.raw().request().url().url())
                    }
                }
            })
        }.run()
    }

    fun setPushAlarm(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        if(pendingIntent != null){
            Log.d("mytagAlarm", "기존 알람 해제")
            alarmManager.cancel(pendingIntent)
        }
        if(MyApplication.prefs.getString("alarm") == ""){
            Log.d("mytagAlarm", "알람 받기로 설정 되어있음")
            val triggerTime = (SystemClock.elapsedRealtime() + 60 * 60 * 24 * ALARM_DAY * 1000)

            //1 분
//            val triggerTime = (SystemClock.elapsedRealtime() + 60 * 1 * 1 * ALARM_DAY * 1000)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
            Log.d("mytagAlarm", "새 알람 등록")
        }
        else{
            Log.d("mytagAlarm", "알람 안 받기로 설정 되어있음")
        }
    }
}
