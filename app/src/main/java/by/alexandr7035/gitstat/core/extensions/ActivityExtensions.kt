package by.alexandr7035.gitstat.core.extensions

import androidx.fragment.app.FragmentActivity
import by.alexandr7035.gitstat.R
import com.permissionx.guolindev.PermissionX

fun FragmentActivity.doWithPermissions(
    vararg permissions: String,
    explanation: String,
    onAllGranted: () -> Unit,
    onSomeDenied: () -> Unit = {}
) {
    PermissionX.init(this)
        .permissions(listOf(*permissions))
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                explanation,
                getString(R.string.ok),
                getString(R.string.cancel)
            )
        }
        .onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                explanation,
                getString(R.string.ok),
                getString(R.string.cancel)
            )
        }
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                onAllGranted()
            } else {
                onSomeDenied()
            }
        }
}