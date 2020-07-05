package com.libktx.game.Mains

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.libktx.game.Application
import com.sun.org.apache.xpath.internal.operations.Bool
import ktx.actors.onChangeEvent
import ktx.actors.onClick
import ktx.actors.onClickEvent
import ktx.actors.txt
import ktx.app.KtxScreen
import ktx.collections.iterate
import ktx.graphics.use
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class BallGame(
        val stage: Stage,
        val batch: Batch,
        val application: Application
) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val backgroundImage = Texture("back.png")
    private val ballImage = Game.assets.findRegion("Ball")
    private val balls = Array<Rectangle>()
//    private var ball = Rectangle()
    private val touchPos = Vector3()

    private var isBall:Boolean = false
    private var userBall:Boolean = true
    private var charBall:Boolean = false

    private var ballX : Float = 0f
    private var ballY : Float = 0f
    private var ballTouched = 0
    private lateinit var pointLabel: Label
    private var deltaX = MathUtils.random(-3f, 3f) * 100
    private var deltaY = MathUtils.random(200f, 400f)
    private var charMove = 0f

    private val pointTable = scene2d.table {
        setFillParent(true)

        pointLabel = label("Point = $ballTouched").cell(padBottom = 450f)
    }

    override fun render(delta: Float) {
        stage.clear()
        camera.update()
        batch.projectionMatrix = camera.combined

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !isBall) {
            ballX = Gdx.input.x.toFloat()
            ballY = 480f - Gdx.input.y.toFloat()
            isBall = true
            balls.add(Rectangle(ballX, ballY, 50f, 50f))
//            ball = Rectangle(ballX, ballY, 50f, 50f)
            charMove = ballX + deltaX*delta*(ballY/(deltaY*delta))

            println("ballX = ${ballX}, ballY = ${ballY}, deltaX = ${deltaX*delta}, deltaY = ${deltaY*delta}")
            println("ballY / delta = ${ballY / (deltaY*delta)}, charMove = ${ballX + deltaX*delta*(ballY/(deltaY*delta))}")
        }

        stage.addActor(pointTable)
        batch.use { batch ->
            batch.draw(backgroundImage, 0f, 0f, 800f, 480f)
            batch.draw(Game.characterImage, Game.character.x, Game.character.y, Game.character.width, Game.character.height)

            if(isBall){
//                println("X = ${ballX}, Y = ${ballY}")
//                println("touch X = ${Gdx.input.x}, Y = ${Gdx.input.y}")
                balls.forEach { ball -> batch.draw(ballImage, ball.x, ball.y, 50f, 50f) }
            }
            else{

            }
        }
        stage.draw()

        Game.character.x = MathUtils.clamp(Game.character.x, 0f, 800f - Game.character.width)
        balls.iterate { ball, iterator ->
            ball.x = MathUtils.clamp(ball.x, 0f, 800f-50f)
            ball.y = MathUtils.clamp(ball.y, Game.character.y, 480f-50f)

            if(ball.x == 0f || ball.x == 800f-50f){
                deltaX *= -1
//            charMove = ballX + deltaX*delta*(ballY/(deltaY*delta))
            }



            if(userBall) {
                ball.x += delta * deltaX
                ball.y -= delta * deltaY
//            if(charMove > 30f)
                Game.character.x -= (Game.character.x - charMove) / 50
 //                println("character.x = ${ball.x}, charMove = $charMove, moveDelta = ${(Game.character.x - charMove) / 30}")
//            else Game.character.x -= (Game.character.x - charMove) / 10
            }
            if(charBall) {
                ball.x += delta * deltaX
                ball.y += delta * deltaY
            }

            if(ball.overlaps(Game.character) && userBall){
                charBall = true
                userBall = false
                ballTouched ++
                pointLabel.txt = "Point = $ballTouched"
                deltaX = MathUtils.random(-2f, 2f) * 100
                deltaY = MathUtils.random(200f, 400f)
            }
            else if(ball.y > 480f-50f || ball.y < 18f){
                application.removeScreen<BallGame>()
                application.setScreen<Game>()
                application.addScreen(Application.context.inject<BallGame>())
                isBall = false
                ballTouched = 0
                pointLabel.txt = "Point = 0"

                stage.clear()
                iterator.remove()
            }
//            println("bally = ${ball.y}")


            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !userBall) {
                if((ball.x - Gdx.input.x >= -3f || ball.x - Gdx.input.x <= 3f) &&
                        (ball.y - Gdx.input.y.toFloat() >= -3f || ball.y - Gdx.input.y.toFloat() <= 3f)){
//                    println("X = ${Gdx.input.x}, BallX = ${ball.x}, - = ${ball.x - Gdx.input.x}")
//                    println("Y = ${Gdx.input.y}, BallY = ${ball.y}, - = ${ball.y - Gdx.input.y}")
                    charBall = false
                    userBall = true
                    deltaX = MathUtils.random(-3f, 3f) * 100
                    deltaY = MathUtils.random(200f, 400f)
                    charMove = ballX + deltaX*delta*(ballY/(deltaY*delta))
                    println("charMove = $charMove")
                }
            }
        }
//        ball.x = MathUtils.clamp(ball.x, 0f, 800f-50f)
//        ball.y = MathUtils.clamp(ball.y, Game.character.y, 480f-50f)
//
//        if(ball.x == 0f || ball.x == 800f-50f){
//            deltaX *= -1
////            charMove = ballX + deltaX*delta*(ballY/(deltaY*delta))
//        }
//
//        if(ball.y == 480f-50f || ball.y < 15f){
//            application.removeScreen<BallGame>()
//            application.setScreen<Game>()
//            application.addScreen(Application.context.inject<BallGame>())
//            isBall = false
//            ballTouched = 0
//            pointLabel.txt = "Point = 0"
//
//            stage.clear()
////            application.screen
//        }
//
//        if(userBall) {
//            ball.x += delta * deltaX
//            ball.y -= delta * deltaY
////            if(charMove > 30f)
//                Game.character.x -= (Game.character.x - charMove) / 50
////            else Game.character.x -= (Game.character.x - charMove) / 10
//        }
//        if(charBall) {
//            ball.x += delta * deltaX
//            ball.y += delta * deltaY
//        }
//
//        if(ball.overlaps(Game.character) && userBall){
//            charBall = true
//            userBall = false
//            ballTouched ++
//            pointLabel.txt = "Point = $ballTouched"
//            deltaX = MathUtils.random(-2f, 2f) * 100
//            deltaY = MathUtils.random(200f, 400f)
//        }
//
//        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !userBall) {
//            if((ball.x - Gdx.input.x.toFloat() >= -3f || ball.x - Gdx.input.x.toFloat() <= 3f) &&
//                    (ball.y - Gdx.input.y.toFloat() >= -3f || ball.y - Gdx.input.y.toFloat() <= 3f)){
//                charBall = false
//                userBall = true
//                deltaX = MathUtils.random(-3f, 3f) * 100
//                deltaY = MathUtils.random(200f, 400f)
//                charMove = ballX + deltaX*delta*(ballY/(deltaY*delta))
//                println("charMove = $charMove")
//            }
//        }

//            batch.draw(ballImage, ball.x, ball.y, ball.width, ball.height)
    }
}