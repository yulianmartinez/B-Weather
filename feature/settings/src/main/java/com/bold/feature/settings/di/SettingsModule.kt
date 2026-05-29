package com.bold.feature.settings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bold.core.model.settings.SettingsRepository
import com.bold.feature.settings.data.repository.SettingsRepositoryImpl
import com.bold.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.bold.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

val settingsModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory { UpdateSettingsUseCase(get()) }
    viewModelOf(::SettingsViewModel)
}
