package com.dicoding.aplikasistoryku

import com.dicoding.aplikasistoryku.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://www.pexels.com/photo/focus-photo-of-super-mario-luigi-and-yoshi-figurines-163036/",
                "2022-02-22T22:22:22Z",
                "Orang Pengirim untuk postingan $i",
                "Deskripsi tes untuk postingan $i",
                0.0,
                "Postingan $i",
                0.0
            )
            items.add(story)
        }
        return items
    }
}