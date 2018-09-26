package cz.josefadamcik.activityjournal.screens.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord

class TimelineAdapter(
        private val layoutInflater: LayoutInflater,
        initialList: List<ActivityRecord>
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    private var list: MutableList<ActivityRecord> = initialList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.timeline_list_item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.timeText.text = item.time
        holder.titleText.text = item.title
        holder.dateText.text = item.date
    }

    fun updateList(items: List<ActivityRecord>) {
        list = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.time)
        val titleText: TextView = view.findViewById(R.id.title)
        val dateText: TextView = view.findViewById(R.id.date)
    }
}
