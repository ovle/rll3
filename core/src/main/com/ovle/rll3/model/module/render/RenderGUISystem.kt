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
import com.ovle.rll3.event.Event.PlayerControlEvent.MouseClickEvent
import com.ovle.rll3.event.Event.PlayerControlEvent.MouseMovedEvent
import com.ovle.rll3.event.EventBus
import com.ovle.rll3.model.module.core.component.ComponentMappers.position
import com.ovle.rll3.model.module.core.component.ComponentMappers.taskPerformer
import com.ovle.rll3.model.module.core.component.ComponentMappers.template
import com.ovle.rll3.model.module.core.entity.*
import com.ovle.rll3.model.module.core.system.EventSystem
import com.ovle.rll3.model.template.TemplatesRegistry
import com.ovle.rll3.view.fontName
import com.ovle.rll3.view.palette.Palette
import com.ovle.rll3.view.spriteHeight
import com.ovle.rll3.view.spriteWidth
import com.ovle.rll3.view.uncentered
import ktx.ashley.get
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin


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
    private val dy = 20.0f
    private val guiZoom = 8.0f

    private val actionsPoint = vec2(75.0f, 5.0f)
    private val selectedActionRegion = guiRegions[1][0]
    private val hoveredActionRegion = guiRegions[3][0]

    private var controlledEntity: Entity? = null
    private var buttons = listOf<ButtonInfo>()
    private data class ButtonInfo(
        val iconPoint: GridPoint2,
        val frame: Rectangle,
        val callback: () -> Unit,

        var selected: Boolean = false,
        var hovered: Boolean = false
    )


    override fun subscribe() {
        EventBus.subscribe<MouseClickEvent> {
            onMouseClick(it.viewportPoint, it.button)
        }
        EventBus.subscribe<MouseMovedEvent> {
            onMouseMoved(it.viewportPoint)
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        stageBatch.begin()

        drawSystemInfo()
        selectedEntity()?.let {
            val isOwned = it[taskPerformer] != null
            if (!isOwned) return@let

            if (controlledEntity != it) {
                controlledEntity = it
                initActions(it)
            }

            drawActions()
        }

        stageBatch.end()
    }


    private fun drawSystemInfo() {
        val interaction = playerInteraction()!!

        val hoveredPoint = interaction[position]!!.gridPosition
        val interactionInfo = playerInteractionInfo()!!
        val hoveredEntity = interactionInfo.hoveredEntity
        val selectedEntity = interactionInfo.selectedEntity
//        val focusedEntity = interactionInfo.focusedEntity

        val locationInfo = locationInfo()
        val locationPoint = locationInfo.locationPoint

        val game = gameInfo()!!
        val time = game.time
        val worldAreaName = game.world.area(locationPoint).name
        val point = vec2(0.0f, Gdx.graphics.height - dy)

        val info = arrayOf(
            "$worldAreaName $locationPoint",
            "turn ${time.turn}",
            "$hoveredPoint " + (hoveredEntity?.let { "(${it.name()})" } ?: ""),
            selectedEntity?.run {
                val selectedPoint = get(position)!!.gridPosition
                "selected: $selectedPoint " + ("(${name()})")
            }
        ).filterNotNull()

        info.forEachIndexed {
            i, _ ->
            font.draw(stageBatch, info[i], point.x, point.y - dy * i)
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
                frame = Rectangle(point.x, point.y, spriteWidth * guiZoom, spriteHeight * guiZoom),
                callback = { println(skill.name) }
            )
            buttonInfo
        }
    }

    //todo multiple viewports issue
    private fun onMouseClick(viewportPoint: Vector2, mouseButton: Int) {
        buttons.forEach {
            it.selected = false
        }
        val button = button(viewportPoint) ?: return
        button.selected = true

        //todo skill select event
    }

    //todo multiple viewports issue
    private fun onMouseMoved(viewportPoint: Vector2) {
        buttons.forEach {
            it.hovered = false
        }
        val button = button(viewportPoint) ?: return
        button.hovered = true
    }

    private fun button(viewportPoint: Vector2) =
        buttons.find { it.frame.contains(viewportPoint.uncentered()) }
}