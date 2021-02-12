package com.ovle.rll3.model.module.core.system

import com.ovle.rll3.model.module.ai.AISystem
import com.ovle.rll3.model.module.controls.PlayerControlsSystem
import com.ovle.rll3.model.module.entityAction.EntityActionSystem
import com.ovle.rll3.model.module.game.GameSystem
import com.ovle.rll3.model.module.gathering.ResourceSystem
import com.ovle.rll3.model.module.health.HealthSystem
import com.ovle.rll3.model.module.health.StaminaSystem
import com.ovle.rll3.model.module.interaction.BaseInteractionSystem
import com.ovle.rll3.model.module.interaction.EntityInteractionSystem
import com.ovle.rll3.model.module.interaction.TileInteractionSystem
import com.ovle.rll3.model.module.render.*
import com.ovle.rll3.model.module.skill.SkillSystem
import com.ovle.rll3.model.module.space.MoveSystem
import com.ovle.rll3.model.module.task.TaskSystem
import com.ovle.rll3.model.module.tile.TileSystem
import com.ovle.rll3.model.module.time.TimeSystem
import ktx.inject.Context

//todo order matters here?
//todo use modules
fun systems(context: Context) =
    with(context) {
        listOf(
            //client
            PlayerControlsSystem(), //system?
            //client.render
            CameraSystem(inject()), //system?
            RenderLocationSystem(inject(), inject(), inject()),
            RenderObjectsSystem(inject(), inject()),
            AnimationSystem(),

            //server
            GameSystem(),
            TimeSystem(),
            TaskSystem(),
            AISystem(),
            EntityActionSystem(),
            MoveSystem(),
            HealthSystem(),
//            HungerSystem(),
            StaminaSystem(),
            BaseInteractionSystem(),
            EntityInteractionSystem(),
            TileInteractionSystem(),
            SkillSystem(),
            ResourceSystem(),
            TileSystem()
        )
    }
