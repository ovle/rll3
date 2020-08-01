package com.ovle.rll3.model.ecs.component.special

import com.ovle.rll3.model.ecs.component.BaseComponent
import com.ovle.rll3.model.ecs.component.dto.TimeInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.system.quest.QuestInfo
import java.io.Serializable

class WorldComponent(
    var world: WorldInfo,
    var time: TimeInfo,
    val quests: MutableList<QuestInfo> = mutableListOf()
): BaseComponent, Serializable