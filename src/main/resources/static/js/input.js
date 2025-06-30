let inputs = document.querySelectorAll("  .modal-body > form > input.form-control");
Object.values(inputs)
    .forEach(i => {
        let parent = i.parentElement;
        let priceApiece = parseFloat(parent.querySelector("span[data-price]").textContent);
        let subtotal = parent.querySelector("span[data-subtotal]");
        i.oninput = (e) => {
            subtotal.textContent = (i.value * priceApiece).toFixed(2);
        }
    });
