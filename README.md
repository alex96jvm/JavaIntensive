# Intern Management API

## Описание

Intern Management API - это REST web-сервис, который предоставляет возможность управления стажерами, включая создание, получение, обновление и удаление стажеров и их оценок. API позволяет интегрировать управление стажерами в другие приложения и системы.

## Технологии

- Java / Servlet API
- Jackson
- MapStruct
- JDBC / PostgreSQL
- JUnit5 / Mockito / H2 Database (для тестирования)

## Эндпоинты

### 1. Получить всех стажеров

**`GET java-intensive/v1/interns`**

- **Ответ:**
    - `200 OK`: Список всех стажеров.

```json
[
    {
        "id": 1,
        "firstName": "Ivan",
        "lastName": "Ivanov",
        "marks": []
    },
    {
        "id": 2,
        "firstName": "Petr",
        "lastName": "Petrov",
        "marks": [
            {
                "subject": "Java Core",
                "mark": 5
            },
            {
                "subject": "SQL",
                "mark": 4
            }
        ]
    }
]
```

### 2. Получить стажера по ID

**`GET java-intensive/v1/interns/{id}`**

#### Ответы
- **200 OK**
  - **Описание:** Стажер успешно найден.
  - **Тело ответа:**
    ```json
    {
      "id": 1,
      "firstName": "Ivan",
      "lastName": "Ivanov",
      "marks": []
    }
    ```
- **404 Not Found**
  - **Описание:** Стажер с указанным ID не найден.
  - **Тело ответа:**
    ```json
    {
      "message": "Intern not found"
    }
    ```

### 3. Добавить нового стажера

**`POST java-intensive/v1/interns`**

#### Запрос
- **Content-Type:** `application/json`
- **Тело запроса:**
    ```json
    {
      "firstName": "Ivan",
      "lastName": "Ivanov"
    }
    ```

#### Ответы
- **201 Created**
  - **Описание:** Стажер успешно создан.
  - **Тело ответа:**
    ```json
    {
      "id": 1,
      "firstName": "Ivan",
      "lastName": "Ivanov",
      "marks": []
    }
    ```
- **400 Bad Request**
  - **Описание:** Неверный запрос (ошибки валидации).
  - **Тело ответа:**
    ```json
    {
      "message": "Firstname can not be less than 2 and more than 30 symbols"
    }
    ```

### 4. Выставить оценку стажеру

**`POST java-intensive/v1/interns/marks`**
  
#### Запрос
- **Content-Type:** `application/json`
- **Тело запроса:**
    ```json
    {
      "subject": "Java Core",
      "mark": 5,
      "internId": 1
    }
    ```

#### Ответы
- **200 OK**
  - **Описание:** Оценки стажера успешно обновлены.
  - **Тело ответа:**
    ```json
    {
      "id": 1,
      "firstName": "Ivan",
      "lastName": "Ivanov",
      "marks": [
        {
          "subject": "Java Core",
          "mark": 5
        }
      ]
    }
    ```
- **400 Bad Request**
  - **Описание:** Неверный запрос (ошибки валидации).
  - **Тело ответа:**
    ```json
    {
      "message": "Mark must be in the range from 1 to 5"
    }
    ```

### Ошибки валидации

- **400 Bad Request**
  - **Сообщение:**
    - `"Invalid request"` — Запрос не может быть обработан (например, отсутствуют обязательные поля).
    - `"Firstname can not be less than 2 and more than 30 symbols"` — Имя стажера некорректно (длина менее 2 или более 30 символов).
    - `"Lastname can not be less than 2 and more than 30 symbols"` — Фамилия стажера некорректна (длина менее 2 или более 30 символов).
    - `"Mark must be in the range from 1 to 5"` — Оценка находится вне допустимого диапазона (не может быть меньше 1 или больше 5).
    - `"Subject can not be less than 2 and more than 30 symbols"` — Название предмета некорректно (длина менее 2 или более 30 символов).

### 5. Удалить стажера (каскадное удаление информации об оценках)

**`DELETE java-intensive/v1/interns/{id}`**

#### Ответы
- **204 No Content**
  - **Описание:** Стажер успешно удален.
- **404 Not Found**
  - **Описание:** Стажер с указанным ID не найден.
  - **Тело ответа:**
    ```json
    {
      "message": "Intern not found"
    }
    ```
- **400 Bad Request**
  - **Описание:** ID обязателен для удаления.
  - **Тело ответа:**
    ```json
    {
      "message": "ID is required for deletion"
    }
    ```
