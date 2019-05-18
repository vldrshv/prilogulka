**CONTENT**
------------
* [Задачи](./README.md#Задачи)
	- [Видео](./README.md#Видео)
	- [Гифткарты](./README.md#Гифткарты)
	- [Пользователь](./README.md#Пользователь)
* [Описание пакетов](./README.md#Описание_пакетов)
* [API](./README.md#API)
	- [Приложение (контекст)](./README.md#Приложение (контекст))
	- [Пользователь](./README.md#Пользователь)
	- [Видео](./README.md#Видео)
	- [Гифткарты](./README.md#Гифткарты)
	- [Действия пользователя](./README.md#Действия_пользователя)

# PRILOGULKA
- [X] сделано
- [ ] не сделано

# Задачи
## Видео
- [X] запрос в соответствии с анкетой
- [x] локальное бд видосов
	- [x] добавление видео
	- [x] обновление датасета в соответствии с сервером
- [X] сохранение видосов в бд (работает, нужно думать про синхронизацию при входе 
	или заполнять бд видосами на день) (п.с я сохраняю, но пока номинально,
	т.е. обновляя просто при каждом входе)
- [X] дата синхронизации с сервером (если делаем заполнение видосами на день)
	* _загружаем данные в локальный стор, когда он обнуляется, закачиваем новые_
- [X] кол-во воспроизведений всего
- [X] кол-во воспроизведений подряд
- [X] удаление просмотренного видоса из бд
- [x] добавление просмотренного видоса в локальную бд (+ начисление денег)
- [ ] отправка на сервер статистики просмотра
- [x] бд действий пользователя
	- [x] само бд на просмотр и покупку карт
	- [x] вставка действий
	- [x] считаем локально деньги

## Гифткарты
- [x] бд гифткарт
- [x] покупка гифткарты
- [ ] использование гифткарты
	- [ ] удаление ее из бд купленных карт
- [ ] проверка актуальности (дата) на использование
	- [ ] удаление, если истек срок
- [ ] проверка актуальности (дата) на хранение
	- [ ] удаление, если истек срок
- [ ] дата синхронизации с сервером (или качаем каждый раз при входе)
- [ ] отправка статистики на сервер (что купил, сколько заплатил, кто купил)
- [x] генерация QR кода
- [x] бд купленных гифткарт (локальная)
	- [x] добавление
	- [ ] удаление

## Пользователь
- [ ] загружать купленные гифткарты
- [ ] загружать заработанные деньги
# Описание пакетов
## data
### android.interraction
#### DatePicker.java
Описывает класс по взаимодействию с выбором даты на пользовательском экране. Возвращает дату в отформатированном виде.
#### HintDialogs.java
Описывает класс по взаимодействию с диалоговым окном. Вызывается при покупке гифт-карты.
### managers
#### CoefficientManager.java
Менеджер коэффициентов.
* `public double getAgeCoefficient(String date)`
Выдает возрастной коэффициент соглассно вводимой дате в формате "dd.mm.yyyy"
* `public double getAgeCoefficient(int age)`
Выдает возрастной коэффициент соглассно вводимому возрасту
* `private void parseCoefs()`
Парсит коэффициенты в список коэффициентов
* `private void parseCoefsFromJson(String s) throws JSONException`
Парсит коэффициенты в список коэффициентов из строки JSON
* `private int parseStringDateToAge(String date) throws ParseException`
Парсит строку в формате "dd.mm.yyyy" в число (возраст)
* `private boolean contains(int age, ArrayList<Coefficient.Category> list)`
Проверка на то, что возраст содержится в листе возрастных категорий
---
#### GeofenceManager.java
#### SharedPreferencesManager.java
### service
#### GiftCardService.kt
#### QuestionnaireService.k
#### UserService.kt
#### VideoService.kt
### userData
#### Auth.kt
#### Questionnaire.kt
#### QuestionnaireInfo.kt
#### SerializeObject.java
#### User.kt
#### UserInfo.kt
### Coefficient.kt
### Districts.java
### GiftCard.kt
### Time.java
### Video.kt
### VideoAction.kt
## data_base
## facetracker
## login_signin
## menu
## recycle_view_adapters

# API
---
## Приложение (контекст)
- Пользователь
	1. `GET /api/v1/user?email={email}`
	Ответ:
	``` JSON
	{
		"user":{
			"id":8,
			"email":"mail@gmail.com",
			"created_at":"2019-04-27T17:18:03.921Z",
			"updated_at":"2019-05-11T14:41:42.904Z",
			"name":"Name",
			"lastname":"Lastname",
			"location":"location",
			"birthday":"dd.mm.yyyy",
			"sex":0,
			"pin":12345,
			"emailChecked":false,
			"confirm_token":"KK6nBfeZ45FSnh0ioRUrLw",
			"lastDateOnline":null,
			"current_video_coeff":1.20000004768372,
			"current_balance":0.0,
			"location_coeff":6,
			"user_coeff":113
		}
	} 
	```
	2. `POST /api/v1/users + Body(user)`
	``` JSON
	@Body
	{
		"user":{
			"email":"mail@gmail.com",
			"name":"Name",
			"lastname":"Lastname",
			"location":"location",
			"birthday":"dd.mm.yyyy",
			"sex":0,
			"current_video_coeff":1.20000004768372,
			"current_balance":0.0,
			"location_coeff":6,
			"user_coeff":0
		}
	} 
	```
	3. `PATCH /api/v1/users/{id} + Body(user)`
	``` JSON
	@Body
	{
		"user":{
			"id": 1,
			"user_coeff": 1
		}
	} 
	```
	Ответ:
	``` JSON
	{
		"user":{
			"email":"mail@gmail.com",
			"name":"Name",
			"lastname":"Lastname",
			"location":"location",
			"birthday":"dd.mm.yyyy",
			"sex":0,
			"current_video_coeff":1.20000004768372,
			"current_balance":0.0,
			"location_coeff":6,
			**"user_coeff":1**
		}
	} 
	```
- Пользовательские покупки
	* Купленные гифткарты
	> !!!!
---
## Пользователь
- Данные пользователя
	* Контекст -> пользователь -> 1
- Баланс пользователя 
	* Контекст -> пользователь -> 1
- Анкета
	1. `GET` Контекст -> пользователь -> 1
	2. `POST /api/v1/questionnaires + Body(questionnaire)`
---
## Видео
- Все видео
	* `GET /api/v1/videos`
	Ответ:
	``` JSON
	[
		{
			"video":{
				"id":4,
				"b2b_client_id":3,
				"name":"death",
				"url":"death.mp4",
				"watch_counter":null,
				"ad_compaing_ids":null,
				"created_at":"2019-04-25T10:47:27.575Z",
				"updated_at":"2019-04-25T10:47:27.575Z",
				"price":null
			}
		},
		{
			"video":{
				"id":5,
				"b2b_client_id":3,
				"name":"demix",
				"url":"demix.mp4",
				"watch_counter":null,
				"ad_compaing_ids":null,
				"created_at":"2019-04-25T10:47:42.251Z",
				"updated_at":"2019-04-25T10:47:42.251Z",
				"price":null
			}
		}, ...
	]
	```
- Видео по анкете
	* `GET /api/v1/videos/videos_pool?user_id={id}`
	Ответ:
	``` JSON
	{
		"videos":[
			{
				"id": 5,
				"name": "demix.mp4",
				"watch_count": 1
			}, 
			{
				"id": 13,
				"name": "whiskas.mp4",
				"watch_count": 3
			}
		]
	}
	```
---
## Гифткарты
- Все гифткарты
	* `GET /api/v1/gift_cards`
	Ответ:
	``` JSON
	[
		{
			"giftcard":{
				"id":1,
				"ad_compaing_id":1,
				"due_date":"2019-04-21T00:00:00.000Z",
				"image_url":"7580623554058001.jpg",
				"brand":"",
				"vendor":"","
				description":"",
				"price":[100],
				"created_at":"2019-04-21T16:31:07.276Z",
				"updated_at":"2019-04-21T16:31:07.276Z",
				"quantity":null
			}
		}, {
			"giftcard": {
				"id":2,
				"ad_compaing_id":1,
				"due_date":"2019-04-21T00:00:00.000Z",
				"image_url":"11410.jpg",
				"brand":"ебобо",
				"vendor":"ебобоша",
				"description":"тестовая",
				"price":["100","200","300"],
				"created_at":"2019-04-21T16:54:28.432Z",
				"updated_at":"2019-04-21T16:54:28.432Z",
				"quantity":null
			}
		}, ...
	]
	```
- Купленные гифткарты
> !!!!!
---
## Действия пользователя
- Регистрация
	* Приложение -> Пользователь (2)
- Вход
	* Приложение -> Пользователь (1)
- Ответ на анкету
	* Пользователь -> Анкета (2)
- Получение гифткарт
	* Всех (Гифткарты -> Все гифткарты)
	* Конкретной
	`GET /api/v1/gift_cards/{card_id}`
	Ответ:
	``` JSON
	{
		"giftcard": {
			"id":2,
			"ad_compaing_id":1,
			"due_date":"2019-04-21T00:00:00.000Z",
			"image_url":"11410.jpg",
			"brand":"ебобо",
			"vendor":"ебобоша",
			"description":"тестовая",
			"price":["100","200","300"],
			"created_at":"2019-04-21T16:54:28.432Z",
			"updated_at":"2019-04-21T16:54:28.432Z",
			"quantity":null
	}
	```
- Получение видео
	* Всех видео (Видео -> Все видео)
	* Видео по анкете (Видео -> Видео по анкете)
- Покупка гифткарты
	* `GET /api/v1/users_gift_cards/get_gift_card?user_id={user_id}&giftcard_id={card_id}&price={price}`
	Ответ:
	``` JSON
	{
		"users_gift_card":{
			"user_id":7,
			"id":1,
			"giftcard_id":24,
			"image_url":"11410.jpg", **ЭТОГО НЕТ!!!!**
			"price":"300",
			"serial_number":"12321432453",
			"day_bought":null,
			"is_activated":true, **НЕВЕРНО!!!**
			"created_at":"2019-05-12T14:06:59.613Z",
			"updated_at":"2019-05-12T14:10:38.464Z",
			"paid":true
		}
	}
	```
- Активация гифткарты
> !!!!!
