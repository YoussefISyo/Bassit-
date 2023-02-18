package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Album;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.adapters.AlbumAdapter;
import com.optim.bassit.utils.OptimTools;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumsAddEditActivity extends BaseActivity implements AlbumAdapter.ItemClickListener {


    @BindView(R.id.lst)
    RecyclerView lst;

    @Inject
    AppApi appApi;

    HomeFeed mHomeFeed;
    AlbumAdapter adapter;
    List<Album> albums;
    private Album currAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_add_edit);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        mHomeFeed = getIntent().getParcelableExtra("service");

        albums = new ArrayList<>();
        adapter = new AlbumAdapter(this);
        lst.setLayoutManager(new LinearLayoutManager(this));
        lst.setAdapter(adapter);
        lst.getRecycledViewPool().setMaxRecycledViews(1, 0);
        lst.getRecycledViewPool().clear();

        appApi.getAlbums(mHomeFeed.getId()).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {

                    for (Photo pht : response.body()) {

                        boolean bfound = false;
                        for (Album al : albums) {
                            if (al.getTitle().equals(pht.getAlbum())) {
                                al.getPhotos().add(pht);
                                bfound = true;
                            }
                        }
                        if (!bfound) {
                            Album new_album = new Album();
                            new_album.setTitle(pht.getAlbum());
                            new_album.getPhotos().add(pht);
                            albums.add(new_album);

                        }
                    }

                    adapter.fill(albums);
                } else
                    Alert("Erreur de chargement");
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Alert(t.getMessage());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    ArrayList<Parcelable> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    for (Parcelable o : path) {
                        Uri uri = (Uri) o;


                        File imgFile = new File(OptimTools.getAbsolutePathFromUri(uri, this));

                        if (imgFile.exists()) {
                            String photo_path = imgFile.getAbsolutePath();
                            Photo pht = new Photo();
                            pht.setNameLocal(photo_path);
                            currAlbum.getPhotos().add(pht);
                            adapter.photoAdapter.fill(currAlbum);

                            adapter.lst.setAdapter(adapter.photoAdapter);
                            lst.setAdapter(adapter);

                            adapter.lst.scrollToPosition(adapter.photoAdapter.getItemCount() - 1);
                            lst.getRecycledViewPool().clear();
                            adapter.lst.getRecycledViewPool().clear();


                        }


                    }
                    currAlbum = null;

                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                    break;
                }
        }

    }

    @OnClick({R.id.b_add_album})
    public void AddAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Album");
        final EditText input = new EditText(this);

        builder.setView(input);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String s = input.getText().toString();
            if (s.matches(""))
                return;

            OptimTools.hideKeyboard(this);
            Album a = new Album();
            a.setTitle(s);
            albums.add(a);
            adapter.fill(albums);

            adapter.notifyDataSetChanged();
            dialog.dismiss();

        });
        builder.show();

        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @OnClick({R.id.b_cancel})
    public void bCancel() {
        finish();
    }

    @OnClick({R.id.b_confirmer})
    public void bConfirmer() {

        show();
        List<MultipartBody.Part> map = new ArrayList<>();
        Map<String, RequestBody> datainfo = new HashMap<>();
        int i = 0;
        for (Album one : albums) {
            for (Photo pht : one.getPhotos()) {

                MultipartBody.Part imageRequest = prepareFilePart("file[" + i + "]", pht.getName(), pht.isBlocal());
                map.add(imageRequest);

                String s = "key_" + i;
                RequestBody body = RequestBody.create(MediaType.parse("text/plain"), one.getTitle());
                datainfo.put(s, body);
                i++;
            }
        }

        fullyHandleResponse(appApi.uploadAlbums(mHomeFeed.getId(), datainfo, map), new handleResponse() {
            @Override
            public void onSuccess() {
                AlertWait(getString(R.string.albums_saved) , () -> {
                    finish();
                });
            }

            @Override
            public void onFail(String error) {
                hide();
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri, boolean blocal) {

        if (blocal) {
            File file1 = new File(fileUri);
            File file = OptimTools.resizeFile(file1, 1000, this);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } else {
            return MultipartBody.Part.createFormData(partName, fileUri);
        }
        // MultipartBody.Part is used to send also the actual file name

    }

    @Override
    public void addPhoto(Album album) {

        currAlbum = album;

        FishBun.with(this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(10)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(this.getResources().getColor(R.color.colorPrimaryDark), this.getResources().getColor(R.color.colorPrimaryDark), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
//                .setArrayPaths(path)
                .setAlbumSpanCount(1, 1)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back))
                .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check))
                .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check))
                .setAllViewTitle(this.getString(R.string.tous))
                .setMenuAllDoneText(this.getString(R.string.fini))
                .setActionBarTitle(this.getString(R.string.photos))
                .textOnNothingSelected(this.getString(R.string.veuillez_selectionner_au_moins_une_photo))
                .startAlbum();
    }


    @Override
    public void renameAlbum(Album album) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Album");
        final EditText input = new EditText(this);
        input.setText(album.getTitle());
        builder.setView(input);

        builder.setPositiveButton("Modifier", (dialog, which) -> {
            String s = input.getText().toString();
            if (s.matches(""))
                return;

            OptimTools.hideKeyboard(this);

            album.setTitle(s);
            adapter.notifyDataSetChanged();
            dialog.dismiss();

        });
        builder.show();

        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
