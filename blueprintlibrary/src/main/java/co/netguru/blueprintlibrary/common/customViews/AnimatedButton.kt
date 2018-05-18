package co.netguru.blueprintlibrary.common.customViews

interface AnimatedButton {
    fun startAnimation()
    fun revertAnimation()
    fun revertAnimation(onAnimationEndListener: OnAnimationEndListener)
    fun dispose()
}
