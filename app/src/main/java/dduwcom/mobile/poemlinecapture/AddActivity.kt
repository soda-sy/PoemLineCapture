package dduwcom.mobile.poemlinecapture

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dduwcom.mobile.poemlinecapture.data.PoemLineDBHelper
import dduwcom.mobile.poemlinecapture.data.PoemLineDto
import dduwcom.mobile.poemlinecapture.databinding.ActivityAddBinding
import java.util.*
import kotlin.random.Random

class AddActivity : AppCompatActivity() {
    val TAG = "AddActivity"
    private val imageList = listOf(
        R.drawable.book_01,
        R.drawable.book_02,
        R.drawable.book_03
    )
    //이미지 값 랜덤으로 부여
    val randomIndex = Random.nextInt(imageList.size)
    val randomImage = imageList[randomIndex]

    lateinit var addBinding : ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        addBinding.img.setImageResource(randomImage)

        Log.d(TAG, imageList[randomIndex].toString())

        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        addBinding.dateText.text = "$year-${month+1}-$day"

        addBinding.dateBt.setOnClickListener{
            DatePickerDialog(this@AddActivity, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    addBinding.dateText.text = "$year-${month+1}-$day"
                }
            }, year, month, day).show()
        }

        addBinding.saveBt.setOnClickListener{
            val title = addBinding.title?.text.toString()
            val poetName = addBinding.poetName?.text.toString()
            val content = addBinding.contents?.text.toString()
            val oneLine = addBinding.oneLineText?.text.toString()
            val caldate = addBinding.dateText.text.toString()

            //필수 항목을 채워넣지 않으면 return
            if(title.equals("") || poetName.equals("")|| oneLine.equals("")){
                Toast.makeText(this, "빠진 부분의 내용을 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newDto = PoemLineDto(0, randomImage, title, poetName, content, oneLine, caldate)      // 화면 값으로 dto 생성, id 는 임의의 값 0

            if ( addPoemLine(newDto) > 0) {
                setResult(RESULT_OK)

            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        addBinding.cancelBt.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("저장 취소")
                setMessage("정말로 돌아가겠습니까?")
                setIcon(R.drawable.notify)
                setNegativeButton("취소", null)
                setCancelable(false)
                setPositiveButton("확인", object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                })
                show()
            }
        }
    }

    fun addPoemLine(newDto : PoemLineDto) : Long  {
        val helper = PoemLineDBHelper(this)
        val db = helper.writableDatabase

        val newValues = ContentValues()
        newValues.put(PoemLineDBHelper.COL_IMAGE, newDto.image)
        newValues.put(PoemLineDBHelper.COL_TITLE, newDto.title)
        newValues.put(PoemLineDBHelper.COL_POETNAME, newDto.poetName)
        newValues.put(PoemLineDBHelper.COL_CONTENT, newDto.content)
        newValues.put(PoemLineDBHelper.COL_ONELINE, newDto.oneLine)
        newValues.put(PoemLineDBHelper.COL_DATE, newDto.date)

        val result = db.insert(PoemLineDBHelper.TABLE_NAME, null, newValues)

        helper.close()

        return result
    }
}