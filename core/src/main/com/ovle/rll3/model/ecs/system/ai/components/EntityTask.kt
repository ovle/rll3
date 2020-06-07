package com.ovle.rll3.model.ecs.system.ai.components

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

abstract class EntityTask: LeafTask<EntityBlackboard>() {

    protected val currentEntity
        get() = this.`object`.entity

    protected val entities
        get() = this.`object`.engine.entities.toList()

    override fun copyTo(otherTask: Task<EntityBlackboard>) = otherTask
}