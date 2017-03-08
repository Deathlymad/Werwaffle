package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.philip.werwaffle.R;



public class EditProfil extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    public EditText inputTxt;
    public ImageButton selectBut;
    private SharedPreferences prefSettings;
    public String aName;


    public void init(){

        inputTxt = (EditText) findViewById(R.id.creatlobname);
        selectBut = (ImageButton) findViewById(R.id.imageBut);

        prefSettings = getSharedPreferences("profil", MODE_PRIVATE);
        aName = prefSettings.getString("name", "Empty");

        inputTxt.setText(aName);

        ImageView imgView = (ImageView) findViewById(R.id.imageProfile);
        prefSettings = getSharedPreferences("profil", MODE_PRIVATE);
        String aimgDecodableString = prefSettings.getString("img", "None");

        if (aimgDecodableString != "None") {
            imgView.setImageBitmap(com.example.philip.werwaffle.activity.RoundedImageView.getCroppedBitmap(
                    BitmapFactory.decodeFile(aimgDecodableString), 2000));
        }




        selectBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

            }

        });

    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageProfile);
                SharedPreferences.Editor prefEditor = getSharedPreferences("profil", MODE_PRIVATE).edit();
                prefEditor.putString("img", imgDecodableString);
                prefEditor.apply();

                imgView.setImageBitmap(com.example.philip.werwaffle.activity.RoundedImageView.getCroppedBitmap(
                        BitmapFactory.decodeFile(imgDecodableString),2000 ));

            } else {
                Toast.makeText(this, getString(R.string.havent_picked_img), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.went_wrong), Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        String name = inputTxt.getText().toString();
        SharedPreferences.Editor prefEditor = getSharedPreferences("profil", MODE_PRIVATE).edit();
        SharedPreferences preferences = getSharedPreferences("profil", MODE_PRIVATE);
        prefEditor.putString("name", name);
        prefEditor.apply();
        String uniqkey = preferences.getString("uniqueKEy", "None");
        String img = preferences.getString("img", "None");
        addPlayer.addPlayer(name,img,2,0,uniqkey);
    }

}