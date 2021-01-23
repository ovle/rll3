package com.ovle.rll3.model.module.space

import com.ovle.rll3.ExactTurn
import com.ovle.rll3.model.module.core.component.BaseComponent


class MoveComponent(val turnsPerTile: ExactTurn = 0.25, val path: MovePath = MovePath()) : BaseComponent
