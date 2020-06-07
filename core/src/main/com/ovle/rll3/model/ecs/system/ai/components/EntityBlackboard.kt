package com.ovle.rll3.model.ecs.system.ai.components

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

data class EntityBlackboard(val entity: Entity,val  engine: Engine)