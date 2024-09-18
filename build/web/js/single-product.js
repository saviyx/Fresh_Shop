
async function loadProduct() {
//js walim url ekk get karana widiha

const parameters = new URLSearchParams(window.location.search);
        if (parameters.has("id")) {

const productId = parameters.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);
        if (response.ok) {

const json = await response.json();
        console.log(json.product);
        const id = json.product.id;
        document.getElementById("image1").src = "product-images/" + id + "/" + id + "image1.png";
        document.getElementById("image2").src = "product-images/" + id + "/" + id + "image2.png";
        document.getElementById("image3").src = "product-images/" + id + "/" + id + "image3.png";
        document.getElementById("image1-thumb").src = "product-images/" + id + "/" + id + "image1.png";
        document.getElementById("image2-thumb").src = "product-images/" + id + "/" + id + "image2.png";
        document.getElementById("image3-thumb").src = "product-images/" + id + "/" + id + "image3.png";
        document.getElementById("product-title").innerHTML = json.product.title;
        document.getElementById("product-published-on").innerHTML = json.product.date_time;
        document.getElementById("product-qty").innerHTML = json.product.qty;
        document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
        "en-US",
{
minimumFractionDigits: 2
}

).format(json.product.price);
        document.getElementById("product-weight").innerHTML = json.product.weight.weight;
        document.getElementById("product-description").innerHTML = json.product.description;
//            ***************
        document.getElementById("add-to-cart-main").addEventListener(
        "click",
        (e) => {
addToCart(
        json.product.id,
        document.getElementById("add-to-cart-qty").value
        );
        e.preventDefault();
});


        $('.recent-product-activation').slick({
infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        dots: false,
        prevArrow: '<button class="slide-arrow prev-arrow"><i class="fal fa-long-arrow-left"></i></button>',
        nextArrow: '<button class="slide-arrow next-arrow"><i class="fal fa-long-arrow-right"></i></button>',
        responsive: [{
        breakpoint: 1199,
                settings: {
                slidesToShow: 3,
                        slidesToScroll: 3
                }
        },
        {
        breakpoint: 991,
                settings: {
                slidesToShow: 2,
                        slidesToScroll: 2
                }
        },
        {
        breakpoint: 479,
                settings: {
                slidesToShow: 1,
                        slidesToScroll: 1
                }
        }
        ]
});
} else {
window.location = "index.html";
}

} else {
window.location = "index.html";
    }
}



async function addToCart(id, qty) {
// const popup = Notification();
    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty + "",
            );

    if (response.ok) {

        const json = await response.json();
        if(json.success){
            swal("Done",json.content, "success");

        }else{
            swal("Opps!",json.content, "error");

        }

    } else {
        //document.getElementById("message").innerHTML = "Please try again later!";
         swal("Opps!","unable to process your request", "error");

    }

}