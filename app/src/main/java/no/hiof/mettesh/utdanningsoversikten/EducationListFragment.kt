package no.hiof.mettesh.utdanningsoversikten

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_education_list.*
import kotlinx.android.synthetic.main.fragment_education_list.view.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education


class EducationListFragment : Fragment() {

    // Henter inn liste med utdanninger fra Education-klassen
    private var educationList : ArrayList<Education> = Education.educationlist


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Henter inn layout for dette Fragmentet
        return inflater.inflate(R.layout.fragment_education_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        val loginOrEmptylistTextview : TextView = view.textView_login_or_empty
        val loginButton : Button = view.login_button
        val openFilterFloatingButton : FloatingActionButton = view.openFilterFloatingButton


        // Skjuler disse da de kun brukes for favouriteFragment (Og samme layout benyttes)
        loginOrEmptylistTextview.visibility = View.GONE
        loginButton.visibility = View.GONE

        openFilterFloatingButton.setOnClickListener {
            viewBottomSheet()
        }
        setUpRecycleView()
    }



    private fun setUpRecycleView() {

            // Bruker adapteren for å binde layout og RecyclerView
        educationRecyclerView.adapter = EducationAdapter(educationList, View.OnClickListener { view ->


                // Får posisjonen til elementet i lista som er trykket på
                val position = educationRecyclerView.getChildAdapterPosition(view)

                // Får deretter riktig utdannning paserte på denne posisjonen
                val clickedEducation = educationList[position]

                // Oppretter navigasjonen (utifra nav_graph.xml) og sender med id til utdanningen.
                val action = EducationListFragmentDirections.actionEducationListFragmentToEducationDetailFragment(clickedEducation.id)

                // Navigerer til EducationDetailFragment
                findNavController().navigate(action)


            })

        // Etter at alt er satt setter vi dette RecyclerView med det layouten vi ønsker.
        educationRecyclerView.layoutManager = GridLayoutManager(context, 1)

    }

    private fun viewBottomSheet() {

        //Sender med contexten, som jeg evt er opprettet. Derav !!
        val dialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
        dialog.setContentView(view)
        dialog.show()

        fillSpinners(view)

    }

    private fun fillSpinners(view : View) {
        val spinnerLevel : Spinner = view.spinnerLevel
        val spinnerStudyField : Spinner = view.spinnerFieldStudy
        val spinnerPlace : Spinner = view.spinnerPlace


        val levelList = arrayOf("Nivå", "Årsstudium", "Bachelor", "Master")
        val studyField = arrayOf("Fagområde", "Helse", "Historie", "Håndverk", "Idrett", "Kjemi", "Kultur")
        val levelPlace = arrayOf("Sted", "Oslo", "Halden", "Fredrikstad")


        val levelAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelList)
        spinnerLevel.adapter = levelAdapter

        val fieldAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, studyField)
        spinnerStudyField.adapter = fieldAdapter

        val placeAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelPlace)
        spinnerPlace.adapter = placeAdapter

/*        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }*/



        // Fylle lister!
    }
}
