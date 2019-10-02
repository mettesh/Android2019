package no.hiof.mettesh.utdanningsoversikten

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.education_list_item.educationPosterImageView
import kotlinx.android.synthetic.main.education_list_item.educationTitleTextView
import kotlinx.android.synthetic.main.fragment_education_detail.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

/**
 * A simple [Fragment] subclass.
 */
class EducationDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retreives the arguments from the bundle (does some null-check)

        val arguments = arguments?.let { EducationDetailFragmentArgs.fromBundle(it) }

        // Gets the movie with the id (just a number in the list in this case)
        val education = Education.getEducation()[arguments!!.id]

        // Filles up the views with the movie-information
        educationTitleTextView.text = education.title
        educationPosterImageView.setImageResource(education.image)
        educationDescriptionTextView.text = education.description



    }
}
