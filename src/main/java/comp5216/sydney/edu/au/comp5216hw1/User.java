package comp5216.sydney.edu.au.comp5216hw1;

import java.io.Serializable;

public class User implements Serializable{
    public String title;
    public String time;

    public User(String title, String time) {
        this.title = title;
        this.time = time;
    }
}