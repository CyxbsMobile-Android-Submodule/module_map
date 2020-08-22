package com.mredrock.cyxbs.discover.map.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.utils.extensions.sharedPreferences
import com.mredrock.cyxbs.discover.map.bean.*

/**
 *@author zhangzhe
 *@date 2020/8/17
 *@description 所有数据在此缓存
 */


object DataSet {
    private val sharedPreferences by lazy { BaseApp.context.sharedPreferences("map_cache") }
    private val gson = Gson()

    fun saveMapInfo(mapInfo: MapInfo) {
        val s = gson.toJson(mapInfo)
        sharedPreferences.editor {
            putString("MapInfoStore", s)
        }
    }

    fun getMapInfo(): MapInfo? {
        val s1 = sharedPreferences.getString("MapInfoStore", "")
        return if (s1 == "") {
            null
        } else {
            gson.fromJson(s1, MapInfo::class.java)
        }
    }

    fun saveButtonInfo(buttonInfo: ButtonInfo) {
        val s = gson.toJson(buttonInfo)
        sharedPreferences.editor {
            putString("ButtonInfoStore", s)
        }
    }

    fun savePath(path: String) {
        sharedPreferences.editor {
            putString("Path", path)
        }
    }

    fun getPath(): String? {
        return sharedPreferences.getString("Path", null)
    }

    fun savePictureVersion(version: Long) {
        sharedPreferences.editor {
            putLong("PictureVersion", version)
        }
    }

    fun getPictureVersion(): Long {
        return sharedPreferences.getLong("PictureVersion", 0)
    }

    fun getButtonInfo(): ButtonInfo? {
        val s = sharedPreferences.getString("ButtonInfoStore", "")
        return if (s != "") {
            gson.fromJson(s, ButtonInfo::class.java)
        } else {
            null
        }
    }

    fun addCollect(id: String) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }


        if (list == null) {
            list = mutableListOf()
        }
        var isExist = false
        for (t: FavoritePlace in list) {
            if (t.placeId == id) {
                isExist = true
            }
        }
        //如果没收藏该id
        if (!isExist) {
            var placeName = "收藏" + list.size.toString()
            val mapInfo: MapInfo? = getMapInfo()
            if (mapInfo != null) {
                for (t: PlaceItem in mapInfo.placeList) {
                    if (t.placeId == id) {
                        placeName = t.placeName
                    }
                }
            }
            list.add(FavoritePlace(placeName, id))
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun addCollect(favoritePlace: FavoritePlace) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }
        if (list != null) {
            var flag = true
            for (t: FavoritePlace in list) {
                if (t.placeId == favoritePlace.placeId) {
                    t.placeNickname = favoritePlace.placeNickname
                    flag = false
                    break
                }
            }
            if (flag) {
                list.add(favoritePlace)
            }
        } else {
            list = mutableListOf(favoritePlace)
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun deleteCollect(id: String) {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        var list: MutableList<FavoritePlace>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
                } else {
                    null
                }
        if (list != null) {
            for (f: FavoritePlace in list) {
                if (f.placeId == id) {
                    list.remove(f)
                    break
                }
            }
        } else {
            list = mutableListOf()
        }
        sharedPreferences.editor {
            putString("FavoritePlaceStore", gson.toJson(list))
        }
    }

    fun getCollect(): MutableList<FavoritePlace>? {
        val s = sharedPreferences.getString("FavoritePlaceStore", "")
        return if (s != "") {
            gson.fromJson(s, object : TypeToken<MutableList<FavoritePlace>>() {}.type)
        } else {
            null
        }
    }

    fun getSearchHistory(): MutableList<String>? {
        val s = sharedPreferences.getString("SearchHistory", "")
        return if (s != "") {
            gson.fromJson(s, object : TypeToken<MutableList<String>>() {}.type)
        } else {
            null
        }
    }

    fun addSearchHistory(s: String) {
        var searchHistory = getSearchHistory()
        if (searchHistory == null) {
            searchHistory = mutableListOf(s)
        } else {
            var flag = false
            for (t: String in searchHistory.toList()) {
                if (t == s) {
                    flag = true
                }
            }
            if (!flag) {
                searchHistory.add(s)
            }
        }
        sharedPreferences.editor {
            putString("SearchHistory", gson.toJson(searchHistory))
        }
    }

    fun deleteSearchHistory(s: String) {
        var searchHistory = getSearchHistory()
        if (searchHistory == null) {
            searchHistory = mutableListOf()
        }
        for (t: String in searchHistory.toList()) {
            if (t == s) {
                searchHistory.remove(t)
            }
        }
        sharedPreferences.editor {
            putString("SearchHistory", gson.toJson(searchHistory))
        }
    }

    fun clearSearchHistory() {
        val searchHistory: MutableList<String> = mutableListOf()
        sharedPreferences.editor {
            putString("SearchHistory", gson.toJson(searchHistory))
        }
    }

    fun savePlaceDetails(placeDetails: PlaceDetails, id: String) {
        val s = sharedPreferences.getString("PlaceDetailsStore", "")
        var list: MutableList<PlaceDetailsStoreBean>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<PlaceDetailsStoreBean>>() {}.type)
                } else {
                    null
                }
        val placeDetailsStoreBean = PlaceDetailsStoreBean(placeDetails, id)
        if (list != null) {
            var flag = true
            for (i: Int in list.indices) {
                if (list[i].id == id) {

                    list[i] = placeDetailsStoreBean
                    flag = false
                    break
                }
            }
            if (flag) {
                list.add(placeDetailsStoreBean)
            }
        } else {
            list = mutableListOf(placeDetailsStoreBean)
        }
        sharedPreferences.editor {
            putString("PlaceDetailsStore", gson.toJson(list))
        }
    }

    fun clearPlaceDetails() {
        sharedPreferences.editor {
            putString("PlaceDetailsStore", gson.toJson(mutableListOf<PlaceDetailsStoreBean>()))
        }
    }

    fun getPlaceDetails(id: String): PlaceDetails? {
        val s = sharedPreferences.getString("PlaceDetailsStore", "")
        val list: MutableList<PlaceDetailsStoreBean>? =
                if (s != "") {
                    gson.fromJson(s, object : TypeToken<MutableList<PlaceDetailsStoreBean>>() {}.type)
                } else {
                    null
                }
        if (list != null) {
            for (i: Int in list.indices) {
                if (list[i].id == id) {
                    return list[i].placeDetails
                }
            }
        }
        return null
    }


}
