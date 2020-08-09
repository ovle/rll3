package com.ovle.rll3.model.module.quest

import com.ovle.rll3.QuestCondition
import com.ovle.rll3.QuestHook


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