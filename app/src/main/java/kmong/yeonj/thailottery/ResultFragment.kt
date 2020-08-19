package kmong.yeonj.thailottery


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_result.*
import org.threeten.bp.ZoneId
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 *
 */

class ResultFragment : androidx.fragment.app.Fragment() {
    private lateinit var v:View
    lateinit var spinnerAdapter: ArrayAdapter<String>
    lateinit var lotteryResult:RealmResults<LotteryInfo>
    val format = DecimalFormat("###,###,### ")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_result, container, false)
        return v
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lotteryResult = SplashActivity.lotteryResult
        spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        for(i in lotteryResult){
            val milsec = i.date.toLong() * 1000
            val date = org.threeten.bp.Instant.ofEpochMilli(milsec).atZone(ZoneId.systemDefault()).toLocalDate()
            var month = ""
            when(date.monthValue){
                1-> month = context?.getString(R.string.month1)!!
                2-> month = context?.getString(R.string.month2)!!
                3-> month = context?.getString(R.string.month3)!!
                4-> month = context?.getString(R.string.month4)!!
                5-> month = context?.getString(R.string.month5)!!
                6-> month = context?.getString(R.string.month6)!!
                7-> month = context?.getString(R.string.month7)!!
                8-> month = context?.getString(R.string.month8)!!
                9-> month = context?.getString(R.string.month9)!!
                10-> month = context?.getString(R.string.month10)!!
                11-> month = context?.getString(R.string.month11)!!
                12-> month = context?.getString(R.string.month12)!!
            }
            val str = context?.getString(R.string.thailetter)  + " " +date.dayOfMonth.toString() + " "  + month + " " + date.year.plus(543).toString()
            spinnerAdapter.add(str)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context, RecyclerView.VERTICAL, false
        )

        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position < spinner.count){
                    val list: RealmList<PrizeInfo> = RealmList<PrizeInfo>()
                    list.add(lotteryResult[position]!!.prize1)
                    list.add(lotteryResult[position]!!.prizeFirst3)
                    list.add(lotteryResult[position]!!.prizeLast3)
                    list.add(lotteryResult[position]!!.prizeLast2)
                    list.add(lotteryResult[position]!!.prize1Close)
                    list.add(lotteryResult[position]!!.prize2)
                    list.add(lotteryResult[position]!!.prize3)
                    list.add(lotteryResult[position]!!.prize4)
                    list.add(lotteryResult[position]!!.prize5)
                    rView.adapter = LotteryListAdapter(context!!, list)
                }
            }
        }

        button.setOnClickListener {
            val inputString = inputText.text.toString().trim()
            if(inputString.length != 6){
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage(context!!.getString(R.string.alert_6_msg))
                builder.setNegativeButton(R.string.alert_ok, null)
                val dialog = builder.create()
                dialog.show()
            }
            else{
                checkWinning(inputString)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


    fun checkWinning(input:String) {
        val builder = AlertDialog.Builder(context!!)
        val dialogLayout = layoutInflater.inflate(R.layout.result_dialog, null)
        val textResult = dialogLayout.findViewById<TextView>(R.id.textResult)
        val textPrize = dialogLayout.findViewById<TextView>(R.id.textPrize)

        var result = ""
        var prize = 0
        val selectedDraw = lotteryResult[spinner.selectedItemPosition]!!
        for(i in selectedDraw.prize1!!.numbers){
            if(i == input){
                result += context!!.getString(R.string.prize1)
                Log.d("mytag", selectedDraw.prize1!!.prize.replace(",",""))
                prize += selectedDraw.prize1!!.prize.replace(",", "").toInt()
            }
        }
        for(i in selectedDraw.prize2!!.numbers){
            if(i == input){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prize2)
                prize += selectedDraw.prize2!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prize3!!.numbers){
            if(i == input){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prize3)
                prize += selectedDraw.prize3!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prize4!!.numbers){
            if(i == input){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prize4)
                prize += selectedDraw.prize4!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prize5!!.numbers){
            if(i == input){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prize5)
                prize += selectedDraw.prize5!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prize1Close!!.numbers){
            if(i == input){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prize1Close)
                prize += selectedDraw.prize1Close!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prizeFirst3!!.numbers){
            if(i == input.subSequence(0, 3)){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prizeFirst3)
                prize += selectedDraw.prizeFirst3!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prizeLast3!!.numbers){
            if(i == input.subSequence(3, 6)){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prizeLast3)
                prize += selectedDraw.prizeLast3!!.prize.replace(",","").toInt()
            }
        }
        for(i in selectedDraw.prizeLast2!!.numbers){
            if(i == input.subSequence(4, 6)){
                if(result != ""){
                    result += "\n"
                }
                result += context!!.getString(R.string.prizeLast2)
                prize += selectedDraw.prizeLast2!!.prize.replace(",","").toInt()
            }
        }


        if(result == ""){
            result = context!!.getString(R.string.result_fail)
            textPrize.visibility = View.GONE
        }
        else{
            val prizeStr = format.format(prize)
            textPrize.text = prizeStr + " " + context!!.getString(R.string.bat)
        }
        textResult.text = result
        builder.setView(dialogLayout)
        builder.setNegativeButton(R.string.alert_ok, null)
        val dialog = builder.create()
        dialog.show()
        return
    }
}

