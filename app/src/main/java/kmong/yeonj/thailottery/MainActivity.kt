package kmong.yeonj.thailottery

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val ALARM_DAY = 1

    var token = ""
    var phoneNumber = ""
    var userName = ""
    lateinit var database: DatabaseReference
    var status = 0
    lateinit var actionBar: ActionBar
    lateinit var ad:InterstitialAd
    val fragmentManager = supportFragmentManager
    private val fragment1 = MainFragment()
    private val fragment2 = ResultFragment()
    private val fragment3 = EventFragment()
    private val fragment33 = PhoneFragment()
    private val fragment4 = SettingFragment()
    private val fragment5 = MemoFragment()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = fragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_main -> {
                transaction.replace(R.id.frame_layout, fragment1).commitAllowingStateLoss()
                    toolbar_title.text = getString(R.string.title_main)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_result -> {
                transaction.replace(R.id.frame_layout, fragment2).commitAllowingStateLoss()
                toolbar_title.text = getString(R.string.title_result)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_event -> {
                if(phoneNumber.trim() == ""){
                    //전화번호 등록 화면
                    transaction.replace(R.id.frame_layout, fragment33).commitAllowingStateLoss()
                }
                else{
                    //이벤트 참여 화면
                    transaction.replace(R.id.frame_layout, fragment3).commitAllowingStateLoss()
                }
                toolbar_title.text= getString(R.string.title_event)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                transaction.replace(R.id.frame_layout, fragment4).commitAllowingStateLoss()
                toolbar_title.text= getString(R.string.title_setting)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_memo -> {
                transaction.replace(R.id.frame_layout, fragment5).commitAllowingStateLoss()
                toolbar_title.text = getString(R.string.title_memo)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.grad))
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        val menuIcon = ContextCompat.getDrawable(this, R.drawable.abc_ic_menu_overflow_material)
        menuIcon!!.setColorFilter(ContextCompat.getColor(applicationContext, R.color.white), PorterDuff.Mode.SRC_ATOP)
        toolbar.overflowIcon = menuIcon
        init()
    }

    fun init(){
        initAdmob()
        initData()
        initLayout()
        initListener()
    }

    fun initAdmob(){
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener  = object :AdListener(){
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Log.d("mytagad", "배너 광고 로드 실패")
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("mytagad", "배너 광고 로드 성공")
            }
        }

        //전면
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        ad = InterstitialAd(this)
        ad.adUnitId = getString(R.string.whole_ad_unit_id_for_test)
        ad.loadAd(AdRequest.Builder().build())
        ad.adListener = object : AdListener(){
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Log.d("mytagad", "전면 광고 로드 실패")
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("mytagad", "전면 광고 로드 성공")
            }
        }
    }

    fun initData(){
        database = FirebaseDatabase.getInstance().reference
        getDataFromInnerDatabase()
        getDataFromFirebase()

    }

    fun getDataFromInnerDatabase(){
        userName = MyApplication.prefs.getString("userName")
        phoneNumber = MyApplication.prefs.getString("phoneNumber")
        //얘삭제해야됨
//        phoneNumber = "01023453432"
//        userName = "Alex"
    }

    fun getDataFromFirebase(){
        //firebase client 고유 토큰 가져오기
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful){
                Log.d("mytag", "getToken failed")
            }
            token = it.result?.token!!
            Log.d("mytag", "token : " + token)
        }

        val eventRef = database.child("eventUse").child("flag")
        eventRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("mytag", "ondatachange")
                if(p0.value == true){
                    MyApplication.prefs.setString("eventUse", "true")
                }
                else{
                    MyApplication.prefs.setString("eventUse", "false")
                }
            }
        })
    }


    fun initLayout(){
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment2).commitAllowingStateLoss()
    }

    fun initListener(){
        // 얘지워야댐
//        MyApplication.prefs.setString("isManager", "manager")
        Log.d("mytag", "isManager" + MyApplication.prefs.getString("isManager"))
        if(MyApplication.prefs.getString("isManager") == ""){
            Log.d("mytag", "isManager   fds" + MyApplication.prefs.getString("isManager"))

            nav_view.menu.getItem(4).setVisible(false)
        }
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    fun replaceToEventFragment(){
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment3).commitAllowingStateLoss()
    }

    fun startActivity(pos:Int){
        when(pos){
            0->{
                val intent = Intent(applicationContext, MangerWinningActivity::class.java)
                startActivity(intent)
            }
            1->{
                val intent = Intent(applicationContext, ManagerCountActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu1 -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                val dialogLayout = layoutInflater.inflate(R.layout.setting_dialog, null)
                val button = dialogLayout.findViewById<Switch>(R.id.settingButton)
                if(MyApplication.prefs.getString("alarm") == ""){ //알람 받기로 설정되있음
                    button.isChecked = true
                }
                else{ //안받기로 설정되있음
                    button.isChecked = false
                }
                builder.setTitle(getString(R.string.user_setting_title))
                builder.setView(dialogLayout)
                builder.setNegativeButton(R.string.alert_ok) { _, _ ->
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.NOTIFICATION_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                    if(button.isChecked){ //알림 설정
                        MyApplication.prefs.setString("alarm", "")
                        if(pendingIntent != null){
                            Log.d("mytagAlarm", "menu 알림설정 버튼 : 기존 알림 해제")
                            alarmManager.cancel(pendingIntent)
                        }
                        val triggerTime = (SystemClock.elapsedRealtime() + 60 * 60 * 24 * ALARM_DAY * 1000)

                        //1 분
//                        val triggerTime = (SystemClock.elapsedRealtime() + 60 * 1 * 1 * ALARM_DAY * 1000)
                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
                        Log.d("mytagAlarm", "menu 알림 설정 버튼 : 새 알림 등록")
                    }
                    else{ //알림 해제
                        MyApplication.prefs.setString("alarm", "false")
                        if(pendingIntent != null){
                            Log.d("mytagAlarm", "알림 해제 버튼 : 기존 알림 해제")
                            alarmManager.cancel(pendingIntent)
                        }
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }

            R.id.menu2 -> { //앱 공유
                val intent = Intent(Intent.ACTION_SEND)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kmong.yeonj.thailottery")
                intent.setType("text/plain")
                startActivity(Intent.createChooser(intent, getString(R.string.chooser_title2)))
            }
            R.id.menu3->{ //전화번호 재등록
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment33).commitAllowingStateLoss()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

