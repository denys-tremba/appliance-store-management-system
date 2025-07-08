Dear [(${order.client.name})]

We regret to announce that your recent order containing the following:
[# th:each="item : ${order.orderLineItems}"]
    - [(${item.appliance.name})] [(${item.appliance.model})], quantity [(${item.quantity})]
[/]
was disapproved by our employee [(${order.employee.name})].