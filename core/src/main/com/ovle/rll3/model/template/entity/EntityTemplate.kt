package com.ovle.rll3.model.template.entity

import com.ovle.rll3.TemplatedState

//todo template inheritance
data class EntityTemplate(
    var name: String = "",
    var version: String = "0.1",
    var description: String? = null,
    var playable: Boolean? = false,
    var state: TemplatedState = mapOf(),
    var spawns: Collection<SpawnTemplate> = listOf(),
    var skills: List<String> = listOf()
)
