package ru.argosaexpert.presentation.map

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapView

class CustomMap(context: Context, attributes: AttributeSet) : MapView(context, attributes) {

    private var initialMapCenter: IGeoPoint? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                initialMapCenter = mapCenter
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2 && initialMapCenter != null) {
                    controller.setCenter(initialMapCenter)
                    setScrollableAreaLimitLatitude(initialMapCenter!!.latitude, initialMapCenter!!.latitude, 0)
                    setScrollableAreaLimitLongitude(initialMapCenter!!.longitude, initialMapCenter!!.longitude, 0)
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                resetScrollableAreaLimitLatitude()
                resetScrollableAreaLimitLongitude()
                initialMapCenter = null
            }

        }
        // Проверяем, что событие имеет pointerCount = 2
        return false
    }
}