package kmong.yeonj.thailottery

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.GridView

class ExpandableGridView: GridView {
    constructor(context:Context):super(context)
    constructor(context: Context, attrs:AttributeSet):super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec:Int
        if(layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        }
        else{
            heightSpec = heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}