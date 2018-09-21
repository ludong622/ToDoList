package comp5216.sydney.edu.au.comp5216hw1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private int toDoItemID;

    @ColumnInfo(name = "toDoItemTitle")
    private String toDoItemTitle;

    @ColumnInfo(name = "toDoItemTime")
    private String toDoItemTime;

    public ToDoItem(String toDoItemTitle, String toDoItemTime){
        this.toDoItemTitle = toDoItemTitle;
        this.toDoItemTime = toDoItemTime;
    }

    public int getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(int toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemTitle() {
        return toDoItemTitle;
    }

    public String getToDoItemTime() {
        return toDoItemTime;
    }

    public void setToDoItemTitle(String toDoItemTitle) {
        this.toDoItemTitle = toDoItemTitle;
    }

    public void setToDoItemTime(String toDoItemTime) {
        this.toDoItemTime = toDoItemTime;
    }
}
