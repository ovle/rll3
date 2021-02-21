package com.ovle.rll3.model.module.quest

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent

class QuestOwnerComponent(
    var questIds: MutableList<String> = mutableListOf()
) : BaseComponent