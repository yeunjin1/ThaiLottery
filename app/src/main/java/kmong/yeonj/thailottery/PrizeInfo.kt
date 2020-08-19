package kmong.yeonj.thailottery

import io.realm.RealmList
import io.realm.RealmObject


open class PrizeInfo: RealmObject(){
    var numbers: RealmList<String> = RealmList()
    var prize: String = ""
}