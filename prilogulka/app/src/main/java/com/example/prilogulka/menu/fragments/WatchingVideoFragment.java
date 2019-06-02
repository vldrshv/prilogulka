/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.prilogulka.menu.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.UserIO;
import com.example.prilogulka.data.Video;
import com.example.prilogulka.data.VideoAction;
import com.example.prilogulka.data.VideoItem;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.service.VideoService;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.VideoDAO;
import com.example.prilogulka.facetracker.FaceGraphic;
import com.example.prilogulka.facetracker.ui.camera.CameraSourcePreview;
import com.example.prilogulka.facetracker.ui.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity for the face tracker app.  This app detects faces with the rear facing camera, and draws
 * overlay graphics to indicate the position, size, and ID of each face.
 */
public final class WatchingVideoFragment extends Fragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener {

    // CAMERA VARS
    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // ACTIVITY VARS
    Button btnNext;
    VideoView videoView;
    Video currentVideo;
    Menu menu;

    UserInfo user;
    UserIO USER_IO;
    ArrayList<Uri> uriList;
    List<Video> videoList;
    String email;
    int WATCH_ROW = 1;
    float COEF = 1.0f;

    int playingVideoIndex = 0;

    //ActionsDAO actionsDAO;
    VideoDAO videoDAO;
    SharedPreferencesManager spManager;
    VideoService videoService;
    UserService userService;

