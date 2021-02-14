package com.ovle.rll3.model.template.structure

import com.ovle.rll3.assets.loader.StructureTemplates
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.model.template.TemplatesType
import com.ovle.rll3.model.template.TemplatesType.Common

fun structureTemplates(type: TemplatesType = Common, name: String? = null): StructureTemplates {
    val structureTemplates = TemplatesRegistry.structureTemplates
    val result = structureTemplates.getValue(Common).templates.toMutableList()
    if (type != Common) result += structureTemplates.getValue(type).templates
    if (name != null) result.removeIf { it.name != name }

    return StructureTemplates(result)
}