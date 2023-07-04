package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

data class WorldNote(var uuid: UUID, var title: String = "", var summary: String = "", var text: String = "") : Serializable
