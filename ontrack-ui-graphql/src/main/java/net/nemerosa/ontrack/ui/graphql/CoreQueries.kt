package net.nemerosa.ontrack.ui.graphql

import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.Project
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.graphql.dsl.root.RootQueryDelegates.rootQuery
import net.nemerosa.ontrack.ui.graphql.dsl.support.jsonParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreQueries
