/*
 * Copyright (C) 2020 Patrick Goldinger
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

package dev.patrickgold.florisboard.settings.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.ime.theme.ThemeMode
import dev.patrickgold.florisboard.preference.Preferences

class ThemeFragment : PreferenceFragmentCompat() {
    private val prefs get() = Preferences.default()

    private val dayThemeGroup: PreferenceCategory? by lazy { findPreference("theme__day_group") }
    private val nightThemeGroup: PreferenceCategory? by lazy { findPreference("theme__night_group") }
    private val dayThemePref: Preference? by lazy { findPreference(prefs.theme.dayThemeRef.key()) }
    private val nightThemePref: Preference? by lazy { findPreference(prefs.theme.nightThemeRef.key()) }
    private val sunrisePref: Preference? by lazy { findPreference(prefs.theme.sunriseTime.key()) }
    private val sunsetPref: Preference? by lazy { findPreference(prefs.theme.sunsetTime.key()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs_theme)

        prefs.theme.mode.observe(this) { refreshUi(it) }
        prefs.theme.dayThemeRef.observe(this) { dayThemePref. }
        prefs.theme.mode.observe(this) { refreshUi(it) }
    }

    private fun refreshUi(mode: ThemeMode) {
        when (mode) {
            ThemeMode.ALWAYS_DAY -> {
                dayThemeGroup?.isEnabled = true
                nightThemeGroup?.isEnabled = false
                sunrisePref?.isVisible = false
                sunsetPref?.isVisible = false
            }
            ThemeMode.ALWAYS_NIGHT -> {
                dayThemeGroup?.isEnabled = false
                nightThemeGroup?.isEnabled = true
                sunrisePref?.isVisible = false
                sunsetPref?.isVisible = false
            }
            ThemeMode.FOLLOW_SYSTEM -> {
                dayThemeGroup?.isEnabled = true
                nightThemeGroup?.isEnabled = true
                sunrisePref?.isVisible = false
                sunsetPref?.isVisible = false
            }
            ThemeMode.FOLLOW_TIME -> {
                dayThemeGroup?.isEnabled = true
                nightThemeGroup?.isEnabled = true
                sunrisePref?.isVisible = true
                sunsetPref?.isVisible = true
            }
        }
    }
}
