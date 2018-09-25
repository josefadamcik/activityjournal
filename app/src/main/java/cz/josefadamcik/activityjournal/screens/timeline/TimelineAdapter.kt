package cz.josefadamcik.activityjournal.screens.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.josefadamcik.activityjournal.R

class TimelineAdapter(
        private val layoutInflater: LayoutInflater,
        initialList: List<String>
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    private var list: MutableList<String> = initialList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.timeline_list_item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val parts = item.split(";")
        holder.timeText.text = parts[1]
        holder.titleText.text = parts[0]
    }

    fun updateList(items: List<String>) {
        list = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addItem(title: String) {
        list.add(title)
        notifyItemInserted(list.size - 1)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val timeText : TextView = view.findViewById(R.id.time)
        val titleText : TextView = view.findViewById(R.id.title)
    }
}

