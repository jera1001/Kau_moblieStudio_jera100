package com.libktx.game.Mains.Logics.entity

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.Preferences
import ktx.preferences.flush
import ktx.preferences.*

data class Player(
    var turn: Int = 0,
    var ownMoney: Int = 5000
)

data class Character(
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

data class FoodNum(
    var trashFoodNum: Int = 100,
    var normalFoodNum: Int = 5,
    var badCookieNum: Int = 3,
    var goodCookieNum: Int = 3,
    var steakNum: Int = 3,
    var susiNum: Int = 3
)

// read Player Data
fun readPlayerInPreferences(preferences: Preferences, player: Player): Player? {
    return preferences["Player"]
}

// read Character Data
fun readCharacterInPreferences(preferences: Preferences, character: Character): Character? {
    return preferences["Character"]
}

// read FoodNum Data
fun readFoodNumInPreferences(preferences: Preferences, foodNum: FoodNum): FoodNum? {
    return preferences["FoodNum"]
}

// save the Game Data
fun saveDataInPreferences(preferences: Preferences, player: Player, character: Character, foodNum: FoodNum){
    preferences.flush {
        this["Player"] = player
        this["Character"] = character
        this["FoodNum"] = foodNum
    }
}
