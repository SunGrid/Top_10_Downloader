package com.example.top_10_downloader

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso

private const val TAG = "FeedAdapter"

class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    //val tvLink : TextView = v.findViewById(R.id.tvLink)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
   // val tvImageUrl: TextView = v.findViewById(R.id.tvImageUrl)
    val ivImage: ImageView =  v.findViewById(R.id.ivImage)
}


class FeedAdapter(context: Context, private val resource: Int, private var applications: List<FeedEntry> )
    : ArrayAdapter<FeedEntry>(context, resource){

    private val inflater = LayoutInflater.from(context)

    fun setFeedList(feedList: List<FeedEntry>){
        Log.d(TAG, " setFeedList = $feedList")
        this.applications = feedList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount called")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView called")
        Log.d(TAG, " .getView position: $position")
        val view: View
        val viewHolder: ViewHolder


        if (convertView == null){
            Log.d(TAG, "getView called with null convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView  //convertView makes it so you don't create a new view with inflater all the time after created the first time
            viewHolder = view.tag as ViewHolder  //tag is an object and needs to be cast as ViewHolder
        }

//        val tvName: TextView = view.findViewById(R.id.tvName)
//        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
//        val tvSummary: TextView = view.findViewById(R.id.tvSummary)


        val currentApp = applications[position]
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentApp.link))

        viewHolder.tvName.text = currentApp.name

        viewHolder.tvName.setOnClickListener{
            startActivity(this.context, intent, null)
        }
        //viewHolder.tvLink.text = currentApp.link
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary
        //viewHolder.tvImageUrl.text = currentApp.imageURL
        Picasso.get().load(currentApp.imageURL).resize(150,150).into(viewHolder.ivImage)

        viewHolder.ivImage.setOnClickListener{
            startActivity(this.context, intent, null)
        }

        return view
    }
}