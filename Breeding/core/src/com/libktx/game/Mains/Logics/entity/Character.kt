package com.libktx.game.Mains.Logics.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.json.*
val json = Json().fromJson<Character>("Status.json")

class Character{
    companion object {
        var name:String = "Kuruk"
        var clean:Int = 70 // 청결
        var hungry:Int = 80  // 배고픔
        var poop:Int = 30    // 화장실
        var moral:Int = 50   // 도덕성
        var smart:Int = 50   // 지능
        var happy:Int = 50   // 행복
        var health:Int = 50  // 건강
        var price:Int = 1000 // 가격
    }
//    override fun show(){
//        super.show()
//    }
}

class Stats: Component, Pool.Poolable {
    var name = "Kuruk"
    var clean = 100
    var hungry = 80
    var poop = 30
    var moral = 50
    var smart = 50
    var happy = 50
    var health = 50
    var price = 1000

    companion object{
        val mapper = mapperFor<Stats>()
    }

    override fun reset() {
        name = "Kuruk"
        clean = 100
        hungry = 80
        poop = 30
        moral = 50
        smart = 50
        happy = 50
        health = 50
        price = 1000
    }
}

//val Entity.Stat: Stats
//    get() = this[Stats.mapper]
//            ?: throw KotlinNullPointerException("Trying to access a stats component which is null")