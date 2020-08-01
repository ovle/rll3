package com.ovle.rll3.screen.game


import com.ovle.rll3.model.ecs.component.dto.LevelDescription
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.entity.randomId
import com.ovle.rll3.model.ecs.system.level.LevelDescriptionId
import com.ovle.rll3.model.procedural.config.*
import com.ovle.rll3.persistance.Preferences

class GameCreator {

    companion object {
        const val firstLevelDescId: LevelDescriptionId = "1"
        const val defaultPlayerTemplateName = "barbarian"

        //todo test
        val testSeed = 123L
    }


    fun player(): PlayerInfo {
        return PlayerInfo(randomId(), Preferences.playerTemplateName())
    }

    fun world(): WorldInfo {
        return WorldInfo(
            id = randomId(),
            seed = testSeed,
            entryPoint = firstLevelDescId,
            levels = listOf(
                LevelDescription(id = firstLevelDescId, params = villageLevelParams.templateName, connections = listOf("2")),
                LevelDescription(id = "2", params = dungeonLevelParams.templateName, connections = listOf("3.1", "3.2")),
                LevelDescription(id = "3.1", params = caveLevelParams.templateName, connections = listOf("4")),
                LevelDescription(id = "3.2", params = dungeonLevelParams.templateName, connections = listOf("4")),
                LevelDescription(id = "4", params = caveLevelParams.templateName, connections = listOf())
            )
        )
    }
}