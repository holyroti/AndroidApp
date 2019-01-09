package nl.carlodvm.androidapp.Animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;

import nl.carlodvm.androidapp.AugmentedNode;

public class ScalingNode extends AugmentedNode {
    @Nullable
    private ObjectAnimator scalingAnimation = null;
    private float growthPerSecond = 200f;

    private float lastSpeedMultiplier = 3000000f;

    private boolean firstRun = true;

    private float mScale;

    public ScalingNode(Context context, String path, float scale) {
        super(context, path);
        mScale = scale;
    }

    public ScalingNode(Context context, String path) {
        this(context, path, 1.0f);
    }


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        // Animation hasn't been set up.
        if (scalingAnimation == null) {
            return;
        }

        // Check if we need to change the speed of rotation.
        float speedMultiplier = getSpeedMultiplier();

        // Nothing has changed. Continue rotating at the same speed.
        if (!firstRun)
            return;


        if (speedMultiplier == 0.0f) {
            scalingAnimation.pause();
        } else {
            scalingAnimation.resume();

            float animatedFraction = scalingAnimation.getAnimatedFraction();
            scalingAnimation.setDuration(getAnimationDuration());
            scalingAnimation.setCurrentFraction(animatedFraction);
        }
        lastSpeedMultiplier = speedMultiplier;
        firstRun = false;
    }

    /**
     * Sets rotation speed
     */
    public void setGrowthPerSecondPerSecond(float growthPerSecond) {
        this.growthPerSecond = growthPerSecond;
    }

    @Override
    public void onActivate() {
        startAnimation();
    }

    @Override
    public void onDeactivate() {
        stopAnimation();
    }

    private long getAnimationDuration() {
        return (long) (1000 * 360 / (growthPerSecond * getSpeedMultiplier()));
    }

    private float getSpeedMultiplier() {
        return 0.5f;
    }

    private void startAnimation() {
        if (scalingAnimation != null) {
            return;
        }
        scalingAnimation = createAnimator();
        scalingAnimation.setTarget(this);
        scalingAnimation.setDuration(getAnimationDuration());
        scalingAnimation.start();
        firstRun = true;
    }

    private void stopAnimation() {
        if (scalingAnimation == null) {
            return;
        }
        scalingAnimation.cancel();
        scalingAnimation = null;
    }

    /**
     * Returns an ObjectAnimator that makes this node rotate.
     */
    private ObjectAnimator createAnimator() {
        Vector3 scale1 = new Vector3(mScale, mScale, mScale);
        float calculatedScale = mScale / 3;
        Vector3 scale2 = new Vector3(calculatedScale, calculatedScale, calculatedScale);
        calculatedScale *= 3.5;
        Vector3 scale3 = new Vector3(calculatedScale, calculatedScale, calculatedScale);
        Vector3 scale4 = new Vector3(mScale, mScale, mScale);

        ObjectAnimator orbitAnimation = new ObjectAnimator();
        orbitAnimation.setObjectValues(scale1, scale2, scale3, scale4);

        orbitAnimation.setPropertyName("localScale");

        orbitAnimation.setEvaluator(new Vector3Evaluator());

        orbitAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
        orbitAnimation.setInterpolator(new LinearInterpolator());
        orbitAnimation.setAutoCancel(true);

        return orbitAnimation;
    }
}
