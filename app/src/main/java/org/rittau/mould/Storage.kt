package org.rittau.mould

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import java.util.UUID

private val DEFAULT_UUID = UUID.fromString("6506c0fa-d589-4b51-b454-13d1ec7002b4")

@Entity(tableName = "scenarios")
private data class Scenario(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)

@Dao
private interface ScenarioDao {
    @Query("SELECT COUNT(*) == 1 FROM scenarios WHERE id = :id")
    suspend fun hasScenario(id: String): Boolean

    @Insert
    suspend fun insertScenario(scenario: Scenario)
}

@Entity(tableName = "worlds")
private data class World(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)

@Dao
private interface WorldDao {
    @Query("SELECT COUNT(*) == 1 FROM worlds WHERE id = :id")
    suspend fun hasWorld(id: String): Boolean

    @Insert
    suspend fun insertWorld(world: World)
}

@Entity(
    tableName = "campaigns",
    foreignKeys = [
        ForeignKey(
            entity = Scenario::class,
            parentColumns = ["id"],
            childColumns = ["scenario_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = World::class,
            parentColumns = ["id"],
            childColumns = ["world_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index("scenario_id"),
        Index("world_id"),
    ]
)
private data class Campaign(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "scenario_id") val scenarioID: String,
    @ColumnInfo(name = "world_id") val worldID: String,
    @ColumnInfo val name: String,
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
    @ColumnInfo val name: String,
)

@Dao
private interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaign_uuid = :campaignUUID")
    suspend fun selectCharacter(campaignUUID: UUID): List<CampaignCharacter>

    @Upsert
    suspend fun upsertCharacter(character: CampaignCharacter)
}

@Database(
    entities = [Scenario::class, World::class, Campaign::class, CampaignCharacter::class],
    version = 1
)
private abstract class MouldDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
    abstract fun worldDao(): WorldDao
    abstract fun campaignDao(): CampaignDao
    abstract fun characterDao(): CharacterDao
}

private var db: MouldDatabase? = null

private fun getDb(): MouldDatabase {
    return checkNotNull(db) { "Database not initialized" }
}

suspend fun initializeDatabase(applicationContext: android.content.Context) {
    check(db == null) { "Database already initialized" }
    db = Room.databaseBuilder(applicationContext, MouldDatabase::class.java, "mould").build()
    with(getDb()) {
        if (!scenarioDao().hasScenario("default")) {
            scenarioDao().insertScenario(Scenario("default", "Default Scenario"))
        }
        if (!worldDao().hasWorld("default")) {
            worldDao().insertWorld(World("default", "Default World"))
        }
        campaignDao().upsertCampaign(
            Campaign(
                DEFAULT_UUID, "default", "default", "Default Campaign"
            )
        )
    }
}

suspend fun saveCharacter(character: Character) {
    getDb().characterDao().upsertCharacter(CampaignCharacter(DEFAULT_UUID, character.name))
}

suspend fun loadCharacter(): Character {
    val characters = getDb().characterDao().selectCharacter(DEFAULT_UUID)
    if (characters.isEmpty()) {
        return Character("")
    }
    val character = characters[0]
    return Character(character.name)
}
