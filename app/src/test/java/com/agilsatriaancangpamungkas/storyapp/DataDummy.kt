package com.agilsatriaancangpamungkas.storyapp

import com.agilsatriaancangpamungkas.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyDataStoryResponse() : List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photo + $i",
                "Created + $i",
                "name author + $i",
                "description + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}