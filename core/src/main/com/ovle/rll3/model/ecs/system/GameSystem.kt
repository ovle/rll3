package com.ovle.rll3.model.ecs.system

import com.badlogic.gdx.Gdx
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.EventBus.subscribe
import com.ovle.rll3.model.ecs.component.dto.PlayerInfo
import com.ovle.rll3.model.ecs.component.dto.WorldInfo
import com.ovle.rll3.model.ecs.entity.*
import com.ovle.rll3.model.util.random
import com.ovle.rll3.persistance.bytes
import com.ovle.rll3.persistance.stored
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.random.Random


class GameSystem : EventSystem() {

    override fun subscribe() {
        subscribe<Event.GameStarted> { createWorld(it.player, it.world) }
//        subscribe<Event.DebugSaveGame> { saveGame() }
    }

    private fun createWorld(player: PlayerInfo, world: WorldInfo) {
        val worldEntity = newWorld(world, engine)
        val playerEntity = newPlayer(player, engine)
        random = Random(world.seed)

        send(Event.WorldInit(world))
    }

//    private fun saveGame() {
//        val dir = Gdx.files.localStoragePath
//        val saveName = "autosave"
//        val path = Paths.get("$dir/$saveName/")
//        val path1 = Paths.get(path.toString(), "e")
//        val path2 = Paths.get(path.toString(), "l")
//        if (!Files.exists(path)) {
//            Files.createDirectory(path)
//            Files.createFile(path1)
//            Files.createFile(path2)
//        }
//
//        val toFile1 = path1.toFile()
//        allEntities()
//            .map { it.stored().bytes() }
//            .forEach { toFile1.writeBytes(it) }
//
//        val toFile2 = path2.toFile()
//        val curLevel = levelInfo()
//        LevelRegistry.store(curLevel.id)
//        LevelRegistry.levels().forEach {
//            toFile2.writeBytes(it.bytes())
//        }
//        //todo
////        LevelRegistry.levelEntities().forEach {
////            toFile2.writeBytes(it.bytes())
////        }
//    }
}
