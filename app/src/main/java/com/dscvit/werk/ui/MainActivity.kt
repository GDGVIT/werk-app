package com.dscvit.werk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.dscvit.werk.R
import com.dscvit.werk.util.APP_PREF
import com.dscvit.werk.util.PREF_TOKEN
import com.dscvit.werk.util.PrefHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs = PrefHelper.customPrefs(this, APP_PREF)
        val jwtToken = sharedPrefs.getString(PREF_TOKEN, "")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graph = navController.graph
        graph.startDestination =
            if (jwtToken == "") R.id.welcomeFragment else R.id.sessionsOverviewFragment
        navController.graph = graph
    }
}