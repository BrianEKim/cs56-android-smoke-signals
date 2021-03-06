package edu.ucsb.cs56.projects.androidapp.smokesignals;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsb.cs56.projects.androidapp.smokesignals.storage.AppDatabase;
import edu.ucsb.cs56.projects.androidapp.smokesignals.storage.PhoneNumber;


import java.util.List;

/**
 * Created by bioburn on 2016/03/09.
 */
public class WhiteList extends Activity {
    //this is one way you can save and open a text file
    private final static String storeText ="storeText.txt";
    private EditText appendText;
    private TextView phoneNumbers;
    private AppDatabase mDb;
    List<PhoneNumber> whiteListed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whitelist_main);

        appendText=(EditText)findViewById(R.id.editText4);
        phoneNumbers=(TextView)findViewById(R.id.textView);
        mDb = AppDatabase.getDataBaseInstance(getApplicationContext());


        //puts up the list
        populateNumbers();
    }

    private void showToast(final String text) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WhiteList.this, text,
                            Toast.LENGTH_LONG).show();
            }
        };

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }


    public void addNumber(View v) {
        String input = appendText.getText().toString();

        final PhoneNumber phoneNumber = new PhoneNumber(input);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mDb.phoneNumberDao().addPhoneNumber(phoneNumber);
                    whiteListed.add(phoneNumber);
                    setNumbers(whiteListed);
                }
                catch (SQLiteConstraintException e) {
                    showToast(phoneNumber + " is taken");
                }

            }
        }).start();

    }

    public void append(View v){
    }

    private void setNumbers(final List<PhoneNumber> numbers) {
        // Doing UI stuff has to be done on the main Thread

        Handler mainHandler = new Handler(getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                for (PhoneNumber phoneNumber : numbers) {
                    stringBuilder.append(phoneNumber);
                    stringBuilder.append('\n');
                }
                phoneNumbers.setText(stringBuilder.toString());
            }
        };

        mainHandler.post(myRunnable);
    }

    public void populateNumbers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // DB queries have to be run on a separate thread
                whiteListed = mDb.phoneNumberDao().getAll();
                setNumbers(whiteListed);
            }
        }).start();
    }
}
