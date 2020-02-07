package com.example.top_10_downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvLink : TextView = v.findViewById(R.id.tvLink)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: TextView = v.findViewById(R.id.tvSummary)
   // val tvImageUrl: TextView = v.findViewById(R.id.tvImageUrl)
    val ivImage: ImageView =  v.findViewById(R.id.ivImage)
}


class FeedAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry> )
    : ArrayAdapter<FeedEntry>(context, resource){

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val viewHolder: ViewHolder


        if (convertView == null){
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

        viewHolder.tvName.text = currentApp.name
        viewHolder.tvLink.text = currentApp.link
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary
        //viewHolder.tvImageUrl.text = currentApp.imageURL
        Picasso.get().load(currentApp.imageURL).into(viewHolder.ivImage)

        return view
    }
}