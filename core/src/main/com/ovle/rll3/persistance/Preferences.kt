package com.ovle.rll3.persistance

import com.badlogic.gdx.Gdx

object Preferences {

    private const val appKey = "ovle.rll3"

    private const val playerTemplateNameKey = "playerTemplateName"

    private fun getString(key: String, defaultValue: String? = null) =
        Gdx.app.getPreferences(appKey).getString(key, defaultValue)

    private fun setString(key: String, value: String) =
        Gdx.app.getPreferences(appKey).putString(key, value)

    fun flush() {
        Gdx.app.getPreferences(appKey).flush()
    }
}