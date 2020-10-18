package com.ovle.rll3.model.module.game

import com.ovle.rll3.EntityId

class PartyInfo (
    val entities: MutableCollection<EntityId> = mutableSetOf()
): MutableCollection<EntityId> by entities
