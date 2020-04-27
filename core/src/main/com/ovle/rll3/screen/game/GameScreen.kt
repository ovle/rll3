package com.ovle.rll3.screen.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Align.center
import com.badlogic.gdx.utils.Align.right
import com.ovle.rll3.AssetsManager
import com.ovle.rll3.ScreenManager
import com.ovle.rll3.event.Event
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.event.eventLogHook
import com.ovle.rll3.model.ecs.system.*
import com.ovle.rll3.model.ecs.system.interaction.CombatSystem
import com.ovle.rll3.model.ecs.system.interaction.EntityInteractionSystem
import com.ovle.rll3.model.ecs.system.level.LevelRegistry
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.screen.BaseScreen
import com.ovle.rll3.view.initialScale
import com.ovle.rll3.view.layer.TexturesInfo
import com.ovle.rll3.view.layer.image
import com.ovle.rll3.view.layer.label as cLabel
import com.ovle.rll3.view.scaleScrollCoeff
import ktx.scene2d.*
import kotlin.math.min
import kotlin.math.roundToInt


class GameScreen(
    private val assetsManager: AssetsManager,
    screenManager: ScreenManager, batch: Batch, camera: OrthographicCamera
): BaseScreen(screenManager, batch, camera) {

    private lateinit var ecsEngine: PooledEngine
    private val controls = PlayerControls()

    override fun show() {
        super.show()

        val levelTexturesInfo = TexturesInfo(assetsManager.levelTexture)
        val objectsTextureInfo = TexturesInfo(assetsManager.objectsTexture)
        TemplatesRegistry.entityTemplates = assetsManager.entityTemplates
        TemplatesRegistry.structureTemplates = assetsManager.structureTemplates

        val camera = batchViewport.camera as OrthographicCamera

        val systems = listOf(
            GUISystem(stage, assetsManager.guiTexture, camera),
            PlayerControlsSystem(),
            CameraSystem(camera),

            RenderLevelSystem(camera, levelTexturesInfo),
            RenderObjectsSystem(batch, objectsTextureInfo),

            GameSystem(),
            LevelSystem(),
            EntityInteractionSystem(),
            CombatSystem(),
            MoveSystem(),
            AnimationSystem(objectsTextureInfo),
            SightSystem()
        )

        ecsEngine = PooledEngine()
        systems.forEach { ecsEngine.addSystem((it)) }

        EventBus.addHook(::eventLogHook)

        send(Event.CameraScrolled(((1 - initialScale) / scaleScrollCoeff).roundToInt()))

        send(Event.GameStartedEvent())
    }

    override fun hide() {
        super.hide()

        val camera = batchViewport.camera as OrthographicCamera
        send(Event.CameraScrolled(((1 - camera.zoom) / scaleScrollCoeff).roundToInt()))

        ecsEngine.clearPools()
        ecsEngine.removeAllEntities()
        ecsEngine.systems.iterator().forEach {
            ecsEngine.removeSystem(it)
        }

        EventBus.clearSubscriptions()
        EventBus.clearHooks()

        LevelRegistry.clear()
        //todo free other resources?
    }

    override fun renderIntr(delta: Float) {
        ecsEngine.update(min(delta, 1 / 60f))
    }

    override fun rootActor(): Actor {
        return playerInfoActor()
    }

    fun playerInfoActor(): Actor {
        val scale = 4.0f
        val guiTexture = assetsManager.guiTexture
        val portrait = TextureRegion(guiTexture, 0, 24, 24, 24)
        val bg = TextureRegion(guiTexture, 120, 0, 72, 40)
        val pi = image(portrait)

        val leftPart = table {
            add(pi).size(24 * scale, 24 * scale)
                    .padRight(2.0f * scale).padBottom(1.0f * scale)
            row()
            add(cLabel("Name")).padTop(2 * scale)
        }

        val rightPart = table {
            defaults().padBottom(5 * scale).padLeft(10 * scale)

            add(cLabel("05/05")).padTop(6 * scale)
            row()
            label("05/05")
            row()
            label("05/05")
            row()
            label("05/05")
        }

        val percentWidth50 = Value.percentWidth(50.0f)
        val result = table {
            height = bg.regionHeight * scale
            width = bg.regionWidth * scale
            background = TextureRegionDrawable(bg)

            add(leftPart).width(percentWidth50).expand()
            add(rightPart).width(percentWidth50).expand()
        }

        return result
    }

    override fun screenInputProcessor() = controls
}