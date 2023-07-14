package org.rittau.mould.model

import java.util.UUID

data class Campaign(val uuid: UUID, var name: String)

data class LoadedCampaign(val character: Character, val progress: List<ProgressTrack>)
