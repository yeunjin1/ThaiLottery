package kmong.yeonj.thailottery

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults

class ResultListAdapter(val context: Context, val realmResults: RealmResults<WinnerInfo>, val list:ArrayList<Int>):RecyclerView.Adapter<ResultListAdapter.ViewHolder>(){

    var realm:Realm
    init {
        Realm.init(context)
        realm = Realm.getDefaultInstance()

    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var rView:RecyclerView
        var dateText:TextView
        init{
            rView = itemView.findViewById(R.id.rView)
            dateText = itemView.findViewById(R.id.dateText)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_result_item, parent,false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ResultListAdapter.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val date = list[position]
            val result = realmResults.where().equalTo("date", date).findAll()
            holder.rView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context, RecyclerView.VERTICAL, false
            )
            holder.rView.adapter = ResultListItemAdapter(context, result)
            holder.dateText.text = date.toString()
        }
    }
}