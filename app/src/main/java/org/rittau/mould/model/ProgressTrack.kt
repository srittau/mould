package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

const val MAX_PROGRESS = 40

enum class ProgressCompletion {
    InProgress, Completed, Failed
}

data class ProgressTrack(
    val uuid: UUID,
    var name: String,
    var challengeRank: ChallengeRank,
    var notes: String = "",
    var progress: Int = 0,
    var completion: ProgressCompletion = ProgressCompletion.InProgress,
) : Serializable

fun addProgress(progress: Int, rank: ChallengeRank) = minOf(
    MAX_PROGRESS, when (rank) {
        ChallengeRank.Troublesome -> progress + 12
        ChallengeRank.Dangerous -> progress + 8
        ChallengeRank.Formidable -> progress + 4
        ChallengeRank.Extreme -> progress + 2
        ChallengeRank.Epic -> progress + 1
    }
)
