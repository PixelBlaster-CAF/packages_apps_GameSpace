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
package com.blaster.gamespace.widget.tiles

import android.app.GameManager
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.blaster.gamespace.R
import com.blaster.gamespace.utils.GameModeUtils.Companion.describeGameMode
import com.blaster.gamespace.utils.PerformanceModeManager
import com.blaster.gamespace.utils.SharedPreferenceUtils.getPerformanceModeStatus
import com.blaster.gamespace.utils.SharedPreferenceUtils.getSessionHandle
import com.blaster.gamespace.utils.SharedPreferenceUtils.setPerformanceModeStatus
import com.blaster.gamespace.utils.SharedPreferenceUtils.setSessionHandle
import com.blaster.gamespace.utils.di.ServiceViewEntryPoint
import com.blaster.gamespace.utils.entryPointOf


class GameModeTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BaseTile(context, attrs) {

    private var mIsPerformanceModeOn = false

    private val gameModeUtils by lazy {
        context.entryPointOf<ServiceViewEntryPoint>().gameModeUtils()
    }

    private val modes = listOf(
        GameManager.GAME_MODE_STANDARD,
        GameManager.GAME_MODE_PERFORMANCE,
        GameManager.GAME_MODE_BATTERY,
    )

    private var activeMode = GameManager.GAME_MODE_STANDARD
        set(value) {
            field = value
            summary?.text = context.describeGameMode(value)
            isSelected = value != GameManager.GAME_MODE_STANDARD
            gameModeUtils.setActiveGameMode(systemSettings, value)
            if(value==2)
            {
                val handle = PerformanceModeManager.getInstance().turnOnPerformanceMode()
                if (-1 != handle) {
                    setSessionHandle(this.context, handle)
                    setPerformanceModeStatus(this.context, true)
                    return
                }
            }
            else
            {
                val handle2 = getSessionHandle(this.context)
                if (-1 == handle2) {
                } else if (-1 != PerformanceModeManager.getInstance()
                        .turnOffPerformanceMode(handle2)
                ) {
                    setSessionHandle(this.context, -1)
                    setPerformanceModeStatus(this.context, false)
                } else {
                }
            }
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val performanceModeStatus = getPerformanceModeStatus(this.context)
        mIsPerformanceModeOn = performanceModeStatus
        tryTurnOnPerformanceMode(this.context);
        title?.text = context.getString(R.string.game_mode_title)
        activeMode = gameModeUtils.activeGame?.mode ?: GameManager.GAME_MODE_STANDARD
        icon?.setImageResource(R.drawable.ic_speed)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        val current = modes.indexOf(activeMode)
        activeMode = modes[if (current == modes.size - 1) 0 else current + 1]
    }
    private fun tryTurnOnPerformanceMode(appContext: Context) {
        val lastHandle = getSessionHandle(appContext)
        if (isPerformanceModeOn() && -1 == lastHandle) {
            val handle = PerformanceModeManager.getInstance().turnOnPerformanceMode()
            if (-1 != handle) {
                setSessionHandle(appContext, handle)
            }
        }
    }
    fun isPerformanceModeOn(): Boolean {
        return mIsPerformanceModeOn
    }

}
