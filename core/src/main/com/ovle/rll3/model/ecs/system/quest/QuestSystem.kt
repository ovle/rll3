package com.ovle.rll3.model.ecs.system.quest

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.ecs.entity.world
import com.ovle.rll3.model.ecs.system.EventSystem
import ktx.ashley.get


class DebugQuestSystem : EventSystem() {

    private var descriptions = mutableListOf<QuestDescription>()


    override fun subscribe() {
        EventBus.subscribe<Event.GameStartedEvent> { onGameStartedEvent() }
        EventBus.subscribe<Event.TimeChanged> { onTimeChanged() }

        EventBus.subscribe<Event.EntityActionEvent> { onEntityActionEvent(it.source, it.entity, it.action) }
    }

    private fun onEntityActionEvent(source: Entity, entity: Entity, action: String) {
        when (action) {
            in questActions(source) -> {
                takeQuest(performer = source, holder = entity, questId = action)
            }
        }
    }

    //todo availability
    private fun questActions(source: Entity) = descriptions.map { it.id }

    private fun takeQuest(performer: Entity, holder: Entity, questId: String) {
        if (descriptions.isEmpty()) return

        val world = world()!!
        val quests = world[Mappers.world]!!.quests

        val description = descriptions.single { it.id == questId }
        val newQuest = QuestInfo(
            id = randomId(),
            description = description,
            performer = performer,
            holder = holder
        )
        quests.add(newQuest)

        send(Event.QuestStatusUpdated(newQuest))
    }

    private fun onGameStartedEvent() {
        descriptions = questDescriptions(this).toMutableList()
    }

    private fun onTimeChanged() {
        if (descriptions.isEmpty()) return

        val world = world()!!
        val quests = world[Mappers.world]!!.quests

        for (description in descriptions) {
            val quest = quests.singleOrNull { it.description.id == description.id } ?: continue
            if (quest.status.finished) continue

            when {
                description.successCondition.invoke() -> {
                    quest.status = QuestStatus.Completed
                    send(Event.QuestStatusUpdated(quest))
                }
                description.failCondition?.invoke() ?: false -> {
                    quest.status = QuestStatus.Failed
                    send(Event.QuestStatusUpdated(quest))
                }
            }
        }
    }
}