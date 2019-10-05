package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Personaje {
    private Sprite sprite;
    private Animation animacionCaminar;
    private float timerCaminar = 0;

    public Personaje (Texture textura, float x, float y){
        //SPRITESHEET
        TextureRegion texturaCmpleta = new TextureRegion(textura);
        TextureRegion[][] texturas = texturaCmpleta.split(32,64);

        animacionCaminar = new Animation(0.2f, texturas[0][3], texturas[0][2],texturas[0][1]);
        animacionCaminar.setPlayMode(Animation.PlayMode.LOOP);

        sprite = new Sprite(texturas[0][0]); //idle
        sprite.setPosition(x,y);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);

        //Animar
        timerCaminar += Gdx.graphics.getDeltaTime();
        TextureRegion textura = (TextureRegion) animacionCaminar.getKeyFrame(timerCaminar);
        batch.draw(textura, sprite.getX() + sprite.getWidth(),sprite.getY());
    }

    public Sprite getSprite() {
        return sprite;
    }




}

