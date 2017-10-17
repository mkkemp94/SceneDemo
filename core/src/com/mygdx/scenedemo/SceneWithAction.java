package com.mygdx.scenedemo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
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
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                    this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
                    texture.getWidth(),texture.getHeight(),false,false);
        }
    }

    private Stage stage;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        MyActor myActor = new MyActor();

        // Actions
        MoveToAction moveAction = new MoveToAction();
        RotateToAction rotateAction = new RotateToAction();
        ScaleToAction scaleAction = new ScaleToAction();

        // Define actions
        moveAction.setPosition(300f, 0f);
        moveAction.setDuration(5f);
        rotateAction.setRotation(90f);
        rotateAction.setDuration(5f);
        scaleAction.setScale(0.5f);
        scaleAction.setDuration(5f);

        // Add actions concurrently.
//        myActor.addAction(moveAction);
//        myActor.addAction(rotateAction);
//        myActor.addAction(scaleAction);

        // Add actions sequentially.
//        SequenceAction sequenceAction = new SequenceAction();
//        sequenceAction.addAction(moveAction);
//        sequenceAction.addAction(rotateAction);
//        sequenceAction.addAction(scaleAction);
//        myActor.addAction(sequenceAction);

        // import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

        // Also add actions sequentially.
//        myActor.addAction(
//                sequence(
//                        scaleTo(0.5f, 0.5f, 5f),
//                        rotateTo(90.0f, 5f),
//                        moveTo(300.0f, 0f, 5f)
//                )
//        );

        // Also add actions in parallel
//        myActor.addAction(
//                parallel(
//                        scaleTo(0.5f, 0.5f, 5f),
//                        rotateTo(90.0f, 5f),
//                        moveTo(300.0f, 0f, 5f)
//                )
//        );

        // Pooling actions
        final MyActor myActor1 = new MyActor();
        Pool<MoveToAction> actionPool = new Pool<MoveToAction>() {
            @Override
            protected MoveToAction newObject() {
                return new MoveToAction();
            }
        };

        MoveToAction moveToAction3 = actionPool.obtain();
        moveToAction3.setDuration(5f);
        moveToAction3.setPosition(300f, 0);

//        myActor1.addAction(moveToAction3);

        // Logging actions
        myActor1.addAction(sequence(moveToAction3,
                run(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.app.log("STATUS", "Action complete");
                    }
                })));

        stage.addActor(myActor1);

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
