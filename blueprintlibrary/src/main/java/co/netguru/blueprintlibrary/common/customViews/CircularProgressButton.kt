package co.netguru.blueprintlibrary.common.customViews

import android.animation.*
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Handler
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.animatedDrawables.CircularAnimatedDrawable
import co.netguru.blueprintlibrary.common.animatedDrawables.CircularRevealAnimatedDrawable


class CircularProgressButton : AppCompatButton, AnimatedButton, CustomizableByCode {
    private var gradientDrawable: GradientDrawable? = null
    private var isMorphingInProgress: Boolean = false
    private var state: State? = null
    private var animatedDrawable: CircularAnimatedDrawable? = null
    private var revealDrawable: CircularRevealAnimatedDrawable? = null
    private var animatorSet: AnimatorSet? = null
    private var fillColorDone: Int = 0
    private var bitmapDone: Bitmap? = null
    private var params: Params? = null
    private var doneWhileMorphing: Boolean = false

    /**
     * Check if button is animating
     */
    val isAnimating: Boolean?
        get() = state == State.PROGRESS

    private enum class State {
        PROGRESS, IDLE, DONE, STOPPED
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init(context, attrs, defStyleAttr, 0)
    }

    @TargetApi(23)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    /**
     * Commom initializer method.
     *
     * @param context Context
     * @param attrs   Attributes passed in the XML
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        params = Params()

        params!!.paddingProgress = 0f

        if (attrs == null) {
            loadGradientDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_green))
        } else {
            val attrsArray = intArrayOf(android.R.attr.background)// 0

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressButton, defStyleAttr, defStyleRes)
            val typedArrayBG = context.obtainStyledAttributes(attrs, attrsArray, defStyleAttr, defStyleRes)

            loadGradientDrawable(typedArrayBG.getDrawable(0))

            params!!.initialCornerRadius = typedArray.getDimension(
                    R.styleable.CircularProgressButton_initialCornerAngle, 0f)
            params!!.finalCornerRadius = typedArray.getDimension(
                    R.styleable.CircularProgressButton_finalCornerAngle, 100f)
            params!!.spinningBarWidth = typedArray.getDimension(
                    R.styleable.CircularProgressButton_spinning_bar_width, 10f)
            params!!.spinningBarColor = typedArray.getColor(R.styleable.CircularProgressButton_spinning_bar_color,
                    ContextCompat.getColor(context, android.R.color.white))
            params!!.paddingProgress = typedArray.getDimension(R.styleable.CircularProgressButton_spinning_bar_padding, 0f)
            params!!.animationBackground = typedArray.getDrawable(R.styleable.CircularProgressButton_animationBackground)
            params!!.animationTextColor = typedArray.getColor(R.styleable.CircularProgressButton_animationTextColor,
                    ContextCompat.getColor(context, R.color.backgroundColor))

            typedArray.recycle()
            typedArrayBG.recycle()
        }

        state = State.IDLE

        params!!.text = this.text.toString()
        params!!.drawables = this.compoundDrawablesRelative
        if (gradientDrawable != null) {
            background = gradientDrawable
        }
    }

    override fun setBackgroundColor(color: Int) {
        gradientDrawable!!.setColor(color)
    }

    override fun setBackgroundResource(@ColorRes resid: Int) {
        gradientDrawable!!.setColor(ContextCompat.getColor(context, resid))
    }

    private fun loadGradientDrawable(drawable: Drawable?) {
        try {
            gradientDrawable = drawable as GradientDrawable?
        } catch (e: ClassCastException) {
            if (drawable is ColorDrawable) {
                val colorDrawable = drawable as ColorDrawable?

                gradientDrawable = GradientDrawable()
                gradientDrawable!!.setColor(colorDrawable!!.color)
            } else if (drawable is StateListDrawable) {
                val stateListDrawable = drawable as StateListDrawable?

                try {
                    gradientDrawable = stateListDrawable!!.current as GradientDrawable
                } catch (e1: ClassCastException) {
                    throw RuntimeException("Error reading background... Use a shape or a color in xml!", e1.cause)
                }

            }
        }

    }

    override fun setSpinningBarColor(color: Int) {
        params!!.spinningBarColor = color
        if (this.animatedDrawable != null) {
            animatedDrawable!!.setLoadingBarColor(color)
        }
    }

    override fun setSpinningBarWidth(width: Float) {
        params!!.spinningBarWidth = width
    }

    override fun setDoneColor(color: Int) {
        params!!.doneColor = color
    }

    override fun setPaddingProgress(padding: Float) {
        params!!.paddingProgress = padding
    }

    override fun setInitialHeight(height: Int) {
        params!!.initialHeight = height
    }

    override fun setInitialCornerRadius(radius: Float) {
        params!!.initialCornerRadius = radius
    }

    override fun setFinalCornerRadius(radius: Float) {
        params!!.finalCornerRadius = radius
    }

    override fun setAnimationBackground(background: Drawable) {
        params!!.animationBackground = background
    }

    override fun setAnimationTextColor(color: Int) {
        params!!.animationTextColor = color
    }

    /**
     * This method is called when the button and its dependencies are going to draw it selves.
     *
     * @param canvas Canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (state == State.PROGRESS && !isMorphingInProgress) {
            drawIndeterminateProgress(canvas)
        } else if (state == State.DONE) {
            drawDoneAnimation(canvas)
        }
    }

    /**
     * If the animatedDrawable is null or its not running, it get created. Otherwise its draw method is
     * called here.
     *
     * @param canvas Canvas
     */
    private fun drawIndeterminateProgress(canvas: Canvas) =
            if (animatedDrawable == null || !animatedDrawable!!.isRunning) {
                animatedDrawable = CircularAnimatedDrawable(this,
                        params!!.spinningBarWidth,
                        params!!.spinningBarColor)

                val offset = (width - height) / 2

                val left = offset + params!!.paddingProgress!!.toInt()
                val right = width - offset - params!!.paddingProgress!!.toInt()
                val bottom = height - params!!.paddingProgress!!.toInt()
                val top = params!!.paddingProgress!!.toInt()

                animatedDrawable!!.setBounds(left, top, right, bottom)
                animatedDrawable!!.callback = this
                animatedDrawable!!.start()
            } else {
                animatedDrawable!!.draw(canvas)
            }


