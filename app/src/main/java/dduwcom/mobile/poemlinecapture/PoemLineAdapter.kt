package dduwcom.mobile.poemlinecapture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dduwcom.mobile.poemlinecapture.data.PoemLineDto
import dduwcom.mobile.poemlinecapture.databinding.ListItemBinding


class PoemLineAdapter (val poemLines : ArrayList<PoemLineDto>)
    : RecyclerView.Adapter<PoemLineAdapter.PoemLineViewHolder>() {
    val TAG = "PoemLineAdapter"

    class PoemLineViewHolder(
        val itemBinding: ListItemBinding, listener: OnItemClickListener?,
        lcListener: OnItemLongClickListener?
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        val title = itemBinding.title
        val image = itemBinding.image
        val oneLine = itemBinding.oneLineText
        val date = itemBinding.date

        init {
            /*list_item 의 root 항목(ConstraintLayout) 클릭 시*/
            itemBinding.root.setOnClickListener {
                listener?.onItemClick(it, adapterPosition)  // adapterPosition 은 클릭 위치 index
            }

            /*list_item 의 root 항목(ConstraintLayout) 롱클릭 시*/
            itemBinding.root.setOnLongClickListener {
                lcListener?.onItemLongClick(it, adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemLineViewHolder {
        val itemBinding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoemLineViewHolder(itemBinding, listener, lcListener)
    }

    override fun getItemCount(): Int {
        return poemLines.size
    }

    override fun onBindViewHolder(holder: PoemLineViewHolder, position: Int) {
        holder.image.setImageResource(poemLines[position].image)
        holder.title.text = poemLines[position].title.toString()
        holder.oneLine.text = poemLines[position].oneLine.toString()
        holder.date.text = poemLines[position].date.toString()
    }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    var lcListener: OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.lcListener = listener
    }
}