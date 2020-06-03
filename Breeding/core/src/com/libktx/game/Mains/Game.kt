package com.libktx.game.Mains

import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.libktx.game.Mains.assets.GameSkin
import com.libktx.game.Mains.Logics.GameManger
import com.libktx.game.Mains.Logics.SoundManger
import com.libktx.game.Application
import com.libktx.game.Mains.assets.get
import ktx.actors.isShown
import ktx.actors.onClick
import ktx.actors.onClickEvent
import ktx.actors.onTouchDown
import ktx.app.KtxScreen
import ktx.graphics.begin
import ktx.scene2d.*
import ktx.style.get
import ktx.graphics.use
import ktx.log.*
import ktx.scene2d.vis.visCheckBox
import ktx.style.button

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

    val foodButton: KButton
    val careButton: KButton
    val teachButton: KButton
    val storeButton: KButton
    val statusButton: KButton

    private val assets = TextureAtlas("images/Skin.atlas")
    private val backgroundImage = assets.findRegion("fishbowl&back")
    private val characterImage = assets.findRegion("character")
    private val character = Rectangle( 800f/2f - 64f/2f, 20f, 120f, 100f) // character Position
    private val foodImage = assets.findRegion("NomalFood")
    private val careImage = assets.findRegion("Bath")
    private val food = Rectangle(MathUtils.random(20f, 800f - 100f), 100f, 80f, 48f)
    private val care = Rectangle(MathUtils.random(20f, 800f - 100f), 100f, 80f, 48f)

    // Random moving side
    var xMove: Float = (800f/2f - 64f/2f)
    val moveOffset = 64f
    var timeToRandomMove = 5f
    private fun randomMovePosition() = MathUtils.random(xMove-moveOffset, xMove+moveOffset)
    private fun getTimeToMove(): Float {
        val bound = 5f
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

    // Button group side
    private val btnGroup = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        x = 230f
        y = 150f
        touchable = enabled

        table {
            foodButton = button {
                label(text = "Food")
                onClick { println("Food Clicked") }
            }.cell(width = 120f, height = 80f)
            careButton = button {
                label(text = "Care")
                onClick { println("Care Clicked") }
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 150f)
            teachButton = button {
                label(text = "Teach")
                onClick { println("Teach Clicked") }
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 300f)
            storeButton = button {
                label(text = "Store")
                onClick { println("Store Clicked") }
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 450f)
            statusButton = button {
                label(text = "Status")
                onClick { println("Status Clicked") }
            }.cell(width = 120f, height = 80f, padLeft = -120f, padTop = 600f)
        }.cell(padLeft = 50f)
    }

    override fun render(delta: Float) {
        camera.update()

        batch.projectionMatrix = camera.combined

        stage.addActor(btnGroup)

        batch.use { batch ->
            batch.draw(backgroundImage, 0f, 0f, 800f, 480f)
            batch.draw(characterImage, character.x, character.y, character.width, character.height)

            if(foodButton.isChecked) {
                batch.draw(foodImage, food.x, food.y, food.width, food.height)
                food.y -= 30f*delta
                food.y = MathUtils.clamp(food.y, 25f, 100f)
            }

            if(careButton.isChecked) {
                batch.draw(careImage, care.x, care.y, care.width, care.height)
                care.y -= 30f*delta
                care.y = MathUtils.clamp(care.y, 25f, 100f)
            }
        }

        stage.draw()

        if(foodButton.isChecked){
//            timeToRandomMove = 5f
            if(character.x > food.x) {
                character.x -= 200 * delta
                println("Minus : character.x = ${character.x}, food.x = ${food.x}")
            }
            else if(character.x < food.x) {
                character.x += 200 * delta
                println("Plus : character.x = ${character.x}, food.x = ${food.x}")
            }
            if(character.x - food.x > -5 && character.x - food.x < 5) {
                character.x = food.x
                timeToRandomMove -= delta

                println("Clamped : character.x = ${character.x}, food.x = ${food.x}, timeToRandomMove = ${timeToRandomMove}")
                if(timeToRandomMove < 0) {
                    foodButton.isChecked = false
                    xMove = randomMove(delta)
                    food.x = MathUtils.random(20f, 800f - 100f)
                    food.y = 100f
                }
            }
        }

        if(careButton.isChecked){
//            timeToRandomMove = 5f
            if(character.x > care.x) {
                character.x -= 200 * delta
                println("Minus : character.x = ${character.x}, food.x = ${care.x}")
            }
            else if(character.x < care.x) {
                character.x += 200 * delta
                println("Plus : character.x = ${character.x}, food.x = ${care.x}")
            }
            if(character.x - care.x > -5 && character.x - care.x < 5) {
                character.x = care.x
                timeToRandomMove -= delta

                println("Clamped : character.x = ${character.x}, food.x = ${care.x}, timeToRandomMove = ${timeToRandomMove}")
                if(timeToRandomMove < 0) {
                    careButton.isChecked = false
                    xMove = randomMove(delta)
                    care.x = MathUtils.random(20f, 800f - 100f)
                    care.y = 100f
                }
            }
        }

        if(xMove.toInt() > character.x.toInt() && xMove.toInt() != character.x.toInt() && character.x.toInt() <= 800f - 64f){
            character.x += 100*delta
//            println("Plus : xMove = ${xMove}, character.x = ${character.x}")
        }
        else if(xMove.toInt() < character.x.toInt() && xMove.toInt() != character.x.toInt() && character.x.toInt() >= 0f){
            character.x -= 100*delta
//            println("Minus : xMove = ${xMove}, character.x = ${character.x}")
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

