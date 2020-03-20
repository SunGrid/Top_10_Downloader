package com.example.top_10_downloader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel  : ViewModel(), DownloadData.DownloaderCallBack{

    private var downloadData: DownloadData? = null
    private var feedCashedUrl = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
        get() = feed

    init{
        feed.postValue(EMPTY_FEED_LIST)
    }

    fun downloadUrl(feedUrl: String) {
        Log.d(TAG, "downloadURL: called URL $feedUrl")
        if (feedUrl != feedCashedUrl) {
            Log.d(TAG, "downloadUrl starting AsyncTask")
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            feedCashedUrl = feedUrl
            Log.d(TAG, "downloadUrl done")
        } else {
            Log.d(TAG, "downloadUrl - URL not changed")
        }
    }

    fun invalidate(){
        feedCashedUrl = "INVALIDATED"
    }

    override fun onDataAvailable(data: List<FeedEntry>) {
        Log.d(TAG, "onDataAvailable: called")
        feed.value = data
        Log.d(TAG, "onDataAvailable: data = $data")
        Log.d(TAG, "onDataAvailable: end")
    }

    override fun onCleared(){
        Log.d(TAG, "onCleared: cancelling pending downloads")
        downloadData?.cancel(true)
    }
}