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
                mongoClient = MongoClients.create("mongodb://localhost:27017");

        }
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
    }
    
    
    
//   COMENT KODE DIBAWAH DAN UNCOMENT KODE DIATAS UNTUK MENJALANKAN DATABASE LOCAL 
//    public static MongoDatabase getDatabase() {
//        if (mongoClient == null) {
//            // Ambil URI secara aman dari file config.properties
//            String uri = utils.Database.getMongoUrl();
//
//            if (uri == null || uri.isEmpty()) {
//                throw new RuntimeException("Gagal koneksi: MONGODB_URL tidak ditemukan di .env!");
//            }
//
//            mongoClient = MongoClients.create(uri);
//        }
//
//        // Registrasi Codec untuk Mapping POJO (Object Java ke MongoDB)
//        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
//                MongoClientSettings.getDefaultCodecRegistry(),
//                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
//        );
//
//        // DATABASE_NAME pastikan sudah terdefinisi di class ini (misal: "SITARDI_DB")
//        return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
//    }
    
}
