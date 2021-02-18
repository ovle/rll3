package com.ovle.rll3.model.module.template

import com.ovle.rll3.model.module.core.component.BaseComponent
import com.ovle.rll3.model.template.entity.EntityTemplate
import com.ovle.rll3.model.template.entity.view.EntityViewTemplate

class TemplateComponent(
    val template: EntityTemplate,
    val viewTemplate: EntityViewTemplate?
) : BaseComponent