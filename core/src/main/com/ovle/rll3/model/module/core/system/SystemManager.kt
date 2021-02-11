package com.ovle.rll3.model.module.core.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.ovle.rlUtil.gdx.view.PaletteManager
import com.ovle.rll3.assets.AssetsManager
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
import com.ovle.rll3.screen.game.InitGameInfo

//todo the params needed only for render systems
class SystemManager(
    private val assetsManager: AssetsManager,
    private val paletteManager: PaletteManager,
    private val batch: Batch,
    private val stageBatch: Batch,
    private val camera: OrthographicCamera
) {

    //todo order matters here?
    //todo use modules
    fun systems() = listOf(
        PlayerControlsSystem(),

        CameraSystem(camera),
        RenderLocationSystem(camera, assetsManager, paletteManager),
        RenderObjectsSystem(batch, assetsManager),
        RenderInteractionInfoSystem(batch, assetsManager),
        RenderGUISystem(batch, stageBatch, assetsManager, paletteManager),
        AnimationSystem(),

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