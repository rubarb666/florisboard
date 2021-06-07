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
import android.provider.Settings
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.ime.core.Subtype
import dev.patrickgold.florisboard.ime.landscapeinput.LandscapeInputUiMode
import dev.patrickgold.florisboard.ime.onehanded.OneHandedMode
import dev.patrickgold.florisboard.ime.text.gestures.DistanceThreshold
import dev.patrickgold.florisboard.ime.text.gestures.SwipeAction
import dev.patrickgold.florisboard.ime.text.gestures.VelocityThreshold
import dev.patrickgold.florisboard.ime.text.key.KeyHintMode
import dev.patrickgold.florisboard.ime.text.key.UtilityKeyAction
import dev.patrickgold.florisboard.ime.text.smartbar.CandidateView
import dev.patrickgold.florisboard.ime.theme.ThemeMode
import dev.patrickgold.florisboard.util.TimeUtil
import dev.patrickgold.florisboard.util.VersionName

class Preferences private constructor(
    context: Context,
    name: String,
    private val initIfAbsent: Boolean
) : SharedPreferencesWrapper(context, name) {

    companion object {
        private var defaultInstance: Preferences? = null

        fun default() = defaultInstance!!

        fun initDefault(
            context: Context,
            name: String = defaultName(context),
            initIfAbsent: Boolean = true
        ): Preferences {
            assert(defaultInstance == null) { "There is already a default instance!" }
            val instance = new(context, name, initIfAbsent)
            defaultInstance = instance
            return instance
        }

        fun new(
            context: Context,
            name: String = defaultName(context),
            initIfAbsent: Boolean = true
        ) = Preferences(context, name, initIfAbsent)
    }

    fun __sys() {
        applicationContext.get()?.let { context ->
            val contentResolver = context.contentResolver
            keyboard.soundEnabledSystem = Settings.System.getInt(
                contentResolver, Settings.System.SOUND_EFFECTS_ENABLED, 0
            ) != 0
            keyboard.vibrationEnabledSystem = Settings.System.getInt(
                contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED, 0
            ) != 0
        }
    }

    val advanced = AdvancedGroup(this, initIfAbsent)
    class AdvancedGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val forcePrivateMode =
            booleanPref(R.string.pref__advanced__force_private_mode, false)
        val settingsTheme =
            stringPref(R.string.pref__advanced__settings_theme, "auto")
        val showAppIcon =
            booleanPref(R.string.pref__advanced__show_app_icon, true)
    }

    val clipboard = ClipboardGroup(this, initIfAbsent)
    class ClipboardGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val cleanUpAfter =
            intPref(R.string.pref__clipboard__clean_up_after, 15)
        val cleanUpOld =
            booleanPref(R.string.pref__clipboard__clean_up_old, false)
        val enableHistory =
            booleanPref(R.string.pref__clipboard__enable_history, false)
        val enableInternal =
            booleanPref(R.string.pref__clipboard__enable_internal, true)
        val limitHistorySize =
            booleanPref(R.string.pref__clipboard__limit_history_size, true)
        val maxHistorySize =
            intPref(R.string.pref__clipboard__max_history_size, 25)
        val syncToSystem =
            booleanPref(R.string.pref__clipboard__sync_to_system, false)
        val syncToFloris =
            booleanPref(R.string.pref__clipboard__sync_to_floris, true)
    }

    val correction = CorrectionGroup(this, initIfAbsent)
    class CorrectionGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val autoCapitalization =
            booleanPref(R.string.pref__correction__auto_capitalization, true)
        val doubleSpacePeriod =
            booleanPref(R.string.pref__correction__double_space_period, true)
        val rememberCapsLockState =
            booleanPref(R.string.pref__correction__remember_caps_lock_state, false)
    }

    val devtools = DevtoolsGroup(this, initIfAbsent)
    class DevtoolsGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val enabled =
            booleanPref(R.string.pref__devtools__enabled, false)
        val showHeapMemoryStats =
            booleanPref(R.string.pref__devtools__show_heap_memory_stats, false)
    }

    val dictionary = DictionaryGroup(this, initIfAbsent)
    class DictionaryGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val enableSystemUserDictionary =
            booleanPref(R.string.pref__suggestion__enable_system_user_dictionary, true)
        val enableFlorisUserDictionary =
            booleanPref(R.string.pref__suggestion__enable_floris_user_dictionary, true)
    }

    val gestures = GesturesGroup(this, initIfAbsent)
    class GesturesGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val swipeUp =
            shadowStringPref(
                R.string.pref__gestures__swipe_up,
                SwipeAction.SHIFT
            ) { SwipeAction.fromString(it) }
        val swipeDown =
            shadowStringPref(
                R.string.pref__gestures__swipe_down,
                SwipeAction.HIDE_KEYBOARD
            ) { SwipeAction.fromString(it) }
        val swipeLeft =
            shadowStringPref(
                R.string.pref__gestures__swipe_left,
                SwipeAction.SWITCH_TO_NEXT_SUBTYPE
            ) { SwipeAction.fromString(it) }
        val swipeRight =
            shadowStringPref(
                R.string.pref__gestures__swipe_right,
                SwipeAction.SWITCH_TO_PREV_SUBTYPE
            ) { SwipeAction.fromString(it) }
        val spaceBarLongPress =
            shadowStringPref(
                R.string.pref__gestures__space_bar_long_press,
                SwipeAction.SHOW_INPUT_METHOD_PICKER
            ) { SwipeAction.fromString(it) }
        val spaceBarSwipeUp =
            shadowStringPref(
                R.string.pref__gestures__space_bar_swipe_up,
                SwipeAction.SWITCH_TO_CLIPBOARD_CONTEXT
            ) { SwipeAction.fromString(it) }
        val spaceBarSwipeLeft =
            shadowStringPref(
                R.string.pref__gestures__space_bar_swipe_left,
                SwipeAction.MOVE_CURSOR_LEFT
            ) { SwipeAction.fromString(it) }
        val spaceBarSwipeRight =
            shadowStringPref(
                R.string.pref__gestures__space_bar_swipe_right,
                SwipeAction.MOVE_CURSOR_RIGHT
            ) { SwipeAction.fromString(it) }
        val deleteKeySwipeLeft =
            shadowStringPref(
                R.string.pref__gestures__delete_key_swipe_left,
                SwipeAction.DELETE_CHARACTERS_PRECISELY
            ) { SwipeAction.fromString(it) }
        val swipeVelocityThreshold =
            shadowStringPref(
                R.string.pref__gestures__swipe_up,
                VelocityThreshold.NORMAL
            ) { VelocityThreshold.fromString(it) }
        val swipeDistanceThreshold =
            shadowStringPref(
                R.string.pref__gestures__swipe_up,
                DistanceThreshold.NORMAL
            ) { DistanceThreshold.fromString(it) }
    }

    val glide = GlideGroup(this, initIfAbsent)
    class GlideGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val enabled =
            booleanPref(R.string.pref__glide__enabled, false)
        val previewRefreshDelay =
            intPref(R.string.pref__glide__preview_refresh_delay, 150)
        val showPreview =
            booleanPref(R.string.pref__glide__show_preview, true)
        val showTrail =
            booleanPref(R.string.pref__glide__show_trail, true)
        val trailFadeDuration =
            intPref(R.string.pref__glide__trail_fade_duration, 200)
        val trailMaxLength =
            intPref(R.string.pref__glide__trail_max_length, 150)
    }

    val internal = InternalGroup(this, initIfAbsent)
    class InternalGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val isImeSetUp =
            booleanPref(R.string.pref__internal__is_ime_set_up, false)
        val versionOnInstall =
            stringPref(R.string.pref__internal__version_on_install, VersionName.DEFAULT_RAW)
        val versionLastUse =
            stringPref(R.string.pref__internal__version_last_use, VersionName.DEFAULT_RAW)
        val versionLastChangelog =
            stringPref(R.string.pref__internal__version_last_changelog, VersionName.DEFAULT_RAW)
    }

    val keyboard = KeyboardGroup(this, initIfAbsent)
    class KeyboardGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val bottomOffsetPortrait =
            intPref(R.string.pref__keyboard__bottom_offset_portrait, 0)
        val bottomOffsetLandscape =
            intPref(R.string.pref__keyboard__bottom_offset_landscape, 0)
        val fontSizeMultiplierPortrait =
            intPref(R.string.pref__keyboard__font_size_multiplier_portrait, 100)
        val fontSizeMultiplierLandscape =
            intPref(R.string.pref__keyboard__font_size_multiplier_landscape, 100)
        val heightFactor =
            stringPref(R.string.pref__keyboard__height_factor, "normal")
        val heightFactorCustom =
            intPref(R.string.pref__keyboard__height_factor__custom, 100)
        val hintedNumberRowMode =
            shadowStringPref(
                R.string.pref__keyboard__hinted_number_row_mode,
                KeyHintMode.ENABLED_SMART_PRIORITY
            ) { KeyHintMode.fromString(it) }
        val hintedSymbolsMode =
            shadowStringPref(
                R.string.pref__keyboard__hinted_symbols_mode,
                KeyHintMode.ENABLED_SMART_PRIORITY
            ) { KeyHintMode.fromString(it) }
        val keySpacingHorizontal =
            intPref(R.string.pref__keyboard__key_spacing_horizontal, 4)
        val keySpacingVertical =
            intPref(R.string.pref__keyboard__key_spacing_vertical, 10)
        val landscapeInputUiMode =
            shadowStringPref(
                R.string.pref__keyboard__landscape_input_ui_mode,
                LandscapeInputUiMode.DYNAMICALLY_SHOW
            ) { LandscapeInputUiMode.fromString(it) }
        val longPressDelay =
            intPref(R.string.pref__keyboard__long_press_delay, 300)
        val mergeHintPopupsEnabled =
            booleanPref(R.string.pref__keyboard__merge_hint_popups_enabled, false)
        val numberRow =
            booleanPref(R.string.pref__keyboard__number_row, true)
        val oneHandedMode =
            stringPref(R.string.pref__keyboard__one_handed_mode, OneHandedMode.OFF)
        val oneHandedModeScaleFactor =
            intPref(R.string.pref__keyboard__one_handed_mode_scale_factor, 87)
        val popupEnabled =
            booleanPref(R.string.pref__keyboard__popup_enabled, true)
        val soundEnabled =
            booleanPref(R.string.pref__keyboard__sound_enabled, true)
        var soundEnabledSystem =
            false
        val soundVolume =
            intPref(R.string.pref__keyboard__sound_volume, -1)
        val spaceBarSwitchesToCharacters =
            booleanPref(R.string.pref__keyboard__space_bar_switches_to_characters, true)
        val utilityKeyAction =
            shadowStringPref(
                R.string.pref__keyboard__utility_key_action,
                UtilityKeyAction.DYNAMIC_SWITCH_LANGUAGE_EMOJIS
            ) { UtilityKeyAction.fromString(it) }
        val utilityKeyEnabled =
            booleanPref(R.string.pref__keyboard__utility_key_enabled, true)
        val vibrationEnabled =
            booleanPref(R.string.pref__keyboard__vibration_enabled, true)
        var vibrationEnabledSystem =
            false
        val vibrationDuration =
            intPref(R.string.pref__keyboard__vibration_duration, -1)
        val vibrationStrength =
            intPref(R.string.pref__keyboard__vibration_strength, -1)
    }

    val localization = LocalizationGroup(this, initIfAbsent)
    class LocalizationGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val activeSubtypeId =
            intPref(R.string.pref__localization__active_subtype_id, Subtype.DEFAULT.id)
        val subtypes =
            stringPref(R.string.pref__localization__subtypes, "")
    }

    val smartbar = SmartbarGroup(this, initIfAbsent)
    class SmartbarGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val enabled =
            booleanPref(R.string.pref__smartbar__enabled, true)
    }

    val suggestion = SuggestionGroup(this, initIfAbsent)
    class SuggestionGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val api30InlineSuggestionsEnabled =
            booleanPref(R.string.pref__suggestion__api30_inline_suggestions_enabled, true)
        val blockPossiblyOffensive =
            booleanPref(R.string.pref__suggestion__block_possibly_offensive, true)
        val clipboardContentEnabled =
            booleanPref(R.string.pref__suggestion__clipboard_content_enabled, false)
        val clipboardContentTimeout =
            intPref(R.string.pref__suggestion__clipboard_content_timeout, 60)
        val displayMode =
            shadowStringPref(
                R.string.pref__suggestion__display_mode,
                CandidateView.DisplayMode.DYNAMIC_SCROLLABLE
            ) { CandidateView.DisplayMode.fromString(it) }
        val enabled =
            booleanPref(R.string.pref__suggestion__enabled, true)
        val usePrevWords =
            booleanPref(R.string.pref__suggestion__use_prev_words, true)
    }

    val theme = ThemeGroup(this, initIfAbsent)
    class ThemeGroup(d: Preferences, i: Boolean) : PrefGroup(d, i) {
        val mode =
            shadowStringPref(
                R.string.pref__theme__mode,
                ThemeMode.FOLLOW_SYSTEM
            ) { ThemeMode.fromString(it) }
        val dayThemeRef =
            stringPref(R.string.pref__theme__day_theme_ref, "assets:ime/theme/floris_day.json")
        val dayThemeAdaptToApp =
            booleanPref(R.string.pref__theme__day_theme_adapt_to_app, false)
        val nightThemeRef =
            stringPref(R.string.pref__theme__night_theme_ref, "assets:ime/theme/floris_night.json")
        val nightThemeAdaptToApp =
            booleanPref(R.string.pref__theme__night_theme_adapt_to_app, false)
        val sunriseTime =
            intPref(R.string.pref__theme__sunrise_time, TimeUtil.encode(6, 0))
        val sunsetTime =
            intPref(R.string.pref__theme__sunset_time, TimeUtil.encode(18, 0))
    }
}
