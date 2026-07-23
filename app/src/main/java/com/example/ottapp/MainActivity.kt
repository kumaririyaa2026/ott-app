package com.example.ottapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ottapp.databinding.ActivityMainBinding
import com.example.ottapp.explore.ExploreFragment
import com.example.ottapp.home.HomeFragment
import com.example.ottapp.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment by lazy { HomeFragment() }
    private val exploreFragment by lazy { ExploreFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Explore is the main screen for this assignment, so land on it
            // directly while still selecting the matching bottom nav tab.
            showFragment(exploreFragment)
            binding.bottomNavigation.selectedItemId = R.id.nav_explore
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showFragment(homeFragment)
                    true
                }
                R.id.nav_explore -> {
                    showFragment(exploreFragment)
                    true
                }
                R.id.nav_profile -> {
                    showFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
