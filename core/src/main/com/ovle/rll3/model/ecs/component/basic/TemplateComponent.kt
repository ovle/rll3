package com.ovle.rll3.model.ecs.component.basic

import com.badlogic.ashley.core.Component
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.view.EntityViewTemplate

class TemplateComponent(
    val template: EntityTemplate,
    val viewTemplate: EntityViewTemplate?
) : Component