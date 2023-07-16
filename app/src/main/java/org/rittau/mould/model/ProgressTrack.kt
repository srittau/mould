package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

enum class ProgressCompletion {
    InProgress, Completed, Failed
}

enum class ProgressType {
    Other, Vow, Journey
}

class ProgressTrack(
    val campaignUUID: UUID,
    val uuid: UUID,
    name: String,
    var challengeRank: ChallengeRank,
    var type: ProgressType = ProgressType.Other,
    var notes: String = "",
    ticks: Int = 0,
    var completion: ProgressCompletion = ProgressCompletion.InProgress,
) : Track(name, ticks), Serializable {
    fun markProgress(): Int {
        ticks = minOf(
            MAX_TICKS, when (challengeRank) {
                ChallengeRank.Troublesome -> ticks + 12
                ChallengeRank.Dangerous -> ticks + 8
                ChallengeRank.Formidable -> ticks + 4
                ChallengeRank.Extreme -> ticks + 2
                ChallengeRank.Epic -> ticks + 1
            }
        )
        return ticks
    }

    fun complete() {
        completion = ProgressCompletion.Completed
    }

    fun fail() {
        completion = ProgressCompletion.Failed
    }

    fun reopen() {
        completion = ProgressCompletion.InProgress
    }
}
