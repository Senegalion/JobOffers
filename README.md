# JobOffers for Junior Java Developers

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

`JobOffers` is a web application designed to aggregate job offers specifically for Junior Java Developers from various online sources. The primary goal is to provide a centralized platform with up-to-date job listings from diverse websites and applications, simplifying the job search process.

## Project Description

This application automates the collection of job offers, ensuring that Junior Java Developers have access to the latest opportunities in their field. It features robust backend services for data retrieval, storage, and management, along with API endpoints for seamless integration.

This project uses a modular monolithic application architecture with elements of hexagonal principles.

## Features

-   **Automated Job Offer Aggregation**: Fetches job listings from multiple websites and applications.
-   **Junior Java Developer Focus**: Tailored job offers relevant to entry-level Java developers.
-   **RESTful API**: Provides endpoints for accessing and managing job offer data.
-   **Data Storage**: Utilizes MongoDB for efficient data storage and retrieval.
-   **Filtering and Search**: Enables users to filter and search job offers based on various criteria.
-   **Security**: Implements JWT authentication for secure access to API endpoints.
-   **Scheduling**: Uses Spring Scheduler for periodic job offer updates.
-   **Caching**: Redis integration for improved performance and reduced database load.
-   **Containerization**: Docker and Docker Compose for easy deployment and scalability.

## Architecture

![Architecture Diagram](architecture/job_offers_architecture_v3.png)

* The C3 diagram above presents main application components and module dependencies.

## Tech

JobOffers is developed using the following technologies:

**Core:**

|                       |                                                                                                                                                                                                                            |
| :-------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Backend** | <img src="https://img.shields.io/badge/Spring_Boot-v2.7.5-brightgreen" alt="Spring Boot" /> <img src="https://img.shields.io/badge/Java-17-orange" alt="Java" />                                                              |
|                       | Spring Boot (REST Controllers, Test, Data MongoDB, Validation, Security, JWT, Spring Scheduler)                                                                                                                            |
| **Database** | <img src="https://img.shields.io/badge/MongoDB-v6.0-green" alt="MongoDB" /> <img src="https://img.shields.io/badge/MongoExpress-v1.0.0-blue" alt="MongoExpress" />                                                              |
|                       | MongoDB, MongoExpress                                                                                                                                                                                                  |
| **Containerization** | <img src="https://img.shields.io/badge/Docker-v20.10-blueviolet" alt="Docker" /> <img src="https://img.shields.io/badge/Docker_Compose-v1.29-blueviolet" alt="Docker Compose" /> <img src="https://img.shields.io/badge/Docker_Desktop-4.14-blueviolet" alt="Docker Desktop" /> |
|                       | Docker, Docker Compose, Docker Desktop                                                                                                                                                                                 |
| **Testing** | <img src="https://img.shields.io/badge/JUnit5-5.9.1-red" alt="JUnit5" /> <img src="https://img.shields.io/badge/Mockito-4.8.0-red" alt="Mockito" /> <img src="https://img.shields.io/badge/AssertJ-3.23.1-red" alt="AssertJ" />      |
|                       | Wiremock, Testcontainers, MockMvc, Awaitility, JUnit5, Mockito, AssertJ, SpringBootTest, SpringSecurityTest                                                                                                               |
| **Logging** | <img src="https://img.shields.io/badge/Log4j2-2.19.0-lightgrey" alt="Log4j2" />                                                                                                                                            |
|                       | Log4j2                                                                                                                                                                                                               |
| **Networking** | <img src="https://img.shields.io/badge/RestTemplate-v1.0-lightblue" alt="RestTemplate" /> <img src="https://img.shields.io/badge/JSON-v3.7.1-yellowgreen" alt="JSON" /> <img src="https://img.shields.io/badge/HTTP-v1.1-lightgrey" alt="HTTP" /> |
|                       | RestTemplate, JSON, HTTP                                                                                                                                                                                               |
| **Utilities** | <img src="https://img.shields.io/badge/Lombok-v1.18.22-ff69b4" alt="Lombok" /> <img src="https://img.shields.io/badge/Redis-v7.0-red" alt="Redis" /> <img src="https://img.shields.io/badge/Jedis-v4.3.1-orange" alt="Jedis" />  |
|                       | Lombok, Redis (Jedis, Redis-Commander)                                                                                                                                                                                 |
| **Build Tool** | <img src="https://img.shields.io/badge/Maven-v3.8.7-blue" alt="Maven" />                                                                                                                                                |
|                       | Maven                                                                                                                                                                                                                  |
| **Version Control** | <img src="https://img.shields.io/badge/Git-2.38.1-black" alt="Git" /> <img src="https://img.shields.io/badge/GitHub-black" alt="GitHub" /> / GitLab                                                                     |
|                       | Git, GitHub/GitLab                                                                                                                                                                                                   |
| **IDE** | IntelliJ Ultimate                                                                                                                                                                                                    |
| **API Documentation** | <img src="https://img.shields.io/badge/Swagger-v3.0-green" alt="Swagger" />                                                                                                                                            |
|                       | Swagger                                                                                                                                                                                                                |
| **CI/CD** | <img src="https://img.shields.io/badge/Jenkins-2.361.4-blue" alt="Jenkins" />                                                                                                                                            |
|                       | Jenkins                                                                                                                                                                                                                |
| **Project Management**| <img src="https://img.shields.io/badge/SCRUM-blue" alt="SCRUM" /> <img src="https://img.shields.io/badge/Jira-blue" alt="Jira" />                                                                                             |
|                       | SCRUM, Jira                                                                                                                                                                                                            |
| **Dev. Practices** | Code Review, Pair Programming                                                                                                                                                                                        |

## Installation and Setup

1.  **Clone the Repository:**

    ```bash
    git clone [https://github.com/Senegalion/JobOffers.git](https://github.com/Senegalion/JobOffers.git)
    ```

2.  **Navigate to the Project Directory:**

    ```bash
    cd JobOffers
    ```

3.  **Build and Run with Docker Compose:**

    ```bash
    docker-compose up --build
    ```

    This will start the application, MongoDB, MongoExpress, and Redis using Docker containers.

4.  **Configuration:**

    -   Environment variables are managed within the `docker-compose.yml` file.
    -   Adjust configurations for MongoDB, Redis, and other services as needed.

5.  **Access the Application:**

    -   The application will be accessible at `http://localhost:8082`.
    -   MongoExpress will be accessible at `http://localhost:8081`.
        -   **Note:** MongoExpress requires login. Use `admin` as username and `pass` as password.
    -   Redis commander is accessible at `http://localhost:8083`
    -   Swagger UI will be accessible at `http://localhost:8082/swagger-ui/index.html`

## Usage

-   Use the provided REST API endpoints to access and manage job offer data.
-   Utilize Swagger UI for API documentation and testing.
-   Monitor the application and database using MongoExpress and Redis Commander.

## Contribution

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Commit your changes.
4.  Push your changes to your fork.
5.  Submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Author

[Senegalion](https://github.com/Senegalion)

## Contact

For questions or feedback, please contact me via [email](mailto:lupelikan@gmail.com).

## Acknowledgments

-   Shields.io
-   Badges 4 README.md
