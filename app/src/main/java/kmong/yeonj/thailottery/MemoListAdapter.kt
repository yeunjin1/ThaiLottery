package kmong.yeonj.thailottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class MemoListAdapter (realmResult: OrderedRealmCollection<Memo>, val context: Context) : RealmRecyclerViewAdapter<Memo, MemoListAdapter.ViewHolder>(realmResult, true) {

    val dateForamt = DateTimeFormatter.ofPattern("dd-MM-")


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateText:TextView
        var contentText:TextView
        var deleteButton:ImageView
        init{
            dateText = itemView.findViewById(R.id.dateText)
            contentText = itemView.findViewById(R.id.contentText)
            deleteButton = itemView.findViewById(R.id.deleteButton)

            deleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage(R.string.alert_delete_msg)
                builder.setNegativeButton(R.string.alert_cancel){_, _->

                }
                builder.setPositiveButton(R.string.alert_ok){_, _->
                    MyApplication.realm.beginTransaction()
                    getItem(bindingAdapterPosition)?.deleteFromRealm()
                    MyApplication.realm.commitTransaction()
                }
                builder.show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_memo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val item = getItem(position)!!
            val timeMill = item.dateTime
            val instant = Instant.ofEpochMilli(timeMill)
            val z = instant.atZone(ZoneId.systemDefault())

            holder.dateText.text = dateForamt.format(z) + z.year.plus(543)
            holder.contentText.text = item.content
        }
    }
}

