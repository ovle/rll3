package com.ovle.rll3.model.template

import com.badlogic.gdx.math.GridPoint2

enum class EntityType {
    Any,
    Env,
    Creature
}

data class EntityTemplates(val templates: Collection<EntityTemplate>)

data class EntityTemplate(
    var name: String = "",
    var type: EntityType = EntityType.Any,
    var version: String = "0.1",
    var description: String = "",
    var state: Map<String, Any?> = mapOf(),
    var sprite: GridPoint2? = null,
    var animations: Collection<AnimationTemplate> = listOf()
)
