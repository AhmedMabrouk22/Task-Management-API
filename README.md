# Task Management API

The Task Management API is a backend API designed as a simplified Trello clone, enabling efficient task and project management for teams. It supports creating, updating, assigning, and viewing tasks, along with user authentication, authorization, and team collaborations through comments with user mentions.
## Key Features

This API provides foundational project and task management functionalities that allow teams to coordinate and track work.

* **User Authentication & Authorization:** Using JWT, with support for login, signup, access, and refresh tokens.
* **Project Management:** Create, view, update, and delete projects.
* **Task Management:** Create, update, delete, and view tasks with optional assignment to project member.
* **Task Filtering & Sorting:** Show assigned tasks or all tasks based on user permissions.
* **Comments & Mentions:** Add comments on tasks with the ability to mention other users for easy collaboration.

## Future Enhancements
These features are planned for upcoming releases:

1. [ ] **Complete Unit Tests:** Extending test coverage for all endpoints and core functionalities.
2. [ ] **Task Activity Tracking:** Recording task-related activities for a detailed history log.
3. [ ] **Subtasks Support:** Allowing users to create and manage subtasks within larger tasks.
4. [ ] **File Management:** Enabling file uploads for tasks to allow attachments and documentation.

## Technologies Used
* **Backend Framework:** Spring Boot
* **ORM:** Hibernate
* **Database:** PostgreSQL
* **Authentication:** JSON Web Tokens (JWT)
* **Testing:** JUnit


