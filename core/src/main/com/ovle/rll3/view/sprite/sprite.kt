package com.ovle.rll3.view.sprite

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ovle.rll3.model.ecs.component.*
import com.ovle.rll3.view.layer.TextureRegions
import ktx.ashley.get


//todo use config
fun sprite(regions: TextureRegions, x: Int, y: Int): Sprite {
    return Sprite(
        region = regions[y][x] //switched
    )
}

fun sprite(entity: Entity, regions: TextureRegions): Sprite {
    return when {
        //todo hack, will use the template
        entity.has<MoveComponent>() -> sprite(regions,1, 1)
        entity.has<LightComponent>() -> sprite(regions,7, 1)
        entity.has<DoorComponent>() -> {
            val door = entity[Mappers.door]!!
            val x = if (door.closed) 7 else 8
            sprite(regions, x, 8)
        }
        entity.has<PlayerInteractionComponent>() -> sprite(regions,0, 0)
        else -> sprite(regions, 1, 0)
    }
}

class Sprite(
    private val region: TextureRegion? = null
) {

   var flipped: Boolean = false

    fun textureRegion() = region!!

    fun flip() {
        region?.flip(true, false)
//        animations?.values?.forEach { it.flip() }
//        if (offset != null) {
//            offset.x = -offset.x
//        }
    }
}