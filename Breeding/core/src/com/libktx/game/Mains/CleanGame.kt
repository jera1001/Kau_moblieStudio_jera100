package com.libktx.game.Mains

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.libktx.game.Application
import ktx.actors.onClick
import ktx.actors.txt
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.scene2d.*

class CleanGame(
    val stage: Stage,
    val batch: Batch,
    val application: Application
):KtxScreen{
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val backgroundImage = Texture("back.png")
    private val bathImage = Game.assets.findRegion("Bath")
    private val bathRect: Rectangle = Rectangle(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 80f, 80f)

    private var clkX: Float = 0f
    private var clkY: Float = 0f
    private var point: Int = 0
    private var pointLabel: Label
    private val exitBtn: KButton

    private val pointTable = scene2d.table {
        setFillParent(true)
        exitBtn = button {
            label(text = "Close")
            onClick {
                println("Close the Window")
                application.removeScreen<CleanGame>()
                application.setScreen<Game>()
                application.addScreen(Application.context.inject<CleanGame>())
                point = 0
                stage.clear()
            }
        }.cell(padBottom = 450f, padRight = 50f)
        pointLabel = label("Point = $point").cell(padBottom = 450f)
    }


    override fun render(delta: Float) {
        stage.clear()
        camera.update()
        batch.projectionMatrix = camera.combined


        stage.addActor(pointTable)
        batch.use {
            batch.draw(backgroundImage, 0f, 0f, 800f, 480f)
            batch.draw(Game.characterImage, Game.character.x, Game.character.y, Game.character.width, Game.character.height)

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !exitBtn.isPressed) {
                clkX = Gdx.input.x.toFloat()
                clkY = 480f-Gdx.input.y.toFloat()
                bathRect.x = clkX
                bathRect.y = clkY
                batch.draw(bathImage, bathRect.x, bathRect.y, bathRect.width, bathRect.height)
            }
        }
        stage.draw()

        if(Game.character.overlaps(bathRect)){
            Game.character.x = MathUtils.random(50f, 750f)
            Game.character.y = MathUtils.random(20f, 430f)
            point++
            pointLabel.txt = "Point : $point"
        }
        if(exitBtn.isPressed || point == 15){
            println("Close the Window")
            application.removeScreen<CleanGame>()
            application.setScreen<Game>()
            application.addScreen(Application.context.inject<CleanGame>())
            point = 0
            Game.character.y = 20f
            stage.clear()
        }
    }
}