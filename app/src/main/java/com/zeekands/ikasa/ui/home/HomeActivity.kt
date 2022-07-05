package com.zeekands.ikasa.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController: NavController = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView, navController)

    }
}