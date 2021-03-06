﻿# Computer firm
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
> /employees - управление сотрудниками (удаление)
8. Примеры страниц находятся в папке page-examples
### Этап 3: распределение работ
__Мигранов:__ каркас приложения, программирование моделей, запросы, html-страницы\
__Болдырев:__ html-страницы, запросы, README


### Этап 4: распределение работ:
__Мигранов:__
* В /projects можно теперь выбирать работника, для которого будут выводиться проекты, и даты, в промежутке между которых лежит дата начала работы над проектом
* Популярные детали с выбором типа деталей из списка
* Добавление (при попытке добавить плохое - уведомление) и удаление работников (в том числе можно удалять и тех, кто задействован на каких-либо проектах; информация о проектах сохранится, но о работниках - будет ожидаемо потеряна)
* Добавление (при попытке добавить что-то плохое - уведомление) и удаление деталей со склада (при попытке удалить деталь, использующуюся в каких=то проектах, будет выдано уведомление о невозможности этого)
* Добавление (при попытке добавить что-то плохое - уведомление) и удаление клиентов (в том числе можно удалять и тех, кто указан в каких-либо проектах; информация о проектах сохранится, но о клиентах - будет ожидаемо из них удалена)
* Начат интерфейс для добавления проектов.


Чтобы добавление заказов на детали работало корректно (т.е. чтобы при добавлении заказа детали изымались со склада и нельзя было добавить несуществующее число деталей) нужно вручную добавить в базу триггер:

    CREATE OR REPLACE TRIGGER detail_orders_insert_trigger BEFORE INSERT ON DetailOrder
    FOR EACH ROW
    DECLARE
    detail_count Detail.count%TYPE;
    not_enough_details EXCEPTION;
    BEGIN
    SELECT count INTO detail_count FROM Detail WHERE id = :new.detail_id;
    
    IF detail_count < :NEW.count THEN
        RAISE not_enough_details;
    END IF;
    
    UPDATE Detail SET Detail.count = detail_count - :NEW.count WHERE id = :new.detail_id;
    END;

И хранимую процедуру:

    create or replace PROCEDURE get_assembling(type IN VARCHAR2, result OUT NUMBER)
    IS

    wrong_assembling_type EXCEPTION;

    BEGIN

    IF type = 'max' THEN
    SELECT SUM(a) INTO result FROM (SELECT MAX(cost) AS a FROM Detail GROUP BY type_id);
    ELSIF type = 'min' THEN
    SELECT SUM(b) INTO result FROM (SELECT MIN(cost) AS b FROM Detail GROUP BY type_id);
    ELSE
    RAISE wrong_assembling_type;
    END IF;

    END get_assembling;

__Болдырев:__
* Запрос самых популярных клиентов;
* Хранимая процедура.
* clientProjects

