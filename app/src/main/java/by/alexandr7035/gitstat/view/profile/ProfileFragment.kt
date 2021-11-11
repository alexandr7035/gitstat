package by.alexandr7035.gitstat.view.profile

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentProfileBinding
import by.alexandr7035.gitstat.view.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update profile data
        viewModel.getUserLiveData().observe(viewLifecycleOwner, {

            Timber.tag("DEBUG_TAG").d("livedata updated $it")

            if (it != null) {

                Picasso.get().load(it.avatar_url).into(binding!!.profileImageView)

                // This field can be empty
                if (it.name.isEmpty()) {
                    binding?.nameView?.visibility = View.GONE
                } else {
                    binding?.nameView?.visibility = View.VISIBLE
                    binding?.nameView?.text = it.name
                }

                binding!!.loginView.text = getString(R.string.login_string, it.login)

                binding!!.idView.text = it.id.toString()

                val dateFormat = getString(R.string.profile_date_format)
                binding!!.createdView.text = DateFormat.format(dateFormat, it.created_at)
                binding!!.updatedView.text = DateFormat.format(dateFormat, it.updated_at)

                binding!!.followersView.text = it.followers.toString()

                // This field can be empty
                if (it.location.isEmpty()) {
                    binding!!.locationContainer.visibility = View.GONE
                } else {
                    binding!!.locationContainer.visibility = View.VISIBLE
                    binding!!.locationView.text = it.location
                }

                binding!!.totalReposView.text = it.total_repos_count.toString()
                binding!!.privateReposView.text = it.private_repos_count.toString()
                binding!!.publicReposView.text = it.public_repos_count.toString()

            }
        })

        binding!!.reposStatDetailedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_reposOverviewFragment)
        }

        binding?.drawerBtn?.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}