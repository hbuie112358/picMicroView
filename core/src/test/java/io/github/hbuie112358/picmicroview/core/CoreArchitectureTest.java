package io.github.hbuie112358.picmicroview.core;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Architecture rules the plan makes for `core`, checked against its compiled classes. ArchUnit's
 * plain API is called from ordinary JUnit tests rather than through ArchUnit's own JUnit engine,
 * whose support for JUnit 6 is not confirmed (plan D18).
 */
class CoreArchitectureTest {

    private static final String CORE_PACKAGE = "io.github.hbuie112358.picmicroview.core";

    private static JavaClasses coreClasses;

    @BeforeAll
    static void importCoreClasses() {
        coreClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(CORE_PACKAGE);
    }

    /*
     * D14: every chip is an independent object, so no simulator state may live in a static
     * field, which would be shared by every simulator and every test running in parallel.
     * Allowed to match nothing: the rule guards against future fields.
     */
    @Test
    void staticFieldsAreFinal() {
        fields().that().areStatic()
                .should().beFinal()
                .allowEmptyShould(true)
                .because("simulator state belongs to instances, never to classes (plan D14)")
                .check(coreClasses);
    }

    /*
     * D15: TeaVM, which compiles `core` for the browser, does not support reflection. This
     * fails the ordinary JVM build before TeaVM's build would.
     */
    @Test
    void doesNotUseReflection() {
        noClasses()
                .should().dependOnClassesThat().resideInAPackage("java.lang.reflect..")
                .orShould().callMethod(Class.class, "forName", String.class)
                .because("TeaVM cannot compile reflection for the browser (plan D15)")
                .check(coreClasses);
    }
}
