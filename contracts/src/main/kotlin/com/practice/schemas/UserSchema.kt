package com.practice.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.serialization.CordaSerializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

object UserSchema

@CordaSerializable
object UserSchemaV1 : MappedSchema(
        schemaFamily = UserSchema::class.java,
        version = 1,
        mappedTypes = listOf(PersistentUser::class.java)) {
    @Entity
    @Table(name = "user" , indexes = [])

    class PersistentUser(
            @Column(name = "name", nullable = false)
            var name: String,

            @Column(name = "linear_id", nullable = false)
            var linearId: UUID

    ): PersistentState()
    {
        // Default constructor required by hibernate.
        constructor(): this("", UUID.randomUUID())
    }
}

