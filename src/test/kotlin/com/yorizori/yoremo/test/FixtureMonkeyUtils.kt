package com.yorizori.yoremo.test

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

object FixtureMonkeyUtils {

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    inline fun <reified T> giveMeOne(): T = fixtureMonkey.giveMeOne(T::class.java)

    inline fun <reified T> giveMeKotlinBuilder() = fixtureMonkey.giveMeKotlinBuilder<T>()
}
