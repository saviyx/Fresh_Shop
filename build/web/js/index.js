async function homedta() {



    const respone = await fetch(
            "ShopData",
            );
    if (respone.ok) {
        const json = await respone.json();
        categoryList = json.categoryList;
        const productList = json.productList;
        loadproduct("products", productList, "name");
    }

}

function loadproduct(idname, list, property) {



    let homediv2 = document.getElementById("home2");
    let htmlContent1 = '';






    list.forEach(item => {



        htmlContent1 += `<div class="col-sm-6 col-md-6 col-lg-4 col-xl-4" >
                            <div class="products-single fix">
                                                    <div class="box-img-hover">
                                                        <div class="type-lb">
                                                            <p class="sale">Sale</p>
                                                        </div>
                                                        <img class="pvi" src="product-images/${item.id}/${item.id}image1.png" alt="Image">
                                                        <div class="mask-icon">
                                                            <ul>
                                                                <li><a href="single-product.html?id=${item.id}" data-toggle="tooltip" data-placement="right" title="View"><i class="fas fa-eye"></i></a></li>
                                                                <li><a href="#" data-toggle="tooltip" data-placement="right" title="Compare"><i class="fas fa-sync-alt"></i></a></li>
                                                                <li><a href="#" data-toggle="tooltip" data-placement="right" title="Add to Wishlist"><i class="far fa-heart"></i></a></li>
                                                            </ul>
                                                            <a class="cart"  onclick="addToCart(${item.id}, 1);">Add to Cart</a>
                                                        </div>
                                                    </div>
                                                    <div class="why-text">
                                                        <h4>${item.title}</h4>
                                                        <h5>${"Rs. " + item.price + ".00"}</h5>
                                                    </div>
                                                </div>
                                        </div>`;

    });

    homediv2.innerHTML = htmlContent1;


}