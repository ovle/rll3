package com.ovle.rll3.model.template.entity

import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.TemplatesType.Common

fun entityTemplates(type: TemplatesType = Common): EntityTemplates {
    val entityTemplates = TemplatesRegistry.entityTemplates
    val result = entityTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += entityTemplates.getValue(type).templates
    return EntityTemplates(result)
}

fun entityTemplate(type: TemplatesType? = null, name: String) =
    (if (type == null) TemplatesRegistry.entityTemplates.values.flatMap { it.templates }
     else entityTemplates(type).templates)
        .single { it.name == name }