package com.ovle.rll3.persistance

import com.badlogic.ashley.core.Entity
import com.ovle.rll3.model.ecs.id
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

fun Serializable.bytes(): ByteArray {
    val bos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(bos)
    oos.writeObject(this)
    oos.flush()
    return bos.toByteArray()
}

fun Entity.stored() = StoredEntity(this.id(), this.components.toList())