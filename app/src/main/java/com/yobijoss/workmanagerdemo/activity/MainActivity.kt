package com.yobijoss.workmanagerdemo.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.snackbar.Snackbar
import com.yobijoss.workmanagerdemo.R
import com.yobijoss.workmanagerdemo.adapter.MediaRecyclerAdapter
import com.yobijoss.workmanagerdemo.viewmodel.ImageListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_GALLERY = 91

    private lateinit var imageListViewModel: ImageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Fresco.initialize(application)

        val recyclerView = getRecyclerView()
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        imageListViewModel = ViewModelProviders.of(this).get(ImageListViewModel::class.java)
        imageListViewModel.uriListLiveData.observe(
            this,
            Observer { list -> list?.let { updateList(it) } }
        )

        imageListViewModel.outputWorkInfos.observe(this, logStatus())

        fab.setOnClickListener {
            getImageFromGallery()
        }
    }

    private fun logStatus(): Observer<List<WorkInfo>> {
        return Observer { list ->

            list?.let {
                if (list.isNotEmpty()) {
                    statusTextView.append("Work [${list[0].tags.last()}] is Finished?  ${list[0].state.isFinished} \n")
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_gotorepo -> goToRepository()
        R.id.action_sync -> syncImages()
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            resultData?.data?.let {
                imageListViewModel.addUri(it)
            } ?: run {
                Snackbar.make(content, "No se pudo agregar la imagen :(", Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun updateList(uriList: ArrayList<Uri>) {
        getRecyclerView().adapter = MediaRecyclerAdapter(uriList)
    }

    private fun getRecyclerView() = imageRecycleView as RecyclerView


    private fun getImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }


    private fun syncImages(): Boolean {
        statusTextView.text = ""
        imageListViewModel.syncImages()
        return true
    }


    private fun goToRepository(): Boolean {
        val url = resources.getString(R.string.repo_url)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        return true
    }
}
