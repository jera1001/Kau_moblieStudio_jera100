package com.libktx.game.Mains.EAssets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset
import ktx.assets.load

enum class GameSkin(val path: String) {
    Game("images/Skin.atlas")
}

inline fun AssetManager.load(asset: GameSkin) = load<TextureAtlas>(asset.path)
inline operator fun AssetManager.get(asset: GameSkin) = getAsset<TextureAtlas>(asset.path)