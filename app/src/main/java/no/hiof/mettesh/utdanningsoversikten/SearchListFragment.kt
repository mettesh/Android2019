package no.hiof.mettesh.utdanningsoversikten


import android.graphics.Movie
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_search_list.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education

/**
 * A simple [Fragment] subclass.
 */
class SearchListFragment : Fragment() {

    private var educationlist : ArrayList<Education> = Education.getEducation()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()
    }

    private fun setUpRecycleView() {
        // Set our own adapter to be used in the RecycleView, and sends it the data and creates the OnClickListener
        // With the listener gets called when an item in the list is clicked
        educationRecyclerView.adapter = EducationAdapter(educationlist,
            View.OnClickListener { view ->
                // Gets the position of the item that's clicked
                val position = educationRecyclerView.getChildAdapterPosition(view)

                // Gets the movie based on which item got clicked
                val clickedEducation = educationlist[position]

                // Creates the navigation action, including the id argument

                val action = SearchListFragmentDirections.actionSearchListFragmentToEducationDetailFragment(clickedEducation.id)


                // Calls the navigat action, taking us to the MovieDetailFragment
                findNavController().navigate(action)

                // Creates a toast with the movie that got clicked
                Toast.makeText(view.context, clickedEducation.title + " clicked", Toast.LENGTH_LONG).show();
            })

        // Sets the layoutmanager we want to use
        educationRecyclerView.layoutManager = GridLayoutManager(context, 1)
        //movieRecyclerView.layoutManager = GridLayoutManager(context, 2)
        //movieRecyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        //movieRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        //movieRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //movieRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}
