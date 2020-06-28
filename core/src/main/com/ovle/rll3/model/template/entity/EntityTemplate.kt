package com.ovle.rll3.model.template.entity

data class EntityTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String? = null,
    var playable: Boolean? = false,
    var state: Map<String, Any?> = mapOf(),
    var spawns: Collection<SpawnTemplate> = listOf()
)
