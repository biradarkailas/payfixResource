package com.payfix.packages;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.payfix.packages");

        noClasses()
            .that()
            .resideInAnyPackage("com.payfix.packages.service..")
            .or()
            .resideInAnyPackage("com.payfix.packages.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.payfix.packages.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
