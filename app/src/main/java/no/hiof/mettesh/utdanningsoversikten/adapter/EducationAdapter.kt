package no.hiof.mettesh.utdanningsoversikten.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.education_list_item.view.*
import no.hiof.mettesh.utdanningsoversikten.R
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationAdapter(internal var educationList: List<Education>,
                       var clickListener: View.OnClickListener) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    internal var filteredEducationListResult : List<Education> = educationList

    override fun getItemCount(): Int {
        return educationList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.education_list_item, parent, false)
        return EducationViewHolder(itemView)
    }

    // onBindViewHolder kalles når data blir satt til en spesifik viewHolder
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {

        val currentEducation = filteredEducationListResult[position]
        holder.bind(currentEducation, clickListener)
    }

    class EducationViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val schoolIconImageView : ImageView = view.schoolIcon
        private val educationTitleTextView : TextView = view.educationTitle
        private val schoolNameTextView : TextView = view.schoolName
        private val educationImageView : ImageView = view.educationImage
        private val educationShortDescriptionTextView : TextView = view.educationShortDescription
        private val favouriteHeart : ImageView = view.favHeart

        fun bind(item: Education, clickListener: View.OnClickListener) {

            Glide.with(itemView)
                .load(R.drawable.ic_school_teal)
                .centerCrop()
                .placeholder(R.drawable.ic_school_teal)
                .error(R.drawable.ic_school_teal)
                .fallback(R.drawable.ic_school_teal)
                .into(schoolIconImageView)

            educationTitleTextView.text = item.educationTitle

            schoolNameTextView.text = item.school.schoolTitle

            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.andre)
                .error(R.drawable.andre)
                .fallback(R.drawable.andre)
                .into(educationImageView)

            educationShortDescriptionTextView.text = item.descriptionShort


            if(!Education.favouriteEducationlist.contains(item)) {
                favouriteHeart.visibility = View.GONE
            }

            this.itemView.setOnClickListener(clickListener)
        }
    }
}
