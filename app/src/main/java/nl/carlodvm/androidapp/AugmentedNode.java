package nl.carlodvm.androidapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class AugmentedNode extends AnchorNode {

    private static final String TAG = "AugmentedImageNode";

    private Renderable model;
    private TransformableNode TransformableModel;
    private AugmentedImage image;

    public AugmentedNode(Context context, String path) {
        ModelRenderable
                .builder()
                .setSource(context, Uri.parse(path))
                .build()
                .thenAccept((renderable) -> model = renderable)
                .exceptionally(throwable -> {
                    Log.e(TAG, "Exception loading", throwable);
                    return null;
                });
    }

    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void renderNode(AugmentedImage image, ArFragment arFragment) {
        this.setAnchor(image.createAnchor(image.getCenterPose()));
        this.setParent(arFragment.getArSceneView().getScene());

        this.image = image;

        TransformableNode transfomNode = new TransformableNode(arFragment.getTransformationSystem());
        transfomNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));


        float angle = Vector3.dot(new Vector3(55.06649f, 4.32380F, 67F).normalized()
                , new Vector3(54.06649f, 4.32380F, 67F).normalized());

        //Quaternion lookat = Quaternion.lookRotation(dir, Vector3.up());
        //Quaternion rotatation = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90f);
        //this.setLookDirection(dir);
        Quaternion rot = getLocalRotation();
        rot.x = (float) (Math.PI / 2);
        rot.y = angle > 0 ? 0 : (float) Math.PI;
        //lookat.x = (float) (Math.PI / 2);

        transfomNode.setLocalRotation(rot);

        Vector3 center = new Vector3(.0f, .5f, .0f);
        transfomNode.setWorldPosition(center);
        transfomNode.getScaleController().setEnabled(false);
        transfomNode.getRotationController().setEnabled(false);
        transfomNode.getTranslationController().setEnabled(false);
        transfomNode.setParent(this);
        transfomNode.setRenderable(model);
        transfomNode.select();
    }

    public AugmentedImage getImage() {
        return image;
    }

    public TransformableNode getTransformableModel() {
        return TransformableModel;
    }
}
