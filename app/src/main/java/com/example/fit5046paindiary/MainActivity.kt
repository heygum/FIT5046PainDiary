package com.example.fit5046paindiary

import android.content.Intent
import android.icu.math.BigDecimal
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fit5046paindiary.databinding.ActivityMainBinding
import com.example.fit5046paindiary.fragment.*
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mAppBarConfiguration:AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.menu_list_36)
        }

        binding.navView.setCheckedItem(R.id.navHome)
        binding.navView.setNavigationItemSelectedListener {
            val trans = supportFragmentManager.beginTransaction()
            when(it.itemId)
            {
                R.id.navEntry -> trans.replace(R.id.nav_host,EntryFragment())
                R.id.navHome -> trans.replace(R.id.nav_host,HomeFragment())
                R.id.navRecord -> trans.replace(R.id.nav_host,RecordFragment())
                R.id.navReport -> trans.replace(R.id.nav_host,ReportFragment())
                R.id.navMap -> trans.replace(R.id.nav_host,MapFragment())
            }
            trans.commit()
            binding.drawerLayout.closeDrawers()
            true
        }



        binding.navLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.nav_entry_fragment -> Toast.makeText(this,"Entry Hello",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}