package net.nemerosa.ontrack.bdd.model.worlds

import net.nemerosa.ontrack.bdd.model.support.indexOf
import net.nemerosa.ontrack.bdd.model.support.uid
import net.nemerosa.ontrack.kdsl.model.AccountGroup
import net.nemerosa.ontrack.kdsl.model.Project
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class OntrackDSLWorld {

    /**
     * Register of projects
     */
    val projects = indexOf<Project>()

    /**
     * Register of accounts
     */
    val accounts = indexOf<AccountWithPassword>()

    /**
     * Register of account groups
     */
    val accountGroups = indexOf<AccountGroup>()

    /**
     * Unique names
     */
    private val uniqueNames = ConcurrentHashMap<Pair<String, String>, String>()

    /**
     * Management of unique names
     */
    fun uniqueName(group: String, key: String): String =
            uniqueNames.getOrPut(group to key) {
                uid(key)
            }

    /**
     * Clears all context
     */
    fun clear() {
        projects.clear()
        accounts.clear()
        accountGroups.clear()
        uniqueNames.clear()
    }

}