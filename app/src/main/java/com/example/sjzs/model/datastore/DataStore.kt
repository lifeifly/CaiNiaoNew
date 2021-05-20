package com.example.sjzs.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
val name="Skin"
val key= stringPreferencesKey("type")
//Top-Level Function 可以被其他类直接调用
// 扩展属性，相当于在Context中定义了一个datastore属性，直接可以使用preferencesDataStore
val Context.datastore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
     name = "skin"
)