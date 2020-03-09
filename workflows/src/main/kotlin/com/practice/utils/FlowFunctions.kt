package com.practice.utils

import net.corda.core.contracts.UniqueIdentifier
import java.util.*

class FlowFunctions {
    fun stringToLinearID(id: String): UniqueIdentifier
    {
        return UniqueIdentifier.fromString(id)
    }

    fun stringToUUID(id: String): UUID
    {
        return UUID.fromString(id)
    }
}