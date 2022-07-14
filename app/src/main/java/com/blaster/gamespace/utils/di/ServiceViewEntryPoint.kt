package com.blaster.gamespace.utils.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.blaster.gamespace.data.AppSettings
import com.blaster.gamespace.data.SystemSettings
import com.blaster.gamespace.utils.GameModeUtils
import com.blaster.gamespace.utils.ScreenUtils


@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceViewEntryPoint {
    fun appSettings(): AppSettings
    fun systemSettings(): SystemSettings
    fun screenUtils(): ScreenUtils
    fun gameModeUtils(): GameModeUtils
}
