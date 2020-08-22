package kmong.yeonj.thailottery


import `in`.myinnos.androidscratchcard.ScratchCard
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class EventFragment : androidx.fragment.app.Fragment() {

    private lateinit var v:View
    private lateinit var resultText:TextView
    private lateinit var scratchCoin:ScratchCard
    private lateinit var infoText: TextView
    private lateinit var cardView: CardView
    private lateinit var infoTextDone:TextView
    private lateinit var editText:EditText
    private lateinit var editButton:Button
    private lateinit var textViewWin:TextView
    var eventResult = false
    val dateForamt = DateTimeFormatter.ofPattern("yyyyMMdd")
    var winningRatio = 0.0f
    var winningCount = 0
    private lateinit var mainActivity: MainActivity
    var eventParResult = ""
    private val STAT_SUCCESS = 0
    private val STAT_NET_UNCONNECTED = 1
    private val STAT_PHONE_UNREGISTERD = 2
    private val STAT_EVENT_DONE = 3
    private val STAT_NOT_USE = 4
    private var winChildrenCount = -1L
    private lateinit var ad:InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initAdmob()
        v = inflater.inflate(R.layout.fragment_event, container, false)
        resultText = v.findViewById(R.id.resultText)
        scratchCoin = v.findViewById(R.id.scratchCoin)
        infoText = v.findViewById(R.id.textView)
        cardView = v.findViewById(R.id.cardView)
        infoTextDone = v.findViewById(R.id.textViewDone)
        editText = v.findViewById(R.id.trueMoney)
        editButton = v.findViewById(R.id.editButton)
        textViewWin = v.findViewById(R.id.textViewWin)
        init()
        return v
    }



    fun initAdmob(){
        val date = LocalDate.now()
        val today = date.format(dateForamt)
        if(MyApplication.prefs.getString("admobDone") == today){ //오늘 이미 광고 시청
            Log.d("mytagad", "전면광고이미봄")

        }
        else{ //오늘 처음으로 이벤트 페이지 들어감
            //광고 실행 코드
            if(mainActivity.ad.isLoaded){
                Log.d("mytagad", "전면광고실행")
                mainActivity.ad.show()
                MyApplication.prefs.setString("admobDone", today)
            }
            else{
                Log.d("mytagad", "전면광고실행실패: 로드 안됨")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //mainActivity 생성될때
        //당첨 비율, 최대개수, 오늘 당첨자 수 firebase 에서 가져오는 리스너 달기
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity
        val winningRef = mainActivity.database.child("winningInfo")
        winningRef.child("winningRatio").addListenerForSingleValueEvent( //network 연결 되있을때 실행
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("mytag", "loadPost:onCancelled", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    winningRatio = p0.value.toString().toFloat()
                    Log.d("mytag", winningRatio.toString())
                }
            }
        )
        winningRef.child("winningCount").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("mytag", "loadPost:onCancelled", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    winningCount = p0.value.toString().toInt()
                    Log.d("mytag", winningCount.toString())
                }
            }
        )
        val date = LocalDate.now()
        val today = date.format(dateForamt)
        val eventWinRef = mainActivity.database.child("event").child(today).child("win")
        eventWinRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                winChildrenCount = p0.childrenCount
                Log.d("mytag", "childerenCount : " + winChildrenCount.toString())
            }
        })
    }

    fun init(){
        checkStatus()
        initLayout()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        editText.setText(MyApplication.prefs.getString("trueMoney"))

    }

    fun checkStatus() {
        if (!NetworkStatus.isNetworkConnected(mainActivity)) {
            mainActivity.status = STAT_NET_UNCONNECTED
        }
        else {
            mainActivity.status = STAT_SUCCESS
        }
        if(mainActivity.status == STAT_SUCCESS) {
            Log.d("mytag", "eventUse " + MyApplication.prefs.getString("eventUse"))
            if (MyApplication.prefs.getString("eventUse") == "false") {
                mainActivity.status = STAT_NOT_USE
            }
        }
        if(mainActivity.status == STAT_SUCCESS){ // 이벤트 오늘 참여했는지 확인
            checkDone() //backend로 하면 좋을듯
        }
    }

    fun checkDone() {
        val date = LocalDate.now()
        val today = date.format(dateForamt)
        var eventParDate = MyApplication.prefs.getString("eventParDate")
        eventParResult = MyApplication.prefs.getString("eventParResult")
        if (eventParDate == today) {
            //오늘 이미 참여
            mainActivity.status = STAT_EVENT_DONE
            Log.d("mytag", "eventDone : eventParDate : " + eventParDate)
        }
        else if (eventParDate == "") { //앱 삭제했다 다시깔 경우
            //firebase에서 오늘 참여했는지 확인
            val date = LocalDate.now()
            val today = date.format(dateForamt)
            val eventRef = mainActivity.database.child("event").child(today)
            eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("mytag", "loadPost2:onCancelled", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var eventDone = false
                    for (i in p0.children) {
                        //phoneNumber -> 기기 id
//                        val result = p0.child(i.key.toString()).child(mainActivity.phoneNumber)

                        //이전에 등록한 전화번호로 참여한 적이 있는지 확인
                        for(j in i.children){
                            if(j.child("phoneNumber").value == mainActivity.phoneNumber){
                                eventDone = true
                                if(i.key.toString() == "win"){
                                    eventParResult = "true"
                                }
                                else{
                                    eventParResult = "false"
                                }
                                break
                            }
                        }
//                        val result = p0.child(i.key.toString()).child(mainActivity.token)
//                        Log.d(
//                            "mytag",
//                            "checkFirebase: eventDone: " + i.key.toString() + " " + result.exists().toString()
//                        )
//                        if (result.exists()) {
//                            if(i.key.toString() == "win"){
//                                eventParResult = "true"
//                            }
//                            else{
//                                eventParResult = "false"
//                            }
//                            eventDone = true
//                        }
                    }
                    if (eventDone) {
                        MyApplication.prefs.setString("eventParDate", today)
                        MyApplication.prefs.setString("eventParResult", eventParResult)
                        mainActivity.status = STAT_EVENT_DONE
                    }
                    else{
                        MyApplication.prefs.setString("eventParDate", "-1")
                        mainActivity.status = STAT_SUCCESS
                    }
                    initLayout()
                }
            })
        }
        else {
            mainActivity.status = STAT_SUCCESS
            Log.d("mytag", "eventNotDone : eventParDate : " + eventParDate)
        }
    }

    fun initLayout() {
        //얘
//        MyApplication.prefs.setString("trueMoney", "212323")
        when(mainActivity.status){
            STAT_EVENT_DONE -> {
                scratchCoin.visibility = View.INVISIBLE
                Log.d("mytag", "initlaout : " + eventParResult)
                if(eventParResult == "true"){
                    editButton.visibility = View.VISIBLE
                    textViewWin.visibility = View.VISIBLE
                    editText.visibility = View.VISIBLE
                    editText.setText(MyApplication.prefs.getString("trueMoney"))
                    resultText.background = context?.getDrawable(R.drawable.event_win)
                }
                else{
                    editButton.visibility = View.GONE
                    textViewWin.visibility = View.GONE

                    editText.visibility = View.GONE
                    resultText.background = context?.getDrawable(R.drawable.event_lose)
                }
                infoTextDone.visibility = View.VISIBLE
                infoText.visibility = View.INVISIBLE
            }

            STAT_NET_UNCONNECTED -> {
                cardView.visibility = View.GONE
                infoText.text = context?.getString(R.string.event_network)
            }
            STAT_SUCCESS -> {
                cardView.visibility = View.VISIBLE
                scratchCoin.visibility = View.VISIBLE
                infoText.text = context?.getString(R.string.event_default_message)
            }
            STAT_NOT_USE ->{
                cardView.visibility = View.GONE
                infoText.text = context?.getString(R.string.event_stop)
            }
        }
    }

    fun initListener() {
        scratchCoin.setOnScratchListener { scratchCard, visiblePercent ->
            if (visiblePercent > 0.3) {
                if(!NetworkStatus.isNetworkConnected(mainActivity)){
                    val builder2 = AlertDialog.Builder(context!!)
                    builder2.setNegativeButton(R.string.alert_ok){_, _->
                        mainActivity.status = STAT_NET_UNCONNECTED
                        initLayout()
                    }
                    builder2.setMessage(context!!.getString(R.string.event_network2))
                    builder2.show()
                }
                else{
                    if(winChildrenCount >= winningCount){
                        eventResult = false
                        Log.d("mytag", "당첨 개수 초과")
                    }
                    else{
                        val random = Random()
                        val randNum = random.nextInt(100)
                        Log.d("mytag", "winning randNum : " + randNum.toString())
                        if (randNum in 0..(100 * winningRatio).toInt()) {
                            eventResult = true //당첨
                        }
                    }
                    if (eventResult!!) { //당첨이 된 경우
                        resultText.background = context?.getDrawable(R.drawable.event_win)
                        //트루머니 등록 안해놨으면 팝업 생성

                        if(MyApplication.prefs.getString("trueMoney") == "") {
                            val builder = android.app.AlertDialog.Builder(context)
                            val dialogLayout = layoutInflater.inflate(R.layout.event_dialog, null)
                            val input = dialogLayout.findViewById<EditText>(R.id.editText)
                            builder.setView(dialogLayout)
                            builder.setCancelable(false)
                            builder.setNegativeButton(R.string.alert_ok) { _, _ ->
                                if(input.text.isNotEmpty()){
                                    MyApplication.prefs.setString("trueMoney", input.text.toString())
                                    editText.setText(input.text.toString())
//                                    editText.isFocusable = false
                                    val date = LocalDate.now()
                                    val today = date.format(dateForamt)
                                    val eventPushRef = mainActivity.database.child("event").child(today).child("win")
                                        .child(mainActivity.token)
                                    eventPushRef.child("trueMoney").setValue(input.text.toString())
                                }
                                infoText.visibility = View.INVISIBLE
                                infoTextDone.visibility = View.VISIBLE
                                editText.visibility = View.VISIBLE
                                editText.isCursorVisible = false
                                editButton.visibility = View.VISIBLE
                                textViewWin.visibility = View.VISIBLE
                                scratchCoin.visibility = View.INVISIBLE
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }
                        else{ //트루머니 이미 등록된 상태
                            val builder = AlertDialog.Builder(context)
                            val dialogLayout = layoutInflater.inflate(R.layout.event_dialog, null)
                            val input = dialogLayout.findViewById<EditText>(R.id.editText)
                            val trueText = dialogLayout.findViewById<TextView>(R.id.trueText)
                            builder.setView(dialogLayout)
                            trueText.visibility = View.GONE
                            input.visibility = View.GONE
                            builder.setCancelable(false)
                            builder.setNegativeButton(R.string.alert_ok) { _, _ ->
                                infoText.visibility = View.INVISIBLE
                                infoTextDone.visibility = View.VISIBLE
                                editText.visibility = View.VISIBLE
                                editText.isCursorVisible = false
                                editButton.visibility = View.VISIBLE
                                textViewWin.visibility = View.VISIBLE
                                scratchCoin.visibility = View.INVISIBLE
                                val input2 = MyApplication.prefs.getString("trueMoney")
                                editText.setText(input2)
                                editText.isFocusable = false
                            }
                            val dialog = builder.create()
                            dialog.show()
                        }

                    } else {
                        val builder = AlertDialog.Builder(context)
                        val dialogLayout = layoutInflater.inflate(R.layout.event_dialog, null)
                        val input = dialogLayout.findViewById<EditText>(R.id.editText)
                        val trueText = dialogLayout.findViewById<TextView>(R.id.trueText)
                        val resText = dialogLayout.findViewById<TextView>(R.id.resText)
                        builder.setView(dialogLayout)
                        trueText.visibility = View.GONE
                        input.visibility = View.GONE
                        resText.text = context?.getString(R.string.event_lose)
                        builder.setCancelable(false)
                        builder.setNegativeButton(R.string.alert_ok) { _, _ ->
                            resultText.background = context!!.getDrawable(R.drawable.event_lose)
                            infoText.visibility = View.INVISIBLE
                            infoTextDone.visibility = View.VISIBLE
                            scratchCoin.visibility = View.INVISIBLE
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
                //결과 서버에 보내고 내부 저장소에 저장하기
               if (eventResult != null) {
                    writeResultToFirebase()
                    val date = LocalDate.now()
                    val today = date.format(dateForamt)
                    MyApplication.prefs.setString("eventParDate", today)
                    MyApplication.prefs.setString("eventParResult", eventResult.toString())
                }
            }
        }

        editButton.setOnClickListener {
            if(editText.text.isNotEmpty()){
                val input = editText.text.toString()
                val date = LocalDate.now()
                val today = date.format(dateForamt)
                val eventPushRef = mainActivity.database.child("event").child(today).child("win").child(mainActivity.token)
                eventPushRef.child("trueMoney").setValue(input)
                MyApplication.prefs.setString("trueMoney", input)

                //변경 확인 팝업 메세지
                val builder = AlertDialog.Builder(context)
                builder.setMessage(getString(R.string.event_done_msg))
                builder.setNegativeButton(R.string.alert_ok){_, _->}
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    fun writeResultToFirebase() {
        val date = LocalDate.now()
        val today = date.format(dateForamt)
        var eventStr = ""
        if (eventResult)
            eventStr = "win"
        else
            eventStr = "lose"
        val eventPushRef = mainActivity.database.child("event").child(today).child(eventStr)
            .child(mainActivity.token)
        eventPushRef.child("userName").setValue(mainActivity.userName)
        eventPushRef.child("timestamp").setValue(System.currentTimeMillis())
        eventPushRef.child("phoneNumber").setValue(mainActivity.phoneNumber)
        if(eventStr == "win")
            eventPushRef.child("trueMoney").setValue(MyApplication.prefs.getString("trueMoney"))
    }
}
