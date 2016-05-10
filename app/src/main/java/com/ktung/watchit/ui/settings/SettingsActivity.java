package com.ktung.watchit.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.ktung.watchit.R;
import com.ktung.watchit.util.Utils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE_INTENT = 1;
    private static final int TAKE_PICTURE_INTENT = 2;

    @Bind(R.id.selectImageButton)
    protected Button selectImageButton;
    @Bind(R.id.takePictureButton)
    protected Button takePictureButton;

    String imgPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.handleTheme(getApplicationContext(), this, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getFragmentManager().beginTransaction()
        .replace(R.id.prefLayout, new PrefFragment())
        .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (requestCode == SELECT_IMAGE_INTENT) {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int index = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath = cursor.getString(index);
                    cursor.close();
                }
            } else if (requestCode == TAKE_PICTURE_INTENT) {
                File img = new File(imgPath);
                if (!img.exists()) {
                    return;
                }
            }

            if (imgPath != null) {
                preferences.edit().putString("img_cover", imgPath).apply();
            }
        }
    }

    public static class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preference);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (s.equals("darkTheme")) {
                Utils.toaster(getActivity(), R.string.modif_apply_next_time, Toast.LENGTH_SHORT);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.selectImageButton)
    protected void selectImageCb() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE_INTENT);
    }

    @OnClick(R.id.takePictureButton)
    protected void takePictureCb() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            File picFile = null;
            try {
                picFile = createImgFile();
            } catch (IOException e) {
                Timber.e(e, e.getMessage());
            }

            if (picFile != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
                startActivityForResult(i, TAKE_PICTURE_INTENT);
            }
        }
    }

    private File createImgFile() throws IOException {
        String filename = "cover_img";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(filename, ".jpg", storageDir);

        imgPath = img.getAbsolutePath();
        return img;
    }
}
