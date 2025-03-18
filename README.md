# TODO App

A modern, feature-rich TODO application designed to showcase best practices in Android development. This project utilizes Jetpack Compose for the UI, Room for local data persistence, and Dagger Hilt for dependency injection, all orchestrated under a clean MVVM architecture. The app is built with a focus on smooth animations, intuitive navigation, and responsive design, making it a perfect addition to any portfolio.

## Features

- **Task Management:**  
  Create, update, and delete tasks with ease.
  
- **Real-Time Notifications:**  
  Receive notifications for tasks that are scheduled to occur within the next hour.
  
- **Elegant UI/UX:**  
  Built with Jetpack Compose, the app features a modern, responsive interface with smooth animations and transitions.
  
- **Robust Architecture:**  
  Implements a clean separation of concerns using MVVM, ensuring maintainability and testability.
  
- **Dependency Injection:**  
  Leverages Dagger Hilt for efficient and modular dependency management.
  
- **Local Data Persistence:**  
  Uses Room database to manage task storage and retrieval reliably.

## Architecture

The app is structured into three main layers:

- **Presentation Layer:**  
  - Built with Jetpack Compose to create a dynamic and engaging UI.
  - Uses ViewModels to manage UI state and handle business logic.

- **Domain Layer:**  
  - Contains business logic and repository interfaces.
  - Implements task validation and state management.

- **Data Layer:**  
  - Manages local data storage with Room.
  - Provides DAO interfaces for database operations.

Dagger Hilt is integrated throughout the project to inject dependencies, ensuring loose coupling between components.

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/AlexisZhuber/TodoApp
   ```

2. **Open the Project:**

   Open the project in [Android Studio](https://developer.android.com/studio).

3. **Build and Run:**

   Build the project and run it on an emulator or a physical Android device.

## Demonstration

See the app in action by watching the demonstration video on YouTube:  
[TODO App Demo](https://www.youtube.com/watch?v=AdmQNsOXcP0)

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes with clear, concise messages.
4. Open a pull request detailing your changes and improvements.

For major changes, please open an issue first to discuss your proposed changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions, suggestions, or further information, please contact me at [alexismora602@gmail.com](alexismora602@gmail.com).
