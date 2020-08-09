package com.ovle.rll3.model.module.container


//class ContainerSystem : EventSystem() {
//
//    override fun subscribe() {
//        subscribe<Event.GameEvent.EntityEvent.EntityInitializedEvent> { onEntityInitialized(it.entity) }
//        subscribe<Event.GameEvent.EntityEvent.EntityContentInteraction> { onEntityContentInteraction(it.source, it.entity) }
//    }
//
//    private fun onEntityInitialized(entity: Entity) {
//        val containerComponent = entity[Mappers.container] ?: return
//        if (containerComponent.initialized) return
//
//        val content = spawnContent()
//        with(containerComponent) {
//            items.clear()
//            items.addAll(content)
//
//            initialized = true
//        }
//    }
//
//    private fun spawnContent(): List<Entity> {
//        //todo test
//        return listOf(
//            newTemplatedEntity("i1", entityTemplate(TemplatesType.Common, "test item"), engine)
//        )
//    }
//
//    private fun onEntityContentInteraction(source: Entity, entity: Entity) {
//        val containerComponent = entity[Mappers.container]!!
//        val sourceContainerComponent = source[Mappers.container]
//
//        //todo use gui if player entity
//        sourceContainerComponent?.let {
//            val items = containerComponent.items.toList()
//            if (items.isNotEmpty()) {
//                containerComponent.items.clear()
//                it.items.addAll(items)
//
//                send(Event.GameEvent.EntityEvent.EntityTakeItems(source, items))
//            }
//        }
//    }
//}
