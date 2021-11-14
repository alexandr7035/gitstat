package by.alexandr7035.gitstat.view.contributions_grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.databinding.FragmentContributionsGridBinding
import by.alexandr7035.gitstat.extensions.debug
import timber.log.Timber

class ContributionsGridFragment : Fragment() {

    private var binding: FragmentContributionsGridBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionsGridBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = MonthsAdapter()

        binding?.monthRecycler?.adapter = adapter
        binding?.monthRecycler?.layoutManager = LinearLayoutManager(requireContext())
        adapter.setItems(listOf("Mar 2020", "Apr 2020"))
        Timber.debug("set items")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}