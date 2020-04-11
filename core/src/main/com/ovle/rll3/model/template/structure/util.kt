package com.ovle.rll3.model.template.structure

import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.TemplatesType.Common

fun structureTemplates(type: TemplatesType = Common): StructureTemplates {
    val structureTemplates = TemplatesRegistry.structureTemplates
    val result = structureTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += structureTemplates.getValue(type).templates
    return StructureTemplates(result)
}

fun structureTemplate(type: TemplatesType = Common, name: String) =
    structureTemplates(type).templates.single { it.name == name }