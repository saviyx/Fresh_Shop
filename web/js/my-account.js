

async function loadFeatures() {



    const response = await fetch(
            "LoadFeatures",
            );

    if (response.ok) {
        const json = await response.json();

        const categoryList = json.categoryList;
      
        const weightList = json.weightList;
        

        loadSelect("categorySelect", categoryList, "name");
        loadSelect("weightSelect", weightList, "weight");


    } else {

         swal("Oops!","Loading Failed!", "error");
    }
}

function loadSelect(selectTagId, list, property) {

    const selectTag = document.getElementById(selectTagId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item.id;
        optionTag.innerHTML = item[property];
        selectTag.appendChild(optionTag);

    });

}



async function productListing() {

    const categorySelectTag = document.getElementById("categorySelect");
    const weightSelectTag = document.getElementById("weightSelect");
    const titleTag = document.getElementById("title");
    const descriptionTag = document.getElementById("description");
    const priceTag = document.getElementById("price");
    const quantityTag = document.getElementById("quantity");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");

    const data = new FormData();

    data.append("categoryId", categorySelectTag.value);
    data.append("weightId", weightSelectTag.value);
    data.append("title", titleTag.value);
    data.append("description", descriptionTag.value);
    data.append("price", priceTag.value);
    data.append("quantity", quantityTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);


    const response = await fetch(
            "ProductListing",
            {
                method: "POST",
                body: data
            }
    );


    if (response.ok) {
        const json = await response.json();
        const popup = Notification();
       const messageTag = document.getElementById("message");
        if (json.success) {

            categorySelectTag.value = 0;           
            weightSelectTag.value = 0;           
            titleTag.value = "";
            descriptionTag.value = "";
            priceTag.value = "";
            quantityTag.value = "1";
            image1Tag.value = null;
            image2Tag.value = null;
            image3Tag.value = null;


            swal("Done",json.content, "success");
            
        } else {
            swal("Oops!", json.content , "error");
            
        }
    } else {
        swal("Oops!","Please try again later!", "error");

        }
}

