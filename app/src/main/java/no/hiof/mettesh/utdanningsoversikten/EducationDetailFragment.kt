package no.hiof.mettesh.utdanningsoversikten

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_education_detail.view.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Henter inn layout for dette Fragmentet
        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Henter ut argumenter som er sendt med fra liste-fragmentet
        val arguments = arguments?.let { EducationDetailFragmentArgs.fromBundle(it) }

        // Henter utdanningen som har den id-en som ble sendt med fra Liste-Fragmentet.
        val education = Education.getEducation()[arguments!!.id]



        //val detailSchoolIconImageView : ImageView = view.detailSchoolIcon
        val detailEducationTitleTextView : TextView = view.detailEducationTitle
        val detailSchoolNameTextView : TextView = view.detailSchoolName
        val detailEducationDescriptionTextView : TextView = view.detailEducationDescription
        val poenggrenseTextView : TextView = view.detailPoenggrense
        var kravkodeTextView : TextView = view.detailKravkode

        // Setter til infoen om valgt utdanning til de ulike elementene


        detailEducationTitleTextView.text = education.title
        detailSchoolNameTextView.text = education.school.schoolTitle
        kravkodeTextView.text = education.kravkode
        poenggrenseTextView.text = education.poenggrense.toString()
        detailEducationDescriptionTextView.text = education.descriptionLong



    }
}
