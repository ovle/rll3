package com.ovle.rll3.model.template.entity

import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.TemplatesType.Common

fun entityTemplates(type: TemplatesType = Common): EntityTemplates {
    val result = TemplatesRegistry.entityTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += TemplatesRegistry.entityTemplates.getValue(type).templates
    return EntityTemplates(result)
}

fun entityTemplate(type: TemplatesType = Common, name: String) =
    entityTemplates(type).templates.single { it.name == name }