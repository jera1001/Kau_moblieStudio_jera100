package com.libktx.game.Mains

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.libktx.game.Application
import com.libktx.game.Mains.EAssets.GameSkin
import com.libktx.game.Mains.EAssets.load
import ktx.actors.onClickEvent
import ktx.app.KtxScreen
import ktx.scene2d.*

class Menu(
        val stage: Stage,
        val application: Application) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val backgroundImage = Texture("back.png")
    private val view = scene2d.table {
        setFillParent(true)

        background = TextureRegionDrawable(TextureRegion(backgroundImage, 0, 0, 1920, 1080))
        touchable = Touchable.enabled
        onClickEvent { _, _ -> application.setScreen<Game>() }

        table {
            label(text = "Click to ", style = "decorative")
            label(text = "Start", style = "decorative") {
                color = Color.RED
            }
        }.cell(padBottom = 50f)
    }

    override fun show(){
        if(application.assets.isFinished){
            stage.addActor(view)
            Gdx.input.inputProcessor = stage
        }else{
            stage.addActor(view)
        }
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun hide(){
        view.remove()
    }
}