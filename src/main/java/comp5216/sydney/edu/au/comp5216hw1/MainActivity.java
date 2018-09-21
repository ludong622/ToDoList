package comp5216.sydney.edu.au.comp5216hw1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Define variables
    ListView listView;
    ArrayList<User> items;
    UsersAdapter itemsAdapter;
    EditText addItemEditText;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;

    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);

        // Create an instance of ToDoItemDB and Dao
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();

        // Must call it before creating the adapter, because it references the right item list
        readItemsFromDatabase();

        // Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new UsersAdapter(this, items);

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);

        // Setup listView listeners
        setupListViewListener();
    }

    public void onAddItemClick(View view) {
        Log.i("MainActivity", "Clicked button: ADD NEW");
        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
        intent.putExtra("item", "");
        intent.putExtra("position", 0);
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
        itemsAdapter.notifyDataSetChanged();

        saveItemsToDatabase();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                User editedItem = (User)data.getSerializableExtra("item");
                int position = data.getIntExtra("position", -1);
                itemsAdapter.insert(editedItem, 0);
                items.remove(position+1);
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);

                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();

                saveItemsToDatabase();
            }
        }
        if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                User editedItem = (User)data.getSerializableExtra("item");
                int position = data.getIntExtra("position", -1);
                itemsAdapter.insert(editedItem, 0);
                Log.i("Add Item in list:", editedItem + ",position: 0");

                Toast.makeText(this, "add:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();

                saveItemsToDatabase();
            }
        }
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(position); // Remove item from the ArrayList
                                itemsAdapter.notifyDataSetChanged(); // Notify listView adapter to update the list

                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });

                builder.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User updateItem = itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem.title);
                    intent.putExtra("time", updateItem.time);
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void readItemsFromDatabase()
    {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<User>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (ToDoItem item : itemsFromDB) {
                            User itemRead = new User(item.getToDoItemTitle(), item.getToDoItemTime());
                            items.add(itemRead);
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Name: " + item.getToDoItemTitle());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void saveItemsToDatabase()
    {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //delete all items and re-insert
                toDoItemDao.deleteAll();
                for (User todo : items) {
                    ToDoItem item = new ToDoItem(todo.title, todo.time);
                    toDoItemDao.insert(item);
                    Log.i("SQLite saved item", todo.title);
                }
                return null;
            }
        }.execute();
    }
}

