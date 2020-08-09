package com.ovle.rll3.model.module.quest

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.Mappers
import com.ovle.rll3.model.module.core.entity.allEntities
import com.ovle.rll3.model.module.core.entity.entityNullable
import ktx.ashley.get

//todo most quest will not be bounded by particular level
fun questDescriptions(entitySystem: EntitySystem) =
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
                val entities = entitySystem.allEntities().toList()
                val e = entityNullable("b1", entities)
                    ?: return@QuestDescription false

                val lc = e[Mappers.living]!!
                lc.isDead
            },
            failCondition = {
                false
            },
            onSuccess = {
                send(Event.GameEvent.LogCommand("thank you!"))
            }
        )
    )