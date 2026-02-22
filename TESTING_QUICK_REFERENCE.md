# Testing Quick Reference Card

## 🚀 Most Used Commands

```bash
# Run ALL tests
mvn clean test

# Run UNIT tests only
mvn clean test -DskipITs

# Run INTEGRATION tests only
mvn clean verify

# Run SPECIFIC test
mvn test -Dtest=StudentRepositoryTest

# Run SPECIFIC test method
mvn test -Dtest=StudentRepositoryTest#testFindByEmail

# Generate COVERAGE report
mvn clean test jacoco:report

# View coverage HTML report
open target/site/jacoco/index.html  # Mac
start target/site/jacoco/index.html # Windows
```

## 📂 Test File Locations

```
src/test/java/com/example/assignment1/
├── repository/
│   ├── StudentRepositoryTest.java
│   ├── TeacherRepositoryTest.java
│   ├── DepartmentRepositoryTest.java
│   └── CourseRepositoryTest.java
├── controller/
│   ├── StudentControllerTest.java
│   ├── TeacherControllerTest.java
│   └── HomeControllerTest.java
├── integration/
│   ├── StudentIntegrationTest.java
│   ├── StudentDashboardIntegrationTest.java
│   ├── AuthenticationIntegrationTest.java
│   └── EndToEndIntegrationTest.java
├── security/
│   └── SecurityIntegrationTest.java
└── Assignment1ApplicationTests.java

src/test/resources/
└── application-test.properties

.github/workflows/
└── ci.yml
```

## 🧪 Test Types & Uses

### @DataJpaTest (Repository Layer)
```java
@DataJpaTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository repository;
    
    // Uses H2 in-memory database
    // Tests CRUD operations only
}
```

### @SpringBootTest + @AutoConfigureMockMvc (Integration)
```java
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    // Tests full application context
    // Tests web layer
}
```

### @WithMockUser (Security Testing)
```java
@WithMockUser(username = "user@example.com", roles = "STUDENT")
void testSecureEndpoint() throws Exception {
    // Test with authenticated user
}
```

## 📊 Test Statistics

| Category | Count |
|----------|-------|
| Repository Tests | 26 |
| Controller Tests | 12 |
| Integration Tests | 29 |
| Security Tests | 3 |
| **TOTAL** | **70+** |

## 🔧 Key Annotations

| Annotation | Purpose |
|-----------|---------|
| `@DataJpaTest` | Tests repository layer with H2 |
| `@SpringBootTest` | Full application context |
| `@AutoConfigureMockMvc` | Auto-configure MockMvc bean |
| `@MockBean` | Mock a bean in test context |
| `@WithMockUser` | Authenticate test as user |
| `@BeforeEach` | Setup before each test |
| `@Test` | Mark method as test |
| `@Transactional` | Rollback after test |

## 📝 Common Assertions

```java
// Basic assertions
assertTrue(condition);
assertFalse(condition);
assertNull(object);
assertNotNull(object);
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// Collection assertions
assertTrue(collection.contains(element));
assertEquals(5, collection.size());

// Exception assertions
assertThrows(IllegalArgumentException.class, () -> {
    // code that throws exception
});

// MockMvc assertions
mockMvc.perform(get("/endpoint"))
    .andExpect(status().isOk())
    .andExpect(view().name("templateName"))
    .andExpect(model().attributeExists("attribute"));
```

## 🔐 Security Test Patterns

```java
// Unauthenticated user - should redirect
mockMvc.perform(get("/secure-endpoint"))
    .andExpect(status().is3xxRedirection());

// With mock authentication
@WithMockUser(roles = "STUDENT")
void testStudentEndpoint() throws Exception {
    mockMvc.perform(get("/student/dashboard"))
        .andExpect(status().isOk());
}

// Access denied for wrong role
@WithMockUser(roles = "STUDENT")
void testTeacherEndpoint() throws Exception {
    mockMvc.perform(get("/teacher/dashboard"))
        .andExpect(status().isForbidden());
}
```

## 🐛 Troubleshooting Quick Fixes

| Issue | Solution |
|-------|----------|
| Tests not found | Check class name ends with `Test` |
| H2 database error | Add `com.h2database:h2` in test scope |
| Import errors | Check test annotations are from org.junit.jupiter |
| CSRF errors | Add `.with(csrf())` to MockMvc requests |
| Security errors | Use `@WithMockUser` for authenticated tests |
| Coverage not generated | Run `mvn clean test jacoco:report` |

## 🌐 GitHub Actions Quick Reference

### Workflow File
Location: `.github/workflows/ci.yml`

### Triggers
- ✅ Pull request to main
- ✅ Push to main

### What It Does
1. Checks out code
2. Sets up Java 17
3. Runs unit tests
4. Runs integration tests
5. Generates coverage
6. Builds application
7. Uploads artifacts
8. Publishes results

### View Results
Go to: GitHub Actions tab → Select workflow run

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| TESTING.md | Comprehensive guide |
| TEST_EXECUTION_GUIDE.md | Commands & examples |
| TEST_SETUP_COMPLETE.md | Setup summary |
| TEST_FILES_COMPLETE_CHECKLIST.md | Complete checklist |

## ✅ Pre-Commit Checklist

- [ ] All tests pass locally: `mvn clean test`
- [ ] Coverage meets minimum: `mvn jacoco:report`
- [ ] Code compiles: `mvn clean compile`
- [ ] No warnings in logs
- [ ] All new code has tests

## 📋 Test Naming Convention

```
test[MethodName][Scenario][ExpectedResult]

Example:
testFindByEmail_WithValidEmail_ReturnStudent
testFindByEmail_WithInvalidEmail_ReturnEmpty
testAccessDashboard_WithoutAuthentication_RedirectToLogin
```

## 🎯 Coverage Targets

- Overall: **50%+ (Required)**
- Repository: **90%+**
- Controller: **80%+**
- Integration: **70%+**

## 🚨 Common Errors & Fixes

### Error: "Cannot find symbol: Student"
```bash
# Fix: Clean and rebuild
mvn clean compile
```

### Error: "H2 database connection refused"
```bash
# Verify dependency in pom.xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### Error: "No bean of type found"
```java
// Fix: Add @MockBean
@MockBean
private StudentRepository repository;
```

### Error: "CSRF token error"
```java
// Fix: Add csrf() to request
mockMvc.perform(post("/endpoint")
    .with(csrf()))
```

## 💡 Pro Tips

1. **Use @Transactional on integration tests** - Ensures test isolation
2. **Mock external services** - Use @MockBean for faster tests
3. **Test edge cases** - Empty lists, null values, invalid data
4. **Use descriptive names** - Future you will thank you
5. **Keep tests focused** - One thing per test method
6. **Use setUp methods** - Reduce test code duplication
7. **Check coverage reports** - Identify untested code paths

## 📞 Quick Links

- [JUnit 5 Docs](https://junit.org/junit5/)
- [Mockito Docs](https://javadoc.io/doc/org.mockito/mockito-core/)
- [Spring Testing](https://spring.io/guides/gs/testing-web/)
- [GitHub Actions](https://docs.github.com/en/actions)
- [JaCoCo Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

---

**Remember**: Tests are documentation! Write clear, readable tests that explain expected behavior.

