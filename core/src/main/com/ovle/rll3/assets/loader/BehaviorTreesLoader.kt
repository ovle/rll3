package com.ovle.rll3.assets.loader

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.ovle.rll3.model.module.ai.EntityBlackboard
import ktx.assets.file

data class BehaviorTreeInfo(val name: String, val tree: BehaviorTree<EntityBlackboard>)
data class BehaviorTrees(val trees: Collection<BehaviorTreeInfo>)

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

    private fun behaviorTree(fileHandle: FileHandle, btlm: BehaviorTreeLibraryManager, dirName: String)
        = BehaviorTreeInfo(fileHandle.nameWithoutExtension(), btlm.createBehaviorTree("$dirName/${fileHandle.name()}"))
}