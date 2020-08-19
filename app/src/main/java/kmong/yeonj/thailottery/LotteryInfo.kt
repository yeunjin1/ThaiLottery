package kmong.yeonj.thailottery

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LotteryInfo : RealmObject() {
    var date:Int = 0

    @PrimaryKey
    var drawId: Int = 0

    //singulare link에서 null 허용으로 안하면 오류 발생
    var prize1:PrizeInfo? = null
    var prize2:PrizeInfo? = null
    var prize3: PrizeInfo? = null
    var prize4: PrizeInfo? = null
    var prize5: PrizeInfo? = null
    var prize1Close: PrizeInfo? = null
    var prizeLast2: PrizeInfo? = null
    var prizeLast3: PrizeInfo? = null
    var prizeFirst3: PrizeInfo? = null
}