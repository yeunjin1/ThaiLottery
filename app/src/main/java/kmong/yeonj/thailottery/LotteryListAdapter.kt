package kmong.yeonj.thailottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList

class LotteryListAdapter (val context: Context, val list: RealmList<PrizeInfo>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TYPE_GRID = 0
    private val TYPE_TEXT = 1

    inner class gridViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var titleText:TextView
        var prizeText:TextView
        var numberView:ExpandableGridView
        init{
            titleText = itemView.findViewById(R.id.titleText)
            prizeText = itemView.findViewById(R.id.prizeText)
            numberView = itemView.findViewById(R.id.numberView)
        }
    }

    inner class textViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        var titleText:TextView
        var prizeText:TextView
        var numberText:TextView
        init{
            titleText = itemView.findViewById(R.id.titleText)
            prizeText = itemView.findViewById(R.id.prizeText)
            numberText = itemView.findViewById(R.id.numberText)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        when(position){
            0, 1, 2, 3, 4->{
                return TYPE_TEXT
            }
            else->{
                return TYPE_GRID
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_GRID){ //grid
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_prize, parent,false)
            return gridViewHolder(v)

        }
        else{ //text
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_prize_text, parent,false)
            return textViewHolder(v)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is gridViewHolder){
            val item = list[position]
            when(position){
                0->{ //prize1
                    holder.titleText.text = context.getString(R.string.prize1)
                }
                1->{
                    holder.titleText.text = context.getString(R.string.prizeFirst3)
                }
                2->{
                    holder.titleText.text = context.getString(R.string.prizeLast3)
                }
                3->{
                    holder.titleText.text = context.getString(R.string.prizeLast2)
                }
                4->{
                    holder.titleText.text = context.getString(R.string.prize1Close)
                }
                5->{
                    holder.titleText.text = context.getString(R.string.prize2)
                }
                6->{
                    holder.titleText.text = context.getString(R.string.prize3)
                }
                7->{
                    holder.titleText.text = context.getString(R.string.prize4)
                }
                8->{
                    holder.titleText.text = context.getString(R.string.prize5)
                }
            }
            holder.prizeText.text = item?.prize + " "+ context.getString(R.string.bat)
            holder.numberView.adapter = GridViewAdatper(context!!, item?.numbers!!)
        }
        else if(holder is textViewHolder){
            val item = list[position]
            when(position){
                0->{ //prize1
                    holder.titleText.text = context.getString(R.string.prize1)
                }
                1->{
                    holder.titleText.text = context.getString(R.string.prizeFirst3)
                }
                2->{
                    holder.titleText.text = context.getString(R.string.prizeLast3)
                }
                3->{
                    holder.titleText.text = context.getString(R.string.prizeLast2)
                }
                4->{
                    holder.titleText.text = context.getString(R.string.prize1Close)
                }
                5->{
                    holder.titleText.text = context.getString(R.string.prize2)
                }
                6->{
                    holder.titleText.text = context.getString(R.string.prize3)
                }
                7->{
                    holder.titleText.text = context.getString(R.string.prize4)
                }
                8->{
                    holder.titleText.text = context.getString(R.string.prize5)
                }
            }
            holder.prizeText.text = item?.prize + " " + context.getString(R.string.bat)
            val nList = item?.numbers
            if(nList!!.size == 1){
                holder.numberText.text = nList[0]
            }
            else{
                holder.numberText.text = nList[0] + "  " + nList[1]
            }
        }
    }

}