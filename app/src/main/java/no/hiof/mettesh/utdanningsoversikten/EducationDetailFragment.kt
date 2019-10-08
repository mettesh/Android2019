package no.hiof.mettesh.utdanningsoversikten

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_education_detail.view.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationDetailFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Henter inn layout for dette Fragmentet
        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Henter ut argumenter som er sendt med fra liste-fragmentet
        val arguments = arguments?.let { EducationDetailFragmentArgs.fromBundle(it) }

        // Henter utdanningen som har den id-en som ble sendt med fra Liste-Fragmentet.
        val education = Education.educationlist[arguments!!.id]

        // Henter info om eventuell innlogged bruker
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseCurrentUser = firebaseAuth.currentUser


        //val detailSchoolIconImageView : ImageView = view.detailSchoolIcon
        val detailEducationTitleTextView : TextView = view.detailEducationTitle
        val detailSchoolNameTextView : TextView = view.detailSchoolName
        val detailEducationDescriptionTextView : TextView = view.detailEducationDescription
        val poenggrenseTextView : TextView = view.detailPoenggrense
        val kravkodeTextView : TextView = view.detailKravkode
        val favFloatingButton : FloatingActionButton = view.floatingButton_fav

        // Setter til infoen om valgt utdanning til de ulike elementene


        detailEducationTitleTextView.text = education.title
        detailSchoolNameTextView.text = education.school.schoolTitle
        kravkodeTextView.text = education.kravkode
        poenggrenseTextView.text = education.poenggrense.toString()
        detailEducationDescriptionTextView.text = education.descriptionLong


        // If utdanning ligger i lista =
                // Fylt hjerte
                // OnClick vil fjerne utdanning fra liste
        // Om ikke
                // ikke fylt hjerte
                // onClick vil legge til utdanning til liste

        favFloatingButton.setOnClickListener {

            if (firebaseCurrentUser == null) {
                Toast.makeText(
                    context,
                    "Du må være innlogget for å kunne legge til utdanninger i favoritter",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Legger til denne Education i favorittliste
                Education.favouriteEducationlist.add(education)
                Toast.makeText(
                    context,
                    education.title + " er lagt til i favoritter",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
