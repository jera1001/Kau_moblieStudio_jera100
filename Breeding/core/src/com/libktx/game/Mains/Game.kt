package com.libktx.game.Mains

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.libktx.game.Mains.Logics.SoundManger
import com.libktx.game.Application
import com.libktx.game.Mains.Logics.entity.*
import ktx.actors.*
import ktx.app.KtxScreen
import ktx.scene2d.*
import ktx.graphics.use
import ktx.log.*
import kotlin.AssertionError

private val log = logger<Game>()
val PLAYER_PREF = "Player"
val CHARACTER_PREF = "Character"
val FOODNUM_PREF = "foodNum"

class Game(
        val stage: Stage,
        val batch: Batch,
        val application: Application
) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 1000f, 480f) }
    private val soundManger = SoundManger()
//    private val gameManager = createGameManager()
    private val touchPos = Vector3()

    private var preferences: Preferences = Application.preferences

    // Data Load Part //
    private var _newPlayer: Player? = readPlayerInPreferences(preferences, Player())
    private var newPlayer: Player = Player()
        get(){
            if(_newPlayer == null) newPlayer = Player()
            else newPlayer = _newPlayer as Player
            return _newPlayer ?: throw AssertionError("Set Player Not Null")
        }
    private var _newCharacter : Character? = readCharacterInPreferences(preferences, Character())
    private var newCharacter: Character = Character()
        get() {
            if(_newCharacter == null) newCharacter = Character()
            else newCharacter = _newCharacter as Character
            return _newCharacter ?: throw AssertionError("Set Character Not Null")
        }
    private var _foodNum : FoodNum? = readFoodNumInPreferences(preferences, FoodNum())
    private var foodNum : FoodNum = FoodNum()
        get() {
            if(_foodNum == null) foodNum = FoodNum()
            else foodNum = _foodNum as FoodNum
            return _foodNum ?: throw AssertionError("Set FoodNum Not Null")
        }


    val foodButton: KButton
    val careButton: KButton
    val teachButton: KButton
    val storeButton: KButton
    val statusButton: KButton


    private val assets = TextureAtlas("images/Skin.atlas")
    private val backgroundImage = assets.findRegion("fishbowl&back")

    private var foodImage = assets.findRegion("NomalFood")
    private val careImage = assets.findRegion("Bath")
    private val food = Rectangle(MathUtils.random(20f, 800f - 100f), 100f, 80f, 48f)
    private val care = Rectangle(MathUtils.random(20f, 800f - 100f), 100f, 80f, 48f)

    companion object {
        val assets = TextureAtlas("images/Skin.atlas")

        val characterImage = assets.findRegion("character")
        val character = Rectangle( 800f/2f - 64f/2f, 20f, 120f, 100f) // character Position
    }

    private var turn = 0
    private var turnDelta = 0
    private var ownMoney = 50000
    lateinit var turnLabel : Label
    lateinit var ownMoneyLabel : Label

    private val playerTable = scene2d.table {
        table {
            turnLabel = label("Turn : ${newPlayer!!.turn} /")
            ownMoneyLabel = label(" Own Money : ${newPlayer?.ownMoney} Won")
        }.cell(padLeft = 500f, padBottom = 900f)
    }
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

    lateinit var badCookieLabel: Label
    lateinit var goodCookieLabel: Label
    lateinit var normalFoodLabel: Label
    lateinit var steakLabel: Label
    lateinit var susiLabel: Label
    lateinit var trashFoodLabel: Label

    private val foodTable = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        touchable = enabled

        menuWindow

        table {
            trashFoodBtn = button("trashFood") {
                trashFoodLabel = label("Bad Food : (${foodNum!!.trashFoodNum})").cell(padTop = 150f)
                onClick {
                    println("Trash Food Drop!")
                    foodNum!!.trashFoodNum --
                    newCharacter!!.hungry += 10
                    newCharacter!!.happy -= 5
                    newCharacter!!.poop += 20
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    trashFoodLabel.txt = "Bad Food : (${foodNum!!.trashFoodNum})"
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}")
                }
            }.cell(padTop = -100f, padLeft = -100f, width = 100f, height = 100f)

            normalFoodBtn = button("normalFood") {
                normalFoodLabel = label("Normal Food : (${foodNum!!.normalFoodNum})").cell(padTop = 150f)
                onClick {
                    println("Normal Food Drop!")
                    foodNum!!.normalFoodNum--
                    newCharacter!!.hungry += 15
                    newCharacter!!.happy += 5
                    newCharacter!!.poop += 15
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    normalFoodLabel.txt = "Normal Food : (${foodNum!!.normalFoodNum})"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}")
                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)

            steakBtn = button("Steak") {
                steakLabel = label("Steak : (${foodNum!!.steakNum})").cell(padTop = 150f)
                onClick {
                    println("Steak Drop!")
                    foodNum!!.steakNum--
                    newCharacter!!.hungry += 25
                    newCharacter!!.happy += 15
                    newCharacter!!.poop += 30
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    steakLabel.txt = "Steak : (${foodNum!!.steakNum})"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}")
                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)
        }.cell(row = true, padTop = 100f)
        table{
            susiBtn = button("Susi") {
                susiLabel = label("Susi : (${foodNum!!.susiNum})").cell(padTop = 150f)
                onClick{
                    println("Susi Drop!")
                    foodNum!!.susiNum--
                    newCharacter!!.hungry += 20
                    newCharacter!!.happy += 15
                    newCharacter!!.poop += 15
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    susiLabel.txt = "Susi : (${foodNum!!.susiNum})"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}")
                }
            }.cell(padLeft = -100f, width = 100f, height = 100f)

            badCookieBtn = button("badCookie") {
                badCookieLabel = label("Cookie : (${foodNum!!.badCookieNum})").cell(padTop = 150f)
                onClick{
                    println("Bad Cookie Drop!")
                    foodNum!!.badCookieNum--
                    newCharacter!!.hungry += 5
                    newCharacter!!.happy += 20
                    newCharacter!!.poop += 5
                    newCharacter!!.moral -= 10
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    moralLabel.txt = "Moral : ${newCharacter!!.moral}  "
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    badCookieLabel.txt = "Cookie : (${foodNum!!.badCookieNum})"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}, Moral: ${newCharacter!!.moral}")
                }
            }.cell(padLeft = 50f, width = 100f, height = 100f)

            goodCookieBtn = button("goodCookie") {
                goodCookieLabel = label("Good Cookie : (${foodNum!!.goodCookieNum})").cell(padTop = 150f)
                onClick{
                    println("Good Cookie Drop!")
                    foodNum!!.goodCookieNum--
                    newCharacter!!.hungry += 5
                    newCharacter!!.happy += 25
                    newCharacter!!.poop += 5
                    newCharacter!!.moral -= 15
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    hungryLabel.txt = "Hungry : ${newCharacter!!.hungry}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    moralLabel.txt = "Moral : ${newCharacter!!.moral}  "
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    goodCookieLabel.txt = "Good Cookie : (${foodNum!!.goodCookieNum})"
                    turnDelta++
                    println("Hungry: ${newCharacter!!.hungry}, Happy: ${newCharacter!!.happy}, Toilet: ${newCharacter!!.poop}, Moral: ${newCharacter!!.moral}")
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
                    turnDelta++
                    if(newCharacter!!.clean < 90)
                        newCharacter!!.clean = 90
                    else newCharacter!!.clean = 100
                    newCharacter!!.happy += 20
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    cleanLabel.txt = "Clean : ${newCharacter!!.clean}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                }
            }.cell(padLeft = -100f, width = 100f, height = 100f)
            toiletBtn = button("Toilet") {
                label("Toilet").cell(padTop = 150f)
                onClick {
                    println("Toilet Drop!")
                    turnDelta++
                    newCharacter!!.poop = 0
                    newCharacter!!.happy += 10
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    poopLabel.txt = "Toilet : ${newCharacter!!.poop}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                }
            }.cell(padLeft = 100f, width = 100f, height = 100f)
            ballBtn = button("Ball") {
                label("Ball").cell(padTop = 150f)

                onClick {
                    println("Ball Drop!")
                    turnDelta++
                    newCharacter!!.health += 10
                    newCharacter!!.happy += 5
                    newCharacter!!.price = ((newCharacter!!.moral + newCharacter!!.smart + newCharacter!!.happy + newCharacter!!.health) / 4) * 100
                    healthLabel.txt = "Health : ${newCharacter!!.health}"
                    happyLabel.txt = "Happy : ${newCharacter!!.happy}  "
                    priceLabel.txt = "Price : ${newCharacter!!.price} Won"
                    application.setScreen<BallGame>()
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
            label("Name : ${newCharacter!!.name}").cell(expand = true, padLeft = 100f)
            cleanLabel = label("Clean : ${newCharacter!!.clean}").cell(expand = true, padTop = 50f, padLeft = -100f)
            hungryLabel = label("Hungry : ${newCharacter!!.hungry}").cell(expand = true, padTop = 100f, padLeft = -100f)
            poopLabel = label("Toilet : ${newCharacter!!.poop}").cell(expand = true, padTop = 150f, padLeft = -100f)
            table {
                moralLabel = label("Moral : ${newCharacter!!.moral}  ").cell(expand = true)
                smartLabel = label("Smart : ${newCharacter!!.smart}").cell(expand = true)
            }.cell(padTop = 200f, padLeft = -100f)
            table {
                happyLabel = label("Happy : ${newCharacter!!.happy}  ").cell(expand = true)
                healthLabel = label("Health : ${newCharacter!!.health}").cell(expand = true)
            }.cell(padTop = 250f, padLeft = -150f)
            priceLabel = label("Price : ${newCharacter!!.price} Won").cell(expand = true, padTop = 300f, padLeft = -200f)
        }.cell(padTop = -100f, padLeft = -400f, row = true)
    }

    private val strBadCookieBtn: KButton
    private val strGoodCookieBtn: KButton
    private val strNormalFBtn: KButton
    private val strSteakBtn: KButton
    private val strSusiBtn: KButton
    private val strTrashFBtn: KButton

    private val storeTable = scene2d.table {
        setFillParent(true)
        camera.update()
        batch.projectionMatrix = camera.combined

        touchable = enabled

        menuWindow

        table {
            strTrashFBtn = button("trashFood") {
                label("Bad Food : 0won").cell(padTop = 150f)
                onClick {
                    println("Bad Food Bought")
                    foodNum!!.trashFoodNum ++
                    trashFoodLabel.txt = "Bad Food : (${foodNum!!.trashFoodNum})"
                }
            }.cell(padTop = -100f, padLeft = -100f, width = 100f, height = 100f)

            strNormalFBtn = button("normalFood") {
                label("Normal Food : 70won").cell(padTop = 150f)
                onClick {
                    if(newPlayer!!.ownMoney >= 70) {
                        println("Normal Food Bought")
                        foodNum!!.normalFoodNum++
                        newPlayer!!.ownMoney -= 70
                        ownMoneyLabel.txt = " Own Money : ${newPlayer!!.ownMoney} Won"
                        normalFoodLabel.txt = "Normal Food : (${foodNum!!.normalFoodNum})"
                    }

                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)

            strSteakBtn = button("Steak") {
                label("Steak : 150won").cell(padTop = 150f)
                onClick {
                    if(newPlayer!!.ownMoney >= 150) {
                        println("Steak Bought")
                        foodNum!!.steakNum++
                        newPlayer!!.ownMoney -= 150
                        ownMoneyLabel.txt = " Own Money : ${newPlayer!!.ownMoney} Won"
                        steakLabel.txt = "Steak : (${foodNum!!.steakNum})"
                    }
                }
            }.cell(padTop = -100f, padLeft = 50f, width = 100f, height = 100f)
        }.cell(row = true, padTop = 100f)
        table{
            strSusiBtn = button("Susi") {
                label("Susi : 175won").cell(padTop = 150f)
                onClick{
                    if(newPlayer!!.ownMoney >= 175) {
                        println("Susi Bought")
                        foodNum!!.susiNum++
                        newPlayer!!.ownMoney -= 175
                        ownMoneyLabel.txt = " Own Money : ${newPlayer!!.ownMoney} Won"
                        susiLabel.txt = "Susi : (${foodNum!!.susiNum})"
                    }
                }
            }.cell(padLeft = -100f, width = 100f, height = 100f)

            strBadCookieBtn = button("badCookie") {
                label("Cookie : 75won").cell(padTop = 150f)
                onClick{
                    if(newPlayer!!.ownMoney >= 75){
                        println("Bad Cookie Bought")
                        foodNum!!.badCookieNum ++
                        newPlayer!!.ownMoney -= 75
                        ownMoneyLabel.txt = " Own Money : ${newPlayer!!.ownMoney} Won"
                        badCookieLabel.txt = "Cookie : (${foodNum!!.badCookieNum})"
                    }
                }
            }.cell(padLeft = 50f, width = 100f, height = 100f)

            strGoodCookieBtn = button("goodCookie") {
                label("Good Cookie : 125won").cell(padTop = 150f)
                onClick{
                    if(newPlayer!!.ownMoney >= 125) {
                        println("Good Cookie Bought")
                        foodNum!!.goodCookieNum++
                        newPlayer!!.ownMoney -= 125
                        ownMoneyLabel.txt = " Own Money : ${newPlayer!!.ownMoney} Won"
                        goodCookieLabel.txt = "Good Cookie : (${foodNum!!.goodCookieNum})"
                    }
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
                onClick {
                    println("Store Clicked")
                    stage.addActor(menuWindow)
                    stage.addActor(storeTable)
                    turnDelta ++
                }
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
//        newPlayer= readPlayerInPreferences(preferences, Player())
//        newCharacter = readCharacterInPreferences(preferences, Character())
//        foodNum = readFoodNumInPreferences(preferences, FoodNum())

        camera.update()
        batch.projectionMatrix = camera.combined

        stage.addActor(playerTable)
        stage.addActor(btnGroup)
        if(turnDelta == 3) {
            newPlayer!!.turn ++
            turnDelta = 0
            turnLabel.txt = "Turn : ${newPlayer!!.turn} / "
        }

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
//            btnClickEvent(batch, delta, ballBtn, ballImage)
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
//        moveToCreature(delta, ballBtn)

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

        saveDataInPreferences(preferences, newPlayer!!, newCharacter!!, foodNum!!)
    }


//    override fun show() {
//
//    }
}

