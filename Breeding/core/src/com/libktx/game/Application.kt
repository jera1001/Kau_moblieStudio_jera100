package com.libktx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.libktx.game.Mains.Game
import com.libktx.game.Mains.Menu
import com.libktx.game.Mains.Objects.Buttons
import com.libktx.game.Mains.Objects.Drawables
import com.libktx.game.Mains.assets.GameSkin
import com.libktx.game.Mains.assets.load
import ktx.app.KtxGame
import ktx.assets.toInternalFile
import ktx.inject.Context
import ktx.inject.register
import ktx.scene2d.Scene2DSkin
import ktx.style.*


class Application : KtxGame<Screen>() {
    val context = Context()
    val assets = AssetManager()

    override fun create() {
        GameSkin.values().forEach { assets.load(it) }
        context.register {
            bindSingleton(TextureAtlas("images/Skin.atlas"))
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton<Viewport>(ScreenViewport())
            bindSingleton(Stage(inject(), inject()))
            bindSingleton(createSkin(inject()))
            Scene2DSkin.defaultSkin = inject()
            bindSingleton(this@Application)
            bindSingleton(Menu(inject(), inject()))
            bindSingleton(Game(inject(), inject(), inject()))
        }

//        playMusic()
        addScreen(context.inject<Menu>())
        addScreen(context.inject<Game>())
        setScreen<Menu>()
        super.create()
    }

    private fun playMusic() {
        Gdx.audio.newMusic("music/rain.mp3".toInternalFile()).apply {
            volume = 0.3f
            setOnCompletionListener { play() }
        }.play()
    }

    fun createSkin(atlas: TextureAtlas): Skin = skin(atlas) { skin ->
        add(defaultStyle, TextureAtlas(Gdx.files.internal("android/assets/images/Skin.atlas")))
        add(defaultStyle, BitmapFont())
        add("decorative", FreeTypeFontGenerator("Maplestory.ttf".toInternalFile())
                .generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                    borderWidth = 2f
                    borderColor = Color.GRAY
                    size = 50
                }))
        label {
            font = skin[defaultStyle]
        }
        label("decorative") {
            font = skin["decorative"]
        }
        textButton("decorative"){
            font = skin["decorative"]
            overFontColor = Color.GRAY
            downFontColor = Color.DARK_GRAY
        }
        button {
            up = skin.getDrawable("buttonUp")
            down = skin.getDrawable("buttonDown")
            over = skin.getDrawable("buttonDown")
        }
        textButton {
            font = skin[defaultStyle]
            up = skin.getDrawable("buttonUp")
            down = skin.getDrawable("buttonDown")
        }

        /*    Food Button     */
        button("badCookie") {
            up = skin.getDrawable("BadCookie")
            down = skin.getDrawable("BadCookie")

        }
        button("goodCookie"){
            up = skin.getDrawable("GoodCookie")
            down = skin.getDrawable("GoodCookie")
        }
        button("normalFood"){
            up = skin.getDrawable("NomalFood")
            down = skin.getDrawable("NomalFood")
        }
        button("Steak"){
            up = skin.getDrawable("Steak")
            down = skin.getDrawable("Steak")
        }
        button("Susi"){
            up = skin.getDrawable("Susi")
            down = skin.getDrawable("Susi")
        }
        button("trashFood"){
            up = skin.getDrawable("Trash")
            down = skin.getDrawable("Trash")
        }

        /*    Care Button    */
        button("Bath"){
            up = skin.getDrawable("Bath")
            down = skin.getDrawable("Bath")
        }
        button("Ball"){
            up = skin.getDrawable("Ball")
            down = skin.getDrawable("Ball")
        }
        button("Toilet"){
            up = skin.getDrawable("Toilet")
            down = skin.getDrawable("Toilet")
        }


        window {
            titleFont = skin[defaultStyle]
            stageBackground = skin["fishbowl&back"]
        }
        window("clickList"){
            titleFont = skin[defaultStyle]
            stageBackground = skin.getDrawable("Menu")
        }
    }

    override fun dispose() {
        context.dispose()
        assets.dispose()
        super.dispose()
    }

}
