package dduwcom.mobile.poemlinecapture.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log


class PoemLineDBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    val TAG="PoemLineDBHelper"

    companion object{
        const val DB_NAME = "poemLine_db"
        const val TABLE_NAME = "poemLine_table"
        const val COL_IMAGE = "image"
        const val COL_TITLE = "title"
        const val COL_POETNAME = "poetName"
        const val COL_CONTENT = "content"
        const val COL_ONELINE = "oneLine"
        const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COL_IMAGE} INTEGER, ${COL_TITLE} TEXT, ${COL_POETNAME} TEXT, ${COL_CONTENT} TEXT" +
                    ", ${COL_ONELINE} TEXT, ${COL_DATE} TEXT)"
        Log.d(TAG, CREATE_TABLE)
        db?.execSQL(CREATE_TABLE)

        //sample data
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, 2131165306, '사는 법', '나태주','시 내용', '너를 생각해야만 했다.', '2023-2-05')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, 2131165308, '꽃멀미', '이해인', NULL,'살아 있는 것은 아픈 것', '2023-3-05')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, 2131165307, '너에게 묻는다', '안도현', '시 전체 내용','누구에게 한 번이라도 뜨거운 사람이었느냐', '2023-11-26')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, 2131165306, '꿈', '황인숙', NULL,'가끔 네 꿈을 꾼다.', '2022-1-20')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, 2131165308, '우주를 건너는 법', '박찬일', '달팽이는 움직이지 않는다. 다만 도달할 뿐이다.', '달팽이와 함께!', '2023-4-15')")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        val DROP_TABLE ="DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}