# Fawary Task E-Commerce Project

## Developer

Mohab Ashraf Kamal Tawfik  
Email: a.mohab148@gmail.com

---

## Development Environment

- **Operating System:** Ubuntu (Linux)
- **IDE:** IntelliJ IDEA
- **Java SDK Version:** Java 21
- **Project SDK Module:** `Fawary_task_e-commerce`

---

## Project Overview

This project is an e-commerce application developed using **Test-Driven Development (TDD)** methodology. All features
are built incrementally with corresponding unit tests ensuring correctness and reliability.

---

## Project Structure

- **Main source files:**  
  `src/main/java`

- **Unit tests:**  
  `src/test/java`

---

## Running Tests

To run the tests, use the following Maven command:

```bash
mvn clean test
```
## Performance Results & Optimization

### Optimization Details

The internal product storage was changed from a list with nested loops to a `HashMap` keyed by product ID. This optimization  improved the efficiency of product lookup, addition, and removal operations by reducing time complexity from linear to near-constant time.

### Performance Results

#### Old Result
![Adding and removing products performance](https://i.ibb.co/VpgXG7Xq/image1.png)

#### New Result
![Adding and removing products performance](https://i.ibb.co/1fDVJxXV/image2.png)