    /**
     * Stops the animation and sets the button in the STOPPED state.
     */
    fun stopAnimation() {
        if (animatedDrawable != null && state == State.PROGRESS && !isMorphingInProgress) {
            state = State.STOPPED
            animatedDrawable!!.stop()
        }
    }

    /**
     * Call this method when you want to show a 'completed' or a 'done' status. You have to choose the
     * color and the image to be shown. If your loading progress ended with a success status you probably
     * want to put a icon for "success" and a blue color, otherwise red and a failure icon. You can also
     * show that a music is completed... or show some status on a game... be creative!
     *
     * @param fillColor The color of the background of the button
     * @param bitmap    The image that will be shown
     */
    fun doneLoadingAnimation(fillColor: Int, bitmap: Bitmap?) {
        if (state != State.PROGRESS) {
            return
        }

        if (isMorphingInProgress) {
            doneWhileMorphing = true
            fillColorDone = fillColor
            bitmapDone = bitmap
            return
        }

        state = State.DONE
        animatedDrawable!!.stop()

        revealDrawable = CircularRevealAnimatedDrawable(this, fillColor, bitmap)

        val left = 0
        val right = width
        val bottom = height
        val top = 0

        revealDrawable!!.setBounds(left, top, right, bottom)
        revealDrawable!!.callback = this
        revealDrawable!!.start()
    }

    /**
     * Method called on the onDraw when the button is on DONE status
     *
     * @param canvas Canvas
     */
    private fun drawDoneAnimation(canvas: Canvas) {
        revealDrawable!!.draw(canvas)
    }

    override fun revertAnimation() {
        revertAnimation(object : OnAnimationEndListener {
            override fun onAnimationEnd() {

            }
        })
    }

