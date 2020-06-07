package com.ovle.rll3.model.template.entity

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.template.AnimationTemplate
import com.ovle.rll3.model.template.SpawnTemplate

data class EntityTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String? = null,
    var state: Map<String, Any?> = mapOf(),
    var sprite: Map<String, GridPoint2>? = null,
    var portrait: Collection<GridPoint2>? = null,
    var animations: Collection<AnimationTemplate> = listOf(),
    var spawns: Collection<SpawnTemplate> = listOf()
)
