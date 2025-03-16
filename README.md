# Minimal Notepad (Backend)
Minimal Notepad is a digital note-taking tool designed for students, teachers, and professionals. It simplifies creating, organizing, and sharing notes with a user-friendly interface. With features like tagging, annotations, and collaboration, it enhances learning and teamwork and offers a seamless and efficient note-taking experience.

This project was created as part of SEP1 (Software Engineering Project 1).

📆 January - March, 2025

## Technical stack:
- Spring Boot
- MariaDB
- dotenv

## How to run locally:
1) Clone the repository
2) Copy the .env.example file and create a new `.env` file in the same directory and add your database password inside .env. You can generate a secret key using [this link](https://jwtsecret.com/), the minimum key length should be at least 128 characters, but it is recommended to use 256 characters
3) Execute the script from the `init-db.sql` file located in the db-init folder to create the database
4) Run the `MinimalnotepadApplication` file to start the application.

## Development Documentation
1. [Project Structure](./docs/SpringBoot_Folder_Structure.md)
2. [Postman JSON Import settings](./src/main/resources/POSTMAN)
3. [Docker documentation](./docs/Docker_Setup.md)
4. [Jenkins Setup Guide](./docs/Jenkins_Setup.md)
5. [Project diagram folder](./docs/diagrams)
