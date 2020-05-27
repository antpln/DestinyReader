package com.plin.destinyreader.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(
    entities = [DestinyPresentationNode::class, DestinyLore::class, DestinyRecord::class, DestinyInventoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class DestinyDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val destinyDatabaseDao: DestinyDatabaseDao

    /**
     * Define a companion object, this allows us to add functions on the SleepDatabase class.
     *
     * For example, clients can call `DestinyDatabase.getInstance(context)` to instantiate
     * a new DestinyDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: DestinyDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * This is an example of a simple Singleton pattern that takes another Singleton as an
         * argument in Kotlin.
         *
         * To learn more about Singleton read the wikipedia article:
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): DestinyDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                Log.i(
                    "destinyreader",
                    File(context.applicationInfo.dataDir + "/databases/destiny_manifest.db").length()
                        .toString()
                )

                // If instance is `null` make a new database instance.
                if (instance == null) {
                    val databaseFile =
                        File(context.applicationInfo.dataDir + "/databases/destiny_manifest.db")
                    val destinationFile =
                        File(context.applicationInfo.dataDir + "/databases/manifest.db")
                    if (destinationFile.exists()) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            DestinyDatabase::class.java,
                            "manifest.db"
                        ).fallbackToDestructiveMigration().build()

                    } else {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            DestinyDatabase::class.java,
                            "manifest.db"
                        )
                            .createFromFile(databaseFile)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}

