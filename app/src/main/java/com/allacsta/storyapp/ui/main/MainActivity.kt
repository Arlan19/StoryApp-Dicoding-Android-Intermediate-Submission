package com.allacsta.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.allacsta.storyapp.R
import com.allacsta.storyapp.data.StoryData
import com.allacsta.storyapp.data.api.ListStory
import com.allacsta.storyapp.data.helper.Constanta.EXTRA_STORY
import com.allacsta.storyapp.data.repository.ResultState
import com.allacsta.storyapp.databinding.ActivityMainBinding
import com.allacsta.storyapp.ui.ViewModelFactory
import com.allacsta.storyapp.ui.adapter.LoadingStateAdapter
import com.allacsta.storyapp.ui.adapter.StoryAdapter
import com.allacsta.storyapp.ui.detail.DetailActivity
import com.allacsta.storyapp.ui.maps.MapsActivity
import com.allacsta.storyapp.ui.story.StoryActivity
import com.allacsta.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        supportActionBar?.show()

        getStories()
        addStories()
//        refreshStory()
    }

//    private fun refreshStory() {
//        binding.btnTry.setOnClickListener {
//            getStories()
//        }
//    }

    private fun addStories() {
        binding.fabAddStory.setOnClickListener{
            startActivity(Intent(this, StoryActivity::class.java))
        }
    }

    private fun getStories() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.story.observe(this){story ->
            adapter.submitData(lifecycle, story)
        }

//        viewModel.getStories().observe(this){ story ->
//            if (story != null){
//                when(story){
//                    is ResultState.Loading ->{
//                        showLoading(true)
//                        hideRefresh()
//                    }
//                    is ResultState.Success ->{
//                        showLoading(false)
//                        adapter.submitData(story.data.listStory)
//                        hideRefresh()
//                    }
//                    is ResultState.Error ->{
//                        showLoading(false)
//                        showToast(story.error)
//                    }
//                }
//            }
//
//        }

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
            override fun onItemClicked(story: ListStory, optionsCompat: ActivityOptionsCompat) {
                val storyData = StoryData(
                    story.name.toString(),
                    story.description.toString(),
                    story.photoUrl.toString()
                )

                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(EXTRA_STORY, storyData)
                startActivity(detailIntent, optionsCompat.toBundle())
            }

        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout ->{
                val logout = AlertDialog.Builder(this)
                logout.setTitle(resources.getString(R.string.logout))
                logout.setMessage(getString(R.string.logout_confirm))
                logout.setPositiveButton(getString(R.string.logout_accept)){ _,_ ->
                    viewModel.logout()
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                    finish()
                    showToast(getString(R.string.logout_status_accept))
                }
                logout.setNegativeButton(getString(R.string.logout_denied)){ _,_ ->
                    showToast(getString(R.string.logout_status_denied))
                }
                logout.show()
            }

            R.id.menu_language ->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.menu_maps ->{
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, true)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



}