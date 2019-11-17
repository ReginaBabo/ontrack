package net.nemerosa.ontrack.acceptance.support;

import java.lang.annotation.*;

/**
 * Used to annotate tests which have been migrated to the Kotlin DSL.
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface KDSL {

    /**
     * Name of the test class
     */
    String value();

}
