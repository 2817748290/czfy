$(window).load(function(){

    $('body').css('visibility','visible');

    // OUR PARTNERS Carousel Starts
    if($(".partners-carousel-images").length > 0){

        $(".partners-carousel-images").carouFredSel({
            items: {visible : 6, width: 151, height : 100},
            direction: 'left',
            responsive: false,
            scroll: {
                items: 1,
                easing: 'elastic',
                duration: 1000,
                pauseOnHover: true
            },
            prev	: {
                button	: "#carousel-directioner-prev",  // The button which will trigger direction to left
                key		: "left"
            },
            next	: {
                button	: "#carousel-directioner-next",  // The button which will trigger direction to right
                key		: "right"
            }
        });

    }
    // OUR PARTNERS Carousel Ends

  

});





