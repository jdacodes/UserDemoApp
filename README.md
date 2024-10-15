
# Sample Android App (Kotlin) Using Reqres API

This is a demo Android application developed in Kotlin, showcasing the usage of the reqres.in REST API for fetching sample data. The app is built using Clean Architecture, with modern Android development practices including Jetpack Compose using Material 3 for UI, Room for database persistence, DataStore for preferences management, and Kotlin Coroutines for asynchronous operations. It also implements pagination to efficiently load large datasets.
## Screenshots
<p align="center">
  <img src="https://github.com/user-attachments/assets/9d2ecd37-b66f-4804-b00d-bdc5e1136e94" alt="homescreen_userdemo" width="30%" />
  <img src="https://github.com/user-attachments/assets/85da1cdb-7020-4dc3-b241-e65ef29c573c" alt="userscreen_userdemo" width="30%" />
  <img src="https://github.com/user-attachments/assets/55f22069-5f67-4729-a995-83112696444e" alt="profilescreen_userdemo" width="30%" />
  <img src="https://github.com/user-attachments/assets/5ef91fc0-bb93-475f-bb21-956e8c25f823" alt="loginscreen_userdemo" width="30%" />
   <img src="https://github.com/user-attachments/assets/85661eb9-7848-4e17-82f2-e56472686b99" alt="signupscreen_userdemo" width="30%" />
   <img src="https://github.com/user-attachments/assets/61710dbc-5bfe-42aa-b32f-2940959de3ba" alt="updateprofilescreen_userdemo" width="30%" />
</p>

## Features

- Clean Architecture: Clear separation of concerns between UI, domain, and data layers, ensuring modularity, scalability, and testability.
- Jetpack Compose with Material 3: Leverages the latest Material Design 3 components in a fully declarative UI framework.
- State Management with StateFlow and Flow: Reactive state management using StateFlow, Flow, and Channel for one-off events.
- Room Database: Local storage of user data with Room, providing an offline-first experience.
- DataStore Preferences: Efficient key-value storage for managing user preferences, supporting Kotlin Coroutines.
- DataStore Preferences: Efficient key-value storage for managing user preferences, supporting Kotlin Coroutines.
- Networking with Retrofit: Integration with the reqres.in API for fetching sample user and color data, including handling network operations such as POST, PATCH, and DELETE.
- Pagination: Handles large datasets with efficient pagination strategies for both user data and colors.
- Kotlin Coroutines & Flow: Manages background tasks and data streams with Coroutines, ensuring smooth and responsive UI interactions.
- Dependency Injection with Hilt: Simplified dependency management using Google's Hilt for DI.
- Repository Pattern: Abstracts the data sources, allowing for seamless data access from both local and remote sources.

## Code Structure

The app's code is organized into the following packages:
-  auth

Handles all authentication-related functionality, including:

    Sign In: Manage user sign-in flow using the API.
    Sign Up: Allows new users to register via the API.
    Forgot Password: Enables users to reset their password via email.

This package ensures all authentication flows are separate from the rest of the app logic, making it modular and easy to maintain or expand in the future.

- core

Contains the core resources and utility classes used throughout the app:

    App Theme: Defines the global theming using Jetpack Compose's Material 3 components (colors, typography, etc.).
    Utilities: Contains reusable helper functions, constants, and extensions that are used across the app.

This package centralizes common resources and utilities to prevent duplication and maintain consistency throughout the app.

- di

Handles the dependency injection setup using Hilt:

    Provides necessary modules to inject dependencies such as Retrofit, Room, and ViewModels across the app.

This ensures that dependencies are managed centrally and allows for easy testing and scalability.

- dashboard

Manages the Home Screen and displays user and color data:

    Horizontal List of Colors: Fetches a list of colors from the API and displays them in a horizontal scrollable list.
    Search Functionality: Provides a search bar to allow users to search through the available data.
    Carousel Element: Displays content in a carousel-like format, adding a dynamic, interactive user experience.

The dashboard package handles the primary user interface for the app's main functionality, giving users access to the color data and search capabilities.

-  navigation

Manages all navigation across the app, using Jetpack Compose Navigation:

    Nested Navigation: Handles navigation between different screens, such as the Home (Dashboard) and Auth flows, ensuring each section of the app is easily navigable.

This package separates navigation concerns from the rest of the codebase, allowing for clean and organized transitions between screens and flows.

-  profile

Handles User Profile management:

    View Profile: Allows users to view their own profile information fetched from the API.
    Update Profile: Enables users to update their profile details, which are saved both to the API and locally via Room.
    Persist to Device: Stores user information on the device, ensuring that updates are reflected locally even when the app is restarted.

The profile package is focused on managing the user’s information and ensures that the data is persisted for offline use.

- userlist

Displays a List of Users and individual user details:

    User List: Fetches and displays a paginated list of users from the API.
    User Detail View: Allows users to click on a specific user to see their detailed information, fetched from the API.

The userlist package handles the logic for fetching and displaying users, as well as managing navigation to individual user details.
## API Integration (requres.in)
- Endpoint: reqres.in provides a set of fake REST API endpoints for testing purposes.
- Example endpoints used in the app:
#### Get all users, Fetch a paginated list of users.

```http
  GET api/users?page=2
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page`      | `Int` | **Required**. page number to fetch |

#### Get user

```http
  GET /api/items/:id
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to fetch |

#### Create a new user

```http
  POST api/users
```
#### Update User, Partially update an existing user.

```http
  PATCH /users/:id
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to update |

#### Delete a user

```http
  DELETE /api/items/:id
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Int` | **Required**. Id of item to delete |

#### Get all colors, Fetch a paginated list of colors.

```http
  GET api/unknown/?page=2
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page`      | `Int` | **Required**. page number to fetch |



## Libraries and Tools Used

- Jetpack Compose: Declarative UI framework for Android with Material 3 components.
- Room Database: For local data persistence with SQLite.
- DataStore: Modern key-value storage for managing preferences.
- Retrofit: Type-safe HTTP client for API communication.
- Dagger-Hilt: Dependency injection framework.
- Kotlin Coroutines & Flow: For handling asynchronous tasks and reactive streams.
- Paging 3: For efficient pagination of user and color data.
- OkHttp: HTTP client for efficient networking.
- GSON: A library for converting Java/Kotlin objects to JSON and vice versa. It is used to parse JSON responses from the API and serialize data when making requests.
## Setup and Installation

1. Clone this repository: ```http git clone https://github.com/jdacodes/UserDemoApp.git```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.
4. The app requires no special permissions and uses dummy data from reqres.in.
## Architecture Overview

Architecture Overview

The app follows Clean Architecture principles, splitting the project into three layers:

    Presentation Layer: The UI is built with Jetpack Compose using Material 3 components. State, StateFlow, Flow, and Channel are used to manage state and one-off events, providing reactive and lifecycle-aware UI updates.
    Domain Layer: Contains the business logic and use cases. This layer is completely independent from the UI and data layers, ensuring that it can be easily tested and reused.
    Data Layer: Handles data operations from both local and remote sources. Room is used for local persistence, while Retrofit handles the remote API calls. Pagination is implemented to efficiently manage large datasets.

Data Persistence

    Room Database: Used to store user data and maintain it offline, with DAOs handling database queries and updates.
    DataStore Preferences: Utilized for storing user preferences or small key-value data, replacing SharedPreferences with better performance and Coroutine support.

Pagination

    The app uses Paging 3 to load paginated data from both the reqres.in API and the local database. This improves app performance and user experience when dealing with large datasets.

Testing

    Currently, no tests have been implemented. Future updates will include unit tests for business logic and integration tests for network and database operations.
