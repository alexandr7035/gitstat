package by.alexandr7035.gitstat.view.login

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.NavGraphDirections
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.GithubAccessScopes
import by.alexandr7035.gitstat.databinding.FragmentLoginBinding
import by.alexandr7035.gitstat.extensions.getClickableSpannable
import by.alexandr7035.gitstat.extensions.navigateSafe
import by.alexandr7035.gitstat.view.MainActivity
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var navController: NavController
    private var binding: FragmentLoginBinding? = null

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // NavController
        val hf: NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val provider = OAuthProvider.newBuilder(getString(R.string.OAUTH_PROVIDER))
        provider.scopes = GithubAccessScopes.getScopes()
        val auth = Firebase.auth

        binding!!.signInBtn.setOnClickListener {

            binding?.loginProgressView?.visibility = View.VISIBLE

            val result = auth.pendingAuthResult

            if (result == null) {
                auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        Timber.tag("DEBUG_AUTH").d("success auth")

                        val token = (authResult.credential as OAuthCredential).accessToken
                        Timber.tag("DEBUG_AUTH").d("token $token")
                        viewModel.saveToken(token!!)
                        (requireActivity() as MainActivity).startSyncData()
                    }

                        // TODO handle error types
                    .addOnFailureListener {
                        binding?.loginProgressView?.visibility = View.GONE
                        Timber.tag("DEBUG_AUTH").d("failure $it")
                        Toast.makeText(requireActivity(), getString(R.string.oauth_error_generic), Toast.LENGTH_LONG).show()
                    }
            }
        }


        val privacyPolicyText = getString(R.string.privacy_policy_text).getClickableSpannable(
            clickListener = {
                showPrivacyPolicy()
            },
            clickableText = getString(R.string.privacy_policy_clickable),
            isBold = true,
            spannableColor = ContextCompat.getColor(requireContext(), R.color.gray_500)
        )

        binding?.privacyPolicyView?.apply {
            text = privacyPolicyText
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }


        binding?.version?.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }

    private fun showPrivacyPolicy() {
        navController.navigateSafe(NavGraphDirections.actionGlobalInfoFragment(
            getString(R.string.privacy_policy),
            null,
            getString(R.string.privacy_policy_full_text)
        ))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}