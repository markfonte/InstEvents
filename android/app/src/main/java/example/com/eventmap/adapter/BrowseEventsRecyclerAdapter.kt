package example.com.eventmap.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.com.eventmap.MapsActivity
import example.com.eventmap.R
import example.com.eventmap.util.EventInfo

class BrowseEventsRecyclerAdapter(private val eventInfos: ArrayList<EventInfo>, private val activity: MapsActivity) :
    RecyclerView.Adapter<BrowseEventsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.event_title)
        val description: TextView = v.findViewById(R.id.event_description)
        val location: TextView = v.findViewById(R.id.event_location)
        val dates: TextView = v.findViewById(R.id.event_dates)
        val image: ImageView = v.findViewById(R.id.event_image)
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


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = eventInfos[position].Title
        holder.description.text = eventInfos[position].Description
        holder.location.text = eventInfos[position].Location
        holder.dates.text = eventInfos[position].StartDate +  " - "  + eventInfos[position].EndDate

        if (eventInfos[position].ImageURL != "") {
            Glide.with(activity)
                .load(eventInfos[position].ImageURL)
                .into(holder.image)
        }
        holder.rowContainer.setOnClickListener {
            // TODO: Do something if event is clicked
        }
    }

}