package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionTypesBinding
import by.alexandr7035.gitstat.extensions.setPieChartData
import by.alexandr7035.gitstat.extensions.setupPieChartView
import by.alexandr7035.gitstat.view.contributions.ContributionsViewModel
import by.alexandr7035.gitstat.view.contributions.plots.contributions_types.model.ContributionTypesListToRecyclerItemsMapper
import by.alexandr7035.gitstat.view.repositories.plots.languages_plot.PieDataValueFormatter
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentContributionTypes : Fragment() {

    private var binding: FragmentContributionTypesBinding? = null
    private val viewModel by viewModels<ContributionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionTypesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = TypesAdapter(showCount = true)
        binding?.typesRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.typesRecycler?.adapter = adapter

        viewModel.getContributionTypesLiveData().observe(viewLifecycleOwner, { typesData ->
            // Detect max value
            // FIXME find better solution
            val commits = typesData.sumOf { it.commitContributions }
            val issues = typesData.sumOf { it.issueContributions }
            val pullRequests = typesData.sumOf { it.pullRequestContributions }
            val reviews = typesData.sumOf { it.pullRequestReviewContributions }
            val repositories = typesData.sumOf { it.repositoryContributions }
            val unknown = typesData.sumOf { it.unknownContributions }

            val total = typesData.sumOf { it.totalContributions }

            val entries = listOf<PieEntry>(
                PieEntry(commits.toFloat(), "Commits"),
                PieEntry(issues.toFloat(), "Issues"),
                PieEntry(pullRequests.toFloat(), "Pull requests"),
                PieEntry(reviews.toFloat(), "Code reviews"),
                PieEntry(repositories.toFloat(), "Repositories"),
                PieEntry(unknown.toFloat(), "Unknown")
            )

            val dataSet = PieDataSet(entries, "")

            val colors = listOf<Int>(
                ContextCompat.getColor(requireContext(), R.color.color_commits),
                ContextCompat.getColor(requireContext(), R.color.color_issues),
                ContextCompat.getColor(requireContext(), R.color.color_pull_requests),
                ContextCompat.getColor(requireContext(), R.color.color_code_reviews),
                ContextCompat.getColor(requireContext(), R.color.color_repositories),
                ContextCompat.getColor(requireContext(), R.color.color_unknown_contributions),
            )

            dataSet.colors = colors
            dataSet.setDrawValues(false)

            binding?.typesChart?.setupPieChartView(isLegendEnabled = false)
            binding?.typesChart?.setPieChartData(dataSet, PieDataValueFormatter())
            binding?.typesChart?.centerText = total.toString()

            binding?.typesChart?.invalidate()

            adapter.setItems(ContributionTypesListToRecyclerItemsMapper.map(typesData, requireContext()))
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}