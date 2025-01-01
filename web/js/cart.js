async  function loadCartItems() {

    const response = await fetch(
            "LoadCartItems",
            );

    if (response.ok) {

        const json = await response.json();
        console.log(json);

        if (json.length == 0) {

            swal("Opss!", "Cart is Empty", "error");


            

        } else {
            let cartItemContainer = document.getElementById("cart-item-container");
            let cartItemRow = document.getElementById("cart-item-row");
            cartItemContainer.innerHTML = "";

            let totalQty = 0;
            let total = 0;
            json.forEach(item => {

                let itemSubTotal = item.product.price * item.qty;
                totalQty += item.qty;
                total += itemSubTotal;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cart-item-a").href = "single-product.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#cart-item-image").src = "product-images/" + item.product.id + "/" + item.product.id + "image1.png";
                cartItemRowClone.querySelector("#cart-item-title").innerHTML = item.product.title;
                cartItemRowClone.querySelector("#cart-item-price").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }

                ).format(item.product.price);

                cartItemRowClone.querySelector("#cart-item-quantity").value = item.qty;
                cartItemRowClone.querySelector("#cart-item-subtotal").innerHTML = new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }

                ).format((itemSubTotal));
                cartItemRowClone.querySelector("#remove").addEventListener("click", (e) => {
                    removeItem(item.product.id);
                });
                cartItemContainer.appendChild(cartItemRowClone);
            });
            document.getElementById("cart-total-qty").innerHTML = totalQty;
            document.getElementById("cart-total").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }

            ).format((total));


        }
    } else {

        swal("Opss!", "unable to process your request", "error");


        }

}

async function removeItem(id) {

    const response = await fetch(
            `RemoveItems?id=${id}`
            );
    
    if (response.ok) {
        let data = await response.json();
        if (data.success) {
              swal("Deleted", "Successfuly removed item from cart", "success");
              window.location.reload();
        }else{
            swal("Opss!", data.content, "error");
        }
    }else{
        swal("Opss!", "unable to process your request", "error");
    }

}