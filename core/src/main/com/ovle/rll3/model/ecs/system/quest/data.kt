package com.ovle.rll3.model.ecs.system.quest

import com.badlogic.ashley.core.EntitySystem
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.allEntities
import com.ovle.rll3.model.ecs.entity.entityNullable
import com.ovle.rll3.model.ecs.entity.levelInfo
import com.ovle.rll3.screen.game.GameCreator.Companion.firstLevelDescId
import ktx.ashley.get

//todo most quest will not be bounded by particular level
fun questDescriptions(entitySystem: EntitySystem) =
    arrayOf(
        QuestDescription(
            id="q1",
            title="Kill the brute",
            description= """
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
                val levelInfo = entitySystem.levelInfo()
                levelInfo.description.id != firstLevelDescId
            },
            onSuccess = {
                send(Event.LogEvent("thank you!"))
            }
        )
    )