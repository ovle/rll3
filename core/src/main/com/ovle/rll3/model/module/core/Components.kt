package com.ovle.rll3.model.module.core

import com.ovle.rlUtil.gdx.ashley.component.mapper
import com.ovle.rll3.model.module.core.component.CoreComponent
import com.ovle.rll3.model.module.core.component.template.TemplateComponent

object Components {
    val core = mapper<CoreComponent>()
    val template = mapper<TemplateComponent>()
}