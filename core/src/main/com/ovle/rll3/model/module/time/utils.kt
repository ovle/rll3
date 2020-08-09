package com.ovle.rll3.model.module.time

import com.ovle.rll3.model.module.time.ticksInTurn
import com.ovle.rll3.model.module.time.turnsInSecond

fun deltaTicks(deltaTime: Float) = (deltaTime * ticksInTurn * turnsInSecond).toInt()