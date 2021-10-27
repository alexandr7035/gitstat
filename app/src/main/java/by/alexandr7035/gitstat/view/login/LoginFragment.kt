package by.alexandr7035.gitstat.view.login

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
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
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentLoginBinding
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

        // TODO move to stings
        val provider = OAuthProvider.newBuilder("github.com")
        val scopes: List<String> = listOf(
            "read:user",
            "repo"
        )
        provider.scopes = scopes

        val auth = Firebase.auth
        // Sign out from previous session preliminarily (if any)
        auth.signOut()
        viewModel.clearToken()

        binding!!.signInBtn.setOnClickListener {

            binding?.loginProgressView?.visibility = View.VISIBLE

            val result = auth.pendingAuthResult

            if (result == null) {
                Timber.tag("DEBUG_AUTH").d("$result")
                auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        Timber.tag("DEBUG_AUTH").d("success")

                        val token = (authResult.credential as OAuthCredential).accessToken
                        Timber.tag("DEBUG_AUTH").d("token $token")

                        viewModel.saveToken(token!!)
                        (requireActivity() as MainActivity).startSyncData()
                    }

                    .addOnFailureListener {
                        binding?.loginProgressView?.visibility = View.GONE
                        Timber.tag("DEBUG_AUTH").d("failure $it")
                        Toast.makeText(requireActivity(), getString(R.string.error_cant_get_data_remote), Toast.LENGTH_LONG).show()
                    }
            }
        }

        val privacyPolicyFullText = getString(R.string.privacy_policy_text)
        val linkText = getString(R.string.privacy_policy_clickable)
        val privacyPolicySpannable = SpannableString(privacyPolicyFullText)

        val privacyPolicyClickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTokenInstructions()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(requireContext(), R.color.gray_500)
            }
        }

        privacyPolicySpannable.setSpan(
            privacyPolicyClickable,
            privacyPolicyFullText.indexOf(linkText),
            privacyPolicyFullText.indexOf(linkText) + linkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        privacyPolicySpannable.setSpan(
            StyleSpan(Typeface.BOLD),
            privacyPolicyFullText.indexOf(linkText),
            privacyPolicyFullText.indexOf(linkText) + linkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding?.privacyPolicyView?.apply {
            text = privacyPolicySpannable
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }


        binding?.version?.text = getString(R.string.app_name_with_version, BuildConfig.VERSION_NAME)
    }

    private fun showTokenInstructions() {
        navController.navigate(R.id.action_loginFragment_to_webViewFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}