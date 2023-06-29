package org.rittau.mould

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import java.util.UUID

private val DEFAULT_UUID = UUID.fromString("6506c0fa-d589-4b51-b454-13d1ec7002b4")

@Entity(tableName = "campaigns")
private data class Campaign(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "name") val name: String,
)

@Dao
private interface CampaignDao {
    @Upsert
    suspend fun upsertCampaign(campaign: Campaign)
}

@Entity(
    tableName = "characters", foreignKeys = [ForeignKey(
        entity = Campaign::class,
        parentColumns = ["uuid"],
        childColumns = ["campaign_uuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )]
)
private data class CampaignCharacter(
    @PrimaryKey @ColumnInfo(name = "campaign_uuid") val campaignUUID: UUID,
    @ColumnInfo(name = "name") val name: String,
)

@Dao
private interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaign_uuid = :campaignUUID")
    suspend fun selectCharacter(campaignUUID: UUID): List<CampaignCharacter>

    @Upsert
    suspend fun upsertCharacter(character: CampaignCharacter)
}

@Database(entities = [Campaign::class, CampaignCharacter::class], version = 1)
private abstract class MouldDatabase : RoomDatabase() {
    abstract fun campaignDao(): CampaignDao
    abstract fun characterDao(): CharacterDao
}

private var db: MouldDatabase? = null

fun initializeDatabase(applicationContext: android.content.Context) {
    check(db == null) { "Database already initialized" }
    db = Room.databaseBuilder(applicationContext, MouldDatabase::class.java, "mould").build()
}

suspend fun saveCharacter(character: Character) {
    val realDb = checkNotNull(db) { "Database not initialized" }
    realDb.campaignDao().upsertCampaign(Campaign(DEFAULT_UUID, "Default Campaign"))
    realDb.characterDao().upsertCharacter(CampaignCharacter(DEFAULT_UUID, character.name))
}

suspend fun loadCharacter(): Character {
    val realDb = checkNotNull(db) { "Database not initialized" }
    val characters = realDb.characterDao().selectCharacter(DEFAULT_UUID)
    if (characters.isEmpty()) {
        return Character("")
    }
    val character = characters[0]
    return Character(character.name)
}
