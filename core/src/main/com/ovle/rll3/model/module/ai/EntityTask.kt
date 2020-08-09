package com.ovle.rll3.model.module.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

abstract class EntityTask: LeafTask<EntityBlackboard>() {

    protected val currentEntity
        get() = this.`object`.entity

    protected val entities
        get() = this.`object`.engine.entities.toList()

    //todo use reflection to copy all TaskAttribute
    override fun copyTo(otherTask: Task<EntityBlackboard>) = otherTask
}