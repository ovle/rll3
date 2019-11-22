package com.ovle.rll3.model.ecs.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.model.ecs.component.MoveComponent
import com.ovle.rll3.model.ecs.component.PlayerComponent
import com.ovle.rll3.model.ecs.get
import com.ovle.rll3.tileHeight
import com.ovle.rll3.tileMapScale
import com.ovle.rll3.tileWidth
import ktx.ashley.get

class PlayerControlSystem : IteratingSystem(Family.all(PlayerComponent::class.java, MoveComponent::class.java).get()) {

    private val player: ComponentMapper<PlayerComponent> = get()
    private val move: ComponentMapper<MoveComponent> = get()
//    private val size: ComponentMapper<Size> = get()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        with (Gdx.input) {
            when {
                isKeyPressed(Input.Keys.LEFT) -> {
                    //            entity[player].direction.x = -128.0f * deltaTime
                }
                isButtonPressed(Input.Buttons.LEFT) -> {
                    //todo
                    val screenX = Gdx.input.x
                    val screenY = Gdx.input.y
                    entity[move]?.add(toGamePoint(screenX, screenY))
                }
                else -> {}
            }
        }
    }

    private fun toGamePoint(screenX: Int, screenY: Int): Vector2 {
        //todo
        val screenPosition = Vector2()
        val playerPosition = Vector2()

        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        val x = (screenX)/ (tileWidth * tileMapScale)
        val y = (screenHeight - screenY)/ (tileHeight * tileMapScale)
//         val x = (screenPosition.x - screenWidth * 0)/ (tileWidth * tileMapScale) + playerPosition.x
//        val y = (- screenPosition.y + screenHeight * (1 - 0))/ (tileHeight * tileMapScale) + playerPosition.y

        val result = Vector2(x, y)
        println(result)
        return result
    }

//    override fun toScreenPoint(position: Point): Point {
//        val playerPosition = game.player.position
//        val screenWidth = Gdx.graphics.width.toFloat()
//        val screenHeight = Gdx.graphics.height.toFloat()
//        val screenX = (position.x - playerPosition.x) * PIXELS_PER_TILE + screenWidth * PLAYER_SCREEN_WIDTH_OFFSET_RATIO;
//        val screenY = (position.y - playerPosition.y) * PIXELS_PER_TILE + screenHeight * PLAYER_SCREEN_HEIGHT_OFFSET_RATIO;
//
//        return Point(screenX, screenY)// - scrollOffset
//    }
}