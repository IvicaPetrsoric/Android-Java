package hr.rma.sl.navigationdrawergoogle;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sandi on 21.4.2016..
 */
public class ContentFragment extends Fragment implements MediaController.MediaPlayerControl {

    private Cursor cursor;

    // Column index for the Thumbnails Image IDs.

    private int columnIndex;

    private int contentType;

    private String[] mAudioPaths;
    private String[] mMusicList;
    private String[] mVideoPaths;
    private String[] mVideoList;
    private String[] mImageIDs;
    private String[] mImageThumbnailList;
    private String[] mImagePaths;
    private String[] mFullImagePaths;

    private ArrayList mVideoData;
    private ArrayList mAudioData;

    GridView sdcardImages;
    private MediaPlayer mplayer;
    private MediaController mMediaController;
    ListView sdcardAudios;
    private int lastPlayingPosition = -1;


    public ContentFragment() {

    }

    public ContentFragment(int contentType) {
        this.contentType = contentType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mplayer = new MediaPlayer();

        if (contentType == 0) {
            // IMAGE THUMBNAIL RESOURCES
            // Import using custom adapter:
            View rootView = inflater.inflate(R.layout.picture_grid, container, false);
            mImageThumbnailList = getThumbnails();
            sdcardImages = (GridView) rootView.findViewById(R.id.gridView1);
            ThumbsAdapter customAdapter = new ThumbsAdapter(getActivity(), mImagePaths);
            sdcardImages.setAdapter(customAdapter);

            sdcardImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showImage(position);
                }
            });

            return rootView;

        } else if (contentType == 1) {
            // VIDEO RESOURCES
            // Import using custom adapter:
            View rootView = inflater.inflate(R.layout.video_audio_list, container, false);
            mVideoList = getVideoList();
            ListView sdcardVideos = (ListView) rootView.findViewById(R.id.videoaudioListView);
            VideoAudioAdapter customAdapter = new VideoAudioAdapter(getActivity(), mVideoData, 1);
            sdcardVideos.setAdapter(customAdapter);

            sdcardVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    playVideo(position);
                }
            });

            return rootView;

            // Import using ArrayAdapter<String> and android.R.layout.simple_list_item_1:
            /*
            View rootView = inflater.inflate(R.layout.video_audio_list, container, false);
            mVideoList = getVideoList();
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, mVideoList);
            ListView sdcardVideos = (ListView) rootView.findViewById(R.id.videoListView);
            sdcardVideos.setAdapter(mAdapter);
            return rootView;
            */
        } else if (contentType == 2) {
            // AUDIO RESOURCES
            // Import using custom adapter:
            View rootView = inflater.inflate(R.layout.video_audio_list, container, false);
            mMusicList = getAudioList();
            sdcardAudios = (ListView) rootView.findViewById(R.id.videoaudioListView);
            final VideoAudioAdapter customAdapter = new VideoAudioAdapter(getActivity(), mAudioData, 2);
            sdcardAudios.setAdapter(customAdapter);

            sdcardAudios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position != lastPlayingPosition) {
                        lastPlayingPosition = position;
                        playAudio(position);
                    } else {
                        mMediaController.show();
                    }
                }
            });

            return rootView;

            // Import using ArrayAdapter<String> and android.R.layout.simple_list_item_1:
            /*
            View rootView = inflater.inflate(R.layout.video_audio_list, container, false);
            mMusicList = getAudioList();
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, mMusicList);
            ListView sdcardAudios = (ListView) rootView.findViewById(R.id.videoListView);
            sdcardAudios.setAdapter(mAdapter);
            return rootView;
            */
        }
        // shouldn't happen:
        return null;
    }


    // Play video in new activity:
    private void playVideo(int position){
        String[] videoInfo = (String[])mVideoData.get(position);
        String video_DISPLAYNAME = videoInfo[0];
        String video_DATA = videoInfo[5];

        Intent myVideoPlayIntent = new Intent(getActivity(), VideoPlayActivity.class);
        myVideoPlayIntent.putExtra("VIDEO_LOCATION", video_DATA);
        myVideoPlayIntent.putExtra("VIDEO_DISPLAYNAME", video_DISPLAYNAME);
        getActivity().startActivity(myVideoPlayIntent);
    }


    // Play video in the current fragment:
    private void playAudio(int position){
        String[] audioInfo = (String[])mAudioData.get(position);
        String audio_DATA = audioInfo[4];

        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(sdcardAudios);

        mplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaController.setMediaPlayer(ContentFragment.this);
                mMediaController.setEnabled(true);
                mMediaController.show();
            }
        });

        try {
            mplayer.reset();
            mplayer.setDataSource(audio_DATA);
            mplayer.prepare();
            mplayer.start();
        } catch (IOException e) {
            // unexpected error
        }
    }


    // Show image in new activity:
    private void showImage(int position){
        String imageInfo = mFullImagePaths[position];
        System.out.println("imageInfoFull: " + imageInfo);
        System.out.println("imageInfoThumb: " + mImagePaths[position]);

        Intent myImageShowIntent = new Intent(getActivity(), ImageShowActivity.class);
        myImageShowIntent.putExtra("IMAGE_LOCATION", imageInfo);
        getActivity().startActivity(myImageShowIntent);
    }


    // Get audio resources
    private String[] getAudioList() {
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };

        // get cursor, newer audios first:
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                MediaStore.Audio.Media.TITLE + " ASC");
        int count = mCursor.getCount();

        // data bundle to be prepared for custom adapter constructor:
        mAudioData = new ArrayList();

        String[] songs = new String[count];
        mAudioPaths = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                String[] audioInfo = new String[5];
                audioInfo[0] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                audioInfo[1] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                audioInfo[2] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                audioInfo[3] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                audioInfo[4] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                mAudioData.add(audioInfo);

                /*
                songs[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)) +
                        " [" +
                        mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) +
                        "];
                mAudioPaths[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                */
                i++;
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return songs;
    }


    // Get video resources
    private String[] getVideoList() {
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.SIZE };

        // get cursor, newer videos first:
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                MediaStore.Video.Media.DATE_TAKEN + " DESC");
        int count = mCursor.getCount();

        // data bundle to be prepared for custom adapter constructor:
        mVideoData = new ArrayList();

        String[] videos = new String[count];
        mVideoPaths = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                String[] videoInfo = new String[6];
                videoInfo[0] = mCursor.
                    getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                videoInfo[1] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN));
                videoInfo[2] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                videoInfo[3] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                videoInfo[4] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                videoInfo[5] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                mVideoData.add(videoInfo);

                /*
                videos[i] = mCursor.
                    getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)) +
                        " [" +
                        mCursor.
                            getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)) +
                        "]";
                mVideoPaths[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                */
                i++;
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return videos;
    }


    // Get thumbnail resources
    private String[] getThumbnails() {

        String[] projection = {
                MediaStore.Images.Thumbnails._ID,
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };


        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null,
                MediaStore.Images.Thumbnails._ID + " DESC");


        int count = mCursor.getCount();

        String[] thumbs = new String[count];
        mImageIDs = new String[count];
        mImagePaths = new String[count];
        mFullImagePaths = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                thumbs[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID)) +
                        " [" +
                        mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID)) +
                        "]";
                mImageIDs[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));


                mImagePaths[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));

                mFullImagePaths[i] = mCursor.
                        getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
                i++;
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return thumbs;
    }


    // --- MediaPlayerControl methods ---
    public void start() {
        mplayer.start();
    }

    public void pause() {
        mplayer.pause();
    }

    public int getDuration() {
        return mplayer.getDuration();
    }

    public int getCurrentPosition() {
        return mplayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mplayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mplayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId(){
        return mplayer.getAudioSessionId();
    }
    // --- MediaPlayerControl methods ---

}
