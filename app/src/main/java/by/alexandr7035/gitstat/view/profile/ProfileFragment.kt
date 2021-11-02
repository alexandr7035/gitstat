package by.alexandr7035.gitstat.view.profile

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation controller
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Update profile data
        viewModel.getUserLiveData().observe(viewLifecycleOwner, {

            if (it != null) {

                // Show if were hidden previously
                showProfileViews()

                Picasso.get().load(it.avatar_url).into(binding!!.profileImageView)

                binding!!.nameView.text = it.name
                binding!!.loginView.text = it.login

                binding!!.idView.text = it.id.toString()

                binding!!.createdView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.created_at)
                binding!!.updatedView.text = DateFormat.format("dd.MM.yyyy HH:mm", it.updated_at)

                binding!!.followersView.text = it.followers.toString()
                binding!!.locationView.text = it.location

                binding!!.totalReposView.text = it.total_repos_count.toString()
                binding!!.privateReposView.text = it.private_repos_count.toString()
                binding!!.publicReposView.text = it.public_repos_count.toString()
            }

            // Cache db is empty
            else {
                hideProfileViews()
            }

        })

        binding!!.reposStatDetailedBtn.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_reposOverviewFragment)
        }

        binding!!.logOutBtn.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_logoutConfirmationDialog)
        }

        binding?.version?.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }


    private fun hideProfileViews() {
        binding!!.headerCard.visibility = View.GONE
        binding!!.profileSummaryCard.visibility = View.GONE
        binding!!.reposSummaryCard.visibility = View.GONE
    }

    private fun showProfileViews() {
        binding!!.headerCard.visibility = View.VISIBLE
        binding!!.profileSummaryCard.visibility = View.VISIBLE
        binding!!.reposSummaryCard.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}