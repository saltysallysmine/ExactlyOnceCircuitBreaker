# Exactly one delivery guarantee with Circuit Breaker.

Подробный запуск тестов описан в файле ProducerTest. Каждый тест стоит запускать отдельно, предварительно перезапустив ConsumerMain. Он не был обёрнут в Mock, так как для проверки важны логи, которые он оставляет.

> Для лучшего понимания того, что происходит в обоих микросервисах, их нужно запускать раздельно. Перезапускайте Consumer и выполняйте следующий тест. Проверяйте в логи обоих приложений.

## Consumer

Методы Consumer, которые будут использованы.

- **Accept action.** Принимает все входящие запросы и выполняет их, если это не дубли. Возвращает всем запросам статус их выполнения.
- **Deny action.** Принимает все входящие запросы, не выполняет их. Возвращает всем запросам статус их выполнения (GATEWAY_TIMEOUT).
- **Accept action randomly.** Принимает все входящие запросы.
    - Выполняет первый запрос, если это не дубль, но не возвращает статус.
    - Выполняет второй запрос, если это не дубль, не возвращает статус.
    - Не выполняет третий запрос, возвращает статус.


## SendRequestTest

Самый простой тест, проверяющий лишь то, что Producer и Consumer могут общаться друг с другом. Проверяет работу accept-action и deny-action.

## TimeoutTest

Проверяет корректную работу Timeout. Отправляет самовоспроизводящиеся запросы на accept-action-randomly. *Первый запрос* выполняется Consumer, но ответ на него не возвращается в Producer. Поэтому тот думает, что запрос не выполнен и *повторяет его*. Consumer видит, что это дубль и не выполняет. Не отвечает. Producer *повторяет запрос*, Consumer видит, что это дубль, не выполняет, но отвечает, что запрос уже выполнен.


## Circuit Breaker Test

Проверяет работу Circuit Breaker. Отправляет 10 запросов в deny-action. Собрав десять ошибок Circuit Breaker закрывается на 10 секунд и не пропускает следующие пять запросов. Далее тест выжиадает открытия Circuit Breaker и отправляет ещё пять запросов.

---

Выполнил Бергман Валерий.