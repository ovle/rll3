package com.ovle.rll3.model.template

import com.ovle.rll3.model.template.EntityTemplatesRegistry.entityTemplates
import com.ovle.rll3.model.template.EntityTemplatesType.Common


object EntityTemplatesRegistry {
    lateinit var entityTemplates: Map<EntityTemplatesType, EntityTemplates>
}

enum class EntityTemplatesType(val value: String) {
    Village("village"),
    Caves("caves"),
    Dungeon("dungeon"),
    Common("common")
}

fun entityTemplates(type: EntityTemplatesType = Common): EntityTemplates {
    val result = entityTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += entityTemplates.getValue(type).templates
    return EntityTemplates(result)
}

fun entityTemplate(type: EntityTemplatesType = Common, name: String) =
    entityTemplates(type).templates.single { it.name == name }