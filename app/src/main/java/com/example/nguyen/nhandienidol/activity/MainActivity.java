package com.example.nguyen.nhandienidol.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nguyen.nhandienidol.R;
import com.example.nguyen.nhandienidol.base.Functions;
import com.example.nguyen.nhandienidol.base.ImageLoader;
import com.example.nguyen.nhandienidol.base.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import com.sangcomz.fishbun.define.Define;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;



public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imgIdol)
    ImageView imgIdol;

    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.imgIdol)
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.imgIdol:
                openOptionUploadImage();
                break;
        }
    }

    // Choose Image
    private void openOptionUploadImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                return;
            }
        }
        CropImage.startPickImageActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Define.ALBUM_REQUEST_CODE && data != null && data.getParcelableArrayListExtra(Define.INTENT_PATH) != null) {
                ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                if (image_uris.size() > 0) {
                    startSendImageFile(image_uris.get(0));
                }
            }


            // handle result of pick image chooser
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri;
                    requestPermissions();
                } else {
                    // no permissions required or already granted, can start crop image activity
                    startCropImageActivity(imageUri);
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                startSendImageFile(resultUri);
            }

        }
    }

    private void startSendImageFile(Uri uri) {
        ImageLoader.getInstance().loadImageIdol(MainActivity.this,
                uri.toString(), imgIdol);

    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(MainActivity.this);
            }
            else {
                Toast.makeText(this, "Bạn cần phải cho phép", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(this);
    }

}
