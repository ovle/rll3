package com.ovle.rll3.model.module.skill

import com.ovle.rll3.model.module.core.Module
import ktx.inject.Context

class SkillModule: Module {

    override fun systems(context: Context) = listOf(
        SkillSystem()
    )
}