// @unit-test:success
// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
Процедура ПроверитьРавенствоСтрокиНаИстину(Фреймворк) Экспорт
	ЭталоннаяСтрока		= "Эталон";
	ПроверяемаяСтрока	= "Эталон";
	
	Фреймворк.ПроверитьРавенство(ЭталоннаяСтрока, ПроверяемаяСтрока,
		"Строки равны.");
КонецПроцедуры

// @unit-test:success
// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
Процедура ПроверитьНеРавенствоСтрокиНаИстину(Фреймворк) Экспорт
	ЭталоннаяСтрока		= "Эталон";
	ПроверяемаяСтрока	= "Результат";
	
	Фреймворк.ПроверитьНеРавенство(ЭталоннаяСтрока, ПроверяемаяСтрока,
		"Строки неравны.");
КонецПроцедуры

// @unit-test:success
// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
Процедура ПроверитьВхождениеСтрокиНаИстина(Фреймворк) Экспорт
	ПолнаяСтрока	= "ФреймворкТестирования";
	ПодстрокаПоиска	= "Тест";
	
	Фреймворк.ПроверитьВхождение(ПолнаяСтрока, ПодстрокаПоиска,
		"Подстрока входит в строку.");
КонецПроцедуры
