package kmong.yeonj.thailottery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_setting.*



/**
 * A simple [Fragment] subclass.
 *
 */
class SettingFragment : androidx.fragment.app.Fragment() {
    private lateinit var v:View
    private lateinit var list:ArrayList<String>
    lateinit var eventRef: DatabaseReference
    lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_setting, container, false)
        init()
        return v
    }

    fun init(){
//        (activity as MainActivity).supportActionBar!!.title = getString(R.string.title_setting)
        initData()
    }

    fun initData(){
//        list = arrayListOf("이벤트 당첨 관리", "팝업 광고 유뮤 사용", "푸시메세지 관리")
        list = arrayListOf(context!!.getString(R.string.setting_list1), context!!.getString(R.string.setting_list2), context!!.getString(R.string.setting_list3))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        eventRef = database.child("eventUse").child("flag")
        listView.adapter = ListViewAdatper(context!!, list)
        listView.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0, 1->{
                    (activity as MainActivity).startActivity(position)
                }
                2->{
                    val builder = AlertDialog.Builder(activity as MainActivity)
                    val dialogLayout = layoutInflater.inflate(R.layout.event_setting_dialog, null)
                    val button = dialogLayout.findViewById<Switch>(R.id.settingButton)
                    if(MyApplication.prefs.getString("eventUse") == "true"){
                        button.isChecked = true
                    }
                    else{
                        button.isChecked = false
                    }
                    builder.setView(dialogLayout)
                    builder.setNegativeButton("확인", { _, _ ->
                        if(button.isChecked){ //알림 설정
                            MyApplication.prefs.setString("eventUse", "true")
                            eventRef.setValue(true)
                        }
                        else{ //알림 해제
                            MyApplication.prefs.setString("eventUse", "false")
                            eventRef.setValue(false)
                        }
                    })
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }}
}
