package dduwcom.mobile.poemlinecapture

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dduwcom.mobile.poemlinecapture.data.PoemLineDBHelper
import dduwcom.mobile.poemlinecapture.data.PoemLineDto
import dduwcom.mobile.poemlinecapture.databinding.ActivityUpdateBinding
import java.util.*

class UpdateActivity : AppCompatActivity() {
    lateinit var updateBinding : ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        val dto = intent.getSerializableExtra("dto") as PoemLineDto
        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        updateBinding.updImg.setImageResource(dto.image)
        updateBinding.updTitle.setText(dto.title.toString())
        updateBinding.updPoetName.setText(dto.poetName.toString())
        updateBinding.updOneLineText.setText(dto.oneLine.toString())
        updateBinding.updContents.setText(dto.content.toString())
        updateBinding.updDateText.setText(dto.date.toString())

        updateBinding.updDateBt.setOnClickListener{
            DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                    updateBinding.updDateText.text = "$year-${month+1}-$day"
                }
            }, year, month, day).show()
        }

        updateBinding.updSaveBt.setOnClickListener{
            dto.title = updateBinding.updTitle.text.toString()
            dto.poetName = updateBinding.updPoetName.text.toString()
            dto.oneLine = updateBinding.updOneLineText.text.toString()
            dto.content = updateBinding.updContents.text.toString()
            dto.date = updateBinding.updDateText.text.toString()

            if (updatePoemLine(dto) > 0) {
                setResult(RESULT_OK)
                Toast.makeText(this, "수정 완료됐습니다.", Toast.LENGTH_SHORT).show()
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        updateBinding.updCancelBt.setOnClickListener{
            AlertDialog.Builder(this).run {
                setTitle("수정 취소")
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

    fun updatePoemLine(dto: PoemLineDto): Int {
        val helper = PoemLineDBHelper(this)
        val db = helper.writableDatabase
        val updateValue = ContentValues()

        updateValue.put(PoemLineDBHelper.COL_IMAGE, dto.image)
        updateValue.put(PoemLineDBHelper.COL_TITLE, dto.title)
        updateValue.put(PoemLineDBHelper.COL_POETNAME, dto.poetName)
        updateValue.put(PoemLineDBHelper.COL_ONELINE, dto.oneLine)
        updateValue.put(PoemLineDBHelper.COL_CONTENT, dto.content)
        updateValue.put(PoemLineDBHelper.COL_DATE, dto.date)

        val whereCaluse = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(dto.id.toString())

        val result =  db.update(
            PoemLineDBHelper.TABLE_NAME,
            updateValue, whereCaluse, whereArgs)

        helper.close()

        return result
    }
}