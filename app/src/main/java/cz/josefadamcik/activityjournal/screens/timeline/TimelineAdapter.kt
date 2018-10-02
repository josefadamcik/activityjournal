package cz.josefadamcik.activityjournal.screens.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.josefadamcik.activityjournal.R
import cz.josefadamcik.activityjournal.model.ActivityRecord
import cz.josefadamcik.activityjournal.model.ActivityRecordDuration
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder

class TimelineAdapter(
    private val layoutInflater: LayoutInflater,
    initialList: List<ActivityRecord>
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    private var list: MutableList<ActivityRecord> = initialList.toMutableList()
    private val timeFormatter  = DateTimeFormatterBuilder().appendPattern("H:mm").toFormatter()
    private val dateFormatter  = DateTimeFormatterBuilder().appendPattern("d.M.YYYY").toFormatter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.timeline_list_item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.timeText.text = timeFormatter.format(item.start)
        holder.titleText.text = item.title
        holder.dateText.text = dateFormatter.format(item.start)
        holder.durationText.text = when (item.duration) {
            is ActivityRecordDuration.Undergoing -> holder.itemView.resources.getString(R.string.activity_undergoing)
            is ActivityRecordDuration.Done -> item.duration.minutes.toString()
        }
    }

    fun updateList(items: List<ActivityRecord>) {
        list = items.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.time)
        val titleText: TextView = view.findViewById(R.id.title)
        val dateText: TextView = view.findViewById(R.id.date)
        val durationText: TextView = view.findViewById(R.id.duration)
    }
}
