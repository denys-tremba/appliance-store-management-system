Dear [(${order.client.name})]

We regret to announce that your recent order containing the following:
[# th:each="item : ${order.orderRowSet}"]
    - [(${item.appliance.name})] [(${item.appliance.model})], quantity [(${item.number})]
[/]
was discarded by our employee [(${order.employee.name})].