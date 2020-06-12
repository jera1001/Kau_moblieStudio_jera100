package com.libktx.game.Mains

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.net.HttpRequestBuilder
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.Json
import com.libktx.game.Mains.assets.GameSkin
import com.libktx.game.Mains.Logics.GameManger
import com.libktx.game.Mains.Logics.SoundManger
import com.libktx.game.Application
import com.libktx.game.Mains.Logics.entity.Character
import com.libktx.game.Mains.Logics.entity.Stats
import com.libktx.game.Mains.Logics.entity.json
import com.libktx.game.Mains.assets.get
import ktx.actors.*
import ktx.app.KtxScreen
import ktx.ashley.get
import ktx.graphics.begin
import ktx.scene2d.*
import ktx.style.get
import ktx.graphics.use
import ktx.log.*
import ktx.scene2d.vis.visCheckBox
import ktx.style.button
import kotlin.reflect.typeOf
import ktx.json.*

private val log = logger<Game>()

class Game(
        val stage: Stage,
        val batch: Batch
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
    private var foodImage = assets.findRegion("NomalFood")
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

    private val menuWindow = scene2d.window("","clickList")


    /*     Food Side      */
    // Food Image
    private val badCookie = assets.findRegion("BadCookie")
    private val goodCookie = assets.findRegion("GoodCookie")
    private val normalFood = assets.findRegion("NomalFood")
    private val steak = assets.findRegion("Steak")
    private val susi = assets.findRegion("Susi")
    private val trashFood  = assets.findRegion("Trash")

    private val badCookieBtn: KButton
    private val goodCookieBtn: KButton
    private val normalFoodBtn: KButton
    private val steakBtn: KButton
    private val susiBtn: KButton
    private val trashFoodBtn: KButton

    private val foodTable = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        touchable = enabled

        menuWindow

        table {
            trashFoodBtn = button("trashFood") {
                label("Bad Food").cell(padTop = 150f)
                onClick {
                    println("Trash Food Drop!")
                    Character.hungry += 10
                    Character.happy -= 5
                    Character.poop += 20
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}")
                }
            }.cell(padTop = -100f, padLeft = -100f, width = 100f, height = 100f)

            normalFoodBtn = button("normalFood") {
                label("Normal Food").cell(padTop = 150f)
                onClick {
                    println("Normal Food Drop!")
                    Character.hungry += 15
                    Character.happy += 5
                    Character.poop += 15
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}")
                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)

            steakBtn = button("Steak") {
                label("Steak").cell(padTop = 150f)
                onClick {
                    println("Steak Drop!")
                    Character.hungry += 25
                    Character.happy += 15
                    Character.poop += 30
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}")
                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)
        }.cell(row = true, padTop = 100f)
        table{
            susiBtn = button("Susi") {
                label("Susi").cell(padTop = 150f)
                onClick{
                    println("Susi Drop!")
                    Character.hungry += 20
                    Character.happy += 15
                    Character.poop += 15
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}")
                }
            }.cell(padLeft = -100f, width = 100f, height = 100f)

            badCookieBtn = button("badCookie") {
                label("Cookie").cell(padTop = 150f)
                onClick{
                    println("Bad Cookie Drop!")
                    Character.hungry += 5
                    Character.happy += 20
                    Character.poop += 5
                    Character.moral -= 10
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    moralLabel.txt = "Moral : ${Character.moral}  "
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}, Moral: ${Character.moral}")
                }
            }.cell(padLeft = 50f, width = 100f, height = 100f)

            goodCookieBtn = button("goodCookie") {
                label("Good Cookie").cell(padTop = 150f)
                onClick{
                    println("Good Cookie Drop!")
                    Character.hungry += 5
                    Character.happy += 25
                    Character.poop += 5
                    Character.moral -= 15
                    hungryLabel.txt = "Hungry : ${Character.hungry}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    moralLabel.txt = "Moral : ${Character.moral}  "
                    println("Hungry: ${Character.hungry}, Happy: ${Character.happy}, Toilet: ${Character.poop}, Moral: ${Character.moral}")
                }
            }.cell(padLeft = 50f, width = 100f, height = 100f)
        }.cell(padTop = 100f, row = true)

        button {
            label(text = "Close")
            onClick {
                println("Close the Window")
                stage.clear()
            }
        }.cell(row = true, padTop = 100f, width = 200f, height = 40f)

    }

    private fun btnClickEvent(batch: Batch, delta:Float, btn:KButton, image: TextureAtlas.AtlasRegion){
        if(btn.isChecked) {
            foodImage = image
            batch.draw(foodImage, food.x, food.y, food.width, food.height)
            food.y -= 30f*delta
            food.y = MathUtils.clamp(food.y, 25f, 100f)
            stage.clear()
        }
    }
    private fun moveToCreature(delta:Float, btn:KButton) {
        if(btn.isChecked){
            if(character.x > food.x) {
                character.x -= 200 * delta
            }
            else if(character.x < food.x) {
                character.x += 200 * delta
            }
            if(character.x - food.x > -5 && character.x - food.x < 5) {
                character.x = food.x
                timeToRandomMove -= delta

                if(timeToRandomMove < 0) {
                    btn.isChecked = false
                    stage.clear()
                    xMove = randomMove(delta)
                    food.x = MathUtils.random(20f, 800f - 100f)
                    food.y = 100f
                }
            }
        }
    }

    /*     Care Side      */
    // Care image
    private val bathImage = assets.findRegion("Bath")
    private val ballImage = assets.findRegion("Ball")
    private val toiletImage = assets.findRegion("Toilet")

    private val bathBtn : KButton
    private val ballBtn : KButton
    private val toiletBtn : KButton

    private val careTable = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        touchable = enabled

        menuWindow

        table {
            bathBtn = button("Bath") {
                label("Bath").cell(padTop = 150f)
                onClick {
                    println("Bath Drop!")
                    if(Character.clean < 90)
                        Character.clean = 90
                    else Character.clean = 100
                    Character.happy += 20
                    cleanLabel.txt = "Clean : ${Character.clean}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                }
            }.cell(padLeft = -100f, width = 100f, height = 100f)
            toiletBtn = button("Toilet") {
                label("Toilet").cell(padTop = 150f)
                onClick {
                    println("Toilet Drop!")
                    Character.poop = 0
                    Character.happy += 10
                    poopLabel.txt = "Toilet : ${Character.poop}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                }
            }.cell(padLeft = 100f, width = 100f, height = 100f)
            ballBtn = button("Ball") {
                label("Ball").cell(padTop = 150f)
                onClick {
                    println("Ball Drop!")
                    Character.health += 10
                    Character.happy += 5
                    healthLabel.txt = "Health : ${Character.health}"
                    happyLabel.txt = "Happy : ${Character.happy}  "
                }
            }.cell(padLeft = 100f, width = 100f, height = 100f)
        }.cell(row = true)

        button {
            label(text = "Close")
            onClick {
                println("Close the Window")
                stage.clear()
            }
        }.cell(row = true, padTop = 100f, width = 200f, height = 40f)
    }

    /*    Status Side    */
    lateinit var cleanLabel: Label
    lateinit var hungryLabel: Label
    lateinit var poopLabel: Label
    lateinit var moralLabel: Label
    lateinit var smartLabel: Label
    lateinit var happyLabel: Label
    lateinit var healthLabel: Label
    lateinit var priceLabel: Label

    private val statusTable = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        touchable = enabled
        menuWindow
        button {
            label(text = "Close")
            onClick {
                println("Close the Window")
                stage.clear()
            }
        }.cell(padTop = -400f, width = 200f, height = 40f)


        table {
            label("Name : ${Character.name}").cell(expand = true, padLeft = 100f)
            cleanLabel = label("Clean : ${Character.clean}").cell(expand = true, padTop = 50f, padLeft = -100f)
            hungryLabel = label("Hungry : ${Character.hungry}").cell(expand = true, padTop = 100f, padLeft = -100f)
            poopLabel = label("Toilet : ${Character.poop}").cell(expand = true, padTop = 150f, padLeft = -100f)
            table {
                moralLabel = label("Moral : ${Character.moral}  ").cell(expand = true)
                smartLabel = label("Smart : ${Character.smart}").cell(expand = true)
            }.cell(padTop = 200f, padLeft = -100f)
            table {
                happyLabel = label("Happy : ${Character.happy}  ").cell(expand = true)
                healthLabel = label("Health : ${Character.health}").cell(expand = true)
            }.cell(padTop = 250f, padLeft = -150f)
            priceLabel = label("Price : ${Character.price} Won").cell(expand = true, padTop = 300f, padLeft = -200f)
        }.cell(padTop = -100f, padLeft = -400f, row = true)
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
                onClick {
                    println("Food Clicked")
                    stage.addActor(menuWindow)
                    stage.addActor(foodTable)
                }
            }.cell(width = 120f, height = 80f)
            careButton = button {
                label(text = "Care")
                onClick {
                    println("Care Clicked")
                    stage.addActor(menuWindow)
                    stage.addActor(careTable)
                }
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
                onClick {
                    println("Status Clicked")
                    stage.addActor(menuWindow)
                    stage.addActor(statusTable)
                }
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

            btnClickEvent(batch, delta, trashFoodBtn, trashFood)
            btnClickEvent(batch, delta, normalFoodBtn, normalFood)
            btnClickEvent(batch, delta, steakBtn, steak)
            btnClickEvent(batch, delta, susiBtn, susi)
            btnClickEvent(batch, delta, badCookieBtn, badCookie)
            btnClickEvent(batch, delta, goodCookieBtn, goodCookie)

            btnClickEvent(batch, delta, bathBtn, bathImage)
            btnClickEvent(batch, delta, toiletBtn, toiletImage)
            btnClickEvent(batch, delta, ballBtn, ballImage)
        }

        stage.draw()

        moveToCreature(delta, trashFoodBtn)
        moveToCreature(delta, normalFoodBtn)
        moveToCreature(delta, steakBtn)
        moveToCreature(delta, susiBtn)
        moveToCreature(delta, badCookieBtn)
        moveToCreature(delta, goodCookieBtn)

        moveToCreature(delta, bathBtn)
        moveToCreature(delta, toiletBtn)
        moveToCreature(delta, ballBtn)

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
//
//    }
}

