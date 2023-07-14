package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

data class Campaign(val uuid: UUID, var name: String) : Serializable

data class LoadedCampaign(val character: Character, val progress: List<ProgressTrack>)
