package me.sunnydaydev.curencyconverter.coregeneral.di

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface ComponentRequirements

interface RequirementsComponentProvider {

    fun <T: ComponentRequirements> getComponentRequirements(): T

}