package kmong.yeonj.thailottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import io.realm.RealmList

class GridViewAdatper(val context: Context, val numbers:RealmList<String>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_view, null)
            holder = ViewHolder()
            holder.textView = view.findViewById(R.id.textView3)
            view.tag = holder
        }
        else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val number = numbers[position]
        holder.textView?.text = number

        return view
    }

    override fun getItem(position: Int): Any {
        return numbers[position]!!
    }

    override fun getItemId(position: Int): Long {
        return 0
    }



    override fun getCount(): Int {
        return numbers.size
    }

    private class ViewHolder{
        var textView: TextView? = null
    }
}