
package com.google.ar.sceneform.overlei;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.samples.Overlei.R;
import com.google.ar.sceneform.ux.ArFragment;

/**
 * At this point i have installed the google sceneform plugin. This is whats going to help be able to work with 3D assets
 */
//My imports

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.samples.Overlei.R;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

//At this point i have installed the google sceneform plugin. This is
//whats going to help be able to work with 3D assets


public class Overlei extends AppCompatActivity {

  private ArFragment fragment;
  private Uri currentltySelectedObject;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //brings up hand and camera to scan environment
    setContentView(R.layout.activity_ux);

    fragment = (ArFragment)
            getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

    initializeGallary();


    fragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane,
                                      MotionEvent motionEvent) -> {


              //checking if the scene being detected is horizontal
              if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                return;
              }

              //creating anchor
              Anchor anchor = hitResult.createAnchor();
              placeObject(fragment, anchor, currentltySelectedObject);

            }
    );

  }

    //show the menu at the bottom (this works)

  public void initializeGallary() {

    LinearLayout gallary = findViewById(R.id.gallery_layout);

    //create chair thumbnails/picturee
    ImageView chair = new ImageView(this);
    chair.setImageResource(R.drawable.chair_thumb);
    chair.setContentDescription("chair asset");

    //parsing the file, gives reference to object
    chair.setOnClickListener(view -> currentltySelectedObject =
            Uri.parse("chair/chair.sfb"));
    gallary.addView(chair);

    //create couch picture/icon
    //where im getting the image from
    ImageView couch = new ImageView(this);
    //imageView resource
    couch.setImageResource(R.drawable.couch_thumb);
    //attaching a description
    couch.setContentDescription("couch asset");
    //setting onclick action to set the currentlySelectedObject
    couch.setOnClickListener(view -> currentltySelectedObject =
            Uri.parse("couch/model.sfb"));
    gallary.addView(couch);

    //lampPost picture/icon
    ImageView lampPost = new ImageView(this);
    lampPost.setImageResource(R.drawable.lamp_thumb);
    lampPost.setContentDescription("lampPost asset");
    lampPost.setOnClickListener(view -> currentltySelectedObject =
            Uri.parse("LampPost.sfb"));
    gallary.addView(lampPost);
  }

//anchor is where im going to place the object takes into
//this method also has 'build' call method called addNodeToScene, find
//this method below

  private void placeObject(ArFragment arFragment, Anchor anchor, Uri model) {
    ModelRenderable.builder().setSource(arFragment.getContext(),
            model).build()

            .thenAccept(renderable -> addNodeToScene(arFragment, anchor,
                    renderable)).exceptionally((throwable -> {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(throwable.getMessage()).setTitle("Error");

      AlertDialog dialog = builder.create();
      dialog.show();
      return null;
    }));
  }


  private void addNodeToScene(ArFragment arFragment, Anchor anchor,
                              Renderable renderable) {
//create an anchor node
    AnchorNode anchorNode = new AnchorNode(anchor);
//create transformable node
    TransformableNode transformableNode = new
            TransformableNode(arFragment.getTransformationSystem());
    transformableNode.setRenderable(renderable);

//make anchorNode parent of transformable
    transformableNode.setParent(anchorNode);
//add node for interaction
    arFragment.getArSceneView().getScene().addChild(anchorNode);

    transformableNode.select();
  }

}

