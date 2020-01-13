package net.nemerosa.ontrack.bdd.model.steps

import net.thucydides.core.pages.PageObject
import net.thucydides.core.pages.Pages

abstract class AbstractOntrackBrowserSteps : AbstractOntrackDSL() {

    lateinit var pages: Pages

    inline fun <reified P : PageObject, T> page(noinline code: P.() -> T): T {
        val page: P = pages.getPage(P::class.java)
        return page.code()
    }

    inline fun <reified P : PageObject> page(noinline code: P.() -> Unit) {
        page<P, Unit>(code)
    }

    inline fun <reified P : PageObject> currentPageAt(noinline code: P.() -> Unit) {
        val page = pages.currentPageAt(P::class.java)
        page.code()
    }

    /**
     * Navigates to and opens the given page designated by the [P] class and runs the provided [code] inside it.
     */
    inline fun <reified P : PageObject> openPage(noinline code: P.() -> Unit = {}) {
        val page = pages.getPage(P::class.java)
        page.open()
        page.code()
    }

}