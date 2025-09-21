Task Tracker

Учебный проект — простой трекер задач на Java.
Реализован по ТЗ №6 и доработан в рамках ТЗ №9 (HTTP API).

📂 Структура проекта
src/
└── tasktracker/
    ├── Main.java              // точка входа (запуск сервера)
    ├── http/                  // HTTP-слой (сервер и обработчики)
    │   ├── HttpTaskServer.java
    │   ├── BaseHttpHandler.java
    │   ├── TasksHandler.java
    │   ├── SubtasksHandler.java
    │   ├── EpicsHandler.java
    │   ├── HistoryHandler.java
    │   └── PrioritizedHandler.java
    └── tracker/
        ├── model/             // модели: Task, Epic, Subtask, Status
        └── controllers/       // TaskManager, HistoryManager и их реализации

test/
└── tracker/
    └── controllers/           // JUnit 5 тесты

Реализованные возможности

Создание и хранение:

обычных задач (Task)

эпиков (Epic)

подзадач (Subtask)

Автоматическое определение статуса эпика:

NEW — если все подзадачи новые

DONE — если все подзадачи завершены

IN_PROGRESS — если подзадачи в разных статусах

Интерфейсы для менеджеров:

TaskManager — управление задачами, эпиками и подзадачами

HistoryManager — управление историей просмотров

Реализации в памяти:

InMemoryTaskManager

InMemoryHistoryManager

История просмотров задач:

хранит порядок открытия задач

дубликаты не сохраняются

при удалении задачи из менеджера она автоматически удаляется и из истории

Удаление задач и синхронизация с историей

Класс-утилита Managers для получения стандартных реализаций

HTTP API (спринт 9):

сервер HttpTaskServer на порту 8080

эндпоинты:

GET/POST/DELETE /tasks

GET/POST/DELETE /subtasks

GET/POST/DELETE /epics

GET /epics/{id}/subtasks

GET /history

GET /prioritized

JUnit 5 тесты для ключевых компонентов

Запуск проекта

Сервер запускается из класса HttpTaskServer или Main.java:

javac -cp src -d out $(find src -name "*.java")
java -cp out tasktracker.Main


После запуска сервер доступен по адресу:

http://localhost:8080/


Примеры запросов:

Получить все задачи:

curl -X GET http://localhost:8080/tasks


Добавить задачу:

curl -X POST http://localhost:8080/tasks \
     -H "Content-Type: application/json" \
     -d '{"name":"Test","description":"Demo task"}'


Получить историю просмотров:

curl -X GET http://localhost:8080/history