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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceDataStore

abstract class Pref<V> : LiveData<V>() {

    private var isSet: Boolean = false

    abstract fun key(): String

    abstract fun default(): V

    abstract fun get(): V

    abstract fun set(value: V)

    override fun setValue(value: V) {
        try {
            super.setValue(value)
        } catch (_: Exception) {
            super.postValue(value)
        } finally {
            isSet = true
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in V>) {
        super.observe(owner, observer)
        if (!isSet) setValue(get())
    }

    override fun observeForever(observer: Observer<in V>) {
        super.observeForever(observer)
        if (!isSet) setValue(get())
    }
}

open class BooleanPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: Boolean
) : Pref<Boolean>() {

    override fun key() = _key

    override fun default() = _default

    override fun get() = dataStore.getBoolean(_key, _default)

    override fun set(value: Boolean) {
        dataStore.putBoolean(_key, value)
        setValue(value)
    }
}

open class FloatPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: Float
) : Pref<Float>() {

    override fun key() = _key

    override fun default() = _default

    override fun get() = dataStore.getFloat(_key, _default)

    override fun set(value: Float) {
        dataStore.putFloat(_key, value)
        setValue(value)
    }
}

open class IntPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: Int
) : Pref<Int>() {

    override fun key() = _key

    override fun default() = _default

    override fun get() = dataStore.getInt(_key, _default)

    override fun set(value: Int) {
        dataStore.putInt(_key, value)
        setValue(value)
    }
}

open class LongPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: Long
) : Pref<Long>() {

    override fun key() = _key

    override fun default() = _default

    override fun get() = dataStore.getLong(_key, _default)

    override fun set(value: Long) {
        dataStore.putLong(_key, value)
        setValue(value)
    }
}

open class ShadowStringPref<V>(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: V,
    val strToValue: (str: String) -> V
) : Pref<V>() {

    override fun key() = _key

    override fun default() = _default

    override fun get(): V {
        val str = dataStore.getString(_key, null)
        return if (str != null) strToValue(str) else _default
    }

    override fun set(value: V) {
        dataStore.putString(_key, value.toString())
        setValue(value)
    }
}

open class StringPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: String
) : Pref<String>() {

    override fun key() = _key

    override fun default() = _default

    override fun get() = dataStore.getString(_key, null) ?: _default

    override fun set(value: String) {
        dataStore.putString(_key, value)
        setValue(value)
    }
}

open class StringSetPref(
    private val dataStore: PreferenceDataStore,
    private val _key: String,
    private val _default: MutableSet<String>
) : Pref<MutableSet<String>>() {

    override fun key() = _key

    override fun default() = _default

    override fun get(): MutableSet<String> = dataStore.getStringSet(_key, null) ?: _default

    override fun set(value: MutableSet<String>) {
        dataStore.putStringSet(_key, value)
        setValue(value)
    }
}

open class StubPref(
    private val _key: String
) : Pref<Unit>() {

    override fun key() = _key

    override fun default() = Unit

    override fun get() = Unit

    override fun set(value: Unit) {
        // Intentionally empty
    }
}
