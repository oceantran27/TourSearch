package com.example.flybooking.navigation.bottombar.droplet

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.flybooking.R
import com.example.flybooking.ui.theme.LightPurple
import com.example.flybooking.ui.theme.RoyalPurple
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.wigglebutton.WiggleButton

@Composable
fun AnimatedBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
//    var selectedItem by remember { mutableStateOf(0) }
    AnimatedNavigationBar(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 0.dp),
        selectedIndex = selectedTab,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Teleport(tween(500, easing = LinearEasing)),
        indentAnimation = Height(
            indentWidth = 48.dp,
            indentHeight = 16.dp,
            animationSpec = tween(
                350,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        navButtons.forEachIndexed { index, button ->
            WiggleButton(
                isSelected = index == selectedTab,
                onClick = {
                    if (index != selectedTab)
                        onTabSelected(index)
                },
                icon = button.icon,
                backgroundIcon = button.outlinedIcon,
                wiggleColor = LightPurple,
                outlineColor = RoyalPurple,
                enterExitAnimationSpec = tween(durationMillis = 500, easing = LinearEasing),
                wiggleAnimationSpec = spring(dampingRatio = .45f, stiffness = 35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .semantics {
                        contentDescription = "bookmark btn"
                    }
            )
        }
    }
}

@Stable
data class NavButton(
    val icon: Int,
    val outlinedIcon: Int,
    var isSelected: Boolean,
    val description: String,
)

val navButtons = listOf(
    NavButton(
        icon = R.drawable.home_search,
        outlinedIcon = R.drawable.home_search_outline,
        isSelected = false,
        description = "Home"
    ),
//    NavButton(
//        icon = R.drawable.compass,
//        outlinedIcon = R.drawable.compass_outline,
//        isSelected = false,
//        description = "Explore"
//    ),
    NavButton(
        icon = R.drawable.bookmark,
        outlinedIcon = R.drawable.bookmark_outline,
        isSelected = false,
        description = "Bookmarks"
    ),
    NavButton(
        icon = R.drawable.setting,
        outlinedIcon = R.drawable.setting,
        isSelected = false,
        description = "Profile"
    ),
)