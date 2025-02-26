package org.mytictackmp.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform