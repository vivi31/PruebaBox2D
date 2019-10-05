package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

class PantallaJuego extends Pantalla {
    private static final float RADIO = 15f;
    private World mundo; // Mundo paralelo donde se aplica la física.
    private Body body;
    private Box2DDebugRenderer debugRenderer;

    //MAPA
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //PERSONAJE
    private Personaje mario;


    public PantallaJuego(Juego juego) {

    }

    @Override
    public void show() {
        crearMundo();
        crearObjetos();
        //MAPA
       cargarMapa();
       definirParedes();
       crearPersonaje();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void definirParedes() {
        ConvertidorMapa.crearCuerpos(mapa,mundo);
    }


    private void crearObjetos() {
        //Body Def
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(20,700); //METROS
        body = mundo.createBody(bodyDef);  //Objeto simulado.

        CircleShape circulo = new CircleShape();
        circulo.setRadius(RADIO);

        //Definir propiedades Físicas
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circulo;

        //De 0 a 1
        fixtureDef.density= 0.4f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.6f;

        body.createFixture(fixtureDef);

        circulo.dispose();

        //PLATAFORMA
        BodyDef bodyPisodef = new BodyDef();
        bodyPisodef.type = BodyDef.BodyType.StaticBody;
        bodyPisodef.position.set(ANCHO/4,10);

        Body bodyPiso = mundo.createBody(bodyPisodef);
        PolygonShape pisoShape = new PolygonShape();
        pisoShape.setAsBox(ANCHO/4, 10);  //LA MITAD DEL TOTAL

        bodyPiso.createFixture(pisoShape,0);

        pisoShape.dispose();



    }

    private void crearMundo() {
        Box2D.init();  //Se crea el mundo virtual.
        Vector2 gravedad = new Vector2(0,-10);
        mundo = new World(gravedad, true);
        debugRenderer = new Box2DDebugRenderer();
    }

    private void cargarMapa(){
        AssetManager manager= new AssetManager(); //cargar recursos;
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver())); // Qué objeto va a interpretar el archivo.
        manager.load("mapa/MapaMario.tmx", TiledMap.class);
        manager.finishLoading(); //Segundo plano2
        mapa = manager.get("mapa/MapaMario.tmx"); // toda la información del mapa.
       rendererMapa = new OrthogonalTiledMapRenderer(mapa);
    }

    private void crearPersonaje(){
        Texture textura = new Texture("marioSprite.png");
        mario = new Personaje(textura, 0 ,0);
    }

    @Override
    public void render(float delta) {
        //Actualizar
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        mario.getSprite().setPosition(x-5f,y-10f);
        borrarPantalla(0,0,0);

        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);

        rendererMapa.render();


        debugRenderer.render(mundo, camara.combined);
        //dibujar personaje
        batch.begin();
        mario.render(batch);
        batch.end();
        //final
        mundo.step(1/60f,6,2);

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

    private class ProcesadorEntrada implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            float x = body.getPosition().x;
            float y = body.getPosition().y;
            body.applyLinearImpulse(500,2000,x,y,true);
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
