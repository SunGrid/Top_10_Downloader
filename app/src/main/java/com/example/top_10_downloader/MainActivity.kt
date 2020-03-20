package com.example.top_10_downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*


class FeedEntry {
    var name: String = ""
    var link: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

}
private const val TAG = "MainActivity"
private const val STATE_URL = "feedUrl"
private const val STATE_LIMIT = "feedLimit"

class MainActivity : AppCompatActivity() {

    private var feedUrl: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    //val feedViewModel: FeedViewModel by viewModels()
    private val feedViewModel by lazy { ViewModelProvider(this).get(FeedViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")

        val feedAdapter = FeedAdapter(this , R.layout.list_record, EMPTY_FEED_LIST)
            xmlListView.adapter = feedAdapter  // needed to make the view to display in app

        if (savedInstanceState != null){
            feedUrl = savedInstanceState.getString(STATE_URL, feedUrl)
            feedLimit = savedInstanceState.getInt(STATE_LIMIT, feedLimit)
        }

        feedViewModel.feedEntries.observe( this, Observer<List<FeedEntry>> {feedEntries -> feedAdapter.setFeedList(feedEntries ?: EMPTY_FEED_LIST)})

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate done")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.mnu10)?.isChecked = true
        } else {
            menu?.findItem(R.id.mnu25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.mnuFree -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"

            R.id.mnuPaid -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"

            R.id.mnuSongs -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"

            R.id.mnu10, R.id.mnu25 -> {
                if (!item.isChecked){
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(TAG, "onOptionsItemSelected : ${item.title} setting feedLimit to $feedLimit")
                } else {
                    Log.d(TAG, "onOptionsItemSelected : $(item.title} setting feedLimit unchanged")
                }
            }

            R.id.mnuRefresh -> feedViewModel.invalidate()  //feedCashedUrl = "INVALIDATED"

            else -> return super.onOptionsItemSelected(item)
        }

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
    }

}
