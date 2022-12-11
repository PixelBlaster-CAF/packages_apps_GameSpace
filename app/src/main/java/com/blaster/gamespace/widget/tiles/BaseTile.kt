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

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blaster.gamespace.R
import com.blaster.gamespace.utils.di.ServiceViewEntryPoint
import com.blaster.gamespace.utils.entryPointOf


abstract class BaseTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnClickListener {
    init {
        isClickable = true
        isFocusable = true
        prepareLayout()
    }

    val appSettings by lazy { context.entryPointOf<ServiceViewEntryPoint>().appSettings() }
    val systemSettings by lazy { context.entryPointOf<ServiceViewEntryPoint>().systemSettings() }

    val title: TextView?
        get() = findViewById(R.id.tile_title)

    val summary: TextView?
        get() = findViewById(R.id.tile_summary)

    val icon: ImageView?
        get() = findViewById(R.id.tile_icon)

    private fun prepareLayout() {
        LayoutInflater.from(context)
            .inflate(R.layout.panel_tile, this, true)
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        isSelected = !isSelected
    }

}