/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

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
public class MongoManager {
    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "SITARDI";

    public static MongoDatabase getDatabase() {

            // 1. Konfigurasi CodecRegistry untuk pemetaan POJO otomatis (Standard Industry)
            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            
            // 2. Terapkan registry tersebut ke pengaturan MongoClient
            MongoClientSettings settings = MongoClientSettings.builder()
            // Ganti URI sesuai dengan koneksi database Anda
            .applyConnectionString(new com.mongodb.ConnectionString("mongodb://localhost:27017")) 
            .codecRegistry(pojoCodecRegistry) // Masukkan codec di sini!
            .build();
            
            // 3. Buat MongoClient dan Database menggunakan pengaturan tersebut
            mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            return database; 
    }   
}