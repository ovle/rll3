package com.ovle.rll3.model.module.quest

import com.ovle.rll3.model.module.core.component.BaseComponent

class QuestOwnerComponent(
    var questIds: MutableList<String> = mutableListOf()
) : BaseComponent