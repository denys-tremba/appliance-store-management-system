Dear [(${order.client.name})]

We are happy to announce that your recent order containing the following:
[# th:each="item : ${order.orderRowSet}"]
    - [(${item.appliance.name})] [(${item.appliance.model})], quantity [(${item.number})]
[/]
was successfully approved by our employee [(${order.employee.name})].