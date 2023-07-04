package dduwcom.mobile.poemlinecapture

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dduwcom.mobile.poemlinecapture.data.PoemLineDBHelper
import dduwcom.mobile.poemlinecapture.data.PoemLineDto
import dduwcom.mobile.poemlinecapture.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var adapter : PoemLineAdapter
    lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //버튼 클릭시, poemLines에 찾는 제목에 해당하는 Dao 불러오기
        binding.searchBt.setOnClickListener{
            val findTitle = binding.searchEt.text.toString()

            var poemLines : ArrayList<PoemLineDto> = getAllPoemLines(findTitle)
            adapter = PoemLineAdapter(poemLines)
            binding.rvPoemLine.adapter = adapter

            binding.rvPoemLine.layoutManager = LinearLayoutManager(this).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            //size가 0이면 사용자에게 해당 제목이 없다는 것을 알리기
            if(poemLines.size == 0)
                Toast.makeText(this, "${binding.searchEt.text.toString()}의 제목은 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("Range")
    fun getAllPoemLines(findText : String) : ArrayList<PoemLineDto>{
        val helper = PoemLineDBHelper(this)
        val db = helper.readableDatabase
        val cursor = db.query(PoemLineDBHelper.TABLE_NAME, null,null,null,null,null,null)
        val poemLines = arrayListOf<PoemLineDto>()

        with(cursor){
            while(moveToNext()){
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val image = getInt(getColumnIndex(PoemLineDBHelper.COL_IMAGE))
                val title = getString(getColumnIndex(PoemLineDBHelper.COL_TITLE))
                val poetName = getString(getColumnIndex(PoemLineDBHelper.COL_POETNAME))
                val content = getString(getColumnIndex(PoemLineDBHelper.COL_CONTENT))
                val oneLine = getString(getColumnIndex(PoemLineDBHelper.COL_ONELINE))
                val date = getString(getColumnIndex(PoemLineDBHelper.COL_DATE))

                //해당하는 제목을 가진 dto만 저장
                val dto = if (findText.equals(title))
                    PoemLineDto(id, image, title, poetName, content, oneLine, date)
                else
                    null // 또는 기본값 등으로 대체

                if (dto != null) {
                    poemLines.add(dto)
                }
            }
        }
        cursor.close()
        helper.close()
        return poemLines
    }
}