package com.libktx.game.Mains.Logics.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.json.*
import com.badlogic.gdx.Preferences
import ktx.preferences.flush
import ktx.preferences.*

data class Player(
        var turn: Int = 0,
        var ownMoney: Int = 5000
)
//fun readPlayerInPreferences()
//fun savePlayerInPreferences(preferences: Preferences, player:Player){
//    preferences.flush {
//        this["Player"] = player
//    }
//}

data class CharacterA
    (
        var name: String = "Kuruk",
        var clean: Int = MathUtils.random(50, 100), // 청결
        var hungry: Int = MathUtils.random(10, 60),  // 배고픔
        var poop: Int = MathUtils.random(0, 50),    // 화장실
        var moral: Int = MathUtils.random(40, 60),   // 도덕성
        var smart: Int = MathUtils.random(10, 70),   // 지능
        var happy: Int = MathUtils.random(30, 80),   // 행복
        var health: Int = MathUtils.random(60, 100),  // 건강
        var price: Int = ((moral + smart + happy + health) / 4) * 100// 가격
    )
// read Player Data
fun readPlayerInPreferences(preferences: Preferences, player: Player): Player? {
    return preferences["Player"]
}

// read Character Data
fun readCharacterInPreferences(preferences: Preferences, character: CharacterA): CharacterA? {
    return preferences["Character"]
}

// save the Game Data
fun saveDataInPreferences(preferences: Preferences, player: Player, character: CharacterA){
    preferences.flush {
        this["Player"] = player
        this["Character"] = character
    }
}

class Character{
    companion object {
        var name: String = "Kuruk"
        var clean: Int = MathUtils.random(50, 100) // 청결
        var hungry: Int = MathUtils.random(10, 60)  // 배고픔
        var poop: Int = MathUtils.random(0, 50)    // 화장실
        var moral: Int = MathUtils.random(40, 60)   // 도덕성
        var smart: Int = MathUtils.random(10, 70)   // 지능
        var happy: Int = MathUtils.random(30, 80)   // 행복
        var health: Int = MathUtils.random(60, 100)  // 건강
        var price: Int = ((moral + smart + happy + health) / 4) * 100// 가격
    }
}
//fun readData(preferences: Preferences,
//             turn: Int, ownMoney: Int,
//             name:String, clean:Int, hungry: Int, poop: Int, moral: Int,
//             smart: Int, happy: Int, health: Int, price: Int){
//
//}
//fun saveData( preferences: Preferences,
//                   turn: Int, ownMoney: Int,
//                   name:String, clean:Int, hungry: Int, poop: Int, moral: Int,
//                   smart: Int, happy: Int, health: Int, price: Int ){
//    preferences["CharName"] = name
//}
