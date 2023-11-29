package by.alexandr7035.gitstat.view.profile

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentProfileBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.contributions_grid.DaysAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update profile data
        viewModel.getUserLiveData().observeNullSafe(viewLifecycleOwner) {

            Picasso.get().load(it.avatar_url).into(binding.profileImageView)

            // This field can be empty
            if (it.name.isEmpty()) {
                binding.nameView.visibility = View.GONE
            } else {
                binding.nameView.visibility = View.VISIBLE
                binding.nameView.text = it.name
            }

            binding.loginView.text = getString(R.string.login_string, it.login)

            binding.idView.text = it.id.toString()

            val dateFormat = getString(R.string.profile_date_format)
            binding.createdView.text = DateFormat.format(dateFormat, it.created_at)
            binding.updatedView.text = DateFormat.format(dateFormat, it.updated_at)

            binding.followersView.text = it.followers.toString()

            // This field can be empty
            if (it.location.isEmpty()) {
                setLocationVisibility(false)
            } else {
                setLocationVisibility(true)
                binding.locationView.text = it.location
            }

            binding.totalReposView.text = it.total_repos_count.toString()
            binding.privateReposView.text = it.private_repos_count.toString()
            binding.publicReposView.text = it.public_repos_count.toString()
        }

        binding.reposStatDetailedBtn.setOnClickListener {
            findNavController().navigateSafe(ProfileFragmentDirections.actionProfileFragmentToReposOverviewFragment())
        }

        binding.drawerBtn.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

        setupContributionsGrid()
    }

    private fun setLocationVisibility(isVisible: Boolean) {
        binding.locationIcon.isGone = !isVisible
        binding.locationLabel.isGone = !isVisible
        binding.locationView.isGone = !isVisible
    }

    private fun setupContributionsGrid() {
        val adapter = DaysAdapter(onDayClick = { contributionDay ->
            findNavController().navigateSafe(
                ProfileFragmentDirections.actionProfileFragmentToContributionDayDialogFragment(
                    contributionDay.count,
                    contributionDay.date,
                    contributionDay.color
                )
            )
        })

        viewModel.loadRecentYearContributions().observeNullSafe(viewLifecycleOwner) {
            val recentYear = it.lastOrNull()?.contributionMonths

            // Get current month number
            val monthFormat = SimpleDateFormat("MM", Locale.US)
            val currentMonth = monthFormat.format(System.currentTimeMillis()).toInt()

            val recentMonth = recentYear?.getOrNull(currentMonth-1)

            if (recentMonth != null) {
                binding.recentMonthContributions.root.isVisible = true

                binding.recentMonthContributions.cellsRecycler.adapter = adapter
                binding.recentMonthContributions.cellsRecycler.layoutManager = GridLayoutManager(requireContext(), 7)
                binding.recentMonthContributions.monthCardTitle.text = getString(R.string.recent_month)
                binding.recentMonthContributions.monthCardTotalContributions.text = recentMonth.contributionDays.sumOf { day ->
                    day.count
                }.toString()

                adapter.setItems(recentMonth.contributionDays)
            }
            else {
                binding.recentMonthContributions.root.isVisible = false
            }
        }
    }
}