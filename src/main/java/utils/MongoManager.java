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

    public static MongoDatabase getDatabase() {

        String url = System.getProperty("MONGODB_URL");
        String databaseName = System.getProperty("DATABASE_NAME");

        if (url == null || url.isEmpty()) {
            throw new RuntimeException("Gagal koneksi: MONGODB_URI tidak ditemukan di VM Options NetBeans!");
        }

        if (mongoClient == null) {
            mongoClient = MongoClients.create(url);
            System.out.println("SITARDI: Berhasil terhubung ke database [" + databaseName + "]");
        }

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        return mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
    }

//   COMENT KODE DIBAWAH DAN UNCOMENT KODE DIATAS UNTUK MENJALANKAN DATABASE LOCAL 
//    public static MongoDatabase getDatabase() {
//        if (mongoClient == null) {
//                mongoClient = MongoClients.create("mongodb://localhost:27017");
//
//        }
//        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
//                MongoClientSettings.getDefaultCodecRegistry(),
//                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
//        );
//        return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
//    }
}
