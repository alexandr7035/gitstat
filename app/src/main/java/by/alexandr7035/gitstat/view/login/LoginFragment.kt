package by.alexandr7035.gitstat.view.login

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.NavGraphDirections
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.GithubAccessScopes
import by.alexandr7035.gitstat.core.extensions.getClickableSpannable
import by.alexandr7035.gitstat.core.extensions.navigateSafe
import by.alexandr7035.gitstat.databinding.FragmentLoginBinding
import by.alexandr7035.gitstat.view.MainActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val provider = OAuthProvider.newBuilder(getString(R.string.OAUTH_PROVIDER))
        val auth = Firebase.auth

        binding.signInBtn.setOnClickListener {
            binding.loginProgressView.visibility = View.VISIBLE

            provider.scopes = if (binding.scopesSwitch.isChecked) {
                GithubAccessScopes.EXTENDED_SCOPES
            } else {
                GithubAccessScopes.BASIC_SCOPES
            }

            val pendingResultTask = auth.pendingAuthResult
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask.addOnSuccessListener { authResult ->
                    processSuccessLogin(authResult)
                }.addOnFailureListener {
                    processFailLogin(it)
                }
            } else {
                auth.startActivityForSignInWithProvider(requireActivity(), provider.build()).addOnSuccessListener { authResult ->
                    processSuccessLogin(authResult)
                }.addOnFailureListener {
                    processFailLogin(it)
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

        binding.privacyPolicyView.apply {
            text = privacyPolicyText
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }

        binding.version.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }

    private fun showPrivacyPolicy() {
        findNavController().navigateSafe(
            NavGraphDirections.actionGlobalInfoFragment(
                getString(R.string.privacy_policy), null, getString(R.string.privacy_policy_full_text)
            )
        )
    }

    private fun processSuccessLogin(authResult: AuthResult) {
        val token = (authResult.credential as OAuthCredential).accessToken
        Timber.tag("DEBUG_AUTH").d("success auth with token $token")
        viewModel.saveToken(token!!)
        (requireActivity() as MainActivity).startSyncData()
    }

    private fun processFailLogin(error: Exception) {
        binding.loginProgressView?.visibility = View.GONE
        Timber.tag("DEBUG_AUTH").d("failure $error")
        Toast.makeText(requireContext(), getString(R.string.oauth_error_generic), Toast.LENGTH_LONG).show()
    }

}