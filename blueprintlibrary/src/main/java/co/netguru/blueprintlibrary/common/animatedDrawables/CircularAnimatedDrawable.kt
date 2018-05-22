package co.netguru.blueprintlibrary.common.animatedDrawables


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator

class CircularAnimatedDrawable
/**
 *
 * @param view View to be animated
 * @param borderWidth The width of the spinning bar
 * @param arcColor The color of the spinning bar
 */
(private val mAnimatedView: View, private val mBorderWidth: Float, arcColor: Int) : Drawable(), Animatable {
    private var mValueAnimatorAngle: ValueAnimator? = null
    private var mValueAnimatorSweep: ValueAnimator? = null
    private val mAnimatorSet: AnimatorSet?

    private val fBounds = RectF()
    private val mPaint: Paint = Paint()
    private var mCurrentGlobalAngle: Float = 0.toFloat()
    private var mCurrentSweepAngle: Float = 0.toFloat()
    private var mCurrentGlobalAngleOffset: Float = 0.toFloat()

    private var mModeAppearing: Boolean = false
    private var mRunning: Boolean = false

    private var shouldDraw: Boolean = false

    init {

        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBorderWidth
        mPaint.color = arcColor

        setupAnimations()

        shouldDraw = true

        mAnimatorSet = AnimatorSet()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        fBounds.left = bounds.left.toFloat() + mBorderWidth / 2f + .5f
        fBounds.right = bounds.right.toFloat() - mBorderWidth / 2f - .5f
        fBounds.top = bounds.top.toFloat() + mBorderWidth / 2f + .5f
        fBounds.bottom = bounds.bottom.toFloat() - mBorderWidth / 2f - .5f
    }

    fun setLoadingBarColor(color: Int) {
        mPaint.color = color
    }

    /**
     * Start the animation
     */
    override fun start() {
        if (isRunning) {
            return
        }

        mRunning = true

        mAnimatorSet!!.playTogether(mValueAnimatorAngle, mValueAnimatorSweep)
        mAnimatorSet.start()
    }

    /**
     * Stops the animation
     */
    override fun stop() {
        if (!isRunning) {
            return
        }

        mRunning = false
        mAnimatorSet!!.cancel()
    }

    /**
     * Method the inform if the animation is in process
     *
     * @return
     */
    override fun isRunning(): Boolean {
        return mRunning
    }

    /**
     * Method called when the drawable is going to draw itself.
     * @param canvas
     */
    override fun draw(canvas: Canvas) {
        var startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset
        var sweepAngle = mCurrentSweepAngle

        if (!mModeAppearing) {
            startAngle += sweepAngle
            sweepAngle = 360f - sweepAngle - MIN_SWEEP_ANGLE
        } else {
            sweepAngle += MIN_SWEEP_ANGLE
        }

        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    /**
     * Set up all the animations. There are two animation: Global angle animation and sweep animation.
     */
    private fun setupAnimations() {
        mValueAnimatorAngle = ValueAnimator.ofFloat(0F, 360f)
        mValueAnimatorAngle!!.interpolator = ANGLE_INTERPOLATOR
        mValueAnimatorAngle!!.duration = ANGLE_ANIMATOR_DURATION.toLong()
        mValueAnimatorAngle!!.repeatCount = ValueAnimator.INFINITE
        mValueAnimatorAngle!!.addUpdateListener { animation -> mCurrentGlobalAngle = animation.animatedValue as Float }

        mValueAnimatorSweep = ValueAnimator.ofFloat(0F, 360f - 2 * MIN_SWEEP_ANGLE)
        mValueAnimatorSweep!!.interpolator = SWEEP_INTERPOLATOR
        mValueAnimatorSweep!!.duration = SWEEP_ANIMATOR_DURATION.toLong()
        mValueAnimatorSweep!!.repeatCount = ValueAnimator.INFINITE
        mValueAnimatorSweep!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                toggleAppearingMode()
                shouldDraw = false
            }
        })

        mValueAnimatorSweep!!.addUpdateListener { animation ->
            mCurrentSweepAngle = animation.animatedValue as Float

            if (mCurrentSweepAngle < 5) {
                shouldDraw = true
            }

            if (shouldDraw) {
                mAnimatedView.invalidate()
            }
        }

    }

    /**
     * This method is called in every repetition of the animation, so the animation make the sweep
     * growing and then make it shirinking.
     */
    private fun toggleAppearingMode() {
        mModeAppearing = !mModeAppearing

        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360
        }
    }


    fun dispose() {
        if (mValueAnimatorAngle != null) {
            mValueAnimatorAngle!!.end()
            mValueAnimatorAngle!!.removeAllUpdateListeners()
            mValueAnimatorAngle!!.cancel()
        }

        mValueAnimatorAngle = null

        if (mValueAnimatorSweep != null) {
            mValueAnimatorSweep!!.end()
            mValueAnimatorSweep!!.removeAllUpdateListeners()
            mValueAnimatorSweep!!.cancel()
        }

        mValueAnimatorSweep = null

        if (mAnimatorSet != null) {
            mAnimatorSet.end()
            mAnimatorSet.cancel()
        }
    }

    companion object {
        private val ANGLE_INTERPOLATOR = LinearInterpolator()
        private val SWEEP_INTERPOLATOR = AccelerateDecelerateInterpolator()
        private const val ANGLE_ANIMATOR_DURATION = 2000
        private const val SWEEP_ANIMATOR_DURATION = 700
        private const val MIN_SWEEP_ANGLE = 50f
    }
}
