package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.collision.Box;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class CardDual extends AppCompatActivity {
    ArFragment arFragment;
    boolean shouldAddModel = true;
    FloatingActionButton btnMusicToggle;
    MediaPlayer mediaPlayer;
    boolean musicEnabled  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_dual);
        arFragment = (CardDualFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

        btnMusicToggle = findViewById(R.id.btnMusicToggle);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.battle_plane_home_music_box);
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

        if(musicEnabled == true){
            // Set Music To False

            btnMusicToggle.setImageResource(R.drawable.ic_cryptids_logo_icon);
            mediaPlayer.start();
        } else if (musicEnabled == false){
            //enable music

            btnMusicToggle.setImageResource(R.drawable.ic_cryptids_music_enabled);

            mediaPlayer.pause();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    mediaPlayer.start();
                                                }
                                            });

        btnMusicToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicEnabled == true){
                    // Set Music To False
                    musicEnabled = false;
                    btnMusicToggle.setImageResource(R.drawable.ic_cryptids_music_enabled);
                    mediaPlayer.pause();
                } else if (musicEnabled == false){
                    //enable music
                    musicEnabled = true;

                    btnMusicToggle.setImageResource(R.drawable.ic_cryptids_logo_icon);
                    mediaPlayer.start();
                }
            }
        });



        // NAVIGATION BAR CODE TODO - MOVE TO FRAGMENT
        // OnClickListeners For Bottom Nav - TODO Code Refs!!!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_open_dual);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        Intent I = new Intent(getApplicationContext(),ScanMenu.class);
                        I.putExtra("is_transfer",false);
                        startActivity(I);
                        //ScanUtils qrScanner;
                        // qrScanner = new ScanUtils(getApplicationContext(), appActivity);
                        break;
                    case R.id.action_view_deck:
                        Intent J = new Intent(getApplicationContext(), ViewDeck.class);
                        startActivity(J);
                        break;
                    case R.id.action_open_dual:
                        Intent K = new Intent(getApplicationContext(),CardDual.class);
                        startActivity(K);
                        break;
                    case R.id.action_view_auctions:
                        //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent W = new Intent(getApplicationContext(),ViewAuctions.class);
                        startActivity(W);
                        break;
                    case R.id.action_view_all:
                        // Toast.makeText(MainActivity.this, "View All Cards", Toast.LENGTH_SHORT).show();
                        //break;
                        Intent M = new Intent(getApplicationContext(),ViewAllSheep.class);
                        startActivity(M);
                        break;
                }
                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUpdateFrame(FrameTime frameTime){
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : augmentedImages) {
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                if (augmentedImage.getName().equals("sheep") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("sheep.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("spinosaurobot") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("spinosaurobot.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("mosasaur") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("mosasaur.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("gymbunny") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("gymbunny.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("poodle") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("poodle.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("shark") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("shark.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("angler_fish") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("angler_fish_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("ballistic_bunny") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("ballistic_bunny_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("barn_owl") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("barn_owl_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("car_walker") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("car_walker_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("dog_plane") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("plane_dog_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("fox") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("fox_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("gerbil") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("gerbil_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("goldfish") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("gold_fish_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("gorilla") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("gorilla_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("koala") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("koala_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("lion") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("lion_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("lola") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("lola_monster_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("mecha_dragon") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("mecha_dragon_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("mecha_horse") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("mecha_horse_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("orange_man") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("orange_man_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("panda") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("panda_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("pink_poodle") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("pink_poodle_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("platypus") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("platypus_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("pug_festive") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("pugdolf_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("raccoon") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("raccoon_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("saitone") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("saitone_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("shiba_inu") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("shiba_inu_model.sfb"));
                    //shouldAddModel = false;
                }

                if (augmentedImage.getName().equals("stone_head") && shouldAddModel) {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("stone_head_model.sfb"));
                    //shouldAddModel = false;
                }



            }
        }
    }

    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        //TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        Node node = new Node();

        //node.getScaleController().setMaxScale(0.02f);
        Box boundingBox = (Box) renderable.getCollisionShape();
        if (boundingBox != null) {
            Vector3 boundingBoxSize = boundingBox.getSize();
            Log.wtf("Bounding Box Size0", boundingBoxSize.toString());
            float maxExtent = Math.max(boundingBoxSize.x, Math.max(boundingBoxSize.y, boundingBoxSize.z));
            float targetSize = 0.1f; // Whatever size you want.
            float scale = targetSize / maxExtent;
            node.setLocalScale(Vector3.one().scaled(scale));
        }
        node.setRenderable(renderable);

        // node.getScaleController().setMinScale(0.01f);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        //node.select();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void placeObject(ArFragment arFragment, Anchor anchor, Uri uri) {
        ModelRenderable.builder()
                .setSource(arFragment.getContext(), uri)
                .build()
                .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                .exceptionally(throwable -> {
                    Toast.makeText(arFragment.getContext(), "Error:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                });
    }


    public boolean setupAugmentedImagesDb(Config config, Session session) {
        AugmentedImageDatabase augmentedImageDatabase;
        Bitmap bitmap;
        //Attempting to add multiple files to our db
        augmentedImageDatabase = new AugmentedImageDatabase(session);
       bitmap = loadAugmentedImage("sheep");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("sheep", bitmap);

        bitmap = loadAugmentedImage("mosasaur");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("mosasaur", bitmap);

        bitmap = loadAugmentedImage("poodle");
        if (bitmap == null) {         return false;    }
        augmentedImageDatabase.addImage("poodle", bitmap);

        bitmap = loadAugmentedImage("shark");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("shark", bitmap);

        bitmap = loadAugmentedImage("spinosaurobot");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("spinosaurobot", bitmap);

        bitmap = loadAugmentedImage("gymbunny");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("gymbunny", bitmap);

        bitmap = loadAugmentedImage("angler_fish");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("angler_fish", bitmap);

        bitmap = loadAugmentedImage("ballistic_bunny");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("ballistic_bunny", bitmap);

        bitmap = loadAugmentedImage("barn_owl");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("barn_owl", bitmap);

        bitmap = loadAugmentedImage("fox");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("fox", bitmap);

        bitmap = loadAugmentedImage("car_walker");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("car_walker", bitmap);



        bitmap = loadAugmentedImage("dog_plane");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("dog_plane", bitmap);





        bitmap = loadAugmentedImage("gerbil");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("gerbil", bitmap);

        bitmap = loadAugmentedImage("goldfish");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("goldfish", bitmap);

        bitmap = loadAugmentedImage("gorilla");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("gorilla", bitmap);

        bitmap = loadAugmentedImage("koala");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("koala", bitmap);

        bitmap = loadAugmentedImage("lion");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("lion", bitmap);

        bitmap = loadAugmentedImage("lola");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("lola", bitmap);

        bitmap = loadAugmentedImage("mecha_dragon");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("mecha_dragon", bitmap);

        bitmap = loadAugmentedImage("mecha_horse");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("mecha_horse", bitmap);

        bitmap = loadAugmentedImage("orange_man");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("orange_man", bitmap);

        bitmap = loadAugmentedImage("panda");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("panda", bitmap);

        bitmap = loadAugmentedImage("pink_poodle");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("pink_poodle", bitmap);

        bitmap = loadAugmentedImage("platypus");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("platypus", bitmap);

        bitmap = loadAugmentedImage("pug_festive");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("pug_festive", bitmap);

        bitmap = loadAugmentedImage("raccoon");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("raccoon", bitmap);

        bitmap = loadAugmentedImage("saitone");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("saitone", bitmap);

        bitmap = loadAugmentedImage("shiba_inu");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("shiba_inu", bitmap);

        bitmap = loadAugmentedImage("stone_head");
        if (bitmap == null) {        return false;    }
        augmentedImageDatabase.addImage("stone_head", bitmap);


        config.setAugmentedImageDatabase(augmentedImageDatabase);
        return true;
    }

    private Bitmap loadAugmentedImage(String target) {
        if (target == "shark") {
            try (InputStream is = getAssets().open("shark_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
        } else if(target == "poodle"){
            try (InputStream is = getAssets().open("poodle_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
        }  else if(target == "sheep"){
            try (InputStream is = getAssets().open("sheep_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
        } else if(target == "spinosaurobot"){
            try (InputStream is = getAssets().open("spinsaurobot_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
        } else if(target == "mosasaur"){
            try (InputStream is = getAssets().open("mosasaur_cardface.png")) //TODO FIX THIS
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }
        } else if(target == "gymbunny"){
            try (InputStream is = getAssets().open("gymbunny_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "angler_fish"){
            try (InputStream is = getAssets().open("anglerfish_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "ballistic_bunny"){
            try (InputStream is = getAssets().open("ballistic_bunny_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "barn_owl"){
            try (InputStream is = getAssets().open("barn_owl_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "car_walker"){
            try (InputStream is = getAssets().open("car_walker_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "dog_plane"){
            try (InputStream is = getAssets().open("dog_plane_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "fox"){
            try (InputStream is = getAssets().open("fox_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "gerbil"){
            try (InputStream is = getAssets().open("gerbil_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "goldfish"){
            try (InputStream is = getAssets().open("goldfish_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "gorilla"){
            try (InputStream is = getAssets().open("gorilla_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "koala"){
            try (InputStream is = getAssets().open("koala_cardface.png"))
           {        return BitmapFactory.decodeStream(is);    }
           catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
           }

        } else if(target == "lion"){
            try (InputStream is = getAssets().open("lion_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "lola"){
            try (InputStream is = getAssets().open("lola_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "mecha_dragon"){
            try (InputStream is = getAssets().open("mecha_dragon_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "mecha_horse"){
            try (InputStream is = getAssets().open("mecha_horse_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "orange_man"){
            try (InputStream is = getAssets().open("orange_man_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "panda"){
            try (InputStream is = getAssets().open("panda_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "pink_poodle"){
            try (InputStream is = getAssets().open("pink_poodle_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "platypus"){
            try (InputStream is = getAssets().open("platypus_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "pug_festive"){
            try (InputStream is = getAssets().open("pug_festive_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "raccoon"){
            try (InputStream is = getAssets().open("raccoon_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "saitone"){
            try (InputStream is = getAssets().open("saitone_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "shiba_inu"){
            try (InputStream is = getAssets().open("shiba_inu_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        } else if(target == "stone_head"){
            try (InputStream is = getAssets().open("stone_head_cardface.png"))
            {        return BitmapFactory.decodeStream(is);    }
            catch (IOException e) {
                Log.e("ImageLoad", "IO Exception", e);
            }

        }

        return null;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer!=null && !mediaPlayer.isPlaying()){
            if(musicEnabled == false){
                mediaPlayer.pause();
            }
            else
                mediaPlayer.start();
        }
    }
}
