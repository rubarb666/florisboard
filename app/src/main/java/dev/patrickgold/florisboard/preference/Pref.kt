/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.preference

import androidx.lifecycle.LiveData
import androidx.preference.PreferenceDataStore

abstract class Pref<V> : LiveData<V>() {

    abstract fun get(): V

    abstract fun set(value: V)

    override fun setValue(value: V) {
        try {
            super.setValue(value)
        } catch (_: Exception) {
            super.postValue(value)
        }
    }
}

open class BooleanPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: Boolean
) : Pref<Boolean>() {

    override fun get() = dataStore.getBoolean(key, default)

    override fun set(value: Boolean) {
        dataStore.putBoolean(key, value)
        setValue(value)
    }
}

open class FloatPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: Float
) : Pref<Float>() {

    override fun get() = dataStore.getFloat(key, default)

    override fun set(value: Float) {
        dataStore.putFloat(key, value)
        setValue(value)
    }
}

open class IntPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: Int
) : Pref<Int>() {

    override fun get() = dataStore.getInt(key, default)

    override fun set(value: Int) {
        dataStore.putInt(key, value)
        setValue(value)
    }
}

open class LongPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: Long
) : Pref<Long>() {

    override fun get() = dataStore.getLong(key, default)

    override fun set(value: Long) {
        dataStore.putLong(key, value)
        setValue(value)
    }
}

open class ShadowStringPref<V>(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: V,
    val strToValue: (str: String) -> V
) : Pref<V>() {

    override fun get(): V {
        val str = dataStore.getString(key, null)
        return if (str != null) strToValue(str) else default
    }

    override fun set(value: V) {
        dataStore.putString(key, value.toString())
        setValue(value)
    }
}

open class StringPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: String
) : Pref<String>() {

    override fun get() = dataStore.getString(key, null) ?: default

    override fun set(value: String) {
        dataStore.putString(key, value)
        setValue(value)
    }
}

open class StringSetPref(
    private val dataStore: PreferenceDataStore,
    val key: String,
    val default: MutableSet<String>
) : Pref<MutableSet<String>>() {

    override fun get(): MutableSet<String> = dataStore.getStringSet(key, null) ?: default

    override fun set(value: MutableSet<String>) {
        dataStore.putStringSet(key, value)
        setValue(value)
    }
}
