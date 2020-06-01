package com.libktx.game.Mains.Logics

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.libktx.game.Mains.EAssets.GameSkin
import ktx.box2d.createWorld
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin


class GameManger(
        val batch: Batch,
        val background: TextureRegion,
        val soundManger: SoundManger,
        skin: Skin = Scene2DSkin.defaultSkin,
        statChangeCallback: (Int) -> Unit) {
    val camera = OrthographicCamera(800f, 480f)
    val moveOffset = 800f
    private val backgroundImage = Texture("fishbowl&back.png")

    fun update(){

    }

    private fun randomMovePosition() = MathUtils.random(0f, moveOffset)

    fun render(){
        batch.use{
            it.draw(backgroundImage, 0f, 0f, 800f, 480f)
            it.color = Color.WHITE
            it.projectionMatrix = camera.combined
        }
    }

    fun updateCamera(delta: Float){

    }

}