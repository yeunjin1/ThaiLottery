package kmong.yeonj.thailottery


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PhoneFragment : Fragment() {
    private lateinit var v:View
    private lateinit var phoneText:EditText
    private lateinit var nameText:EditText
    private lateinit var confirmButton:Button
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_phone, container, false)
        init()
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun init(){

        if(MyApplication.prefs.getString("eventUse") == "false"){
            mainActivity.replaceToEventFragment()
        }
        initData()
        initPermmision()
        initListener()
    }

    fun initPermmision(){
        TedPermission.with(mainActivity)
            .setPermissionListener(object: PermissionListener {
                override fun onPermissionGranted() {
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }
            })
            .setDeniedMessage("승인 거절됨")
            .setPermissions(Manifest.permission.READ_PHONE_STATE)
            .check()
    }

    fun initData(){
        mainActivity = activity as MainActivity
        phoneText = v.findViewById(R.id.phoneText)
        nameText = v.findViewById(R.id.nameText)
        confirmButton = v.findViewById(R.id.confirmButton)
    }

    fun initListener(){
        confirmButton.setOnClickListener {
            if (nameText.text.toString().trim() != "" && phoneText.text.toString().trim() != "") {
                //폰번호 인증
                var thisPhoneNumber = ""
                val telManager = mainActivity.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                var msg: String
                var flg = false
                if(ContextCompat.checkSelfPermission(mainActivity.applicationContext, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                    if(telManager.simState == TelephonyManager.SIM_STATE_READY){
//                        Toast.makeText(context, telManager.line1Number, Toast.LENGTH_LONG).show()
                        thisPhoneNumber = telManager.line1Number
                        if(thisPhoneNumber.startsWith("+")){
                            Log.d("mytag", "thisPhoneNumber : " + thisPhoneNumber)
                            thisPhoneNumber = thisPhoneNumber.replace("+", "0")
                            Log.d("mytag", "thisPhoneNumber : " + thisPhoneNumber)
                        }
                        if(thisPhoneNumber == phoneText.text.toString().trim()){
                            flg = true
                            //인증성공
                            msg = context!!.getString(R.string.alert_success)
                            mainActivity.phoneNumber = phoneText.text.toString()
                            mainActivity.userName = nameText.text.toString()
                            MyApplication.prefs.setString("phoneNumber", phoneText.text.toString())
                            MyApplication.prefs.setString("userName", nameText.text.toString())

                            //관리자인지 확인(관리자 모드로 진입하려면 네트워크 켜야됨
                            val managerRef = mainActivity.database.child("managerInfo")
                            managerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for (i in p0.children) {
                                        if(i.value == thisPhoneNumber){
                                            mainActivity.nav_view.menu.getItem(4).setVisible(true)
                                            MyApplication.prefs.setString("isManager", "manager")
                                            break
                                        }
                                    }
                                }
                            })
                        }
                        else{
                            //얘
                            if(thisPhoneNumber == "" || thisPhoneNumber == null){
                                flg = true
                                msg = context!!.getString(R.string.alert_success2)
                                mainActivity.phoneNumber = phoneText.text.toString()
                                mainActivity.userName = nameText.text.toString()
                                MyApplication.prefs.setString("phoneNumber", phoneText.text.toString())
                                MyApplication.prefs.setString("userName", nameText.text.toString())
                            }
                            else {
                                msg = context!!.getString(R.string.alert_fail)
                            }
                        }
                    }
                    else{
                        //유심인식안됨
//                        msg = context!!.getString(R.string.alert_sim_error)
                        flg = true
                        msg = context!!.getString(R.string.alert_success2)
                        mainActivity.phoneNumber = phoneText.text.toString()
                        mainActivity.userName = nameText.text.toString()
                        MyApplication.prefs.setString("phoneNumber", phoneText.text.toString())
                        MyApplication.prefs.setString("userName", nameText.text.toString())
                    }
                }
                else{
                    msg = context!!.getString(R.string.alert_per_deny)
                }
                val builder2 = AlertDialog.Builder(context!!)
                builder2.setNegativeButton(R.string.alert_ok){_, _->
                    if(flg){
                        mainActivity.replaceToEventFragment()
                    }
                }
                builder2.setMessage(msg)
                builder2.show()
            }
            else {
                val builder2 = AlertDialog.Builder(context!!)
                builder2.setNegativeButton(R.string.alert_ok, null)
                builder2.setMessage(R.string.alert_field)
                builder2.show()
            }
        }
    }
}
