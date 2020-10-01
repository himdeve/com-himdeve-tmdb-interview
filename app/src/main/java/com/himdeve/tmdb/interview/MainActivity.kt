package com.himdeve.tmdb.interview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.himdeve.tmdb.interview.application.core.connectivity.ConnectivityStateMonitor
import com.himdeve.tmdb.interview.domain.util.Constants
import com.himdeve.tmdb.interview.presentation.movies.fragments.MovieListFragment
import com.himdeve.tmdb.interview.presentation.movies.views.MovieDateDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*


const val MOVIE_DATE_DIALOG_POSITION_STATE_KEY = "movieDateDialogPosition"

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val navigationView by lazy { findViewById<NavigationView>(R.id.nav_view) }

    // transient state for the Movie Date Dialog position
    private var movieDateDialogPositionState: Int? = 0

    private lateinit var connectivityStateMonitor: ConnectivityStateMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recovering the instance state
        movieDateDialogPositionState =
            savedInstanceState?.getInt(MOVIE_DATE_DIALOG_POSITION_STATE_KEY)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Show and Manage the Drawer and Back Icon
        setupActionBarWithNavController(navController, drawerLayout)

        // Handle Navigation item clicks
        // This works with no further action on your part if the menu and destination id’s match.
        navigationView.setupWithNavController(navController)

        // Set drawer navigation click listener
        // If an item is part of navigation then it will be handled automatically in nav_graph.xml
        // Here we just launch Himdeve website
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    launchHimdeveWebsiteIntent()
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, R.string.author_info, Snackbar.LENGTH_LONG).show()
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(MOVIE_DATE_DIALOG_POSITION_STATE_KEY, movieDateDialogPositionState ?: 0)
        }

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return navController.navigateUp(drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                showDialogSpinner()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogSpinner() {
        lifecycleScope.launch(Dispatchers.Main) {
            MovieDateDialog(
                this@MainActivity,
                onPositiveClick = { startDate, endDate, position ->
                    setDateOnMovieListFragment(startDate, endDate)
                    movieDateDialogPositionState = position
                },
                onNegativeClick = {}, // No functionality is needed here right now
                position = movieDateDialogPositionState ?: 0
            ).show()
        }
    }

    // null values means no restriction
    private fun setDateOnMovieListFragment(startDate: Calendar?, endDate: Calendar?) {
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager
            ?.fragments?.let { fragments ->
                fragments.forEach { fragment ->
                    if (fragment is MovieListFragment) {
                        fragment.setDate(startDate, endDate)
                    }
                }
            }
    }

    private fun launchHimdeveWebsiteIntent() {
        val url = Constants.HIMDEVE_WEBSITE_URL
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}