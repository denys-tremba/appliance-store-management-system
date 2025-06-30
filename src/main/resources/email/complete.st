Dear [(${order.employee.name})]

We are happy to announce that [(${order.client.name})]'s order containing the following:
[# th:each="item : ${order.orderLineItems}"]
    - [(${item.appliance.name})] [(${item.appliance.model})], quantity [(${item.quantity})]
[/]
was successfully assigned to you.