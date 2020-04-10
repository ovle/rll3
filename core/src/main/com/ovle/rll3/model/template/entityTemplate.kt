package com.ovle.rll3.model.template

import com.badlogic.gdx.math.GridPoint2

data class EntityTemplates(val templates: Collection<EntityTemplate>)

data class EntityTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String = "",
    var state: Map<String, Any?> = mapOf(),
    var sprite: Collection<GridPoint2>? = null,
    var animations: Collection<AnimationTemplate> = listOf(),
    var spawns: Collection<SpawnTemplate> = listOf()
)
