package com.ovle.rll3.model.procedural.config

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.czyzby.noise4j.map.generator.room.RoomType
import com.ovle.rll3.model.procedural.grid.GridFactory
import com.ovle.rll3.model.procedural.grid.processor.TilesProcessor
import com.ovle.rll3.model.tile.TileType
import com.ovle.rll3.view.layer.level.TileToTextureParams


data class LevelSettings<S: LevelGenerationSettings, out G: GridFactory>(
    val generationSettings: S,
    val gridFactory: G,
    val gridValueToTileType: (Float) -> TileType,
    val tileToTexture: (TileToTextureParams) -> Array<TextureRegion>,
    val postProcessors: Array<TilesProcessor>
)

sealed class LevelGenerationSettings(
    val size: Int
) {
    class DungeonGenerationSettings(
        size: Int,
        val roomTypes: Array<RoomType>,
        val maxRoomSize: Int,
        val minRoomSize: Int,
        val tolerance: Int, // Max difference between width and height.
        val windingChance: Float,
        val randomConnectorChance: Float,
        val lightSourceChance: Float,
        val doorChance: Float,
        val trapChance: Float
    ): LevelGenerationSettings(size)

    class TemplateGenerationSettings(
        size: Int,
        val template: Array<Array<Int>>
    ): LevelGenerationSettings(size)
}