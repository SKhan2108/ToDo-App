# Spring Boot TODO Application

## Features
1. Create new todo items with title, description, and target completion date
2. Update existing todo items
3. Mark items as completed/incomplete
4. Delete individual items or all completed items
5. View all items with smart sorting (incomplete items first, sorted by target date)
6. Filter items by completion status

## API Endpoints

### Get all todo items
GET /api/todos
Response: List of all todo items sorted by completion status and target date

### Get todo item by ID
GET /api/todos/{id}
Response: Single todo item

### Create new todo item
POST /api/todos
Body: {
"title": "Task title",
"description": "Task description",
"targetCompletionDate": "2024-12-31T23:59:59"
}

### Update todo item
PUT /api/todos/{id}
Body: {
"title": "Updated title",
"description": "Updated description",
"targetCompletionDate": "2024-12-31T23:59:59"
}

### Mark as completed
PATCH /api/todos/{id}/complete

### Mark as incomplete
PATCH /api/todos/{id}/incomplete

### Delete todo item
DELETE /api/todos/{id}

### Delete all completed items
DELETE /api/todos/completed

### Get completed items only
GET /api/todos/completed

### Get pending items only
GET /api/todos/pending

## Running the Application

1. Clone the repository
2. Navigate to project directory
3. Run: `mvn spring-boot:run`
4. Application will start on http://localhost:8080
5. H2 database console available at http://localhost:8080/h2-console

## Exception handling 
Handled by GlobalExceptionHandler class which will be triggered when exception occurs and return appropriate response

## Database
Uses H2 in-memory database with sample data pre-loaded.
Connection details:
- URL: jdbc:h2:mem:todoapp
- Username: sa
- Password: password

## Testing
You can test the API using tools like Postman, curl, or the provided sample data.