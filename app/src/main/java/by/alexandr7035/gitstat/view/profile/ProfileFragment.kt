package by.alexandr7035.gitstat.view.profile

import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentProfileBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.profile.highlights_bar.HighlightItem
import by.alexandr7035.gitstat.view.profile.highlights_bar.HighlightsAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar.YEAR
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val highlightsAdapter = HighlightsAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val decoration = DividerItemDecoration(
            binding.highlightRecycler.context,
            layoutManager.orientation
        )

        ContextCompat.getDrawable(requireContext(), R.drawable.decoration_repository_item_horizontal)?.let {
            decoration.setDrawable(it)
        }

        binding.highlightRecycler.adapter = highlightsAdapter
        binding.highlightRecycler.addItemDecoration(decoration)
        binding.highlightRecycler.layoutManager = layoutManager

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

            val highlights = listOf(
                HighlightItem(
                    metricValue = it.followers.toString(),
                    metricText = getString(R.string.followers).lowercase(),
                    accentColor = R.color.brand_color_1
                ),
                HighlightItem(
                    metricValue = it.total_repos_count.toString(),
                    metricText = getString(R.string.repos).lowercase(),
                    accentColor = R.color.brand_color_2
                ),
                HighlightItem(
                    metricValue = it.followers.toString(),
                    metricText = getString(R.string.followers).lowercase(),
                    accentColor = R.color.brand_color_3
                ),
                HighlightItem(
                    metricValue = it.followers.toString(),
                    metricText = getString(R.string.followers).lowercase(),
                    accentColor = R.color.brand_color_4
                ),
            )

            highlightsAdapter.setItems(highlights)
        }

        binding.reposStatDetailedBtn.setOnClickListener {
            findNavController().navigateSafe(ProfileFragmentDirections.actionProfileFragmentToReposOverviewFragment())
        }

        binding.drawerBtn.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

    }

    private fun setLocationVisibility(isVisible: Boolean) {
        binding.locationIcon.isGone = !isVisible
        binding.locationLabel.isGone = !isVisible
        binding.locationView.isGone = !isVisible
    }
}