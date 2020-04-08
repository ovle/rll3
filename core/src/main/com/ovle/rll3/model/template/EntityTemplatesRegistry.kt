package com.ovle.rll3.model.template

import com.ovle.rll3.model.template.EntityTemplatesRegistry.entityTemplates
import com.ovle.rll3.model.template.EntityTemplatesType.Common


object EntityTemplatesRegistry {
    lateinit var entityTemplates: Map<EntityTemplatesType, EntityTemplates>
}

enum class EntityTemplatesType(val value: String) {
    Caves("caves"),
    Dungeon("dungeon"),
    Common("common")
}

fun entityTemplates(type: EntityTemplatesType) =
    EntityTemplates(entityTemplates.getValue(type).templates + entityTemplates.getValue(Common).templates)
