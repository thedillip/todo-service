INSERT INTO todo_management.todo (title, description, status, created_at, updated_at)
VALUES
    ('Learn Spring Boot', 'Understand the basics of Spring Boot', 'PENDING', NOW(), NOW()),
    ('Set Up PostgreSQL', 'Install and configure PostgreSQL for the project', 'COMPLETED', NOW(), NOW()),
    ('Add Redis Cache', 'Integrate Redis caching into the backend', 'IN_PROGRESS', NOW(), NOW());