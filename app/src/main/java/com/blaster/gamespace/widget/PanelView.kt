/*
 * Copyright (C) 2021 Chaldeaprjkt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blaster.gamespace.widget

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.os.SystemProperties
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.blaster.gamespace.R
import com.blaster.gamespace.utils.dp
import com.blaster.gamespace.utils.isPortrait
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.max
import kotlin.math.min


class PanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var defaultY: Float? = null
    var relativeY = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.panel_view, this, true)
        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        applyRelativeLocation()
        cpuTemperature()
        brightnessSlider()
    }

    private fun getCpuTemperature(): Float {
        val process: Process
        val prop = SystemProperties.get("ro.pb.cpu_no")
        return try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone$prop/temp")
            process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line: String = reader.readLine()
            if (line != null) {
                val temp = line.toFloat()
                temp / 1000.0f
            } else {
                51.0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0f
        }
    }

    private fun cpuTemperature() {
        val temp = getCpuTemperature()
        val degree = "\u2103"
        val cpuTemp:TextView = findViewById(R.id.cpuTemp)
        cpuTemp.text = "$temp$degree"
    }

    private fun brightnessSlider() {
        var lightBar:SeekBar = findViewById(R.id.seekBar);
        var brightness = Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, 0
        )
        lightBar.setProgress(brightness)
        val contentResolver: ContentResolver = context.contentResolver
        val setting: Uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)

        val observer: ContentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                var brightness1 = Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, 0
                )
                lightBar.setProgress(brightness1)
            }

            override fun deliverSelfNotifications(): Boolean {
                return true
            }
        }
        contentResolver.registerContentObserver(setting, false, observer)

        lightBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, progress
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun applyRelativeLocation() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        layoutParams.height =
            if (wm.isPortrait()) wm.maximumWindowMetrics.bounds.height() / 2 else LayoutParams.MATCH_PARENT

        doOnLayout {
            if (defaultY == null)
                defaultY = y

            y = if (wm.isPortrait()) {
                val safeArea = rootWindowInsets.getInsets(WindowInsets.Type.systemBars())
                val minY = safeArea.top + 16.dp
                val maxY = safeArea.top + (parent as View).height - safeArea.bottom - height - 16.dp
                min(max(relativeY, minY), maxY).toFloat()
            } else {
                defaultY ?: 16f
            }

        }
    }

}
