package ru.argosaexpert.presentation.map

import android.content.Context
import androidx.core.util.Pair
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import java.util.Random
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MapTools {
    private const val EARTH_RADIUS = 6378140.0

    fun getOSMMapTile(): OnlineTileSourceBase {
        val osmPref = arrayOf("a", "b", "c")
        val str = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        return object : OnlineTileSourceBase(
            "HOT",
            1,
            20,
            256,
            "*.png",
            arrayOf("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                val zoom = MapTileIndex.getZoom(pMapTileIndex)
                return str.replace("{s}", osmPref[Random().nextInt(3) + 0])
                    .replace("{z}", zoom.toString()).replace("{x}", x.toString())
                    .replace("{y}", y.toString())
            }
        }
    }

    fun getGoogleMapTile(googleType: Pair<String, String>): OnlineTileSourceBase {
        val url: String = "https://mt{s}.google.com/vt/lyrs={t}&hl=ru&x={x}&y={y}&z={z}".replace(
            "{t}",
            googleType.second
        )
        return object :
            OnlineTileSourceBase(googleType.first, 1, 23, 256, ".png", arrayOf(url), "© Google") {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                return url.replace("{s}", (Random().nextInt(4) + 0).toString())
                    .replace("{z}", MapTileIndex.getZoom(pMapTileIndex).toString())
                    .replace("{x}", x.toString()).replace("{y}", y.toString())
            }
        }
    }

    fun getYandexMapTile(): OnlineTileSourceBase {
        val str =
            "https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=2.28.0&x={x}&y={y}&z={z}&lang=ru-RU"
        return object : OnlineTileSourceBase(
            "Yandex Map",
            1,
            19,
            256,
            ".png",
            arrayOf("https://core-renderer-tiles.maps.yandex.net/tiles?l=map&v=2.28.0&x={x}&y={y}&z={z}&lang=ru-RU"),
            "© Yandex"
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                return str.replace("{z}", MapTileIndex.getZoom(pMapTileIndex).toString())
                    .replace("{x}", x.toString()).replace("{y}", y.toString())
            }
        }
    }

    fun distanceBetween(a: GeoPoint, b: GeoPoint): Double {
        val lat1 = Math.toRadians(a.latitude)
        val lng1 = Math.toRadians(a.longitude)
        val lat2 = Math.toRadians(b.latitude)
        val cordlen = sin((lat2 - lat1) / 2.0).pow(2.0) + cos(lat1) * cos(lat2) * sin(
            (Math.toRadians(
                b.longitude
            ) - lng1) / 2.0
        ).pow(2.0)
        return EARTH_RADIUS * atan2(
            sqrt(cordlen),
            sqrt(1.0 - cordlen)
        ) * 2.0
    }
}