package no.hiof.mettesh.utdanningsoversikten.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.education_list_item.view.*
import no.hiof.mettesh.utdanningsoversikten.R
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationAdapter(private val items: ArrayList<Education>, var clickListener: View.OnClickListener) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        // Log so we can see when the onCreateViewHolder method is called
        Log.d("EducatinAdapter", "Creating View")

        // Instansierer riktig xml-fil inn i riktig view som den tilhører
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.education_list_item, parent, false)

        // Create the viewholder with the corresponding view (list item)
        return EducationViewHolder(itemView)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        // Log so we can see when the bind method is called
        Log.d("EducationAdapter", "Binding View $position")

        // Gets the education data we are going to use at the given position
        val currentEducation = items[position]

        // Gives the education data and clickListener to the ViewHolder
        // Makes it fill up the given position with the new data and add the clicklistener to the view
        holder.bind(currentEducation, clickListener)
    }

    class EducationViewHolder (view: View) : RecyclerView.ViewHolder(view) {


        // Gets a reference to all the specific views we are going to use or fill with data
        private val educationPosterImageView : ImageView = view.educationPosterImageView
        private val educationTitleTextView : TextView = view.educationTitleTextView

        fun bind(item: Education, clickListener: View.OnClickListener) {
            // Fills the views with the given data
            educationPosterImageView.setImageResource(item.image)
            educationTitleTextView.text = item.title
            // Sets the onClickListener
            this.itemView.setOnClickListener(clickListener)
        }
    }
}
