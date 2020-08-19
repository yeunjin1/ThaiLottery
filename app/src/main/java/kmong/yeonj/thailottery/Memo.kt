package kmong.yeonj.thailottery

import io.realm.RealmObject
 open class Memo: RealmObject() {
    var dateTime: Long = 0
    var content: String = ""
}