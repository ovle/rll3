package com.ovle.rll3.model.ecs.component.advanced

import com.ovle.rll3.model.ecs.component.BaseComponent

class QuestOwnerComponent(
    var questIds: MutableList<String> = mutableListOf()
) : BaseComponent