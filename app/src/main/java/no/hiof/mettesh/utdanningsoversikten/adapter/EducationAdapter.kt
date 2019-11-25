package no.hiof.mettesh.utdanningsoversikten.adapter

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.education_list_item.view.*
import no.hiof.mettesh.utdanningsoversikten.FirebaseFunctions.Companion.addFavToFirestore
import no.hiof.mettesh.utdanningsoversikten.FirebaseFunctions.Companion.removeFavFromFirestore
import no.hiof.mettesh.utdanningsoversikten.R
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationAdapter(internal var educationList: List<Education>, var clickListener: View.OnClickListener) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    override fun getItemCount(): Int {
        return educationList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.education_list_item, parent, false)
        return EducationViewHolder(itemView)
    }

    // onBindViewHolder kalles når data blir satt til en spesifik viewHolder
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {

        val currentEducation = educationList[position]
        holder.bind(currentEducation, clickListener)

    }

    class EducationViewHolder (view: View) : RecyclerView.ViewHolder(view) {


        private lateinit var firebaseAuth : FirebaseAuth

        private val schoolIconImageView : ImageView = view.schoolIcon
        private val educationTitleTextView : TextView = view.educationTitle
        private val schoolNameTextView : TextView = view.schoolName
        private val educationImageView : ImageView = view.educationImage
        private val educationShortDescriptionTextView : TextView = view.educationShortDescription
        private val favouriteHeart : ImageView = view.favHeart

        fun bind(item: Education, clickListener: View.OnClickListener) {

            firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            Glide.with(itemView).load(R.drawable.ic_school_yellow)
                .centerCrop()
                .placeholder(R.drawable.ic_school_yellow)
                .error(R.drawable.ic_school_yellow)
                .fallback(R.drawable.ic_school_yellow)
                .into(schoolIconImageView)

            educationTitleTextView.text = item.educationTitle

            schoolNameTextView.text = item.school.schoolTitle

            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(educationImageView)

            educationShortDescriptionTextView.text = item.descriptionShort

            // Hjertet skal ikke vises om man ikke er logget inn
            if(currentUser == null) {
                favouriteHeart.visibility = View.GONE
            }

            if(Education.favouriteEducationlist.contains(item)){
                favouriteHeart.setImageResource(R.drawable.ic_favorite_filled)
            }

            favouriteHeart.setOnClickListener {
                if(this.itemView.context.isConnectedToNetwork()){
                    addEducationToFavouriteAndChangeHeart(currentUser, item, favouriteHeart)
                }
                else {
                    showAlertBox("Ingen nettilgang", "Du må være tilkoblet internett for å kunne legge til/fjerne utdanninger", "Ok")
                }
            }

            this.itemView.setOnClickListener(clickListener)
        }

        private fun addEducationToFavouriteAndChangeHeart(firebaseCurrentUser: FirebaseUser?, education: Education, favouriteHeart: ImageView) {

            if (Education.favouriteEducationlist.contains(education)) {
                favouriteHeart.setImageResource(R.drawable.ic_favorite_border)

                // Metode ligger i FirebaseFunctions da den benyttes flere steder
                removeFavFromFirestore(firebaseCurrentUser!!, education)

                showToast("Utdanning fjernet fra favoritter")
            }
            else {
                favouriteHeart.setImageResource(R.drawable.ic_favorite_filled)

                // Metode ligger i FirebaseFunctions da den benyttes flere steder
                addFavToFirestore(firebaseCurrentUser!!, education)

                showToast("Utdanning lagt til i favoritter")
            }
        }

        private fun showToast(text: String) {
            Toast.makeText(
                this.itemView.context,
                text,
                Toast.LENGTH_LONG
            ).show()
        }

        private fun showAlertBox(title: String, message: String, buttonText: String) {
            val alertBox = AlertDialog.Builder(this.itemView.context, R.style.AlertDialogTheme)

            alertBox.setTitle(title)
            alertBox.setMessage(message)

            alertBox.setPositiveButton(buttonText) { dialog, which ->

            }
            val alert = alertBox.create()
            alert.show()
        }

        private fun Context.isConnectedToNetwork(): Boolean {
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
        }

    }
}
