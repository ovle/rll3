package com.ovle.rll3.model.ecs.system.time

fun deltaTicks(deltaTime: Float) = (deltaTime * ticksInTurn * turnsInSecond).toInt()