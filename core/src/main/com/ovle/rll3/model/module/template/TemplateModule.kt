package com.ovle.rll3.model.module.template

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.module.core.Module
import com.ovle.rll3.model.template.entity.entityViewTemplate
import ktx.inject.Context

class TemplateModule: Module {
    override fun components(): List<Component> {
        return super.components()
    }

//    val viewTemplate = entityViewTemplate(name = template.name)
//    TemplateComponent(template, viewTemplate),
}