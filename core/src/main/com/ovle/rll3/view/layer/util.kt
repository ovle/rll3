package com.ovle.rll3.view.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.GridPoint2
import com.ovle.rll3.model.tile.LightValueType
import com.ovle.rll3.model.util.config.LightConfig
import com.ovle.rll3.view.layer.TextureRegions
import com.ovle.rll3.view.layer.TextureTileSet


fun lightValueType(
    lightInfo: Map<GridPoint2, Double>, position: GridPoint2, positionDown: GridPoint2, isPitFloor: Boolean, isRoomFloorUp: Boolean, isWall: Boolean, isDoorUp: Boolean
): LightValueType {
    val tileLightValue = lightInfo[position] ?: 0.0
    val tileLightDownValue = lightInfo[positionDown] ?: 0.0

    val isFullLight = tileLightValue > LightConfig.halfLightCap
    val isHalfLight = !isFullLight && tileLightValue > LightConfig.darknessCap
    val isFullLightDown = tileLightDownValue > LightConfig.halfLightCap
    val isHalfLightDown = !isFullLightDown && tileLightDownValue > LightConfig.darknessCap

    return when {
        isFullLight -> when {
            !isPitFloor || isRoomFloorUp -> LightValueType.Full
            else -> LightValueType.No
        }
        isHalfLight -> when {
            !isPitFloor || isRoomFloorUp -> LightValueType.Half
            else -> LightValueType.No
        }
        else -> when {
            isWall && isFullLightDown && !isDoorUp -> LightValueType.Full
            isWall && isHalfLightDown && !isDoorUp -> LightValueType.Half
            else -> LightValueType.No
        }
    }
}

fun Array<TextureRegion>.withChance(chance: Float, defaultValue: Array<TextureRegion>)
    = if (Math.random() <= chance) this else defaultValue

fun indexedTextureTile(tileSetConfig: TextureTileSet, index: Int, textureRegions: TextureRegions) =
    textureRegions[tileSetConfig.originX + index % tileSetConfig.size][tileSetConfig.originY + index / tileSetConfig.size]
