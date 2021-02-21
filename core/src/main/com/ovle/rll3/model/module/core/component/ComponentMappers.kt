package com.ovle.rll3.model.module.core.component

import com.ovle.rlUtil.gdx.ashley.component.mapper
import com.ovle.rll3.model.module._deprecated.DoorComponent
import com.ovle.rll3.model.module._deprecated.StashComponent
import com.ovle.rll3.model.module.ai.AIComponent
import com.ovle.rll3.model.module.collision.CollisionComponent
import com.ovle.rll3.model.module.container.CarrierComponent
import com.ovle.rll3.model.module.container.ContainerComponent
import com.ovle.rll3.model.module.entityAction.EntityActionComponent
import com.ovle.rll3.model.module.interaction.PlayerInteractionComponent
import com.ovle.rll3.model.module.game.GameComponent
import com.ovle.rll3.model.module.resource.ResourceComponent
import com.ovle.rll3.model.module.resource.SourceComponent
import com.ovle.rll3.model.module.life.HealthComponent
import com.ovle.rll3.model.module.light.LightSourceComponent
import com.ovle.rll3.model.module.perception.PerceptionComponent
import com.ovle.rll3.model.module.quest.QuestOwnerComponent
import com.ovle.rll3.model.module.render.RenderComponent
import com.ovle.rll3.model.module.space.MoveComponent
import com.ovle.rll3.model.module.space.PositionComponent
import com.ovle.rll3.model.module.task.TaskPerformerComponent
import com.ovle.rll3.model.module.task.TasksComponent
import com.ovle.rll3.model.module.core.component.template.TemplateComponent
import com.ovle.rll3.model.module.time.TimeComponent


//todo use modules
object ComponentMappers {
    val game = mapper<GameComponent>()
    val time = mapper<TimeComponent>()
    val tasks = mapper<TasksComponent>()
    val playerInteraction = mapper<PlayerInteractionComponent>()

    val core = mapper<CoreComponent>()
    val template = mapper<TemplateComponent>()
    val position = mapper<PositionComponent>()
    val collision = mapper<CollisionComponent>()
    val light = mapper<LightSourceComponent>()
    val perception = mapper<PerceptionComponent>()
    val move = mapper<MoveComponent>()
    val render = mapper<RenderComponent>()
    val health = mapper<HealthComponent>()
    val entityAction = mapper<EntityActionComponent>()
    val ai = mapper<AIComponent>()
    val questOwner = mapper<QuestOwnerComponent>()
    val taskPerformer = mapper<TaskPerformerComponent>()
    val resource = mapper<ResourceComponent>()
    val source = mapper<SourceComponent>()

    val container = mapper<ContainerComponent>()
    val carrier = mapper<CarrierComponent>()

    //todo remove these
    val door = mapper<DoorComponent>()
    val stash = mapper<StashComponent>()
}