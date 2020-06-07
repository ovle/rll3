package com.ovle.rll3.model.ecs.system.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.ecs.system.ai.components.EntityBlackboard
import ktx.assets.file


data class BehaviorTrees(val trees: Collection<BehaviorTree<EntityBlackboard>>)

class BehaviorTreesLoader(resolver: FileHandleResolver): SynchronousAssetLoader<BehaviorTrees, AssetLoaderParameters<BehaviorTrees>>(resolver) {

    override fun load(manager: AssetManager?, fileName: String, file: FileHandle?, parameter: AssetLoaderParameters<BehaviorTrees>?)
        = loadBehaviorTrees(fileName)

    override fun getDependencies(p0: String?, p1: FileHandle?, p2: AssetLoaderParameters<BehaviorTrees>?): Array<AssetDescriptor<Any>>? = null

    private fun loadBehaviorTrees(dirName: String): BehaviorTrees {
        val dir = file(dirName)
        val btlm = BehaviorTreeLibraryManager.getInstance()
        val result = dir.list().map { behaviorTree(it, btlm, dirName) }
        return BehaviorTrees(result)
    }

    private fun behaviorTree(fileHandle: FileHandle, btlm: BehaviorTreeLibraryManager, dirName: String): BehaviorTree<EntityBlackboard>
        = btlm.createBehaviorTree("$dirName/${fileHandle.name()}")
}