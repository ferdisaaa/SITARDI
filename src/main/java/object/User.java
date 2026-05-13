/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

/**
 *
 * @author ASUS
 */
public class User {

    private String UserId;
    private String Username;
    private String Name;
    private String Email;
    private String Password;
    private String UrlImg;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUsrname() {
        return Username;
    }

    public void setUsrname(String Username) {
        this.Username = Username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUrlImg() {
        return UrlImg;
    }

    public void setUrlImg(String UrlImg) {
        this.UrlImg = UrlImg;
    }

}
