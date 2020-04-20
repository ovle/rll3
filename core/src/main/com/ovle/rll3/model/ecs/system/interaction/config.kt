package com.ovle.rll3.model.ecs.system.interaction

//todo load from templates
val combatActions = listOf(
    CombatAction(
        name = "strike",
        damageMod = 1,
        staminaCost = 1
    ),
    CombatAction(
        name = "heavy strike",
        damageMod = 2,
        staminaCost = 3
    ),

    CombatAction(
        name = "block",
        damageTakenMod = -1,
        hitMade = false,
        staminaCost = 0
    ),
    CombatAction(
        name = "evade",
        hitMade = false,
        hitTaken = false,
        staminaCost = 1
    ),
    CombatAction(
        name = "wait",
        hitMade = false,
        staminaCost = -1
    )
)