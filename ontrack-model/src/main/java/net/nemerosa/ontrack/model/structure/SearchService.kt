package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.model.Ack

/**
 * Service to search based on text
 */
interface SearchService {

    /**
     * Seach entry point
     */
    fun search(request: SearchRequest): Collection<SearchResult>

    /**
     * Makes sure all search indexes are initialized.
     */
    fun indexInit()

    /**
     * Resetting all search indexes, optionally restoring them.
     *
     *
     * This method is mostly used for testing but could be used
     * to reset faulty indexes.
     *
     * @param reindex `true` to relaunch the indexation afterwards
     * @return OK if indexation was completed successfully
     */
    fun indexReset(reindex: Boolean): Ack

}