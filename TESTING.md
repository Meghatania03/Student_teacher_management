# Assignment1 - Testing Documentation

## Overview
This document describes the testing strategy and setup for the Assignment1 Spring Boot application.

## Test Structure

### Unit Tests
Unit tests are designed to test individual components in isolation using mocks.

#### Repository Tests
- **StudentRepositoryTest.java**: Tests for Student repository CRUD operations
- **DepartmentRepositoryTest.java**: Tests for Department repository CRUD operations

#### Controller Tests
- **StudentControllerTest.java**: Tests for Student controller endpoints
- **HomeControllerTest.java**: Tests for Home controller endpoints (login, redirect)

### Integration Tests
Integration tests verify that multiple components work together correctly.

#### Integration Test Files
- **StudentIntegrationTest.java**: Tests Student entity relationships with Department
- **StudentDashboardIntegrationTest.java**: Tests the complete flow of student dashboard access
- **SecurityIntegrationTest.java**: Tests security-related functionality

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Only Unit Tests
```bash
mvn clean test -DskipITs
```

### Run Only Integration Tests
```bash
mvn clean verify -DskipUnitTests=false
```

### Run Specific Test Class
```bash
mvn test -Dtest=StudentRepositoryTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=StudentRepositoryTest#testFindByEmail
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## Test Configuration

### Test Database
Tests use an in-memory H2 database configured in `application-test.properties`:
- URL: `jdbc:h2:mem:testdb`
- Driver: `org.h2.Driver`
- Hibernate DDL: `create-drop` (creates and drops schema for each test)

### Test Properties
- Spring Security Test: `@WithMockUser` for user authentication
- MockMvc: For testing web layer
- DataJpaTest: For testing persistence layer
- SpringBootTest: For full application integration tests

## Dependencies

Key testing dependencies:
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **Spring Test**: Spring framework testing utilities
- **Spring Security Test**: Security testing utilities
- **H2 Database**: In-memory database for testing
- **JaCoCo**: Code coverage analysis

## Continuous Integration

### GitHub Actions Workflow
The CI/CD pipeline is defined in `.github/workflows/ci.yml` and:

1. **Runs on**: Pull requests and pushes to main branch
2. **Environment**: Ubuntu latest with PostgreSQL 15
3. **Java Version**: JDK 17
4. **Steps**:
   - Checks out code
   - Sets up Java 17
   - Runs unit tests
   - Runs integration tests
   - Generates test reports
   - Checks code coverage with JaCoCo
   - Builds the application
   - Uploads artifacts
   - Publishes test results

### Code Coverage Requirements
- Minimum line coverage: 50%
- Covered by JaCoCo plugin
- Results uploaded to Codecov

## Test Best Practices

### Unit Tests
- Use `@DataJpaTest` for repository tests
- Use `@WebMvcTest` or `@SpringBootTest` with `@AutoConfigureMockMvc` for controller tests
- Mock external dependencies
- Test both success and failure scenarios
- Use descriptive test names

### Integration Tests
- Use `@SpringBootTest` for full application context
- Use `@Transactional` to rollback changes after each test
- Test actual interactions between components
- Use real database (or in-memory for tests)
- Test end-to-end flows

### Naming Conventions
- Test class names end with `Test` or `Tests`
- Test methods follow pattern: `test[MethodName][Scenario][ExpectedResult]`
- Example: `testFindByEmailSuccessful`, `testFindByEmailNotFound`

## Adding New Tests

When adding new features:

1. Create unit tests for the component in isolation
2. Create integration tests for component interactions
3. Ensure minimum 50% code coverage
4. Run full test suite locally before pushing
5. Verify GitHub Actions pipeline passes

## Troubleshooting

### Tests Fail with "Cannot find symbol"
- Clean and rebuild: `mvn clean compile test`
- Update IDE indexes: In IntelliJ, use File > Invalidate Caches

### Database Connection Errors in Tests
- Verify H2 dependency is in pom.xml with `test` scope
- Check `application-test.properties` is in `src/test/resources`

### Security Test Issues
- Add `@WithMockUser` annotation for authenticated tests
- Use `SecurityMockMvcRequestPostProcessors.csrf()` for POST requests
- Import correct Spring Security Test classes

### Coverage Report Not Generated
- Run: `mvn clean test jacoco:report`
- Check `target/site/jacoco/index.html` after build

## Coverage Reports

After running tests, view coverage reports:
- **HTML Report**: `target/site/jacoco/index.html`
- **CSV Report**: `target/site/jacoco/jacoco.csv`
- **XML Report**: `target/site/jacoco/jacoco.xml`

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Testing Documentation](https://docs.spring.io/spring-framework/reference/testing.html)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
