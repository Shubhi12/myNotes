package com.saloni.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.R.id.content;

public class EditNote extends AppCompatActivity {
    EditText mBodyText, nameText;
    ImageButton save, delete;
    public static String curDate = "";
    NDb mydb;
    int id_To_Update;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mBodyText=(EditText)findViewById(R.id.txtcontent);
        nameText=(EditText)findViewById(R.id.txtname);
        save=(ImageButton)findViewById(R.id.save);
        delete=(ImageButton)findViewById(R.id.delete);
        mydb = new NDb(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String nam = rs.getString(rs.getColumnIndex(NDb.name));
                String contents = rs.getString(rs.getColumnIndex(NDb.remark));
                if (!rs.isClosed()) {
                    rs.close();
                }
                nameText.setText((CharSequence) nam);
                mBodyText.setText((CharSequence) contents);
            }
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                Calendar c = Calendar.getInstance();
                //System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                curDate = formattedDate;
                if (extras != null) {
                    int Value = extras.getInt("id");
                    if (Value > 0) {
                        if (mBodyText.getText().toString().trim().equals("")
                                || nameText.getText().toString().trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        } else {
                            if (mydb.updateNotes(id_To_Update, nameText.getText()
                                    .toString(), curDate, mBodyText.getText()
                                    .toString())) {
                                Toast.makeText(getApplicationContext(), "Your note Updated Successfully!!!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        if (mBodyText.getText().toString().trim().equals("")
                                || nameText.getText().toString().trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Please fill in name of the note", Toast.LENGTH_LONG).show();
                        } else {
                            if (mydb.insertNotes(nameText.getText().toString(), curDate,
                                    mBodyText.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Added Successfully.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unfortunately Task Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb.deleteNotes(id_To_Update);
                Toast.makeText(EditNote.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(
                getApplicationContext(),
                MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    }
