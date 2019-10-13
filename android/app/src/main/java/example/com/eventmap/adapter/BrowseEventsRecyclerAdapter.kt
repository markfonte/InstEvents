package example.com.eventmap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import example.com.eventmap.R
import example.com.eventmap.util.EventInfo

class BrowseEventsRecyclerAdapter(private val eventInfos: ArrayList<EventInfo>) :
    RecyclerView.Adapter<BrowseEventsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.event_title)
        val description: TextView = v.findViewById(R.id.event_description)
        val location: TextView = v.findViewById(R.id.event_location)
        val rowContainer: ConstraintLayout = v.findViewById(R.id.event_row_container)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_event,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = eventInfos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = eventInfos[position].Title
        holder.description.text = eventInfos[position].Description
        holder.location.text = eventInfos[position].Location
        holder.rowContainer.setOnClickListener {
            // TODO: Do something if event is clicked
        }
    }

}