package com.ovle.rll3.model.util

import com.badlogic.gdx.math.GridPoint2
import com.ovle.rlUtil.Tile
import com.ovle.rlUtil.TileArray
import com.ovle.rll3.model.module.light.LightSourceComponent
import com.ovle.rll3.model.module.light.AOETilePosition
import com.ovle.rll3.model.module.game.LocationInfo
import com.ovle.rll3.model.module.core.entity.entitiesWith
import com.ovle.rlUtil.gdx.math.lineOfSight.rayTracing.fieldOfView
import com.ovle.rll3.model.module.light.Components.light
import com.ovle.rll3.model.procedural.config.location.solidWallTypes
import ktx.ashley.get

object LightConfig {
    const val radius = 4

    const val fullLightCap = 5.0f
    const val halfLightCap = 2.5f
    const val darknessCap = 0.0f
}

//fun lightValueType(
//    lightInfo: Map<GridPoint2, Double>, position: GridPoint2, positionDown: GridPoint2, isPitFloor: Boolean, isRoomFloorUp: Boolean, isWall: Boolean, isDoorUp: Boolean
//): LightValueType {
//    val tileLightValue = lightInfo[position] ?: 0.0
//    val tileLightDownValue = lightInfo[positionDown] ?: 0.0
//
//    val isFullLight = tileLightValue > LightConfig.halfLightCap
//    val isHalfLight = !isFullLight && tileLightValue > LightConfig.darknessCap
//    val isFullLightDown = tileLightDownValue > LightConfig.halfLightCap
//    val isHalfLightDown = !isFullLightDown && tileLightDownValue > LightConfig.darknessCap
//
//    return when {
//        isFullLight -> when {
//            !isPitFloor || isRoomFloorUp -> LightValueType.Full
//            else -> LightValueType.No
//        }
//        isHalfLight -> when {
//            !isPitFloor || isRoomFloorUp -> LightValueType.Half
//            else -> LightValueType.No
//        }
//        else -> when {
//            isWall && isFullLightDown && !isDoorUp -> LightValueType.Full
//            isWall && isHalfLightDown && !isDoorUp -> LightValueType.Half
//            else -> LightValueType.No
//        }
//    }
//}

//todo cache / memoize
fun lightTiles(locationInfo: LocationInfo): List<AOETilePosition> {
    val lightSources = entitiesWith(locationInfo.entities, LightSourceComponent::class)
    return lightSources.map { it[light]!!.area.aoePositions }.flatten()
}

fun lightByPosition(AOETiles: List<AOETilePosition>) = AOETiles.groupBy { it.tilePosition }
    .mapValues { it.value.sumByDouble { lightTilePosition -> lightTilePosition.value.toDouble() } }

fun lightPositions(position: GridPoint2, tiles: TileArray, lightConfig: LightConfig, obstacles: List<GridPoint2>): List<AOETilePosition> {
    return fieldOfView(
        position,
        lightConfig.radius,
        ::lightTilePassMapper,
        tiles,
        obstacles
    ).map {
        AOETilePosition(lightValue(lightConfig.fullLightCap, position, it), it)
    }
}

fun lightValue(maxLightValue: Float, center: GridPoint2, position: GridPoint2): Float {
    val distance = center.dst(position)
    return (maxLightValue / (distance * distance))
}

fun lightTilePassMapper(tile: Tile) = when(tile) {
    in solidWallTypes -> false
    else -> true    //todo will there be more options?
}