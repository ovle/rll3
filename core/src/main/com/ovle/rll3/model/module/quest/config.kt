package com.ovle.rll3.model.module.quest

import com.badlogic.ashley.core.Engine
import com.ovle.rlUtil.event.EventBus.send
import com.ovle.rll3.event.LogCommand
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.util.entityNullable
import com.ovle.rll3.model.module.life.Components.life
import com.ovle.rll3.model.module.quest.dto.QuestDescription
import ktx.ashley.get

//todo most quest will not be bounded by particular level
fun questDescriptions(engine: Engine) =
    arrayOf(
        QuestDescription(
            id = "q1",
            title = "Kill the brute",
            description = """
                I'm so tired of these stupid barbarians. Please, kill one of them.
                """.trimIndent(),
            precondition = {
                true
            },
            successCondition = {
                val entities = engine.allEntities().toList()
                val e = entityNullable("b1", entities)
                    ?: return@QuestDescription false

                val lc = e[life]!!
                lc.isDead
            },
            failCondition = {
                false
            },
            onSuccess = {
                send(LogCommand("thank you!"))
            }
        )
    )