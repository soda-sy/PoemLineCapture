package dduwcom.mobile.poemlinecapture

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dduwcom.mobile.poemlinecapture.data.PoemLineDBHelper
import dduwcom.mobile.poemlinecapture.data.PoemLineDto
import dduwcom.mobile.poemlinecapture.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val REQ_ADD = 1
    val REQ_UPDATE = 2
    val REQ_DELETE = 3

    lateinit var adapter : PoemLineAdapter
    lateinit var binding : ActivityMainBinding
    lateinit var poemLines : ArrayList<PoemLineDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        poemLines = getAllPoemLines()
        adapter = PoemLineAdapter(poemLines)
        binding.rvPoemLine.adapter = adapter

        binding.rvPoemLine.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        adapter.setOnItemClickListener(object : PoemLineAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("dto", poemLines.get(position))
                startActivityForResult(intent, REQ_UPDATE)
            }
        })

        val onLongClickListener = object: PoemLineAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                AlertDialog.Builder(this@MainActivity).run {
                    setTitle("글귀 삭제")
                    setMessage("${poemLines[position].title} 삭제하겠습니까?")
                    setNegativeButton("취소", null)
                    setCancelable(false)
                    setPositiveButton("확인", object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            if ( deletePoemLine(poemLines.get(position).id) > 0) {
                                refreshList(RESULT_OK)
                            }
                        }
                    })
                    show()
                }
            }
        }
        adapter.setOnItemLongClickListener(onLongClickListener)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.poemAdd -> {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                startActivityForResult(intent, REQ_ADD)
            }
            R.id.developerInfo -> {
                val intent = Intent(this@MainActivity, DevProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.close -> {
                AlertDialog.Builder(this@MainActivity).run {
                    setTitle("앱 종료")
                    setMessage("정말로 종료하겠습니까?")
                    setIcon(R.drawable.notify)
                    setNegativeButton("취소", null)
                    setCancelable(false)
                    setPositiveButton("확인", object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            finish()
                        }
                    })
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_UPDATE -> {
                refreshList(resultCode)
            }
            REQ_ADD -> {
                refreshList(resultCode)
            }
        }
    }

    private fun refreshList(resultCode: Int) {
        if (resultCode == RESULT_OK) {
            poemLines.clear()
            poemLines.addAll(getAllPoemLines())
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this@MainActivity, "취소됐습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deletePoemLine(id: Long) : Int {
        val helper = PoemLineDBHelper(this)
        val db = helper.writableDatabase

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(id.toString())

        val result = db.delete(PoemLineDBHelper.TABLE_NAME, whereClause, whereArgs)

        helper.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllPoemLines() : ArrayList<PoemLineDto>{
        val helper = PoemLineDBHelper(this)
        val db = helper.readableDatabase
        val cursor = db.query(PoemLineDBHelper.TABLE_NAME, null,null,null,null,null,null)
        val poemLines = arrayListOf<PoemLineDto>()

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
        cursor.close()
        helper.close()
        return poemLines
    }
}