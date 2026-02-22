# Test Execution Guide

## Quick Start

### 1. Run All Tests
```bash
mvn clean test
```

### 2. Run Only Unit Tests (excluding integration tests)
```bash
mvn clean test -DskipITs
```

### 3. Run Only Integration Tests
```bash
mvn clean verify -DskipUnitTests=false
```

### 4. Generate Code Coverage Report
```bash
mvn clean test jacoco:report
```

Coverage report: `target/site/jacoco/index.html`

### 5. Run Specific Test Class
```bash
mvn test -Dtest=StudentRepositoryTest
```

### 6. Run Specific Test Method
```bash
mvn test -Dtest=StudentRepositoryTest#testFindByEmail
```

### 7. Run Tests with Verbose Output
```bash
mvn clean test -X
```

## Test Files Created

### Unit Tests (Data Layer)
- **StudentRepositoryTest**: Tests CRUD operations on Student repository
- **TeacherRepositoryTest**: Tests CRUD operations on Teacher repository
- **DepartmentRepositoryTest**: Tests CRUD operations on Department repository
- **CourseRepositoryTest**: Tests CRUD operations on Course repository

### Unit Tests (Controller Layer)
- **StudentControllerTest**: Tests Student controller endpoints with mocks
- **TeacherControllerTest**: Tests Teacher controller endpoints with mocks
- **HomeControllerTest**: Tests Home controller endpoints (login, redirect)

### Integration Tests
- **StudentIntegrationTest**: Tests Student entity relationships with Department
- **StudentDashboardIntegrationTest**: Tests complete student dashboard flow
- **AuthenticationIntegrationTest**: Tests role-based access control
- **SecurityIntegrationTest**: Tests security-related functionality

## CI/CD Workflow

GitHub Actions workflow (`.github/workflows/ci.yml`) automatically:
1. Checks out code on PR/push to main
2. Sets up Java 17
3. Runs unit tests with Maven
4. Runs integration tests with PostgreSQL
5. Generates JaCoCo coverage report
6. Builds the application
7. Uploads artifacts and reports
8. Publishes test results

## Key Testing Patterns

### 1. Using @DataJpaTest
```java
@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
}
```

### 2. Using @WebMvcTest with MockMvc
```java
@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
}
```

### 3. Using @SpringBootTest for Integration Tests
```java
@SpringBootTest
@AutoConfigureMockMvc
class StudentDashboardIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
}
```

### 4. Security Testing with @WithMockUser
```java
@WithMockUser(username = "student@example.com", roles = "STUDENT")
void testStudentDashboard() throws Exception {
    // Test implementation
}
```

### 5. Testing with CSRF Protection
```java
mockMvc.perform(get("/student/dashboard")
        .with(csrf()))
    .andExpect(status().isOk());
```

## Expected Test Results

- **Total Unit Tests**: 20+
- **Total Integration Tests**: 8+
- **Expected Coverage**: Minimum 50%
- **All tests should pass**: Green build ✓

## Troubleshooting

### Maven Not Found
Install Maven or add to PATH: https://maven.apache.org/download.cgi

### Database Errors in Tests
H2 database should be used automatically for tests. Check `application-test.properties`.

### Security Test Errors
- Import correct Spring Security Test classes
- Use `@WithMockUser` for authentication
- Use `csrf()` post processor for POST requests

### Coverage Report Not Generated
```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

## Performance Tips

1. **Skip Lombok compilation during tests** (already configured)
2. **Use H2 in-memory database** for faster test execution
3. **Parallel test execution** (if needed):
```bash
mvn test -DthreadCount=4 -DparallelTestClasses=true
```

## Test Coverage Targets

- Repository layer: 90%+
- Controller layer: 80%+
- Service layer: 85%+
- Overall: 50%+ (minimum requirement in JaCoCo config)

## Continuous Integration Benefits

✓ Automatic testing on every pull request
✓ Catches bugs before merge to main
✓ Generates coverage reports
✓ Builds and artifacts automatically
✓ Consistent test environment
✓ Test results published automatically
