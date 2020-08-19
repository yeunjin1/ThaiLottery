package kmong.yeonj.thailottery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdatper(val context: Context, val list:ArrayList<String>): BaseAdapter() {
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
        holder.textView?.text = list[position]
        holder.textView?.setPadding(0, 12, 0, 12)
        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]!!
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    private class ViewHolder{
        var textView: TextView? = null
    }


}