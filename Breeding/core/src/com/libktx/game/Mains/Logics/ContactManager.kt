package com.libktx.game.Mains.Logics

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.libktx.game.Mains.Logics.EntityType.*
import com.libktx.game.Mains.Logics.entity.*
import ktx.async.schedule
import ktx.math.component1
import ktx.math.component2

class ContactManager(
        val gameManger: GameManger,
        val soundManger: SoundManger) : ContactListener {

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        if (fixtureA.userData != null && fixtureB.userData != null) {
            val entityB = fixtureB.body.userData as Entity
            val entityA = fixtureA.body.userData as Entity
            beginContact(entityA, entityB)
            beginContact(entityB, entityA)
        }
    }

    fun beginContact(entityA: Entity, entityB: Entity){
        val typeA = entityA.entityType
        val typeB = entityB.entityType

        when{
            typeA == CHARACTER && typeB == FOOD -> haveFood(entityA as Character, entityB as Food)
            typeA == CHARACTER && typeB == CARE -> haveCare(entityA as Character, entityB as Care)
            typeA == CHARACTER && typeB == TEACH -> haveTeach(entityA as Character, entityB as Teach)
            typeA == FOOD && typeB == STORE -> buyFood(entityA as Food, entityB as Store)
        }
    }

    fun haveFood(character: Character, food: Food){

    }

    fun haveCare(character: Character, care: Care){

    }

    fun haveTeach(character: Character, teach: Teach){

    }

    fun buyFood(food: Food, store: Store){

    }

    override fun endContact(contact: Contact?) {
        TODO("Not yet implemented")
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        TODO("Not yet implemented")
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        TODO("Not yet implemented")
    }

}