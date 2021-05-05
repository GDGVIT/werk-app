package com.dscvit.werk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivitySessionBinding

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViews()
    }

    private fun setupViews() {
        val navController = findNavController(R.id.session_nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.appBarTitle.text = getString(R.string.session)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val appBarTitle: String = when (it.itemId) {
                R.id.sessionFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.sessionFragment)
                    "Session"
                }
                R.id.chatFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.chatFragment)
                    "Chat"
                }
                R.id.walkieTalkieFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.walkieTalkieFragment)
                    "Walkie Talkie"
                }
                R.id.leaderboardFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.leaderboardFragment)
                    "Leaderboard"
                }
                else -> {
                    ""
                }
            }
            binding.appBarTitle.text = appBarTitle
            true
        }
    }
}