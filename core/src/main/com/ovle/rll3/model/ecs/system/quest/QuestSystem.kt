package com.ovle.rll3.model.ecs.system.quest

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.ecs.component.util.Mappers
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.ecs.entity.world
import com.ovle.rll3.model.ecs.system.EventSystem
import com.ovle.rll3.model.ecs.system.quest.QuestStatus.*
import ktx.ashley.get


class DebugQuestSystem : EventSystem() {

    private var descriptions = mutableListOf<QuestDescription>()


    override fun subscribe() {
        EventBus.subscribe<Event.GameStartedEvent> { onGameStartedEvent() }
        EventBus.subscribe<Event.TimeChanged> { onTimeChanged() }
//todo
//        EventBus.subscribe<Event.EntityInteractionEvent> { onEntityActionEvent(it.source, it.entity, it.interaction) }
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
            if (quest.status.isTerminal) continue
            val questInProcess = quest.status == InProcess

            when {
                questInProcess && description.successCondition.invoke() -> {
                    quest.status = Completed
                    send(Event.QuestStatusUpdated(quest))
                }
                questInProcess && description.failCondition?.invoke() ?: false -> {
                    quest.status = Failed
                    send(Event.QuestStatusUpdated(quest))
                }
            }
        }
    }

    private fun onEntityActionEvent(source: Entity, entity: Entity, action: String) {
        val world = world()!!
        val quests = world[Mappers.world]!!.quests
        when (action) {
            in questsToTake(source, quests, descriptions).ids() -> {
                takeQuest(source, entity, action, quests)
            }
            in questsToReward(source, quests, descriptions).ids() -> {
                takeQuestReward(source, entity, action, quests)
            }
        }
    }

    private fun questsToTake(source: Entity, quests: List<QuestInfo>, descriptions: List<QuestDescription>): List<QuestDescription> {
        val questsDescriptions = quests.performedBy(source).descriptions()
        return descriptions.filter { it !in questsDescriptions }
    }

    private fun questsToReward(source: Entity, quests: List<QuestInfo>, descriptions: List<QuestDescription>): List<QuestDescription> {
        val questsDescriptions = quests.performedBy(source).hasStatus(Completed).descriptions()
        return descriptions.filter { it in questsDescriptions }
    }

    private fun takeQuest(performer: Entity, holder: Entity, descriptionId: String, quests: MutableList<QuestInfo>) {
        if (descriptions.isEmpty()) return

        val description = descriptions.single { it.id == descriptionId }
        val newQuest = QuestInfo(
            id = randomId(),
            description = description,
            performer = performer,
            holder = holder
        )
        quests.add(newQuest)

        send(Event.QuestStatusUpdated(newQuest))
    }

    private fun takeQuestReward(performer: Entity, holder: Entity, descriptionId: String, quests: MutableList<QuestInfo>) {
        val description = descriptions.single { it.id == descriptionId }
        val quest = quests.quest(description)!!
        quest.status = Rewarded

        description.onSuccess?.invoke(quest)

        send(Event.QuestStatusUpdated(quest))
    }


    private fun List<QuestInfo>.performedBy(source: Entity) = filter { it.performer == source }
    private fun List<QuestInfo>.hasStatus(status: QuestStatus) = filter { it.status == status }
    private fun List<QuestInfo>.descriptions() = map { it.description }.toSet()
    private fun List<QuestInfo>.quest(id: String) = singleOrNull { it.id == id }
    private fun List<QuestInfo>.quest(description: QuestDescription) = singleOrNull { it.description == description }
    private fun List<QuestDescription>.ids() = map { it.id }
}