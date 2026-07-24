package com.example.ottapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ottapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Wires bottom nav tab taps to the matching destination in
        // navigation.xml - the menu item ids and fragment ids match
        // (nav_home / nav_explore / nav_profile), so this is all that's
        // needed for navigation to work correctly.
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
