package no.hiof.mettesh.utdanningsoversikten.Fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_education_detail.view.*
import no.hiof.mettesh.utdanningsoversikten.Utils.FirebaseFunctions
import no.hiof.mettesh.utdanningsoversikten.Models.Education
import no.hiof.mettesh.utdanningsoversikten.R

class EducationDetailFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var detailEducationTitleTextView: TextView
    private lateinit var detailSchoolNameTextView: TextView
    private lateinit var sendToWebImgView: ImageView
    private lateinit var studyImage : ImageView
    private lateinit var pointsRequiredTextView: TextView
    private lateinit var favButton: Button
    private lateinit var schoolUrl: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()

        val arguments = arguments?.let {
            EducationDetailFragmentArgs.fromBundle(it)
        }
        val education = Education.educationlist.find { it.id == arguments!!.id }
        val firebaseCurrentUser = firebaseAuth.currentUser

        setViewContentAndLogic(view, education!!, firebaseCurrentUser)
    }

    private fun setViewContentAndLogic(view: View, education: Education, firebaseCurrentUser: FirebaseUser?) {

        detailEducationTitleTextView = view.detailEducationTitle
        detailSchoolNameTextView = view.detailSchoolName
        sendToWebImgView = view.sendToWebImgView
        studyImage = view.studyImage
        pointsRequiredTextView = view.pointsRequiredTextView
        favButton = view.floatingButton_fav
        schoolUrl = education.school.webPage

        Glide.with(view)
            .load(education.image)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(studyImage)

        detailEducationTitleTextView.text = education.educationTitle
        detailSchoolNameTextView.text = education.school.schoolTitle
        pointsRequiredTextView.text = education.pointsRequired.toString()

        if (Education.favouriteEducationlist.contains(education)) {
            favButton.text = "Fjern fra favoritter"
        } else {
            favButton.text = "Legg til i favoritter"
        }

        detailSchoolNameTextView.setOnClickListener { openWebBroser(schoolUrl) }
        sendToWebImgView.setOnClickListener { openWebBroser(schoolUrl) }
        favButton.setOnClickListener {
            if(context!!.isConnectedToNetwork()){
                addEducationToFavouriteAndChangeButtonText(firebaseCurrentUser, education, favButton)
            }
            else {
                showToast("Du må ha internettilkobling for å kunne legge til/fjerne utdanninger")
            }
        }
    }

    private fun addEducationToFavouriteAndChangeButtonText(firebaseCurrentUser: FirebaseUser?, education: Education, favButton: Button) {
        if (firebaseCurrentUser == null) {

            showToast("Du må være innlogget for å kunne legge til/fjerne utdanninger")

        } else {

            if (Education.favouriteEducationlist.contains(education)) {

                FirebaseFunctions.removeFavFromFirestore(
                    firebaseCurrentUser,
                    education
                )

                favButton.text = "Legg til i favoritter"

                showToast(education.educationTitle + " er fjernet fra favoritter")

            } else {

                FirebaseFunctions.addFavToFirestore(
                    firebaseCurrentUser,
                    education
                )

                favButton.text = "Fjern fra favoritter"

                showToast(education.educationTitle + " er lagt til i favoritter")

            }
        }
    }

    fun openWebBroser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }
}
