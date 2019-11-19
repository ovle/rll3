//package com.ovle.rll3.model.ecs.system
//
//import com.badlogic.ashley.core.ComponentMapper
//import com.badlogic.ashley.core.Entity
//import com.badlogic.ashley.core.Family
//import com.badlogic.ashley.systems.IteratingSystem
//import com.badlogic.gdx.Gdx
//import com.badlogic.gdx.Input
//import com.badlogic.gdx.math.Vector2
//import com.badlogic.gdx.utils.viewport.Viewport
//import com.ovle.rll3.model.ecs.component.Player
//import com.ovle.rll3.model.ecs.component.Position
//import com.ovle.rll3.model.ecs.component.Size
//import com.ovle.rll3.model.ecs.get
//
//class PlayerControlSystem(private val viewport: Viewport) : IteratingSystem(Family.all(Player::class.java, Position::class.java).get()) {
//
//    private val player: ComponentMapper<Player> = get()//ComponentMapper.getFor(Player::class.java)
//    private val position: ComponentMapper<Position> = get()
//    private val size: ComponentMapper<Size> = get()
//
//    private val origin = Vector2()
//    private val currentTo = Vector2()
//    private var movingByTouch = false
//
//    private val tmp = Vector2()
//
//    override fun processEntity(entity: Entity, deltaTime: Float) {
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
////            entity[player].direction.x = -128.0f * deltaTime
//        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
////            entity[player].direction.x = 128.0f * deltaTime
//        }
//
//        // sort de la zone ?
////        tmp.set(entity[player].direction)
////                .add(entity[position].position)
////
////        if (tmp.x.between(0f, screenWidth - entity[size].size.x)) {
////            entity[position].position.add(entity[player].direction)
////        }
//    }
//}