let totalProductList;
let totalProductCount;

async function loadfilteringdata() {

    const respone = await fetch(
            "LoadAdvanceSearchFilters"
            );
    if (respone.ok) {

        const json = await respone.json();
        const categoryList = json.categoryList;
        const conditionList = json.conditionList;
        const productList = json.productList;

        console.log(categoryList);
        console.log(productList);
        console.log(json.productCount);

        loadSelect("categorySelect", categoryList);
        totalProductList = productList;
        totalProductCount = json.productCount;
        pagesCount = Math.ceil(totalProductCount / productsPerPageNo);

        loadProduct();

    }
}

function loadSelect(selectTagId, list) {
    const selectTag = document.getElementById(selectTagId);

    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item.name;
        selectTag.appendChild(optionTag);
    });

}

function resetFilters() {
    //reset slider
    $("#slider-range").slider("option", "values", [0, 500000]);
    $("#amount").val("Rs0 - Rs500000");
    document.getElementById("categorySelect").value = 0;
    document.getElementById("subCategorySelect").value = 0;
    document.getElementById("SortBy").value = 0;
    document.getElementById("allRadio").checked = true;

    filterResults();

}

var paginationPageNumber = 1;
var productsPerPageNo = 2;

async function filterResults() {

    let categorySelect = document.getElementById("categorySelect");
    let sortBySelect = document.getElementById("sortSelect");

    let startValue = 0; //starting price slider
    let endValue = 3000; //ending price slider

    let data = {
        categoryId: categorySelect.value,
        startPrice: startValue,
        endValue: endValue,
        sortBy: sortBySelect.value,
    };

    let response = await fetch(
            "AdvanceSearch",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {

        let json = await response.json();
        console.log(json);
        if (json.success) {

            totalProductList = json.productList;
            totalProductCount = json.productCount;
            pagesCount = Math.ceil(totalProductCount / productsPerPageNo);
            loadProduct();

        } else {
            alert(json.content);
        }

    } else {
        alert("Error occured. Please try again later.");
    }

}

let productCardHtml = document.getElementById("productCard");

function loadProduct() {
    let productCardView = document.getElementById("productCardView");
    let htmlContent1 = ``;

    loadPagination();

    if (totalProductList) {

        let paginationCountHandle = 1;
        let firstResultNumber = ((paginationPageNumber - 1) * productsPerPageNo) + 1;
        let lastResultNumber = paginationPageNumber * productsPerPageNo;

        totalProductList.forEach(item => {

            if (paginationCountHandle >= firstResultNumber && paginationCountHandle <= lastResultNumber) {
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
            }
            paginationCountHandle++;

        });

        productCardView.innerHTML = htmlContent1;

    } else {
        alert("No products to show!");
    }

}

let paginationView = document.getElementById("paginationView");
let paginationPreviousBtn = document.getElementById("paginationPreviousBtn");
let paginationNumberBtn = document.getElementById("paginationNumberBtn");
let paginationNextBtn = document.getElementById("paginationNextBtn");
let pagesCount = 0;

function loadPagination() {

    paginationView.innerHTML = "";
    paginationView.appendChild(paginationPreviousBtn);

    for (let x = 1; x <= pagesCount; x++) {
        let paginationNumberBtnClone = paginationNumberBtn.cloneNode(true);
        paginationNumberBtnClone.innerHTML = `<a href="#">${x}</a>`;

        let currentLoop = x;

        if (x == paginationPageNumber) {
            paginationNumberBtnClone.classList.add("active");
        }

        paginationNumberBtnClone.addEventListener(
                "click",
                e => {
                    goToPage(currentLoop);
                }
        );
        paginationView.appendChild(paginationNumberBtnClone);
    }

    paginationView.appendChild(paginationNextBtn);

}

function goToPreviousPage() {
    if (paginationPageNumber > 1) {
        paginationPageNumber--;
        loadProduct();
    }
}

function goToNextPage() {
    if (paginationPageNumber < pagesCount) {
        paginationPageNumber++;
        loadProduct();
    }
}

function goToPage(pageno) {
    paginationPageNumber = pageno;
    loadProduct();
}