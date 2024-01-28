
    document.addEventListener("DOMContentLoaded", function () {
    var imageUrls = [
    "https://skopskoeho.mk/wp-content/uploads/2019/04/skopsko-kale-2.jpg",
    "https://macedonia-timeless.com/wp-content/uploads/2018/09/zaliv-na-koskite.jpg",
    "https://www.bhfieldschool.org/uploaded/gallery/10_Synagogue%20Basilica_5716_2019_1-11293.jpg",
    "https://macedonia-timeless.com/wp-content/uploads/2018/09/sveti-naum-ohridski.jpg"
    ];

    var index = 0;

    function changeBackground() {
    document.getElementById("background-container").style.backgroundImage = "url('" + imageUrls[index] + "')";
    index = (index + 1) % imageUrls.length;
    }
    changeBackground();
    setInterval(changeBackground, 3000);
    });

