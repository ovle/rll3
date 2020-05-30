package com.ovle.rll3.model.ecs.system.quest

import com.badlogic.ashley.core.Entity

/**
 * quest taken by the holder
 */
data class QuestInfo(
    val id: String,
    val description: QuestDescription,
    val performer: Entity,
    val holder: Entity,

    var status: QuestStatus = QuestStatus.InProcess
)

enum class QuestStatus(val isTerminal: Boolean) {
    InProcess(false),
    Completed(false),
    Rewarded(true),
    Failed(true);
}