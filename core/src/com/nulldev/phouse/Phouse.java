package com.nulldev.phouse;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Phouse extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    //Strings:
    String logTag = "Phouse:";

    //Default to android...
	Application.ApplicationType platform = Application.ApplicationType.Android;

    @Override
	public void create () {
        //Set app type
        platform = Gdx.app.getType();

        if(platform == Application.ApplicationType.Desktop) {
            Gdx.app.log(logTag, "Platform: Desktop");
        } else if(platform == Application.ApplicationType.Android) {
            Gdx.app.log(logTag, "Platform: Android");
        }

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
