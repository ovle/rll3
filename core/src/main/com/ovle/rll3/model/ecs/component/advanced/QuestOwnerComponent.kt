package com.ovle.rll3.model.ecs.component.advanced

import com.badlogic.ashley.core.Component

class QuestOwnerComponent(
    var questIds: MutableList<String> = mutableListOf()
) : Component