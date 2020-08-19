package kmong.yeonj.thailottery

import io.realm.RealmObject

open class WinnerInfo : RealmObject() {
    var phoneNumber: String = ""
    var name: String = ""
    var money: String = ""
    var date:Int = 0
}