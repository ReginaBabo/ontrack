package net.nemerosa.ontrack.bdd.model.pages

import net.serenitybdd.core.pages.WebElementFacade
import org.openqa.selenium.By
import org.openqa.selenium.support.PageFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractModule(
        val parent: AbstractPage
) {

    init {
        init()
    }

    private fun init() {
        PageFactory.initElements(parent.driver, this)
    }

    protected fun textInput(by: By) = object : ReadWriteProperty<AbstractModule, String> {

        override fun getValue(thisRef: AbstractModule, property: KProperty<*>): String =
                parent.find<WebElementFacade>(by).text

        override fun setValue(thisRef: AbstractModule, property: KProperty<*>, value: String) {
            parent.find<WebElementFacade>(by).sendKeys(value)
        }

    }

}