    override fun revertAnimation(onAnimationEndListener: OnAnimationEndListener) {
        if (state == State.IDLE) {
            return
        }

        state = State.IDLE

        if (animatedDrawable != null && animatedDrawable!!.isRunning) {
            stopAnimation()
        }

        if (isMorphingInProgress) {
            animatorSet!!.cancel()
        }

        isClickable = false

        val fromWidth = width
        val fromHeight = height

        val toHeight = params!!.initialHeight!!
        val toWidth = params!!.initialWidth
        var cornerAnimation: ObjectAnimator? = null
        if (gradientDrawable != null) {
            cornerAnimation = ObjectAnimator.ofFloat(gradientDrawable,
                    context.getString(R.string.cornerRadius),
                    params!!.finalCornerRadius,
                    params!!.initialCornerRadius)
        }

        val widthAnimation = ValueAnimator.ofInt(fromWidth, toWidth)
        widthAnimation.addUpdateListener { valueAnimator ->
            val layoutParams = layoutParams
            layoutParams.width = valueAnimator.animatedValue as Int
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(fromHeight, toHeight)
        heightAnimation.addUpdateListener { valueAnimator ->
            val layoutParams = layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            setLayoutParams(layoutParams)
        }

        animatorSet = AnimatorSet()
        animatorSet!!.duration = ANIMATION_LENGTH
        if (gradientDrawable != null) {
            animatorSet!!.playTogether(cornerAnimation, widthAnimation, heightAnimation)
        } else {
            animatorSet!!.playTogether(widthAnimation, heightAnimation)
        }
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isClickable = true
                isMorphingInProgress = false
                text = params!!.text
                setCompoundDrawablesRelative(params!!.drawables!![0], params!!.drawables!![1], params!!.drawables!![2], params!!.drawables!![3])
                onAnimationEndListener.onAnimationEnd()
            }
        })

        isMorphingInProgress = true
        animatorSet!!.start()
    }

    override fun dispose() {
        if (animatedDrawable != null) {
            animatedDrawable!!.dispose()
        }

        if (revealDrawable != null) {
            revealDrawable!!.dispose()
        }
    }

    /**
     * Method called to start the animation. Morphs in to a ball and then starts a loading spinner.
     */
    override fun startAnimation() {
        background = params!!.animationBackground
        setTextColor(params!!.animationTextColor)

        if (state != State.IDLE) {
            return
        }

        if (isMorphingInProgress) {
            animatorSet!!.cancel()
        } else {
            params!!.initialWidth = width
            params!!.initialHeight = height
        }

        state = State.PROGRESS

        params!!.text = text.toString()

        this.setCompoundDrawables(null, null, null, null)
        this.text = null
        isClickable = false

        val toHeight = params!!.initialHeight!!

        val cornerAnimation = ObjectAnimator.ofFloat(gradientDrawable,
                context.getString(R.string.cornerRadius),
                params!!.initialCornerRadius,
                params!!.finalCornerRadius)

        val widthAnimation = ValueAnimator.ofInt(params!!.initialWidth, toHeight)
        widthAnimation.addUpdateListener { valueAnimator ->
            val layoutParams = layoutParams
            layoutParams.width = valueAnimator.animatedValue as Int
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(params!!.initialHeight!!, toHeight)
        heightAnimation.addUpdateListener { valueAnimator ->
            val layoutParams = layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            setLayoutParams(layoutParams)
        }

        animatorSet = AnimatorSet()
        animatorSet!!.duration = ANIMATION_LENGTH
        animatorSet!!.playTogether(cornerAnimation, widthAnimation, heightAnimation)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isMorphingInProgress = false

                if (doneWhileMorphing) {
                    doneWhileMorphing = false
                    val runnable = { doneLoadingAnimation(fillColorDone, bitmapDone) }
                    Handler().postDelayed(runnable, 50)
                }
            }
        })

        isMorphingInProgress = true
        animatorSet!!.start()
    }

    /**
     * Class with all the params to configure the button.
     */
    class Params {
        internal var text: String? = null
        internal var initialHeight: Int? = null
        internal var initialWidth: Int = 0
        internal var spinningBarWidth: Float = 0.toFloat()
        internal var spinningBarColor: Int = 0
        internal var animationTextColor: Int = 0
        internal var doneColor: Int = 0
        internal var paddingProgress: Float? = null
        internal var initialCornerRadius: Float = 0.toFloat()
        internal var finalCornerRadius: Float = 0.toFloat()
        internal var animationBackground: Drawable? = null
        internal var drawables: Array<Drawable>? = null

        fun text(text: String): Params {
            this.text = text
            return this
        }

        fun initialHeight(initialHeight: Int?): Params {
            this.initialHeight = initialHeight
            return this
        }

        fun initialWidth(initialWidth: Int): Params {
            this.initialWidth = initialWidth
            return this
        }

        fun spinningBarWidth(spinningBarWidth: Float): Params {
            this.spinningBarWidth = spinningBarWidth
            return this
        }

        fun spinningBarColor(spinningBarColor: Int): Params {
            this.spinningBarColor = spinningBarColor
            return this
        }

        fun doneColor(doneColor: Int): Params {
            this.doneColor = doneColor
            return this
        }

        fun paddingProgress(paddingProgress: Float?): Params {
            this.paddingProgress = paddingProgress
            return this
        }

        fun initialCornerRadius(initialCornerRadius: Float): Params {
            this.initialCornerRadius = initialCornerRadius
            return this
        }

        fun finalCornerRadius(finalCornerRadius: Float): Params {
            this.finalCornerRadius = finalCornerRadius
            return this
        }

        fun drawables(drawables: Array<Drawable>): Params {
            this.drawables = drawables
            return this
        }

        companion object {

            fun create(): Params {
                return Params()
            }
        }


    }

    companion object {

        var ANIMATION_LENGTH: Long = 300
    }
}
