package com.ovle.rll3.model.template

import com.ovle.rll3.model.template.entity.EntityTemplates
import com.ovle.rll3.model.template.structure.StructureTemplates


object TemplatesRegistry {
    lateinit var entityTemplates: Map<TemplatesType, EntityTemplates>
    lateinit var structureTemplates: Map<TemplatesType, StructureTemplates>
}

