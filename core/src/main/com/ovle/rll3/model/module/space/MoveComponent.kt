package com.ovle.rll3.model.module.space

import com.ovle.rlUtil.gdx.ashley.component.BaseComponent
import com.ovle.rll3.ExactTurn


class MoveComponent(val turnsPerTile: ExactTurn = 0.25, val path: MovePath = MovePath()) : BaseComponent
