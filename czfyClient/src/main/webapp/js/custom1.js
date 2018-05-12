var menuState = 1;
var left_width = $('.leftSide').css('width');
$(document).ready(function(){

    // Normal Slider Starts
    $(".slider").nivoSlider({
        effect:"random",
        slices:15,
        boxCols:8,
        boxRows:4,
        animSpeed:500,
        pauseTime:3000,
        startSlide:0,
        directionNav:true,
        controlNav:false,
        controlNavThumbs:false,
        pauseOnHover:true,
        manualAdvance:false,
        prevText:'',
        nextText: ''
    });
    // Normal Slider Ends


     //导航栏的移入效果
        $('nav > ul >li').hover(function(){
            $(this).find('.bg-color').stop().animate({'left':'0'},200);
            $(this).find('.list_icon').show();

        },function(){
            $(this).find('.bg-color').stop().animate({'left':left_width},200);
            $(this).find('.list_icon').hide();
        });

    // Metro Slider Starts
    var metroSlider = 0;
    $('.metro-slider .col').each(function(){
        metroSlider = metroSlider + 670;
    });
    $('.metro-slider').css('width',metroSlider+'px');

    $('.metro-wrapper').tinyscrollbar({
        axis: 'x',
        size: 'auto'
    });
    // Metro Slider Ends


    // Side Menu Opener Starts
    $('#opener').click(function(){

        var subLevel = $('#subLevel');
        var activeMenu = $('.active-menu-item');

        if(subLevel.hasClass('active-sub-level')){

            if(activeMenu.length > 0){

                activeMenu.removeClass('active-menu-item background-color');
                subLevel.children('ul').fadeOut('fast');

            }

            subLevel.removeClass('active-sub-level');
            $(this).children('div').removeClass('opener-minus').addClass('opener-plus');

        }else{

            subLevel.addClass('active-sub-level');
            $(this).children('div').removeClass('opener-plus').addClass('opener-minus');

        }

    });
    // Side Menu Opener Ends

    

    // Left Side Search Box Starts
    var bodyHeight = parseInt( $('.rightSide').css('height').replace('px','') );

    if(bodyHeight < 645){

        $('.search-and-misc').css('position','relative').css('margin-top','20px');

    }
    // Left Side Search Box Ends

});

// Tab Switcher Starts
function switchTabs(tab){

    var parent = tab.parent('.tab-links').parent('.tabs');

    parent.children('.tab-links').children('a.active').removeClass('active').addClass('not-active');
    tab.removeClass('not-active').addClass('active');

    parent.children('.contents').children('article').css('display','none');
    parent.children('.contents').children('article'+tab.attr('href')).css('display','block');

}
// Tab Switcher Ends

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

    widthCalc();

  

});

$(window).resize(function(){

    widthCalc();

    // Metro Slider Responsiveness Fix Starts
    $('.metro-wrapper').tinyscrollbar({
        axis: 'x',
        size: 'auto'
    });
    // Metro Slider Responsiveness Fix Ends

});

function widthCalc(){

//    if($('body').width() < 801){
//        $('.rightSide').css('width','100%').css('width',($('body').width()-10)+'px');
//    }else{
//        $('.rightSide').css('width','100%').css('width',$('body').width()+'px');
//    }
    $('.rightSide').css('width','100%').css('width',$('body').width()+'px');
    $('#menu > li > ul > li').css('width', ($('#menu > li').width()-20) +'px');
    $('#subLevel').css('height', '100%').css('height','-=5px');
    $('.lines').css('width', '100%').css('width','-=275px');
    $('.gray-box').css('width', '100%').css('width','-=40px');
    $('.gray-box-input').css('width', '100%').css('width','-=227px');
    $('.caroufredsel_wrapper').css('width', '77%').css('width','-=51px');
    $('.footer').css('width', '100%').css('width','+=10px');
    $('.accordion-title').css('width', '100%').css('width','-=40px');
    $('.sideway-description').css('width', '100%').css('width','-=260px');
    $('.random-blog-item').css('width', '100%').css('width','-=20px');
    $('.random-blog-title').css('width', '100%').css('width','-=60px');
    $('.random-blog-descr').css('width', '100%').css('width','-=60px');
    $('.comments').css('width', '100%').css('width','-=20px');
    $('.name-and-message').css('width', '100%').css('width','-=50px');
    $('.sub-comment').css('width', '100%').css('width','-=20px');
    $('.sub-comment .name-and-message').css('width', '100%').css('width','-=65px');
    $('.tabs .contents .article').css('width', '100%').css('width','-=30px');
    $('.message-box').css('width', '100%').css('width','-=5px');
    $('.message-text').css('width', '100%').css('width','-=80px');
    $('.portfolio-title').css('width', '90%');
    $('.portfolio-subtitle').css('width', '90%');
    $('.metro-misc').css('width', '100%').css('width','-=71px');

}

function closeThis(menu){
    menu.slideUp();
}

// Back to top Starts
$(function() {
    $(window).scroll(function() {
        if($(this).scrollTop() != 0) {
            $('#toTop').fadeIn('slow');
        } else {
            $('#toTop').fadeOut('slow');
        }
    });

    $('#toTop').click(function() {
        $('body,html').animate({scrollTop:0},800);
    });
});
// Back to top Ends