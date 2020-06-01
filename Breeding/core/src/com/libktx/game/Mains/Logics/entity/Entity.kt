package com.libktx.game.Mains.Logics.entity

import com.libktx.game.Mains.Logics.EntityType

interface Entity : Comparable<Entity>{
    val entityType: EntityType
}