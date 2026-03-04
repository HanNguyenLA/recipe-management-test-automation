# Recipe Management Test Automation

## 📌 Overview

This project is an **automation testing framework** for a **Recipe Management Web Application**, built to validate core functionalities such as **Login** and **Recipe CRUD (Create, Read, Update, Delete)**.

The framework is designed following **industry best practices**, using **Selenium + TestNG + Page Object Model (POM)**, integrated with **Jenkins CI** for automated execution and **Allure Report** for test reporting.

---

## 🧰 Tech Stack

* **Language**: Java
* **Automation Tool**: Selenium WebDriver
* **Test Framework**: TestNG
* **Build Tool**: Maven
* **Design Pattern**: Page Object Model (POM)
* **CI Tool**: Jenkins (Continuous Integration)
* **Reporting**: Allure Report
* **Version Control**: Git & GitHub

---

## 🧪 Test Design

- Implemented **Page Object Model (POM)** for better maintainability and readability
- Applied **data-driven testing** using **TestNG DataProvider**
- Reused the same test logic with multiple datasets, especially for Login and Recipe CRUD scenarios
---

## 🧪 Test Scope

### 🔐 Login Feature

* Login with valid credentials
* Login with invalid credentials
* Validation error messages

### 🍳 Recipe Management (CRUD)

* Create a new recipe
* View recipe details
* Update existing recipe
* Delete recipe

---

## ▶️ How to Run Tests Locally

### 1️⃣ Prerequisites

* Java JDK 21
* Maven installed
* Chrome browser

### 2️⃣ Run via Maven

```bash
mvn clean test
```

---

## 📊 Allure Report

### Generate report

```bash
allure serve target/allure-results
```

The report provides:

* Test execution status
* Steps and screenshots
* Execution timeline

---

## 🔄 Jenkins CI Integration

The project is integrated with **Jenkins CI** to automatically:

* Pull source code from GitHub
* Execute automation tests using Maven
* Generate Allure test reports

This setup supports **Continuous Integration (CI)** by ensuring tests are executed automatically on each build.

---

## 🚀 Future Improvements

* Add API automation tests
* Integrate with deployment pipeline (CI/CD)
* Cross-browser testing
* Docker support

---

## 👤 Author

**Han Nguyen Ngoc**

---

