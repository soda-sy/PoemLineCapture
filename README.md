# PoemLineCapture_시인의 시를 기록하고 영감을 담는 앱
---
## 앱 제작 동기
---
* 사용자들이 선택한 시를 손쉽게 추가하고 편리하게 관리
* 개인적인 시집을 통해 자신만의 시적 세계 구성
* 자신의 즐겨찾는 시들을 손쉽게 보관하고 영감을 담아낼 수 있는 기능을 제공
---
## ENG) The motivation for developing an app
---
* Easy and convenient addition and management of user-selected poems.
* Building one's own poetic world through a personal collection of poems.
* Providing functionality to easily store and capture inspiration from favorite poems.
---

## 앱 제작
---
* SQLite를 활용한 DB 접근 & 관리
  
  > SQLiteOpenHelper를 사용해 TABLE 생성과 삭제
  > 
  > 각 Activity에서 helper를 사용해 데이터 삭제, 수정, 추가, 읽기 가능
  ```
  CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COL_IMAGE} INTEGER, ${COL_TITLE} TEXT, ${COL_POETNAME} TEXT, ${COL_CONTENT} TEXT" +
                    ", ${COL_ONELINE} TEXT, ${COL_DATE} TEXT)"

  DROP_TABLE ="DROP TABLE IF EXISTS $TABLE_NAME"
  ```
* RecyclerView를 활용해 실시간으로 DB 업데이트 & 수정 기능
  
  > adapter & adapterView 생성 후 recyclerView에 DB 연결 후 사용
  >
  > helper.ReadableDatabase의 cursor 기능을 사용해 dao에 저장
  ```
   with(cursor){
            while(moveToNext()){
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val image = getInt(getColumnIndex(PoemLineDBHelper.COL_IMAGE))
                Log.d("tt", "${image}")
                val title = getString(getColumnIndex(PoemLineDBHelper.COL_TITLE))
                val poetName = getString(getColumnIndex(PoemLineDBHelper.COL_POETNAME))
                val content = getString(getColumnIndex(PoemLineDBHelper.COL_CONTENT))
                val oneLine = getString(getColumnIndex(PoemLineDBHelper.COL_ONELINE))
                val date = getString(getColumnIndex(PoemLineDBHelper.COL_DATE))
                val dto = PoemLineDto(id, image, title, poetName, content, oneLine, date)

                poemLines.add(dto)
            }
        }
  ```
* Dao & Dto를 만들어 DB 접근 객체를 따로 구성
  
    >Dto - 다양한 타입의 데이터 교환 가능 객체
    ```
    PoemLineDto(val id : Long, var image : Int, var title : String?, var poetName : String?,
                  var content : String?, var oneLine : String?, var date : String?) : Serializable
    ```
* JetPack의 다양한 도구와 라이브러리 사용

  >  DatePickerDialog를 사용해 날짜 관리
  ```
  DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    updateBinding.updDateText.text = "$year-${month+1}-$day"
                }
            }, year, month, day).show()
  ```
