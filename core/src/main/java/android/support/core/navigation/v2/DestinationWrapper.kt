package android.support.core.navigation.v2

import android.support.core.navigation.Destination
import androidx.fragment.app.Fragment

class DestinationWrapper(
    val destination: Destination,
    val fragment: Fragment,
    val isUpdateCurrent: Boolean = false
)