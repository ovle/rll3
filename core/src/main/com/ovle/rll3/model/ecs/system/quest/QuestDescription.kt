package com.ovle.rll3.model.ecs.system.quest

typealias QuestCondition = (() -> Boolean)
typealias QuestHook = (() -> Unit)?

class QuestDescription(
    val id: String,
    val title: String,
    val description: String,
    val precondition: QuestCondition,
    val successCondition: QuestCondition,
    val failCondition: QuestCondition?,
    val onSuccess: QuestHook = null,
    val onFail: QuestHook = null,
    val onFinish: QuestHook = null
) {
}