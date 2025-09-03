Task Tracker

Простой трекер задач на Java 

---

 Структура проекта

```
src/
└── TaskTracker/
    ├── Main.java                  // точка входа
    └── tracker/
        ├── model/                // Task, Epic, Subtask, Status
        └── controllers/          // интерфейсы и реализации менеджеров

test/
└── tracker/
    └── controllers/              // JUnit-тесты
```

---
 Основные возможности

-  Создание задач (`Task`)
-  Создание эпиков (`Epic`) и подзадач (`Subtask`)
-  Автоматический расчёт статуса эпика
-  История просмотров последних 10 задач
-  Разделение на интерфейсы (`TaskManager`, `HistoryManager`)
-  JUnit 5 тесты для ключевых компонентов

---

 Запуск

Проект можно запустить из `Main.java`.


---


