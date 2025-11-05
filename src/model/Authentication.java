package model;

public class Authentication {
    private Long id;
    private String user;
    private String password;
    private String access;

    public Authentication(){}
    public Authentication(Long id, String user, String password, String access) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.access = access;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String acess) {
        this.access = acess;
    }
}
