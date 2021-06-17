package org.meicode.appfilm.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.pm.PackageManager;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.Util;

import org.meicode.appfilm.R;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class VideoPlayerActivity extends AppCompatActivity {
    private PlayerView playerV;
    private SimpleExoPlayer SimExo;
    ProgressBar progressBar;
    private ImageButton pipBtn;

    boolean playWhenReady = true;
    int currentWindow = 0;
    long playbackPosition = 0;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);

        playerV = findViewById(R.id.player);
        progressBar = findViewById(R.id.progress_bar);
        url = getIntent().getStringExtra("url");
        pipBtn = findViewById(R.id.pipBtn);

        pipBtn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                    getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
                enterPIP();
            } else {
                Toast.makeText(this, "Điện thoại của bạn không hỗ trợ chức năng này", Toast.LENGTH_LONG);
            }
        });
    }

    private void enterPIP() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            long videoPosition = SimExo.getCurrentPosition();
            playerV.setUseController(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder params = new PictureInPictureParams.Builder();
                this.enterPictureInPictureMode(params.build());
            } else {
                this.enterPictureInPictureMode();
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (!isInPictureInPictureMode) {
            playerV.setUseController(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpPlayer();

        SimExo.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState == Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void setUpPlayer() {
        if(url.contains("youtube")) {
            setUpPlayerYoutube(url);
        } else {
            setUpPlayerExo(url);
        }
    }

    private void setUpPlayerExo(String url){
        LoadControl Load = new DefaultLoadControl();
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelector  Track = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        SimExo = new SimpleExoPlayer.Builder(this).build();
        playerV.setPlayer(SimExo);
        DataSource.Factory dataSourceFac = new DefaultDataSourceFactory(this, Util.getUserAgent(this,"movieapp"));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFac)
                .createMediaSource(MediaItem.fromUri(url));
        SimExo.setMediaSource(mediaSource);
        SimExo.prepare();
        SimExo.setPlayWhenReady(true);
    }
    @SuppressLint("StaticFieldLeak")
    private void setUpPlayerYoutube(String url) {
        SimExo = new SimpleExoPlayer.Builder(this).build();
        playerV.setPlayer(SimExo);
        new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if(ytFiles != null) {
                    int videoTag = 22; //video tag for 720p MP4
                    int audioTag = 140; //Audio tag for m4a
                    MediaSource audioSrc = new ProgressiveMediaSource
                            .Factory(new DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).getUrl()));
                    MediaSource videoSrc = new ProgressiveMediaSource
                            .Factory(new DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).getUrl()));
                    SimExo.setMediaSource(new MergingMediaSource(
                            true,
                            videoSrc,
                            audioSrc),
                            true
                    );
                    SimExo.prepare();
                    SimExo.setPlayWhenReady(playWhenReady);
                    SimExo.seekTo(currentWindow, playbackPosition);
                }
            }
        }.extract(url, false, true);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        enterPIP();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SimExo.release();
    }
}