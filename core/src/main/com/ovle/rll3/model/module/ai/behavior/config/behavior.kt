package com.ovle.rll3.model.module.ai.behavior.config

import com.ovle.rll3.model.module.ai.behavior.BehaviorTemplate
import com.ovle.rll3.model.module.ai.behavior.config.bt.*
import com.ovle.rll3.model.module.task.EntityConditions.isAttackTarget
import com.ovle.rll3.model.module.task.EntityConditions.isHaveAttackTarget
import com.ovle.rll3.model.module.task.EntityConditions.isHaveAvailableAttackTarget
import com.ovle.rll3.model.module.task.EntityConditions.isHaveAvailableFood
import com.ovle.rll3.model.module.task.EntityConditions.isHungry
import com.ovle.rll3.model.module.task.EntityConditions.isInDanger


val baseBehavior = BehaviorTemplate(
    name = "base",
    selector = { e, l ->
        when {
            isHungry(e, l) -> when {
                isHaveAvailableFood(e, l) -> eatBt
                else -> huntBt
            }
            isInDanger(e, l) -> when {
                else -> runawayBt
            }
            else -> when {
                isHaveAttackTarget(e, l) -> combatBt
                isAttackTarget(e, l) -> startCombatBt
                isHaveAvailableAttackTarget(e, l) -> startCombatBt
                else -> restBt
            }
        }
    }
)

//todo
val behaviors = listOf(
    baseBehavior
)