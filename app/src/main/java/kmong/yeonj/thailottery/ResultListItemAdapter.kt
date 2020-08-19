package kmong.yeonj.thailottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class ResultListItemAdapter(val context: Context, realmResult: OrderedRealmCollection<WinnerInfo>):
    RealmRecyclerViewAdapter<WinnerInfo, ResultListItemAdapter.ViewHolder>(realmResult, true)  {
    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var nameText:TextView
        var phoneText:TextView
        var moneyText:TextView
        init{
            nameText = itemView.findViewById(R.id.nameText)
            phoneText = itemView.findViewById(R.id.phoneText)
            moneyText = itemView.findViewById(R.id.moneyText)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.inner_row_result_item, parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ResultListItemAdapter.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val item = getItem(position)!!
            holder.phoneText.text = item.phoneNumber
            holder.nameText.text = item.name
            if(item.money == "null"){
                holder.moneyText.text = "미등록"
            }
            else{
                holder.moneyText.text = item.money
            }
        }
    }
}