package com.samples.nowinandroid_demo.navigation

import com.samples.nowinandroid_demo.core.designsystem.icon.Icon
import com.samples.nowinandroid_demo.core.navigation.NiaNavigationDestination

data class TopLevelDestination(
    override val route: String,
    override val destination: String,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int
) : NiaNavigationDestination
