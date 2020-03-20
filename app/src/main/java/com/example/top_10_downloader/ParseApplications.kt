package com.example.top_10_downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

private const val TAG = "ParseAppications"

class ParseApplications {

    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String) : Boolean {
        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            var href = ""

            while (eventType != XmlPullParser.END_DOCUMENT){
                val tagName = xpp.name?.toLowerCase()
                //val prfx = xpp.prefix?.toLowerCase()

                //Log.d(TAG, "parse: Found Tag as $tagName ")
                when (eventType){

                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG, "parse: Starting tag for $tagName")

                        if (tagName == "entry") {
                            inEntry = true
                        }
                        if (tagName == "link"){
                            Log.d(TAG, "parse: Found Link tag with this link ${xpp.getAttributeValue(null, "href")}")
                            href = xpp.getAttributeValue(null, "href")
                        }
                        if (tagName == "image"){
                            Log.d(TAG, "parse: image found as: ${xpp.name}")
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
//                        Log.d(TAG, "parse: Ending tag for $tagName")

                        if (inEntry){

                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() //create new object
                                }
                                "name" -> currentRecord.name = textValue
                                "link" -> currentRecord.link = href //textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> currentRecord.imageURL = textValue
                            }
                        }
                    }
                }

                //noting else to do.
                eventType = xpp.next()
            }

//            for (app in applications){
//                Log.d(TAG, "******************")
//                Log.d(TAG, app.toString())
//            }

        } catch ( e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}