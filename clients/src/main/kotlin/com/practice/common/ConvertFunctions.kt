package com.practice.common

import net.corda.core.contracts.UniqueIdentifier
import org.springframework.stereotype.Component

@Component
open class ConvertFunctions {
    fun stringToUniqueIdentifier(id: String): UniqueIdentifier
    {
        return UniqueIdentifier.fromString(id)
    }
}