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
        }
        button(Buttons.toggle(), extend = defaultStyle){
            Drawables.buttonChecked
        }
        window {
            titleFont = skin[defaultStyle]
            stageBackground = skin["fishbowl&back"]
        }
    }

    override fun dispose() {
        context.dispose()
    }

}
