package com.ovle.rll3.model.ecs

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem


public inline fun <reified T : com.badlogic.ashley.core.Component> EntitySystem.get(): ComponentMapper<T> = ComponentMapper.getFor(T::class.java)