package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

data class CampaignNote(var uuid: UUID, var title: String = "", var date: String = "", var text: String = "") : Serializable
