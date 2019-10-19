package no.hiof.mettesh.utdanningsoversikten.adapter

import android.content.Context
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
                       var clickListener: View.OnClickListener) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>(), Filterable {

    internal var filteredEducationListResult : List<Education> = educationList


    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch : String = charString.toString()
                if(charSearch.isEmpty()){
                    filteredEducationListResult = educationList
                } else {

                    val resultList = ArrayList<Education>()

                    for(row : Education in educationList){
                        if(educationContainsString(row, charSearch)) {
                            resultList.add(row)
                        }
                    }
                    filteredEducationListResult = resultList
                }
                val filterResults : FilterResults = Filter.FilterResults()
                filterResults.values = filteredEducationListResult

                return filterResults

            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredEducationListResult = filterResults!!.values as List<Education>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredEducationListResult.size
    }

    // onCreatedViewHolder kalles når det trengs et nytt element i listen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {

        // Instansierer riktig xml-fil inn i riktig view som den tilhører
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.education_list_item, parent, false)

        // Oppretter viewHolderen med riktig view.
        return EducationViewHolder(itemView)
    }

    // onBindViewHolder kalles når data blir satt til en spesifik viewHolder
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {

        // Henter utdanning basert på id sendt med.
        val currentEducation = filteredEducationListResult[position]

        // Fyller opp current element med data.
        holder.bind(currentEducation, clickListener)
    }

    class EducationViewHolder (view: View) : RecyclerView.ViewHolder(view) {


        // Henter inn en referanse til alle view-ene vi skal fylle med data:
        private val schoolIconImageView : ImageView = view.schoolIcon
        private val educationTitleTextView : TextView = view.educationTitle
        private val schoolNameTextView : TextView = view.schoolName
        private val educationImageView : ImageView = view.educationImage
        private val educationShortDescriptionTextView : TextView = view.educationShortDescription
        private val favouriteHeart : ImageView = view.favHeart

        fun bind(item: Education, clickListener: View.OnClickListener) {

            Glide.with(itemView)
                .load(item.school.schoolIcon)
                .centerCrop()
                .placeholder(R.drawable.hiof_icon_background)
                .error(R.drawable.hiof_icon_background)
                .fallback(R.drawable.hiof_icon_background)
                .into(schoolIconImageView)

            educationTitleTextView.text = item.title

            schoolNameTextView.text = item.school.schoolTitle

            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.hiof_icon_background)
                .error(R.drawable.hiof_icon_background)
                .fallback(R.drawable.hiof_icon_background)
                .into(educationImageView)

            educationShortDescriptionTextView.text = item.descriptionShort


            if(!Education.favouriteEducationlist.contains(item)) {
                favouriteHeart.visibility = View.GONE
            }

            this.itemView.setOnClickListener(clickListener)
        }
    }


    fun educationContainsString(row : Education, charSearch : String): Boolean {
        return row.title.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.school.schoolTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.descriptionLong.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.descriptionShort.toLowerCase().contains(charSearch.toLowerCase())
    }
}
