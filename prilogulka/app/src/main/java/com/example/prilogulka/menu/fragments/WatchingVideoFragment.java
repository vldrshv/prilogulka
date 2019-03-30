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
import android.widget.TextView;
import android.widget.VideoView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.Video;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
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

/**
 * Activity for the face tracker app.  This app detects faces with the rear facing camera, and draws
 * overlay graphics to indicate the position, size, and ID of each face.
 */
public final class WatchingVideoFragment extends Fragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener {

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    Button btnNext;
    VideoView videoView;
    TextView textViewVideoCounter, textViewTimeNow, textViewTimeFull;
    Menu menu;

    int money = 0;
    Uri uriList[];
    String email;
    float WP = 51.12f;
    float COEF = 1.0f;

    int playingVideoIndex = 0;

    UserActionsDataBase actionsDB;
    SharedPreferencesManager spManager;

    private static final String CLASS_TAG = "WatchingVideoFragment";
    final String CLASS_TITLE = "Рекламные ролики";
    //==============================================================================================
    // Activity Methods
    //==============================================================================================
    /**
     * TODO: почистить класс
     */
    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Рекламные ролики");
        super.onCreate(icicle);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_watching_video, container, false);

        initUIReference(rootView);

        textViewVideoCounter.setText(money + "");

        Uri myUri1 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video1.mp4");
        Uri myUri2 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video2.mp4");
        Uri myUri3 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video3.mp4");
        Uri myUri4 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video4.mp4");
        Uri myUri5 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video5.mp4");
        Uri myUri6 = Uri.parse("http://ec2-18-194-207-144.eu-central-1.compute.amazonaws.com/1/video6.mp4");

        uriList = new Uri[]{myUri1, myUri2, myUri3, myUri4, myUri5, myUri6};

        videoView.setVideoURI(uriList[0]);

        checkCameraPermission();

        setHasOptionsMenu(true);

        return rootView;
    }

    private void initUIReference(ViewGroup rootView) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);
        textViewVideoCounter = rootView.findViewById(R.id.textViewVideoCounter);
        textViewTimeNow = rootView.findViewById(R.id.tvTimeNow);
        textViewTimeFull = rootView.findViewById(R.id.tvTimeFull);

        videoView = rootView.findViewById(R.id.videoPlayer);
        videoView.stopPlayback();
        videoView.setOnCompletionListener(this);

        btnNext = rootView.findViewById(R.id.button_next);
        btnNext.setEnabled(true);
        btnNext.setOnClickListener(this);

        mPreview = (CameraSourcePreview) rootView.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) rootView.findViewById(R.id.faceOverlay);

        spManager = new SharedPreferencesManager(getContext());
        email = spManager.getActiveUser();
        COEF = spManager.getWPCoefficient();
        System.out.println("COEF = " + COEF);
        actionsDB = new UserActionsDataBaseImpl(getContext());
    }

    public void checkCameraPermission() {
        int rc = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
            createCameraSource();
        }
    }

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
        if (playingVideoIndex == uriList.length - 1)
            playingVideoIndex = 0;
        else
            playingVideoIndex++;

        stopVideo();
        videoView.setVideoURI(uriList[playingVideoIndex]);
        textViewTimeNow.setText(videoView.getCurrentPosition()+"");
        textViewTimeFull.setText(videoView.getDuration() + "");
        menu.getItem(0).setTitle("Состояние счета: " + getMoney());
    }

    private void stopVideo() {
        videoView.stopPlayback();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    public float getMoney(){
        return new UserActionsDataBaseImpl(getContext()).getUserMoney(email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                item.setTitle("Состояние счета: " + getMoney());
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(CLASS_TAG, "VIDEO #" + playingVideoIndex + " was over, STARTING new video");
        money++;

        actionsDB.insertUserActions(new Video(1, email,
                playingVideoIndex + "", (int)(WP * COEF),
                Time.getTodayTime() + " " + Time.getToday()));
        textViewVideoCounter.setText(money + "");
        nextVideo();
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
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
        videoView.resume();
    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        mPreview.stop();
        Log.i(CLASS_TAG, "onPause");
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
//
            //mPreview.release();
//
        }
        Log.i(CLASS_TAG, "onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        mPreview.stop();
        videoView.pause();
        Log.i(CLASS_TAG, "onStop");
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

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
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

        Log.i(CLASS_TAG, (mCameraSource == null) + "");
        Log.i(CLASS_TAG, (mGraphicOverlay == null) + "");

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

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
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
}
