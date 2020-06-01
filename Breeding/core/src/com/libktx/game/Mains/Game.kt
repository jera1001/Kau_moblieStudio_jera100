package com.libktx.game.Mains

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClickEvent
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class Game(
        val stage: Stage,
        val batch: Batch
) : KtxScreen {
//   val soundManger = SoundManager()
//    var gameManager= createGameManger()
//    val inputProcessor = object : KtxInputAdapter {
//        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
////            gameManager.
//        }
//    }
//    val inputListner

//    private fun createGameManger() = GameManger(batch, Scene2DSkin.defaultSkin["background"], SoundManger) {
//        updateHeartPanel(it)
//    }

    private fun createGameManger() = GameManger(batch, ) {

    }

    override fun show() {
//        stage.addActor(view)
//        stage.addActor(inputListner)
//        inputListner.setKeyboardFocus()
    }
}