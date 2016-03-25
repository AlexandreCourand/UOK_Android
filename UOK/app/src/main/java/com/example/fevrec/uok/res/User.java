package com.example.fevrec.uok.res;

/**
 * Created by beaussan on 25/03/16.
 */
public class User {
    private String name;
    private String alias;
    private int id = 0;
    private String email;
    private String password;
    private String passwdHash;
    private String salt;
    private String telNumber;
    private boolean isPro;
    private String location;
    private boolean isAcceptingGlobal =true;

    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setIsPro(boolean isPro) {
        this.isPro = isPro;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAcceptingGlobal() {
        return isAcceptingGlobal;
    }

    public void setIsAcceptingGlobal(boolean isAcceptingGlobal) {
        this.isAcceptingGlobal = isAcceptingGlobal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (isPro != user.isPro) return false;
        if (isAcceptingGlobal != user.isAcceptingGlobal) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (alias != null ? !alias.equals(user.alias) : user.alias != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (passwdHash != null ? !passwdHash.equals(user.passwdHash) : user.passwdHash != null)
            return false;
        if (salt != null ? !salt.equals(user.salt) : user.salt != null) return false;
        if (telNumber != null ? !telNumber.equals(user.telNumber) : user.telNumber != null)
            return false;
        return !(location != null ? !location.equals(user.location) : user.location != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwdHash != null ? passwdHash.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (telNumber != null ? telNumber.hashCode() : 0);
        result = 31 * result + (isPro ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (isAcceptingGlobal ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwdHash='" + passwdHash + '\'' +
                ", salt='" + salt + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", isPro=" + isPro +
                ", location='" + location + '\'' +
                ", isAcceptingGlobal=" + isAcceptingGlobal +
                '}';
    }
}