    private static final String CLASS_TAG = "WatchingVideoFragment";
    final String CLASS_TITLE = "Рекламные ролики";

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Рекламные ролики");
        super.onCreate(icicle);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_watching_video, container, false);

        USER_IO = new UserIO(getContext());
        user = USER_IO.readUser();

        initUIReference(rootView);
        initDB();

        initServices();

        uriList = new ArrayList<>();
        setVideoURIList();

        checkCameraPermission();
        setHasOptionsMenu(true);

        return rootView;
    }
    private void parseVideos(Video video) {
        if (video == null) {
            Toast.makeText(getContext(), "К сожалению, смотреть нечего :( Мы работаем над этим", Toast.LENGTH_LONG).show();
            return;
        }
        for (VideoItem videoItem : video.getVideoItemList()) {
            Video v = new Video();
            v.getVideoItem().setId(videoItem.getId());
            v.getVideoItem().setUrl(videoItem.getName());
            v.getVideoItem().setWatchCounter(videoItem.getWatchCounter());
            v.getVideoItem().setWatchInRow(videoItem.getWatchInRow());
            videoList.add(v);
        }
    }
    private List<Video> checkVideosExistInDB() {
        return videoDAO.selectAll();
    }
    private void getVideosFromServer() {
        try {
//            if (spManager.getQuestionnaire()) {
                Video video = videoService.getVideos(user.getUser().getId()).execute().body(); // ответил на анкету
                parseVideos(video);
//                System.out.println(videoList);
//                Toast.makeText(getContext(), "ОТВЕТИЛ. БЕРУ", Toast.LENGTH_SHORT).show();
//            }
//            else {
            if (videoList == null || videoList.isEmpty()) {
                videoList = videoService.getAllVideos().execute().body(); // не ответил
                if (videoList != null)
                    videoDAO.insert(videoList);
                Toast.makeText(getContext(), "НЕ (!!!) ОТВЕТИЛ. БЕРУ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setVideoURIList() {
        videoList = new ArrayList<>();
        List<Video> dbVideos = checkVideosExistInDB();
        if (dbVideos.size() == 0)
            getVideosFromServer();
        else
            videoList.addAll(dbVideos);

        if (videoList != null) {
            currentVideo = videoList.get(0);
            WATCH_ROW = currentVideo.getVideoItem().getWatchInRow();
            for (Video v : videoList) {
                Log.i(CLASS_TAG, v.toString());
                uriList.add(Uri.parse("http://92.53.65.46:3000/" + v.getVideoItem().getUrl()));
            }
            videoView.setVideoURI(uriList.get(0));
        }
        else  // нечего смотреть
            Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
        //showHint();

    }
    private void initUIReference(ViewGroup rootView) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);

        videoView = rootView.findViewById(R.id.videoPlayer);
        videoView.stopPlayback();
        videoView.setOnCompletionListener(this);

        btnNext = rootView.findViewById(R.id.button_next);
        btnNext.setEnabled(true);
        btnNext.setOnClickListener(this);

        mPreview = (CameraSourcePreview) rootView.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) rootView.findViewById(R.id.faceOverlay);

    }
    private void initServices() {

        spManager = new SharedPreferencesManager(getContext());
        email = spManager.getActiveUser();
        COEF = spManager.getWPCoefficient();
        System.out.println("COEF = " + COEF);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        videoService = retrofit.create(VideoService.class);
        userService = retrofit.create(UserService.class);
    }
    private void initDB() {
        videoDAO = new VideoDAO(getContext());
    }

    private void sendStatistic(){
        //todo как начисляем прайс? из фронта или в беке?
        VideoAction videoAction = new VideoAction();
        videoAction.getUserVideoAction().setUserId(user.getUser().getId());
        videoAction.getUserVideoAction().setVideoId(currentVideo.getVideoItem().getId());
        videoAction.getUserVideoAction().setIncome(1);//currentVideo.getVideoItem().getPrice());
        videoAction.getUserVideoAction().setWasWatched(1);

        System.out.println(videoAction);
        try {
            videoService.postUserVideoAction(videoAction).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
    private void updateUserLocal() {
        try {
            user = userService.getUser(user.getUser().getEmail()).execute().body();
            USER_IO.writeUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.button_next:
                nextVideo();
                break;
        }
    }
    private void nextVideo() {
        /**
         * WATCH_COUNTER - количество всех просмотров, WATCH_ROW - просмотры подряд
         *
         * если WATCH_ROW = 0 (то юзер посмотрел видос подряд сколько нужно) {
         *      если WATCH_COUNTER = 0, значит больше видео смотреть не надо
         *          удалить из БД
         *      иначе
         *          WATCH_COUNTER --
         *
         *      смотри следующий видос
         *
         * }
         * иначе
         *      смотри еще тот же
         */
        // todo убрать гавнокод
        if (currentVideo == null)
            return;

        stopVideo();
        WATCH_ROW--;
        if (WATCH_ROW == 0) { // просмотров к ряду
            if (currentVideo.getVideoItem().getWatchCounter() == 0) { // всего просмотров
                // больше смотреть не надо, удаляем
                videoDAO.delete(currentVideo);
            } else { // уменьшаем кол-во просмотров
                videoList.get(playingVideoIndex).getVideoItem().setWatchCounter(
                        currentVideo.getVideoItem().getWatchCounter() - 1);
                videoDAO.update(currentVideo);
            }
            if (playingVideoIndex == uriList.size() - 1)
                playingVideoIndex = 0;
            else
                playingVideoIndex++;

            WATCH_ROW = videoList.get(playingVideoIndex).getVideoItem().getWatchInRow();
        }


        videoView.setVideoURI(uriList.get(playingVideoIndex));
        currentVideo = videoList.get(playingVideoIndex);

    }
    private void stopVideo() {
        videoView.stopPlayback();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(CLASS_TAG, "VIDEO #" + playingVideoIndex + " was over, STARTING new video");

        sendStatistic();
        //updateUserLocal();
        nextVideo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        menu.getItem(0).setVisible(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //todo connect to server
                try {
                    user = userService.getUser(email).execute().body();
                    double money = 0;
                    if (user != null)
                        money = user.getUser().getCurrent_balance();
                    item.setTitle("Состояние счета: " + money);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                item.setTitle("Состояние счета: " + getMoney());
                return true;
            case R.id.action_help:
                HintDialogs hd = new HintDialogs(getContext());
                hd.showHint(getString(R.string.watchingVideoHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }


    /**
     * *********************************************************************************************
     *                          Camera and Face Tracking
     * *********************************************************************************************
     */
    public void checkCameraPermission() {
        int rc = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
            createCameraSource();
        }
    }
    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    public void requestCameraPermission() {
        Log.w(CLASS_TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, RC_HANDLE_CAMERA_PERM);

            return;
        }

        final Activity thisActivity = getActivity();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }
    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    public void createCameraSource() {

        Context context = getContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(CLASS_TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }
    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(CLASS_TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(CLASS_TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(CLASS_TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }
    /**
     * *********************************************************************************************
     *                                  Camera Source Preview
     * *********************************************************************************************
     *
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(CLASS_TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
    /**
     * *********************************************************************************************
     *                                  Graphic Face Tracker
     * *********************************************************************************************
     *
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }
    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
//            Snackbar.make(mGraphicOverlay, "onUpdate",
//                    Snackbar.LENGTH_SHORT)
//                    .show();
            videoView.start();
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
//            Snackbar.make(mGraphicOverlay, "onMissing",
//                    Snackbar.LENGTH_SHORT)
//                    .show();
            videoView.pause();
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
//            Snackbar.make(mGraphicOverlay, "onDone",
//                    Snackbar.LENGTH_SHORT)
//                    .show();
            videoView.pause();
        }
    }
    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }
    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        mPreview.stop();
    }
    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }
}
