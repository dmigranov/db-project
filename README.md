# Computer firm
Этот репозиторий содержит программу для работы с базой данных компьютерной фирмы.
## Необходимые ссылки:
1. [Тех.задание](https://docs.google.com/document/d/1JG3gvsnaaMV6IHLN1vlIc-pOF0KQGwu5ElBx9AYlxEk/edit#heading=h.hrdrj6drhwfu)
2. [Этап 1, описание модели базы данных](https://docs.google.com/document/d/1BUxNiFNGl13Af2aUMZCWfH4oPwwLe8DiQMGPu0X5TXg/edit)
3. [Этап 2, описание хранимых функций, триггеров, запросов](https://docs.google.com/document/d/15lbmJ290gfnNCbu7r1mXm0CwARJfAiW5LDTZ4gc5d6Y/edit)

## Для запуска приложения:
0. Установите следующие компоненты:
  - Java версии 8 или выше;
  - Play Framework 1.5.x;
  - Oracle Database 18c XE;
1. Перейдите в папку с установленным Play Framework;
2. В консоли выполните команду:
> play new computerfirm (Windows);\
> ./play new computerfirm (Linux);
3. Клонируйте содержимое репозитория в созданную папку computerfirm;
4. В файле computerfirm/conf/application.conf установите необходимые логин и пароль к установленной базе Oracle
в полях соответственно;
> db.default.user;\
> db.default.pass;\
> db.default.driver=oracle.jdbc.OracleDriver\
> db.default.url=jdbc:oracle:thin:@localhost:1521/XE
5. В папке с установленным Play Framework в консоли выполните команду:
> play run computerfirm (Windows);\
> ./play run computerfirm (Linux);
6. В браузере в строке поиска наберите URL:
> localhost:9000
7. На данный момент доступные URL:
> / - главная страница с ссылками на другие
> /salary - зарплаты сотрудников\
> /projects?{id} - проекты, на которых задействован сотрудник с данным id, с возможностью выбора сотрудника в выпадающем списке\
> /details - список деталей на складе с возможностью добавления\
> /allprojects?{type, order} - список всех проектов (сортировка по сумме и типу)\
> /troubles - список неполадок, c количеством их происшествий
> /addproject - добавить проект (не закончено)
> /populardetails - популярные детали
> /troubles - список неполадок, c количеством их происшествий
8. Примеры страниц находятся в папке page-examples
### Этап 3: распределение работ
__Мигранов:__ каркас приложения, программирование моделей, запросы, html-страницы\
__Болдырев:__ html-страницы, запросы, README


28.04.19 - добавлен выбор работника, для которого выводятся проекты, в /projects, добавление детали в /details - Мигранов

05.05.19 - закончены популярные детали (добавлен выбор типа деталей из списка), управление работниками (удаление) - Мигранов
