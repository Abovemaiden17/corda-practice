package com.practice.schemas

import com.practice.states.UserState
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.serialization.CordaSerializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@CordaSerializable
object UserSchema : MappedSchema(
        schemaFamily = UserState::class.java,
        version = 1,
        mappedTypes = listOf(UserState::class.java)) {
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

