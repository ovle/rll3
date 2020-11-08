package com.ovle.rll3.model.module.render

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion.split
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.ovle.rll3.assets.AssetsManager
import com.ovle.rll3.event.Event.GameEvent.*
import com.ovle.rll3.event.Event.PlayerControlEvent.MouseClickEvent
import com.ovle.rll3.event.Event.PlayerControlEvent.MouseMovedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.event.EventBus.send
import com.ovle.rll3.model.module.core.component.ComponentMappers.action
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.module.skill.SkillTemplate
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.view.*
import com.ovle.rll3.view.palette.Palette
import ktx.ashley.get
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin


private const val textInfoDy = 20.0f
private const val guiZoom = 7.0f

private val portraitPoint = vec2(8.0f, 8.0f)
private val actionsPoint = vec2(24.0f, 8.0f)

private val systemInfoPoint = vec2(0.0f, Gdx.graphics.height - textInfoDy)
private val namePoint = vec2(2.0f * guiZoom, 8.0f * guiZoom)


class RenderGUISystem(
    private val batch: Batch,
    assetsManager: AssetsManager,
    private val stageBatch: Batch
) : EventSystem() {

    private val guiRegions = split(assetsManager.guiTexture.texture, spriteWidth.toInt(), spriteHeight.toInt())

    private val font = Scene2DSkin.defaultSkin.getFont(fontName)
        .apply {
//            data.scale(-0.6f)
            color = Palette.palette.last().cpy()
        }
    private val selectedActionRegion = guiRegions[0][1]
    private val hoveredActionRegion = guiRegions[0][3]

    private var controlledEntity: Entity? = null
    private var buttons = listOf<ButtonInfo>()

    private data class ButtonInfo(
        val iconPoint: GridPoint2,
        val frame: Rectangle,
        val callback: () -> Unit,

        var selected: Boolean = false,
        var hovered: Boolean = false
    ) {
        val guiViewportFrame = with(frame) {
            Rectangle(x * tileWidth, y * tileHeight, width * guiZoom, height * guiZoom)
        }
    }


    override fun subscribe() {
        EventBus.subscribe<MouseClickEvent> {
            onMouseClick(it.viewportPoint, it.stageViewportPoint, it.button)
        }
        EventBus.subscribe<MouseMovedEvent> {
            onMouseMoved(it.viewportPoint, it.stageViewportPoint)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        stageBatch.begin()

        drawSystemInfo()
        selectedEntity()?.let {
            val isOwned = it.id() in gameInfo()!!.party
            if (!isOwned) return@let

            if (controlledEntity != it) {
                controlledEntity = it
                initActions(it)
            }

            drawEntityInfo(it)
            drawActions()
        }

        stageBatch.end()
    }

    private fun drawEntityInfo(entity: Entity) {
        val template = entity[template]!!
        val portrait = template.viewTemplate!!.portrait
        portrait?.let {
            val region = guiRegions[it.y][it.x]
            val point = vec2(portraitPoint.x, portraitPoint.y)

            draw(point, region)
        }

        val name = template.template.name
        val info = arrayOf(
            "$name" //todd
        ).filterNotNull()

        info.forEachIndexed {
            i, _ ->
            font.draw(stageBatch, info[i], namePoint.x, namePoint.y /*- textInfoDy * i*/)
        }
    }


    private fun drawSystemInfo() {
        val interaction = playerInteraction()!!

        val hoveredPoint = interaction[position]!!.gridPosition
        val interactionInfo = playerInteractionInfo()!!
        val hoveredEntity = interactionInfo.hoveredEntity
        val selectedEntity = interactionInfo.selectedEntity
//        val focusedEntity = interactionInfo.focusedEntity

        val locationInfo = locationInfo()
//        val locationPoint = locationInfo.locationPoint

        val game = gameInfo()!!
        val turn = game.turn
//        val worldAreaName = game.world.area(locationPoint).name

        val info = arrayOf(
//            "$worldAreaName $locationPoint",
            "turn ${turn.turn}",
            "$hoveredPoint " + (hoveredEntity?.let { "(${it.name()})" } ?: ""),
            selectedEntity?.run {
                val selectedPoint = get(position)!!.gridPosition
                "selected: $selectedPoint " + ("(${name()})")
            }
        ).filterNotNull()

        info.forEachIndexed {
            i, _ ->
            font.draw(stageBatch, info[i], systemInfoPoint.x, systemInfoPoint.y - textInfoDy * i)
        }
    }

    private fun drawActions() {
        buttons.forEach {
            val iconPoint = it.iconPoint
            val region = guiRegions[iconPoint.y][iconPoint.x]
            val point = vec2(it.frame.x, it.frame.y)

            draw(point, region)
            val hovered = it.hovered
            if (hovered) {
                draw(point, hoveredActionRegion)
            }
            val selected = it.selected
            if (selected) {
                draw(point, selectedActionRegion)
            }
        }
    }

    private fun draw(screenPoint: Vector2, region: TextureRegion) {
        stageBatch.draw(
            position = screenPoint,
            region = region,
            zoom = guiZoom
        )
    }


    private fun initActions(entity: Entity) {
        val templateComponent = entity[template]!!
        val skills = templateComponent.template.skills.map {
            TemplatesRegistry.skillTemplates[it]!!
        }

        buttons = skills.mapIndexed {
            index, skill ->
            val iconPoint = skill.icon
            val point = vec2(index * spriteWidth, 0.0f)
                .add(actionsPoint.x, actionsPoint.y)
            val buttonInfo = ButtonInfo(
                iconPoint = iconPoint,
                frame = Rectangle(point.x, point.y, spriteWidth, spriteHeight),
                callback = { send(SkillSelectCommand(skill)) }
            )
            buttonInfo
        }
    }

    private fun onMouseClick(viewportPoint: Vector2, stageViewportPoint: Vector2, mouseButton: Int) {
        val guiPoint = guiPoint(stageViewportPoint)
        val button = button(guiPoint) ?: return

        //todo deselect on turn's end
        buttons.forEach {
            it.selected = false
        }

        button.selected = true
        button.callback.invoke()
    }

    private fun onMouseMoved(viewportPoint: Vector2, stageViewportPoint: Vector2) {
        val guiPoint = guiPoint(stageViewportPoint)

        buttons.forEach {
            it.hovered = false
        }
        val button = button(guiPoint) ?: return
        button.hovered = true
    }

    private fun button(viewportPoint: Vector2) =
        buttons.find {
            it.guiViewportFrame.contains(viewportPoint/*.uncentered()*/)
        }

    private fun guiPoint(viewportPoint: Vector2) = viewportPoint//.div(guiZoom)
}