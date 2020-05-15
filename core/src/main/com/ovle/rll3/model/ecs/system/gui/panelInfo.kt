package com.ovle.rll3.model.ecs.system.gui

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextArea

class EntityPanelInfo {
    var entity: Entity? = null

    lateinit var portraitWidget: Image
    lateinit var nameWidget: Label

    lateinit var healthInfoWidget: Label
    lateinit var staminaInfoWidget: Label
}

class WorldPanelInfo {
    lateinit var portraitWidget: Image
    lateinit var levelNameWidget: Label
    lateinit var timeWidget: Label
}

class LogPanelInfo {
    var opened: Boolean = true

    lateinit var textWidget: TextArea
}