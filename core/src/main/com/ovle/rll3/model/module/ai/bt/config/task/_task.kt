package com.ovle.rll3.model.module.ai.bt.config.task

import com.badlogic.gdx.ai.btree.Task.Status.*
import com.ovle.rll3.TaskExec
import com.ovle.rll3.model.module.ai.bt.result
import com.ovle.rll3.point


fun findResourceStorageTask(): TaskExec = { (btParams) ->
    //todo
    result(SUCCEEDED, point(123, 77))
}

fun successTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner
    result(SUCCEEDED)
}

fun failTask(): TaskExec = { (btParams) ->
    val owner = btParams.owner
    result(FAILED)
}