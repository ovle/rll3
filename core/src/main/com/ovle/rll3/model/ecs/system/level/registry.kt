package com.ovle.rll3.model.ecs.system.level

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.component.LevelInfo


object LevelRegistry {

    private val levelsById = mutableMapOf<LevelId, LevelInfo>()
    private val connections = mutableMapOf<ConnectionId, ConnectionData>()
    private val storedEntities = mutableMapOf<LevelId, Collection<StoredEntity>?>()

    fun levelInfo(levelId: LevelId) = levelsById[levelId]!!
    fun addLevel(levelInfo: LevelInfo) {
        levelsById[levelInfo.id] = levelInfo
    }

    fun connection(connectionId: ConnectionId?) = connections[connectionId]
    fun addConnection(connectionId: ConnectionId, connectionData: ConnectionData) {
        connections[connectionId] = connectionData
    }

    fun store(levelId: LevelId): Collection<Entity> {
        val level = levelsById[levelId]
        val entitiesToStore = level!!.objects.toList()
        val componentsToStore = entitiesToStore.map { StoredEntity(it.components.toList()) }

        storedEntities[levelId] = componentsToStore
        level.objects.clear()

        return entitiesToStore
    }

    fun restore(levelId: LevelId): Collection<StoredEntity>? {
        val result = storedEntities[levelId]?.toList()
        clearStoredEntities(levelId)
        return result
    }

    private fun clearStoredEntities(levelId: LevelId) {
        storedEntities[levelId] = null
    }
}