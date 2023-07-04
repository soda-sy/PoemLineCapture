package dduwcom.mobile.poemlinecapture.data

import java.io.Serializable

class PoemLineDto(val id : Long, var image : Int, var title : String?, var poetName : String?,
                  var content : String?, var oneLine : String?, var date : String?) : Serializable