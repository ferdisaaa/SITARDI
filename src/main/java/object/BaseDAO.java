/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package object;

import java.util.List;
import org.bson.conversions.Bson;

/**
 *
 * @author ASUS
 */
interface BaseDAO<T> {
       // Operasi CRUD Dasar
    void save(T entity);
    void update(Bson filter, T entity);
    void delete(Bson filter);
    
    // Operasi Searching/Reading
    List<T> findAll();
    T findOne(Bson filter); // Mencari satu data spesifik
    List<T> findMany(Bson filter); // Mencari banyak data berdasarkan kriteria
}
