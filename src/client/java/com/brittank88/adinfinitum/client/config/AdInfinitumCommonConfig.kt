package com.brittank88.adinfinitum.client.config

import gg.essential.vigilance.Vigilant
import java.io.File

object AdInfinitumCommonConfig : Vigilant(File("./config/ad-infinitum-config.toml")) {

    var demoCheckbox = false

    var demoSwitch = false

    init {
        category("Property Overview") {

            subcategory("Subcategory 1") {
                checkbox(::demoCheckbox, "Checkbox", "This is a checkbox property. It stores a boolean value.")
            }

            subcategory("Subcategory 2") {
                checkbox(::demoSwitch, "Switch", "This is a switch property. It stores a boolean value.")
            }
        }

        initialize()
    }
}