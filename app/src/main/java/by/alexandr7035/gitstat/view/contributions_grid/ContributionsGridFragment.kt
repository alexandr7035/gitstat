package by.alexandr7035.gitstat.view.contributions_grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.databinding.FragmentContributionsGridBinding
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.core.WebViewFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContributionsGridFragment : Fragment() {

    private var binding: FragmentContributionsGridBinding? = null
    private val viewModel by viewModels<ContributionsGridViewModel>()
    private val safeArgs by navArgs<ContributionsGridFragmentArgs>()

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
        Timber.debug("set items")

        viewModel.getContributionYearWithMonths(safeArgs.contributionYear).observe(viewLifecycleOwner, {
            Timber.debug("MONTHS $it")
            // Set months list (reversed)
            adapter.setItems(it.contributionMonths.reversed())
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}