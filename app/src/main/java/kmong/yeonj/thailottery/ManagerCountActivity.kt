package kmong.yeonj.thailottery

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_manager_count.*

class ManagerCountActivity : AppCompatActivity() {

    lateinit var eventRef: DatabaseReference
    lateinit var database:DatabaseReference
    lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_count)
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
        initListener()
    }

    fun initDatabase(){
        database = FirebaseDatabase.getInstance().reference
        eventRef = database.child("winningInfo")
        eventRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    if(i.key == "winningRatio"){
                        ratioText.setText(i.value.toString())
                    }
                    else{
                        countText.setText(i.value.toString())
                    }
                }
            }
        })
    }

    fun initListener(){
        button2.setOnClickListener {
            val inputRatio = ratioText.text.toString()
            val count = countText.text.toString()
            if(inputRatio.length != 0 && count.length != 0){
                if(inputRatio.toDouble() >= 0 && inputRatio.toDouble() <= 1 && count.toInt() >= 0) {
                    eventRef.child("winningRatio").setValue(ratioText.text.toString())
                    eventRef.child("winningCount").setValue(countText.text.toString())
                    val builder = android.app.AlertDialog.Builder(this@ManagerCountActivity)
                    builder.setMessage(getString(R.string.count_update_msg))
                    builder.setNegativeButton("확인", { _, _ -> })
                    val dialog = builder.create()
                    dialog.show()}
                else{
                    val builder = android.app.AlertDialog.Builder(this@ManagerCountActivity)
                    builder.setMessage(getString(R.string.count_update_fail_msg))
                    builder.setNegativeButton("확인", { _, _ -> })
                    val dialog = builder.create()
                    dialog.show()
                }
            }
            else{
                val builder = android.app.AlertDialog.Builder(this@ManagerCountActivity)
                builder.setMessage(getString(R.string.count_update_fail_msg))
                builder.setNegativeButton("확인", { _, _ -> })
                val dialog = builder.create()
                dialog.show()
            }

        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
