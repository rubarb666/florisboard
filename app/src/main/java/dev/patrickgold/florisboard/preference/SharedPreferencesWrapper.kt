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

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import java.lang.ref.WeakReference

abstract class SharedPreferencesWrapper protected constructor(
    context: Context,
    name: String
) : PreferenceDataStore() {

    companion object {
        fun defaultName(context: Context) = context.packageName + "_preferences"
    }

    val applicationContext: WeakReference<Context> = WeakReference(context.applicationContext ?: context)
    protected val shared: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        if (key == null) return defValue
        return try {
            shared.getBoolean(key, defValue)
        } catch (_: Exception) {
            defValue
        }
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        if (key == null) return defValue
        return try {
            shared.getFloat(key, defValue)
        } catch (_: Exception) {
            defValue
        }
    }

    override fun getInt(key: String?, defValue: Int): Int {
        if (key == null) return defValue
        return try {
            shared.getInt(key, defValue)
        } catch (_: Exception) {
            defValue
        }
    }

    override fun getLong(key: String?, defValue: Long): Long {
        if (key == null) return defValue
        return try {
            shared.getLong(key, defValue)
        } catch (_: Exception) {
            defValue
        }
    }

    override fun getString(key: String?, defValue: String?): String {
        if (key == null) return defValue ?: ""
        return try {
            shared.getString(key, defValue) ?: ""
        } catch (_: Exception) {
            defValue ?: ""
        }
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        if (key == null) return defValues ?: mutableSetOf()
        return try {
            shared.getStringSet(key, defValues) ?: mutableSetOf()
        } catch (_: Exception) {
            defValues ?: mutableSetOf()
        }
    }

    fun contains(key: String?): Boolean {
        if (key == null) return false
        return shared.contains(key)
    }

    override fun putBoolean(key: String?, value: Boolean) {
        if (key == null) return
        shared.edit().putBoolean(key, value).apply()
    }

    override fun putFloat(key: String?, value: Float) {
        if (key == null) return
        shared.edit().putFloat(key, value).apply()
    }

    override fun putInt(key: String?, value: Int) {
        if (key == null) return
        shared.edit().putInt(key, value).apply()
    }

    override fun putLong(key: String?, value: Long) {
        if (key == null) return
        shared.edit().putLong(key, value).apply()
    }

    override fun putString(key: String?, value: String?) {
        if (key == null) return
        shared.edit().putString(key, value).apply()
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        if (key == null) return
        shared.edit().putStringSet(key, values).apply()
    }
}
