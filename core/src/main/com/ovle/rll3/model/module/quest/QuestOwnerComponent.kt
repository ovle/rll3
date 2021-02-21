package com.ovle.rll3.model.module.quest

import com.ovle.rll3.model.module.core.component.EntityComponent

class QuestOwnerComponent(
    var questIds: MutableList<String> = mutableListOf()
) : EntityComponent()