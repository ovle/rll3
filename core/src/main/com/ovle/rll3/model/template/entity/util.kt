package com.ovle.rll3.model.template.entity

import com.ovle.rll3.assets.loader.EntityTemplates
import com.ovle.rll3.assets.loader.EntityViewTemplates
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.TemplatesType.Common

fun entityTemplates(type: TemplatesType = Common): EntityTemplates {
    val entityTemplates = TemplatesRegistry.entityTemplates
    val result = entityTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += entityTemplates.getValue(type).templates
    return EntityTemplates(result)
}

fun entityViewTemplates(type: TemplatesType = Common): EntityViewTemplates {
    val entityViewTemplates = TemplatesRegistry.entityViewTemplates
    val result = entityViewTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += entityViewTemplates.getValue(type).templates
    return EntityViewTemplates(result)
}

fun entityTemplate(type: TemplatesType? = null, name: String) =
    (if (type == null) TemplatesRegistry.entityTemplates.values.flatMap { it.templates }
     else entityTemplates(type).templates)
        .single { it.name == name }

fun entityViewTemplate(type: TemplatesType? = null, name: String) =
    (if (type == null) TemplatesRegistry.entityViewTemplates.values.flatMap { it.templates }
     else entityViewTemplates(type).templates)
        .singleOrNull { it.name == name }