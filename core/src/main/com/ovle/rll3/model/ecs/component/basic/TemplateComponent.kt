package com.ovle.rll3.model.ecs.component.basic

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.view.EntityViewTemplate

class TemplateComponent(
    val template: EntityTemplate,
    val viewTemplate: EntityViewTemplate?
) : BaseComponent