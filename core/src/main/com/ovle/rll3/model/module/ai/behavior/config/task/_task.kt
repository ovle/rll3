package com.ovle.rll3.model.module.ai.behavior.config.task

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.ai.behavior.result
import com.ovle.rlUtil.gdx.math.point


fun findResourceStorageTask(): TaskExec = { (btParams) ->
    //todo
    result(SUCCEEDED, point(123, 77))
}

fun findNearestHideout(): TaskExec = { (btParams) ->
    //todo
    result(SUCCEEDED, point(100, 100))
}

//todo skip multiple turns
fun restTask(): TaskExec = { (btParams) ->
    result(SUCCEEDED)
}

fun successTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner
    result(SUCCEEDED)
}

fun failTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner
    result(FAILED)
}

//fun isHungry(): TaskExec = { (btParams) ->
//    val owner = btParams.owner
//    result(SUCCEEDED)
//}
//
//fun isHaveFoodNear(): TaskExec = { (btParams) ->
//    val owner = btParams.owner
//    result(SUCCEEDED)
//}
//
//fun isInDanger(): TaskExec = { (btParams) ->
//    val owner = btParams.owner
//    result(SUCCEEDED)
//}
//
//fun isHaveAttackTarget(): TaskExec = { (btParams) ->
//    val owner = btParams.owner
//    result(SUCCEEDED)
//}