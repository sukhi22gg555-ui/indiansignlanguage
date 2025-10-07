package com.example.indiansignlanguage.components

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Build a video file name from text: lowercase, spaces -> underscores, remove extra punctuation, add .mp4
 */
fun toVideoFileName(text: String): String {
    val base = text
        .trim()
        .lowercase()
        .replace("[\\s]+".toRegex(), "_")
        .replace("[^a-z0-9_]+".toRegex(), "")
        .trim('_')
    return if (base.isNotEmpty()) "$base.mp4" else "video.mp4"
}

/**
 * Return an asset:/// URI for the given videos/<fileName> if it exists in assets, else null.
 */
fun assetVideoUriOrNull(context: Context, fileName: String): Uri? {
    return try {
        context.assets.open("videos/$fileName").close()
        Uri.parse("asset:///videos/$fileName")
    } catch (_: Exception) {
        null
    }
}

@Composable
fun AssetVideoPlayer(
    assetUri: Uri,
    modifier: Modifier = Modifier,
    playWhenReady: Boolean = true
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val exoPlayer = remember(assetUri) {
        ExoPlayer.Builder(context).build().apply {
            val item = MediaItem.fromUri(assetUri)
            setMediaItem(item)
            prepare()
            this.playWhenReady = playWhenReady
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
            }
        }
    )
}
