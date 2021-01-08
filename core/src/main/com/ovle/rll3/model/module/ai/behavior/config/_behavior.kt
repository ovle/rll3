package com.ovle.rll3.model.module.ai.behavior.config

//val baseBehavior = BehaviorInfo(
//    name = "base",
//    bt = {
//        tree {
//            select {
//                seq {
//                    task("is hungry?", isHungry())
//                    select {
//                        select {
//                            task("is have available food?", isHaveFoodNear())
//                            task("hunt", failTask())
//                        }
//                        task("eat", failTask())
//                    }
//                }
//                seq {
//                    task("is in danger?", isInDanger())
//                    task("runaway", failTask())
//                }
//                select {
//                    task("is have attack target?", isHaveAttackTarget())
//                    seq {
//                        task("is attack target?", failTask())
//                        task("attack", failTask())
//                    }
//                    seq {
//                        task("is have potential attack target?", failTask())
//                        task("is have attack intention?", failTask())
//                        task("attack", failTask())
//                    }
//                }
//            }
//        }
//    }
//)
