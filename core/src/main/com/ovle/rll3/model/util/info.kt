package com.ovle.rll3.model.util

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.module.core.Components.template
import com.ovle.rll3.model.module.core.entity.name
import com.ovle.rll3.model.module.life.LifeComponent
import com.ovle.rll3.model.module.space.PositionComponent
import ktx.ashley.has

fun Collection<Entity>.info(): String {
    val entityInfos = this.map { it.info() }
    return entityInfos.groupBy { it }.entries.joinToString(", ") { (k, v) -> "${v.count()}x $k" }
}

fun Any?.info(recursive: Boolean = false): String = when {
    this == null -> "(nothing)"
    this is Entity -> when {
        this.has(template) -> this.name()
        else -> "(unknown entity)"
    } + if (recursive) {
        this.components.map { it.info() }
            .filterNot { it.isBlank() }
            .joinToString(
                prefix = " {\n",
                postfix = "\n}",
                separator = "\n",
                transform = { s -> "  $s" }
            )
    } else ""
    this is Component -> when {
        this is LifeComponent -> "he: [he:${health}/st:${stamina}/hu:${hunger}]"
        this is PositionComponent -> "pos: [${gridPosition.info()}]"
        else -> ""
    }
    this is GridPoint2 -> this.toString()
    else -> "(unknown)"
}