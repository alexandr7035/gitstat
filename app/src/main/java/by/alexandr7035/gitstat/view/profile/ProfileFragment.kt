package by.alexandr7035.gitstat.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentProfileBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

//    private var binding: FragmentProfileBinding? = null
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update profile data
        viewModel.getUserLiveData().observeNullSafe(viewLifecycleOwner, { profileUi ->

            Picasso.get().load(profileUi.avatar_url).into(binding.profileImageView)

            binding.idView.text = profileUi.id
            binding.loginView.text = getString(R.string.login_string, profileUi.login)

            // These fields can be empty
            binding.nameView.isVisible = profileUi.name.isVisible
            binding.nameView.text = profileUi.name.value
            setLocationVisibility(profileUi.location.isVisible)
            binding.locationView.text = profileUi.location.value

            binding.createdView.text = profileUi.created_at
            binding.updatedView.text = profileUi.updated_at

            binding.followersView.text = profileUi.followers

//            binding!!.totalReposView.text = it.total_repos_count.toString()
//            binding!!.privateReposView.text = it.private_repos_count.toString()
//            binding!!.publicReposView.text = it.public_repos_count.toString()
        })

        binding.reposStatDetailedBtn.setOnClickListener {
            findNavController().navigateSafe(ProfileFragmentDirections.actionProfileFragmentToReposOverviewFragment())
        }

        binding.drawerBtn.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

    }

    private fun setLocationVisibility(isVisible: Boolean) {
        binding.locationIcon.isVisible = isVisible
        binding.locationLabel.isVisible = isVisible
        binding.locationView.isVisible = isVisible
    }
}