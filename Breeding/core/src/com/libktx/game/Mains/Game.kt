package com.libktx.game.Mains

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.libktx.game.Mains.EAssets.GameSkin
import com.libktx.game.Mains.Logics.GameManger
import com.libktx.game.Mains.Logics.SoundManger
import com.libktx.game.Application
import com.libktx.game.Mains.EAssets.get
import jdk.nashorn.internal.runtime.ScriptRuntime.apply
import ktx.actors.*
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.assetDescriptor
import ktx.async.*
import ktx.collections.gdxArrayOf
import ktx.scene2d.*
import ktx.style.get
import ktx.assets.load
import ktx.assets.getAsset
import ktx.graphics.use

class Game(
        val stage: Stage,
        val batch: Batch,
        val application: Application
) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
    private val soundManger = SoundManger()
    private val gameManager = createGameManager()
    private val touchPos = Vector3()
    private val backImage = Texture("fishbowl&back.png")

    private val btnGroup = scene2d.table {
        x = 0f // max = 600f
        y = 0f // max = 455f
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
        }.cell(padLeft = 0f)
    }
    private fun createGameManager() = GameManger(batch, Scene2DSkin.defaultSkin["fishbowl&back"], soundManger) {

    }

    override fun render(delta: Float) {
        camera.update()

        batch.projectionMatrix = camera.combined

        batch.use {
            it.draw(backImage, 0f, 0f, 800f, 480f)
        }
    }

    override fun show() {
        stage.addActor(btnGroup)
    }
}

