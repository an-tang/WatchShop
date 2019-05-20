$(document).ready(function () {

    var productCarousel = $('.product-carousel'),
        brandObj = $('a.brand-link'),
        nameObj = $('h3.product-name'),
        priceObj = $('p.price'),
        available = $('span.available'),
        quantity = $('.product-quantity'),
        quantityUp = $('button.quantity-up'),
        quantityDown = $('button.quantity-down'),
        addToCartBtn = $('button.add-to-cart'),
        table = $('table#details'),
        commentContainer = $('.comment-container'),
        postCommentBtn = $('button.post-comment'),
        cartBadge = $('#cart-count-badge');

    var id = parseInt(valib.getValueFromURL('id'));

    function initStickyNavbar() {
        $('.section-product-overview').waypoint({
            handler: function (direction) {
                if (direction === 'down') {
                    $('.navigation').addClass('stick');
                } else {
                    $('.navigation').removeClass('stick');
                }
            },
            offset: -1
        });
    }

    function initComponents() {
        // Initialize product carousel
        productCarousel.carousel({
            interval: 0
        });

        // Initialize Add to cart button
        addToCartBtn.click(function () {
            valib.ajaxGET('/rest/users/isLoggedIn', function (obj) {
                var isLoggedIn = Boolean(obj);

                if (isLoggedIn) {
                    var data = {
                        idProduct: id,
                        amount: parseInt(quantity.text())
                    };

                    valib.ajaxPOST({
                        url: '/rest/cart',
                        data: data,
                        onStateChange: function (response) {
                            // Show user that the product has been put into their cart
                            valib.ajaxGET('/rest/cart', function (obj) {
                                var count = 0;

                                // Get cart count (total items) and all products
                                obj.forEach(item => {
                                    count += item.amount;
                                });
                                cartBadge.text(count.toString());
                            });
                        }
                    });
                } else {
                    // Notify the user that he or she is not logged in

                }
            });
        });
    }

    function initQuantity() {

        function toString(num) {
            var result = num < 10 ? '0' + num.toString() : num.toString();
            return result;
        }

        // Initialize quantity section
        quantity.text(toString(1));
        quantityDown.attr('disabled', 'disabled');

        // Set click handlers for buttons
        quantityUp.click(function () {
            var currentQty, newQty;

            currentQty = parseInt(quantity.text());
            newQty = currentQty + 1;

            quantity.text(toString(newQty));
            quantityDown.removeAttr('disabled');
        });

        quantityDown.click(function () {
            var currentQty, newQty;

            currentQty = parseInt(quantity.text());
            newQty = currentQty - 1;

            if (newQty > 0) {
                quantity.text(toString(newQty));
                if (newQty === 1) {
                    $(this).attr('disabled', 'disabled');
                }
            }
        });
    }

    function getData() {

        valib.ajaxGET('/rest/products/details/' + id, function (obj) {
            console.log(obj);
            var brand = obj.product.firm.name;

            obj.images.forEach(image => {
                // productCarousel;
            });

            brandObj.text(brand).attr('href', `${brand.toLowerCase()}-watches`);
            nameObj.text(obj.product.codeName);
            priceObj.text(obj.product.price.toLocaleString() + 'đ');
            available.text(obj.product.available);


            table.html(`
            <tr>
                <td class="spec-title">Bảo hành/Bảo hiểm</td>
                <td class="spec-content">${obj.insurance}</td>
            </tr>
            <tr>
                <td class="spec-title">Đổi trả</td>
                <td class="spec-content">30 ngày</td>
            </tr>
            <tr>
                <td class="spec-title">Giao hàng</td>
                <td class="spec-content">Miễn phí toàn quốc</td>
            </tr>
            <tr>
                <td class="spec-title">Nhãn hiệu</td>
                <td class="spec-content">${brand}</td>
            </tr>
            <tr>
                <td class="spec-title">Nguồn gốc</td>
                <td class="spec-content">${obj.origin.name}</td>
            </tr>
            <tr>
                <td class="spec-title">Kiểu máy</td>
                <td class="spec-content">${obj.model.name}</td>
            </tr>
            <tr>
                <td class="spec-title">Đồng hồ dành cho</td>
                <td class="spec-content">Nam/Nữ</td>
            </tr>
            <tr>
                <td class="spec-title">Kích cỡ</td>
                <td class="spec-content">${obj.size}</td>
            </tr>
            <tr>
                <td class="spec-title">Chất liệu</td>
                <td class="spec-content">Vỏ: ${obj.caseMaterial}; Dây: ${obj.chainMaterial}; Kính: ${obj.glassMaterial}</td>
            </tr>
            <tr>
                <td class="spec-title">Khả năng chịu nước</td>
                <td class="spec-content">${obj.waterResistance}</td>
            </tr>
            `);
        });
    }

    function initComments() {

        function getComments() {
            valib.ajaxGET('/rest/comments/productDetail/' + id, function (obj) {
                if (obj.length > 0) {
                    var html = '';

                    // Process retrieved data into HTML

                    commentContainer.html(html);
                } else {

                }
            });
        }

        function postComment() {
            valib.ajaxGET('/rest/users/isLoggedIn', function (obj) {
                var isLoggedIn = Boolean(obj);
                if (isLoggedIn) {
                    // 1. Make comment object from Comment Form

                    // 2. Post this object to server
                    valib.ajaxPOST({
                        url: '/url where you want to submit your data',
                        data: 'data you want to submit to server',
                        onStateChange: function (response) {
                            // 3. Do something when the request is successful
                            // e.g Refresh the comments to see the new comment
                            getComments();
                        }
                    });
                }
            });
        }

        getComments();
        postCommentBtn.click(postComment);
    }

    initStickyNavbar();
    initComponents();
    initQuantity();
    getData();
    // initComments();
});