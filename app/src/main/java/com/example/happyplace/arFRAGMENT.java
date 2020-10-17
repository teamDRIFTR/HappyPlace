package com.example.happyplace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class arFRAGMENT extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_fragment);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        SetUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setModelup(){
        modelRenderable.builder()
                .setSource(this, R.raw.wolves)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(arFRAGMENT.this, "error", Toast.LENGTH_SHORT);
                    return null;
                });
    }
    private void SetUp() {
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult , Plane plane , MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                Create(anchorNode);
            }
        });
    }

    private void Create(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }

}
