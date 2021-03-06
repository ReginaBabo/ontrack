package net.nemerosa.ontrack.graphql.schema;

import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import net.nemerosa.ontrack.model.structure.ProjectEntityType;
import net.nemerosa.ontrack.model.structure.PromotionRun;
import net.nemerosa.ontrack.model.structure.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@Component
public class GQLTypePromotionRun extends AbstractGQLProjectEntity<PromotionRun> {

    public static final String PROMOTION_RUN = "PromotionRun";

    private final GQLProjectEntityInterface projectEntityInterface;

    @Autowired
    public GQLTypePromotionRun(
            GQLTypeCreation creation,
            List<GQLProjectEntityFieldContributor> projectEntityFieldContributors,
            GQLProjectEntityInterface projectEntityInterface
    ) {
        super(PromotionRun.class, ProjectEntityType.PROMOTION_RUN, projectEntityFieldContributors, creation);
        this.projectEntityInterface = projectEntityInterface;
    }

    @Override
    public String getTypeName() {
        return PROMOTION_RUN;
    }

    @Override
    public GraphQLObjectType createType(GQLTypeCache cache) {
        return newObject()
                .name(PROMOTION_RUN)
                .withInterface(projectEntityInterface.getTypeRef())
                .fields(projectEntityInterfaceFields())
                .field(
                        newFieldDefinition()
                                .name("build")
                                .description("Associated build")
                                .type(new GraphQLNonNull(new GraphQLTypeReference(GQLTypeBuild.BUILD)))
                                .build()
                )
                // Promotion level
                .field(
                        newFieldDefinition()
                                .name("promotionLevel")
                                .description("Associated promotion level")
                                .type(new GraphQLNonNull(new GraphQLTypeReference(GQLTypePromotionLevel.PROMOTION_LEVEL)))
                                .build()
                )
                // OK
                .build();
    }

    @Override
    protected Optional<Signature> getSignature(PromotionRun entity) {
        return Optional.ofNullable(entity.getSignature());
    }
}
