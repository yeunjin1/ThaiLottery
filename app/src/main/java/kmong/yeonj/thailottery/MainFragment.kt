package kmong.yeonj.thailottery


import `in`.myinnos.androidscratchcard.ScratchCard
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*



/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : androidx.fragment.app.Fragment() {
    private lateinit var v:View
    private lateinit var scratchCard:ScratchCard
    private lateinit var textView:TextView
    private lateinit var numText1:TextView
    private lateinit var numText2:TextView
    private lateinit var numText3:TextView
    private lateinit var refreshButton: ImageView
    private lateinit var memoButton: ImageView
    private var num1 = -1
    private var num2 = -1
    private var num3 = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)
        init()
        return v
    }

    fun init(){
        initData()
        initLayout()
        initListener()
    }

    fun initData(){
        val random = Random()
        num1 = random.nextInt(10)
        num2 = random.nextInt(10)
        num3 = random.nextInt(10)
    }

    fun initLayout(){
        scratchCard = v.findViewById(R.id.scratchCard)
        textView = v.findViewById(R.id.textView)
        numText1 = v.findViewById(R.id.number1)
        numText2 = v.findViewById(R.id.number2)
        numText3 = v.findViewById(R.id.number3)
        memoButton = v.findViewById(R.id.memoButton)
        refreshButton = v.findViewById(R.id.refreshButton)

        numText1.text = num1.toString()
        numText2.text = num2.toString()
        numText3.text = num3.toString()
    }


    fun initListener(){
        scratchCard.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                textView.visibility = View.INVISIBLE
            }
            false
        }
//        scratchCard.setOnScratchListener { scratchCard, visiblePercent ->
//            if(visiblePercent > 0){
//                textView.visibility = View.INVISIBLE
//            }
//        }
        memoButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, num1.toString() + " " + num2.toString() + " " + num3.toString())
            sendIntent.setType("text/plain")
            startActivity(Intent.createChooser(sendIntent, context!!.getString(R.string.chooser_title)))

        }
        refreshButton.setOnClickListener {
            val fm = (activity as MainActivity).fragmentManager!!.beginTransaction()
            fm.replace(R.id.frame_layout, MainFragment()).commitAllowingStateLoss()
        }
    }


}
