package kmong.yeonj.thailottery

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context:Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key:String): String{
        return prefs.getString(key, "").toString()
    }

    fun setString(key: String, str:String){
        prefs.edit().putString(key, str).apply()
    }

    fun getFloat(key:String): Float{
        return prefs.getFloat(key, 0.0f)
    }

    fun setFloat(key: String, fl: Float){
        prefs.edit().putFloat(key, fl).apply()
    }

    fun removeKey(key:String){
        val edit = prefs.edit()
        edit.remove(key)
        edit.commit()
    }
}