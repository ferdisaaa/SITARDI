/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sitardi.Serial;

/**
 *
 * @author Al
 * @param <T>
 */
public interface SerialDtHandler<T> {
    void onDataReceived(T data);
}
