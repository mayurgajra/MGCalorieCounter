package com.mayurg.tracker_data.local

import androidx.room.*
import com.mayurg.tracker_data.local.entity.TrackedFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFood(trackerFoodEntity: TrackedFoodEntity)

    @Delete
    suspend fun deleteTrackedFood(trackerFoodEntity: TrackedFoodEntity)

    @Query(
        """
        SELECT *
        FROM trackedfoodentity
        WHERE dayOfMonth =:day AND month =:month AND year =:year
    """
    )
    fun getFoodsForDate(day: Int, month: Int, year: Int): Flow<List<TrackedFoodEntity>>
}