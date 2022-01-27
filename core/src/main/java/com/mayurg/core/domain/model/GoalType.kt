package com.mayurg.core.domain.model

sealed class GoalType(val name: String) {
    object LooseWeight : GoalType("lose_weight")
    object KeepWeight : GoalType("keep_weight")
    object GainWeight : GoalType("gain_weight")

    companion object {
        fun fromString(name: String): GoalType {
            return when (name) {
                "lose_weight" -> LooseWeight
                "keep_weight" -> KeepWeight
                "gain_weight" -> GainWeight
                else -> KeepWeight
            }
        }
    }
}
