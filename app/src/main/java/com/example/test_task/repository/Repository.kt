package com.example.test_task.repository

import com.example.test_task.utils.NetworkUtils.Companion.generateAllIdsUrl
import com.example.test_task.utils.NetworkUtils.Companion.generateUrlById
import com.example.test_task.utils.NetworkUtils.Companion.getResponseFromUrl
import org.json.JSONObject

class Repository {

    suspend fun getAllIds(): ArrayList<Int> {
        val url = generateAllIdsUrl()
        val response = getResponseFromUrl(url)


        val jsonArray = JSONObject(response).getJSONArray("data")
        val list = ArrayList<Int>()
        for (i in 0 until jsonArray.length()) {
            val id = jsonArray
                .getJSONObject(i)
                .get("id")
                .toString()
                .toInt()
            list.add(id)
        }
        return list
    }


    suspend fun getDataById(id: Int): JSONObject {
        val url = generateUrlById(id)
        val response = getResponseFromUrl(url)

        return JSONObject(response)
    }
}