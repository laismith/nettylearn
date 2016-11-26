package com.zjut.object;

/**
 * Created by Ryan on 2016/11/22.
 */
public class User {
    private String name;
    private String id;
    private String age;
    public User() {
    }
    public User(String name, String id, String age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }
    public User(String name, long id, int age) {
        this.name = name;
        this.id = Long.toString(id);
        this.age = Integer.toString(age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
