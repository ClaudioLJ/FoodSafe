package com.example.alimentate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.alimentate.same.AnalyticsFragment
import com.example.alimentate.same.DashboardFragment
import com.example.alimentate.same.MLFragment
import com.example.foodsafe.R
import com.example.foodsafe.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Fragment inicial (Dashboard por default)
        replaceFragment(DashboardFragment())

        // Listener de la barra de abajo
        bind.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> replaceFragment(DashboardFragment())
                R.id.nav_analytics -> replaceFragment(AnalyticsFragment())
                R.id.nav_ml        -> replaceFragment(MLFragment())
            }
            true
        }
    }

    private fun replaceFragment(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, f)
            .commit()
    }
}

