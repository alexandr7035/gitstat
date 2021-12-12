package by.alexandr7035.gitstat.core.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

// Prevents crashes when second navigation request is triggered
// from a fragment that is no longer the current destination
fun NavController.navigateSafe(navDirection: NavDirections)  {
    val action = currentDestination?.getAction(navDirection.actionId)

    if (action != null) {
        navigate(navDirection)
    }
}
