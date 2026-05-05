package com.example.eventregistration

data class Variation(val label: String, val soundRes: Int?)

data class Rhythm(val name: String, val variations: List<Variation>) {
    val hasSound: Boolean get() = variations.isNotEmpty()
}
