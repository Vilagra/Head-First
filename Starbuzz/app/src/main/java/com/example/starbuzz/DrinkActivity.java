package com.example.starbuzz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRIKNO = "drinkno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        Intent intent = getIntent();
        int id = (int) intent.getExtras().get(EXTRA_DRIKNO);
        try {
            SQLiteOpenHelper starbuzzDetabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDetabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("Drink", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"}, "_id=?", new String[]{Integer.toString(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                ImageView image = (ImageView) findViewById(R.id.photo);
                TextView text1 = (TextView) findViewById(R.id.name);
                TextView description = (TextView) findViewById(R.id.description);
                boolean isFavorite = (cursor.getInt(3)) == 1;
                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
                image.setImageResource(photoId);
                text1.setText(nameText);
                description.setText(descriptionText);
                cursor.close();
                db.close();
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public void onFavoriteClicked(View view) {
        int drinkNo = (int) getIntent().getExtras().get(EXTRA_DRIKNO);
        new UpdateDrinkTask().execute(drinkNo);

    }
    private class UpdateDrinkTask extends AsyncTask<Integer,Void,Boolean> {
        ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(DrinkActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkNo = drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper =
                    new StarbuzzDatabaseHelper(DrinkActivity.this);
            try {
                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues,
                        "_id = ?", new String[]{Integer.toString(drinkNo)});
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }


    }


}
