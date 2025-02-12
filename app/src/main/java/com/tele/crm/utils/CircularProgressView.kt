package com.tele.crm.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 50 // Current progress (0 to 100)
    private var maxProgress = 100 // Max progress value
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.GRAY
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.YELLOW
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 64f
        textAlign = Paint.Align.CENTER
    }

    fun setProgress(value: Int) {
        progress = value.coerceIn(0, maxProgress) // Ensure value is between 0 and maxProgress
        invalidate() // Redraw the view
    }

    fun setMaxProgress(value: Int) {
        maxProgress = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) - 30f // Adjust radius to avoid clipping

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Calculate sweep angle for progress
        val sweepAngle = (progress.toFloat() / maxProgress) * 360f

        // Draw progress arc
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f, // Start at top
            sweepAngle,
            false,
            progressPaint
        )

        // Draw progress text
        val progressText = "$progress%"
        canvas.drawText(progressText, centerX, centerY + (textPaint.textSize / 3), textPaint)
    }
}
