package com.darkndev.drawingusage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class DrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var xCoordinate = 0F
    private var yCoordinate = 0F
    private lateinit var path: Path
    private var strokes = ArrayList<Stroke>()
    private var backupStrokes = ArrayList<Stroke>()

    private lateinit var bitmap: Bitmap
    private lateinit var localCanvas: Canvas
    private val canvasPaint = Paint()
    //private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    var strokeColor = Color.BLACK
    var strokeWidth = 20F

    companion object {
        private const val TOUCH_TOLERANCE = 4F
    }

    init {
        canvasPaint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.GREEN
            strokeWidth = 10F
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            alpha = 0xff
        }
    }

    fun undoStroke() {
        if (strokes.isNotEmpty()) {
            backupStrokes.add(strokes.last())
            strokes.removeLast()
            invalidate()
        }
    }

    fun redoStroke() {
        if (backupStrokes.isNotEmpty()) {
            strokes.add(backupStrokes.last())
            backupStrokes.removeLast()
            invalidate()
        }
    }

    fun clearCanvas() {
        strokes.clear()
        backupStrokes.clear()
        invalidate()
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    override fun onDraw(canvas: Canvas) {

        canvas.save()
        localCanvas.drawColor(Color.WHITE)

        for (stroke in strokes) {
            canvasPaint.color = stroke.color
            canvasPaint.strokeWidth = stroke.strokeWidth
            localCanvas.drawPath(stroke.path, canvasPaint)
        }
        canvas.drawBitmap(bitmap, 0F, 0F, canvasPaint)
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        localCanvas = Canvas(bitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
            }

            MotionEvent.ACTION_UP -> {
                touchUp()
            }
        }
        invalidate()
        return true
    }

    private fun touchStart(x: Float, y: Float) {
        path = Path()
        strokes.add(Stroke(strokeColor, strokeWidth, path))
        path.reset()
        path.moveTo(x, y)

        xCoordinate = x
        yCoordinate = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - xCoordinate)
        val dy = abs(y - yCoordinate)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(xCoordinate, yCoordinate, (x + xCoordinate) / 2, (y + yCoordinate) / 2)
            xCoordinate = x
            yCoordinate = y
        }
    }

    private fun touchUp() {
        path.lineTo(xCoordinate, yCoordinate)
    }
}