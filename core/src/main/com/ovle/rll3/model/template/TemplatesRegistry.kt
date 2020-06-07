package com.ovle.rll3.model.template

import com.ovle.rll3.assets.loader.EntityTemplates
import com.ovle.rll3.assets.loader.EntityViewTemplates
import com.ovle.rll3.assets.loader.StructureTemplates


object TemplatesRegistry {
    lateinit var entityTemplates: Map<TemplatesType, EntityTemplates>
    lateinit var entityViewTemplates: Map<TemplatesType, EntityViewTemplates>
    lateinit var structureTemplates: Map<TemplatesType, StructureTemplates>
}

