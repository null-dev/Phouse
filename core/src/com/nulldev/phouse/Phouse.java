package com.nulldev.phouse;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nulldev.lib.LibNullWIFISocketClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Phouse extends ApplicationAdapter {
    private long previousTime;

    public static String currentStage = "mainStage";

    private int menuScale = 200;

    private String WIFIServerIP = "0.0.0.0";

    private Stage mainStage;
    public static Stage WIFIMouseStage;
    private BitmapFont font;
    private BitmapFont headerFont;
    private BitmapFont buttonFont;
    private Skin skin;
    private Table mainMenuTable;

    private Label mainMenuHeader;
    private Label mainMenuInstruct;

    private TextButton WIFIButton;
    private TextButton USBButton;
    private TextButton BluetoothButton;
    private TextButton enterIPButton;

	SpriteBatch batch;
	Texture img;

    //Strings:
    String logTag = "Phouse:";

    @Override
	public void create () {
        //=======================[UNIVERSAL COMPONENTS]=======================
        previousTime = System.currentTimeMillis();
        mainStage = new Stage();
        WIFIMouseStage = new Stage();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        headerFont = new BitmapFont(Gdx.files.internal("ui/fonts/PhouseHeader.fnt"));
        buttonFont = new BitmapFont(Gdx.files.internal("ui/fonts/Buttons.fnt"));
        buttonFont.setScale(2);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        //=======================[MAIN MENU COMPONENTS]=======================
        mainMenuHeader = new Label("Phouse", skin);
        mainMenuHeader.setStyle(new Label.LabelStyle(headerFont, Color.WHITE));
        mainMenuHeader.setFontScale(3);
        mainMenuInstruct = new Label("Choose a communication method below:", skin);
        mainMenuInstruct.setFontScale(2);

        WIFIButton = new TextButton("WIFI", skin);
        WIFIButton.setStyle(new TextButton.TextButtonStyle(WIFIButton.getStyle().up, WIFIButton.getStyle().down, WIFIButton.getStyle().checked, buttonFont));
        WIFIButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                currentStage = "WIFIEnterIP";
                IPInputListener IPListener = new IPInputListener();
                Gdx.input.getTextInput(IPListener, "IP:", "", "Enter your computer's IP");
            }
        });
        USBButton = new TextButton("USB", skin);
        USBButton.setStyle(new TextButton.TextButtonStyle(USBButton.getStyle().up,USBButton.getStyle().down,USBButton.getStyle().checked,buttonFont));
        BluetoothButton = new TextButton("Bluetooth", skin);
        BluetoothButton.setStyle(new TextButton.TextButtonStyle(BluetoothButton.getStyle().up,BluetoothButton.getStyle().down,BluetoothButton.getStyle().checked,buttonFont));
        enterIPButton = new TextButton("Enter IP Manually", skin);

        mainMenuTable = new Table();
        mainMenuTable.setFillParent(true);
        mainMenuTable.add(mainMenuHeader).row();
        mainMenuTable.add(mainMenuInstruct).row();
        mainMenuTable.add(WIFIButton).size(menuScale).row();
        mainMenuTable.add(USBButton).size(menuScale).row();
        mainMenuTable.add(BluetoothButton).size(menuScale).row();
        mainStage.addActor(mainMenuTable);
        Gdx.input.setInputProcessor(mainStage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(currentStage == "mainStage") {
            mainStage.act();
            mainStage.draw();
        } else if(currentStage == "WIFIMouse") {
            //Get accelerometer data
            //NO DELAY REDUCE LAG SO YEAH
            //if((System.currentTimeMillis() - previousTime) > 9) {
            //    previousTime = System.currentTimeMillis();
                float acX = Gdx.input.getAccelerometerX();
                float acY = Gdx.input.getAccelerometerY();
                float acZ = Gdx.input.getAccelerometerZ();
                String message = Float.toString(acX) + ";" + Float.toString(acY) + ";" + Float.toString(acZ);

                //Send the packet
                LibNullWIFISocketClient.sendCommand(message);
            //}
        }
	}

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
        LibNullWIFISocketClient.stop();
        font.dispose();
        headerFont.dispose();
        buttonFont.dispose();
        mainStage.dispose();
    }
}

class IPInputListener implements Input.TextInputListener {
    @Override
    public void input (String text) {
        //Validate IP
        if(!validateIP(text)) {
            Phouse.currentStage = "WIFIEnterIP";
            IPInputListener IPListener = new IPInputListener();
            Gdx.input.getTextInput(IPListener, "(Invalid IP) - IP:", "", "Enter your computer's IP");
        } else {
            //IP Valid, ready to start server
            LibNullWIFISocketClient.serverPort = 29992;
            //Start server yeah :)
            LibNullWIFISocketClient.connect(text);

            Phouse.currentStage = "WIFIMouse";
            Gdx.input.setInputProcessor(Phouse.WIFIMouseStage);
        }
    }

    @Override
    public void canceled () {
        Phouse.currentStage = "mainStage";
    }

    /*
    Validates IPs with a pattern.
    Based on: http://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
     */
    public static boolean validateIP(final String ip){
        Pattern pattern = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}