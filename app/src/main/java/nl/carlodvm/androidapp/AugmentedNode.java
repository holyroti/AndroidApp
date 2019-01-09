package nl.carlodvm.androidapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.function.Consumer;

public class AugmentedNode extends AnchorNode {

    private static final String TAG = "AugmentedImageNode";

    private Renderable model;
    private TransformableNode TransformableModel;
    private AugmentedImage image;
    private TransformableNode transfomNode;

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
    public void renderNode(AugmentedImage image, ArFragment arFragment, Consumer<TransformableNode> config) {
        this.setAnchor(image.createAnchor(image.getCenterPose()));
        this.setParent(arFragment.getArSceneView().getScene());

        this.image = image;

        setTransformableNode(new TransformableNode(arFragment.getTransformationSystem()));
        config.accept(transfomNode);
        transfomNode.getScaleController().setEnabled(false);
        transfomNode.getRotationController().setEnabled(false);
        transfomNode.getTranslationController().setEnabled(false);
        transfomNode.setParent(this);
        transfomNode.setRenderable(model);
        transfomNode.select();
    }

    private void setTransformableNode(TransformableNode transfomNode) {
        if(this.transfomNode == null) {
            this.transfomNode = transfomNode;
        }else{
            this.transfomNode.setRenderable(null);
            this.transfomNode.setParent(null);
            this.transfomNode.setEnabled(false);
            this.transfomNode = transfomNode;
        }
    }

    public AugmentedImage getImage() {
        return image;
    }

    public TransformableNode getTransformableModel() {
        return TransformableModel;
    }
}
