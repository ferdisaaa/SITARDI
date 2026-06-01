/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author ASUS
 */
class MongoManager {

    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "SITARDI";

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
//            mongoClient = MongoClients.create("mongodb://localhost:27017");
            mongoClient = MongoClients.create("mongodb+srv://gezsyx:cfAFDG21e7weksghjgr@cluster0.e5mez1h.mongodb.net/?appName=Cluster0");
//            pw=cfAFDG21e7weksghjgr
        }
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
    }
}
