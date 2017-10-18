package com.mygdx.scenedemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


// Need this

/**
 * Created by mkemp on 9/29/17.
 */

public class SceneWithAction implements ApplicationListener {

    public class MyActor extends Actor {
        Texture texture = new Texture("jet.png");
        public boolean started = false;

        public MyActor() {
            setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    ((SceneWithAction.MyActor) event.getTarget()).started = true;
                    return true;
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                    this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
                    texture.getWidth(),texture.getHeight(),false,false);
        }

        @Override
        public void act(float delta) {
            if (started)
                super.act(delta);
        }
    }

    private Stage stage;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        /////////////////////////////////////

        // Concurrent actor
        MyActor myConcurrentActor = new MyActor();

        // Actions
        MoveToAction concurrentMoveToAction = new MoveToAction();
        RotateToAction concurrentRotateAction = new RotateToAction();
        ScaleToAction concurrentScaleAction = new ScaleToAction();

        // Define actions
        concurrentMoveToAction.setPosition(500f, 0f);
        concurrentMoveToAction.setDuration(3f);
        concurrentRotateAction.setRotation(145f);
        concurrentRotateAction.setDuration(3f);
        concurrentScaleAction.setScale(0.3f);
        concurrentScaleAction.setDuration(3f);

        // Add actions concurrently.
        myConcurrentActor.addAction(concurrentMoveToAction);
        myConcurrentActor.addAction(concurrentRotateAction);
        myConcurrentActor.addAction(concurrentScaleAction);

        // Add actor to the stage.
        stage.addActor(myConcurrentActor);

        /////////////////////////////////////

        // Sequential actor
        MyActor mySequentialActor = new MyActor();

        // Actions
        MoveToAction sequentialMoveToAction = new MoveToAction();
        RotateToAction sequentialRotateAction = new RotateToAction();
        ScaleToAction sequentialScaleAction = new ScaleToAction();

        // Define actions
        sequentialMoveToAction.setPosition(300f, 0f);
        sequentialMoveToAction.setDuration(2f);
        sequentialRotateAction.setRotation(90f);
        sequentialRotateAction.setDuration(3f);
        sequentialScaleAction.setScale(0.1f);
        sequentialScaleAction.setDuration(3f);

        // Add actions sequentially.
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(sequentialMoveToAction);
        sequenceAction.addAction(sequentialRotateAction);
        sequenceAction.addAction(sequentialScaleAction);
        mySequentialActor.addAction(sequenceAction);

        // Add actor to the stage.
        stage.addActor(mySequentialActor);

        /////////////////////////////////////

        // import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

        /////////////////////////////////////

        // Inline sequential actor (uses above import)
        MyActor myInlineSequentialActor = new MyActor();

        // Also actions sequentially inline.
        myInlineSequentialActor.addAction(
                sequence(
                        scaleTo(0.5f, 0.5f, 2f),
                        rotateTo(90.0f, 2f),
                        moveTo(300.0f, 0f, 2f)
                )
        );

        // Add actor to the stage.
        stage.addActor(myInlineSequentialActor);

        /////////////////////////////////////

        // Inline parallel actor
        MyActor myInlineParallelActor = new MyActor();

        // Also add actions in parallel
        myInlineParallelActor.addAction(
                parallel(
                        scaleTo(0.5f, 0.5f, 5f),
                        rotateTo(45.0f, 5f),
                        moveTo(600.0f, 0f, 5f)
                )
        );

        // Add actor to the stage.
        stage.addActor(myInlineParallelActor);

        /////////////////////////////////////

        // Pooling actor
        MyActor myPooledActor = new MyActor();

        // Create a pool
        Pool<MoveToAction> actionPool = new Pool<MoveToAction>() {
            @Override
            protected MoveToAction newObject() {
                return new MoveToAction();
            }
        };

        // New action
        MoveToAction poolMoveToAction = actionPool.obtain();
        poolMoveToAction.setDuration(1f);
        poolMoveToAction.setPosition(450f, 0);

        // Add action
        myPooledActor.addAction(poolMoveToAction);

        // Add actor to the stage.
        stage.addActor(myPooledActor);

        /////////////////////////////////////

        // Logged actor
        MyActor myLoggedActor = new MyActor();

        // New action
        MoveToAction poolMoveToActionLogged = actionPool.obtain();
        poolMoveToActionLogged.setDuration(1.5f);
        poolMoveToActionLogged.setPosition(425f, 100f);

        // Add action
        myLoggedActor.addAction(
                sequence(
                        poolMoveToActionLogged,
                        run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Gdx.app.log("STATUS", "Action complete");
                                    }
                                }
                        )
                )
        );

        stage.addActor(myLoggedActor);

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
