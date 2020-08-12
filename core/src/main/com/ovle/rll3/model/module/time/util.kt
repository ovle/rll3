package com.ovle.rll3.model.module.time

fun deltaTicks(deltaTime: Float) = (deltaTime * ticksInTurn * turnsInSecond).toInt()