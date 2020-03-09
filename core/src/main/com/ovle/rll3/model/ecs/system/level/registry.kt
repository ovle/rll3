package com.ovle.rll3.model.ecs.system.level

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelInfo


object LevelRegistry {

    private val levelsById = mutableMapOf<LevelId, LevelInfo>()
    private val levelsByDescriptionId = mutableMapOf<LevelDescriptionId, LevelInfo>()
    private val entitiesByLevel = mutableMapOf<LevelId, Collection<StoredEntity>?>()

    fun levelInfo(levelId: LevelId) = levelsById[levelId]

    fun levelInfoByDesciption(levelDescriptionId: LevelDescriptionId) = levelsByDescriptionId[levelDescriptionId]

    fun addLevel(levelInfo: LevelInfo) {
        levelsById[levelInfo.id] = levelInfo
        levelsByDescriptionId[levelInfo.descriptionId] = levelInfo
    }

    fun store(levelId: LevelId): Collection<Entity> {
        val level = levelsById[levelId]
        val entitiesToStore = level!!.objects.toList()
        val componentsToStore = entitiesToStore.map { StoredEntity(it.components.toList()) }

        entitiesByLevel[levelId] = componentsToStore
        level.objects.clear()

        return entitiesToStore
    }

    fun restore(levelId: LevelId): Collection<StoredEntity>? {
        val result = entitiesByLevel[levelId]?.toList()
        clearStoredEntities(levelId)
        return result
    }

    private fun clearStoredEntities(levelId: LevelId) {
        entitiesByLevel[levelId] = null
    }

    fun clear() {
        levelsById.clear()
        levelsByDescriptionId.clear()
        entitiesByLevel.clear()
    }
}