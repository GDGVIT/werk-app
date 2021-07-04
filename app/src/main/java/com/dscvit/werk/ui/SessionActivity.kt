package com.dscvit.werk.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivitySessionBinding
import com.dscvit.werk.ui.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionBinding

    private val args: SessionActivityArgs by navArgs()
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        taskViewModel.sessionDetails = args.session

        setupViews()
    }

    private fun setupViews() {
        val navController = findNavController(R.id.session_nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.appBarTitle.text = args.session.sessionName
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val appBarTitle: String = when (it.itemId) {
                R.id.sessionFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.sessionFragment)
                    args.session.sessionName
                }
                R.id.chatFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.chatFragment)
                    "Chat"
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