package com.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.NonNull;

public final class MongoFactoryUtil {
    private static MongoDatabase database;
    private static MongoClient mongoClient;

    private MongoFactoryUtil() {
    }

    public static MongoDatabase connect(@NonNull final String dataBaseName) {
        if (database == null) {
            database = getMongoClient().getDatabase(dataBaseName);
        }
        return database;
    }

    private static MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = new MongoClient("localhost", 27017);
        }
        return mongoClient;
    }
}