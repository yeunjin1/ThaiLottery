package kmong.yeonj.thailottery

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import io.realm.RealmResults
import kmong.yeonj.thailottery.MyApplication.Companion.realm
import kotlinx.android.synthetic.main.activity_manger_winning.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MangerWinningActivity : AppCompatActivity() { //이벤트 당첨 확인 액티비티

    lateinit var database:DatabaseReference
    lateinit var realmResult:RealmResults<WinnerInfo>
    lateinit var actionBar: ActionBar

    var list = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manger_winning)
        init()
    }

    fun init(){
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.grad))
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        val backArrow = ContextCompat.getDrawable(applicationContext, R.drawable.abc_ic_ab_back_material)
        backArrow!!.setColorFilter(ContextCompat.getColor(applicationContext, R.color.white), PorterDuff.Mode.SRC_ATOP)
        actionBar.setHomeAsUpIndicator(backArrow)
        actionBar.setDisplayHomeAsUpEnabled(true)
        initDatabase()
        initAdapter()
    }

    fun initDatabase(){
        database = FirebaseDatabase.getInstance().reference
        val dateForamt = DateTimeFormatter.ofPattern("yyyyMMdd")
        var date = LocalDate.now()
        realmResult = realm.where(WinnerInfo::class.java).lessThan("date", date.format(dateForamt).toInt()).greaterThan("date", date.minusDays(31).format(dateForamt).toInt()).findAll()
        date = date.minusDays(1)
        for(i in 1..30){
            val today = date.format(dateForamt)
            list.add(today.toInt())
            val result = realmResult.where().equalTo("date", today.toInt()).findAll()
            if(result.isEmpty()){
                val managerRef = database.child("event").child(today).child("win")
                managerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (i in p0.children) {
                            realm.beginTransaction()
                            val winnerInfo = realm.createObject(WinnerInfo::class.java)
                            winnerInfo.phoneNumber = i.key.toString()
                            winnerInfo.name = i.child("userName").value.toString()
                            winnerInfo.money = i.child("trueMoney").value.toString()
                            winnerInfo.date = today.toInt()

                            realm.commitTransaction()
                        }
                    }
                })
            }
            else{
            }
            date = date.minusDays(1)
        }
        //30일 이전 메모리 정리
        realm.beginTransaction()
        realmResult.where().lessThan("date", date.format(dateForamt).toInt()).findAll().deleteAllFromRealm()
        realm.commitTransaction()


    }




    fun initAdapter(){
        result_rView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        result_rView.adapter = ResultListAdapter(applicationContext, realmResult, list)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
