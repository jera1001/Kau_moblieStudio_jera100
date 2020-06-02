package com.libktx.game.Mains

import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.libktx.game.Mains.assets.GameSkin
import com.libktx.game.Mains.Logics.GameManger
import com.libktx.game.Mains.Logics.SoundManger
import com.libktx.game.Application
import com.libktx.game.Mains.assets.get
import ktx.app.KtxScreen
import ktx.scene2d.*
import ktx.style.get
import ktx.graphics.use
import ktx.log.*

private val log = logger<Game>()

class Game(
        val stage: Stage,
        val batch: Batch,
        val application: Application
) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 1000f, 480f) }
    private val soundManger = SoundManger()
//    private val gameManager = createGameManager()
    private val touchPos = Vector3()

    private val assets = TextureAtlas("images/Skin.atlas")
    private val backgroundImage = assets.findRegion("fishbowl&back")
    private val characterImage = assets.findRegion("character")
    private val character = Rectangle( 800f/2f - 64f/2f, 20f, 120f, 100f) // character Position
    var xMove: Float = (800f/2f - 64f/2f)
    val moveOffset = 64f
    var timeToRandomMove = 1.5f
    private fun randomMovePosition() = MathUtils.random(xMove-moveOffset, xMove+moveOffset)
    private fun getTimeToMove(): Float {
        val bound = 1.5f
        return MathUtils.random(bound - 0.5f, bound + 0.5f)
    }
    private fun randomMove(delta: Float): Float {
        timeToRandomMove -= delta

        if(timeToRandomMove < 0) {
//            val (x, y) = when (MathUtils.random(1, 2)) {
//                1 -> moveOffset to randomMovePosition()
//                else -> -moveOffset to randomMovePosition()
//            }
            val x = randomMovePosition()
            println("x = ${x}")

            xMove = x
            println("timeToRnadomMove = ${timeToRandomMove}, xMove = ${xMove}")
            xMove = MathUtils.clamp(xMove, 20f, 800f - 80f)

            timeToRandomMove = getTimeToMove()
            println("timeToRandomMove = ${timeToRandomMove}")
            return xMove
        }
        return character.x
    }

    private val btnGroup = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        x = 230f
        y = 150f
//        background = TextureRegionDrawable(TextureRegion(backgroundImage, 0, 0, 1920, 1080))
        touchable = enabled
        table {
            button {
                label(text = "Food")
            }.cell(width = 120f, height = 80f)
            button {
                label(text = "Care")
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 150f)
            button {
                label(text = "Teach")
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 300f)
            button {
                label(text = "Store")
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 450f)
            button {
                label(text = "Status")
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 600f)
        }.cell(padLeft = 50f)
    }
//    private fun createGameManager() = GameManger(batch, Scene2DSkin.defaultSkin["fishbowl&back"], soundManger) {
//
//    }

    override fun render(delta: Float) {
        camera.update()

        batch.projectionMatrix = camera.combined
        stage.addActor(btnGroup)

        batch.use {
            it.draw(backgroundImage, 0f, 0f, 800f, 480f)
            it.draw(characterImage, character.x, character.y, character.width, character.height)
        }
        stage.draw()
//        xMove = randomMove(delta)
//        println("xMove = ${xMove}")
        if(xMove.toInt() > character.x.toInt() && xMove.toInt() != character.x.toInt() && character.x.toInt() <= 800f - 64f){
            character.x += 100*delta
//            log.debug{ "xMove = $xMove, character.x = ${character.x}" }
            println("Plus : xMove = ${xMove}, character.x = ${character.x}")
        }
        else if(xMove.toInt() < character.x.toInt() && xMove.toInt() != character.x.toInt() && character.x.toInt() >= 0f){
            character.x -= 100*delta
//            log.debug{ "xMove = ${xMove}, character.x = ${character.x}" }
            println("Minus : xMove = ${xMove}, character.x = ${character.x}")
        }
        else{
            xMove = randomMove(delta)
        }
        character.x = MathUtils.clamp(character.x, 20f, 800f - 80f)
    }

//    override fun show() {
//        stage.addActor(btnGroup)
//        stage.draw()
//    }
}

