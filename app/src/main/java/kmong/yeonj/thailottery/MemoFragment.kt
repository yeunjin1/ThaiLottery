package kmong.yeonj.thailottery


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import io.realm.Sort
import kmong.yeonj.thailottery.MyApplication.Companion.realm
import kotlinx.android.synthetic.main.fragment_memo.*


/**
 * A simple [Fragment] subclass.
 *
 */
class MemoFragment : Fragment() {
    lateinit var memoResult:RealmResults<Memo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memoResult = realm.where(Memo::class.java).findAll().sort("dateTime", Sort.DESCENDING)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(
            activity!!,
            RecyclerView.VERTICAL,
            false
        )
        rView.layoutManager = layoutManager
        rView.adapter = MemoListAdapter(memoResult, context!!)
        addButton.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            val dialogLayout = layoutInflater.inflate(R.layout.add_memo_dialog, null)
            val memoContent = dialogLayout.findViewById<EditText>(R.id.memoContent)
            builder.setView(dialogLayout)
            builder.setPositiveButton(context!!.getString(R.string.alert_add)){_, _->
                if(memoContent.text.toString().trim() != ""){
                    realm.beginTransaction()
                    var newMemo = realm.createObject(Memo::class.java)
                    newMemo.content = memoContent.text.toString()
                    newMemo.dateTime = System.currentTimeMillis()
                    realm.commitTransaction()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
        rView.scrollTo(0, 0)
    }

}
