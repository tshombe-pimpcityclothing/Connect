import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Replace 'path/to/your/keyfile.json' with the actual path to your service account key file
cred = credentials.Certificate('path/to/your/keyfile.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

<script type="module" src="{{ 'cart.js' | asset_url }}" defer="defer"></script>
<link rel="preload stylesheet" href="{{ 'cart.css' | asset_url }}" as="style" >

<div class="cart-page color-{{ section.settings.color_scheme }} gradient">
  <div class="grid">
    <div class="grid__item">

	<form id="CartPageForm" action="{{ routes.cart_url }}" method="post" novalidate class="{% if cart.item_count == 0 %}is-empty{% endif %}">
      <header class="section-header text-center cart__page-empty" style="padding: 100px">
      <div class="text-spacing">
        <h2 class="section-header__title">{{ 'general.404.title' | t }}</h2>
      </div>
      <div class="rte">
        <p>{{ 'cart.general.empty' | t}}</p>
        <p class="rte">{{ 'cart.general.continue_browsing_html' | t: url: routes.all_products_collection_url }}</p>
      </div>       
    </header>
      
      {%- if cart.item_count > 0 -%}
      <div class="cart__page cart cart-page-form">
        <div class="cart__page-col">

          <form action="{{ routes.cart_url }}" method="post" novalidate class="">

            {% if additional_checkout_buttons and section.settings.cart_additional_buttons %}
              <div class="text-center" style="width:100%; padding-bottom: 5px"><span>{{ 'products.general.quick_shop' | t }}</span></div>
              <div class="additional-checkout-buttons additional-checkout-buttons--vertical" style="padding-bottom: 20px">{{ content_for_additional_checkout_buttons }}</div>
            <div style="width:100%; position:relative display: flex; padding-bottom: 22px">
              <div class="cart__page-separator" style="position: relative;display: flex;width: 100%;">
                <span style="padding-left:15px; padding-right:15px; text-transform: uppercase;">{{ 'cart.general.or' | t }}</span>
              </div>
            </div>
            {% endif %}

            
            <div data-cart-products data-locale="{{ request.locale.iso_code }}-{{ localization.country.iso_code }}" data-currency="{{ cart.currency.symbol }}">
              {%- for item in cart.items -%}
                {%- render 'cart-item', product: item, forloop: forloop -%}              
              {%- endfor -%}
            </div>

            {%- if section.settings.cart_terms_conditions_enable -%}
            <div class="ajaxcart__note ajaxcart__note--terms">
              <input type="checkbox" id="CartTermsPage" class="cart__terms-checkbox">
              <span for="CartPageAgree">
                {%- if section.settings.cart_terms_conditions_link != blank -%}
                {{ 'cart.general.terms_html' | t: url: section.settings.cart_terms_conditions_link }}
                {%- else -%}
                {{ 'cart.general.terms' | t }}
                {%- endif -%}
              </span>
            </div>
            {%- endif -%}

            <div class="cart__checkout-wrapper">
              <a href="{% if section.settings.button_link != blank %}{{ section.settings.button_link }}{% else %}{{routes.all_products_collection_url}}{% endif %}" type="button" class="btn cart__return">
                {% render 'icons-arrow-previous' class: 'btn-icon'%}{{ 'customer.login.cancel' | t }}
              </a>


              <button type="submit" name="update" id="CartPageUpdate" class="btn cart__update visually-hidden">
                {{ 'cart.general.update' | t }}
              </button>
              <button type="submit" name="checkout" {% if section.settings.cart_terms_conditions_enable %} data-terms="CartTermsPage"{% endif %} class="btn cart__checkout">
                {{ 'cart.general.checkout' | t }}
              </button>

            </div>
          </form>
        </div>
        <div class="cart__page-col" style="padding: 0; background: black">
          <div class="cart__page-col--container">
            <div class="grid" style="max-width: calc(373px + 5vw);">
              {% if section.settings.discount_text != blank %}
              <div class="grid__item medium-up--one-whole"  style="position:relative; padding-left: 30px;margin-bottom: 60px;">
              <div id="CartDiscount" class="discount-header">
                {{ section.settings.discount_text }}
              </div>
              </div>
            {% endif %}
          
              <div class="grid__item medium-up--one-whole">
                {%- if section.settings.cart_payment_icons_enable -%}
                <ul class="inline-list payment-icons {% unless section.settings.cart_colorize_payment_icons %} payment-icons--greyscale{% endunless %} payment_icons-alignment" style="margin-left:0px;">
                  {% if section.settings.cart_payment_cod_text %}
                  <li class="payment-icon {%if section.settings.enlarge_icons%}large-icon{%endif%}">
                    <div class="payment-icon--cod">
                      <span>{{ 'cart.general.cash' | t }}</span>
                    </div>
                  </li>
                  {% endif %}
                  {%- for type in shop.enabled_payment_types -%}
                  <li class="payment-icon {%if section.settings.enlarge_icons %}large-icon{%endif%}">
                    {{ type | payment_type_svg_tag }}
                  </li>
                  {%- endfor -%}
                </ul>
                {%- endif -%}
  
  
              </div>
  
              {%- if section.settings.cart_notes_enable -%}
              <div class="cart__page-note">              
                <div class="grid__item medium-up--one-whole" style="position:relative; padding-left: 10px; margin: 14px 0;">   
                  <div class="grid__item--textarea">
                    <textarea data-input-notes class="input-full cart-notes" id="CartNote" placeholder=" " value="{{ cart.note }}">{{ cart.note }}</textarea>
                    <label class="floating-label" for="CartNote">{{ 'cart.general.note' | t }}</label>
                  </div>
                </div>
              </div>
              {%- endif -%}
  
              {% if section.settings.gift_wrapping_product != blank%}
              {%- assign current_variant = section.settings.gift_wrapping_product.selected_or_first_available_variant -%}
              <form action="/cart/add" data-productid="{{ section.settings.gift_wrapping_product.id }}" id="AddToCartForm-{{current_variant.id}}" class="product-single__form" method="post" target="dummyframe">
                <div class="grid__item medium-up--one-whole cart__page-note--divider"></div>
                <div class="grid__item medium-up--one-whole medium-up--text-left">
                  <div class="ajaxcart__note ajaxcart__note--terms">
                    
                    <input type="hidden" name="id" data-productid="{{ section.settings.gift_wrapping_product.id }}" value="{{  current_variant.id }}" data-gift-variant/>
                    <input type="checkbox" id="CartGiftPage" data-gift-checkbox class="cart__gift-checkbox" value="{{  current_variant.id }}">
                    <span for="CartGiftPage">
                      {{ 'cart.general.gift_wrapping' | t }}
                    </span>
                  </div>
                </div>
              </form>
              {% endif %}
  
              
              
              <div class="grid__item medium-up--one-whole text-center medium-up--text-right">
                <div data-cart-discounts>
                  {%- if cart.cart_level_discount_applications != blank -%}
                  <div class="grid grid--full">
                    <div class="grid__item one-half medium-up--text-right">
                      <p class="h4">{{ 'cart.general.discounts' | t }}</p>
                    </div>
                    <div class="grid__item one-half">
                      {%- for cart_discount in cart.cart_level_discount_applications -%}
                      <p class="h4 cart__discount">
                        {{ cart_discount.title }} (-{{ cart_discount.total_allocated_amount | money }})
                      </p>
                      {%- endfor -%}
                    </div>
                  </div>
                  {%- endif -%}
                </div>
  
  
                {% if section.settings.discount_field %}
                <form class="">
                {% render "cart-discount", section_id: "CartPage" %}
                </form>
                {% endif %}
          
                <div class="cart__item-sub cart__item-row">
                  <div class="ajaxcart__subtotal">{{ 'cart.general.subtotal' | t }}</div>
                  <div class="cart__item-price" data-cart-total>{{ cart.original_total_price  | money }}</div>
                </div>
  
                
                <div class="{% unless cart.total_discounts > 0 %} hide{% endunless %} cart__item-sub cart__item-row">
                {%- assign cartTotalDiscounts = cart.total_discounts | money -%}              
                  <div class="ajaxcart__subtotal">{{ 'cart.general.savings_html' | t: savings: '' }}</div>
                  <div class="cart__item-price" data-savings>-{{ cartTotalDiscounts }}</div>
                </div>
  
                <div class="cart__item-sub cart__item-row ">
                  <div class="ajaxcart__subtotal cart__item-total">{{ 'customer.order.total' | t }}</div>
                  <div><span class="cart__item-currency-total">{{cart.currency.iso_code}}</span> <span class="cart__item-price-total" data-cart-subtotal>{{ cart.total_price | money }}</span></div>
                </div>
  
                {%- if section.settings.purchase_policy_enable -%}
                <div class="text-left product__policies rte">
                  <small>
                    {%- if cart.taxes_included -%}
                    {{ 'products.product.include_taxes' | t }}
                    {% else %}
                    {{ 'cart.general.shipping_at_checkout' | t }}
                    {%- endif -%}
                    {%- if shop.shipping_policy.body != blank -%}
                    {{ 'products.product.shipping_policy_html' | t: link: shop.shipping_policy.url }}
                    {%- endif -%}
                  </small>
                </div>
                {%- endif -%}
                {% assign collection = collections[section.settings.featured_collection_cart] %}          
                {% if collection != empty %} 
                <script type="module" src="{{ 'video.js' | asset_url }}" defer="defer"></script>
                <script type="module" src="{{ 'product-form.js' | asset_url }}" defer="defer"></script>
                <script type="module" src="{{ 'collection-form.js' | asset_url }}" defer="defer"></script>
                <div  class="place-upsell">
                  <div class="ajaxcart__subtotal" style="text-transform: none; padding-top: 20px">{{ section.settings.upsell_title | upcase }}</div>
                </div>
                <div data-section-id="cartdrawer"
                     data-section-type="upsell-slider"
                     data-basic="true"
                     data-aos>
                  {% comment %}
                  <div class="upsellproducts upsellproducts-cartdrawer">
                    {%- for product in collection.products -%}  
                    {% assign index = forloop.index0 %}
                    {% if product.selected_or_first_available_variant.available %}
                    {%- render 'upsell-grid-item', product: product, index: index -%} 
                    {% endif %}
                    {%- endfor -%}
                  </div> 
                  {% endcomment %}
                  {%- render 'collection-slider', section: section, per_row: 4, product_limit: collection.products.size, collection: collection, forloop: 0, show_content: false, quick_mode: "quick-buy", block: block, embed_title: false, show_content: "hidden" -%}
                </div>
                {% endif %} 
              </div>
              
              
              
            </div>
          </div>
        </div>
      </div>
      {%- endif -%}
      </form>
      
    </div>
  </div>
</div>


{% schema %}
{
  "name": "Cart Page",
  "settings": [
    {
      "type": "header",
      "content": "Discount"
    },
    {
      "type": "checkbox",
      "id": "discount_field",
      "label": "Show discount field",
      "default": true
    },
    {
      "type": "richtext",
      "id": "discount_text",
      "label": "Text",
      "default": "<p>Offer special discounts or once-in-a-lifetime deals.<\/p>"
    },
    {
      "type": "header",
      "content": "Upsell"
    },
    {
      "type": "text",
      "id": "upsell_title",
      "label": "Heading",
      "default": "You might also like"
    },
    {
      "type": "checkbox",
      "id": "upsell_price_enable",
      "label": "Show price",
      "default": true
    },
    {
      "type": "collection",
      "id": "featured_collection_cart",
      "label": "Collection"
    },
    {
      "type": "header",
      "content": "Terms and conditions"
    },
    {
      "type": "checkbox",
      "id": "cart_notes_enable",
      "label": "Enable order notes"
    },
    {
      "type": "checkbox",
      "id": "purchase_policy_enable",
      "label": "Enable purchasing policy"
    },
    {
      "type": "checkbox",
      "id": "cart_terms_conditions_enable",
      "label": "Enable terms and conditions checkbox",
      "default": true
    },
    {
      "type": "url",
      "id": "cart_terms_conditions_link",
      "label": "Terms and conditions page"
    },
    {
      "type": "url",
      "id": "button_link",
      "label": "Return button link"
    },
    {
      "type": "header",
      "content": "Payment options"
    },
    {
      "type": "checkbox",
      "id": "cart_additional_buttons",
      "label": "Enable additional checkout buttons",
      "info": "The buttons can appear on either your cart page or your checkout page, but not both.",
      "default": true
    },
    {
      "type": "checkbox",
      "id": "cart_payment_icons_enable",
      "label": "Enable payment icons"
    },
    {
      "type": "checkbox",
      "id": "cart_payment_cod_text",
      "label": "Enable cash on delivery",
      "default": false
    },
    {
      "type": "header",
      "content": "Gift wrapping"
    },
    {
      "type": "product",
      "id": "gift_wrapping_product",
      "label": "Product"
    },
    {
      "type": "header",
      "content": "Layout"
    },
    {
      "type": "checkbox",
      "id": "enlarge_icons",
      "label": "Enlarge icons",
      "default": false
    }
  ]
}
{% endschema %}
'{
@include prefix(transform, scale(1), ms webkit spec);
}

// Output:
.element {
-ms-transform: scale(1);
-webkit-transform: scale(1);
transform: scale(1);
}
==============================================================================*/
/*================ Media Query Mixin ================*/
/*================ Responsive Show/Hide Helper ================*/
/*================ Responsive Text Alignment Helper ================*/
/*============================================================================
Convert pixels (unitless) to ems
eg. for a relational value of 12px write calc-font-size(12) when the parent is 16px
if the parent is another value say 24px write calc-font-size(12, 24)
Based on https://github.com/thoughtbot/bourbon/blob/master/app/assets/stylesheets/functions/_px-to-em.scss
==============================================================================*/
/*============================================================================
Flexbox prefix mixins from Bourbon
https://github.com/thoughtbot/bourbon/blob/master/app/assets/stylesheets/css3/_flex-box.scss
==============================================================================*/
/*================ VARIABLES ================*/
/*============================================================================
Grid Breakpoints and Class Names
- Do not change the variable names
- Breakpoint pixel values are used in the window.theme.breakpoints object
==============================================================================*/
/*============================================================================
Generate breakpoint-specific column widths and push classes
- Default column widths: $grid-breakpoint-has-widths: ($small, $medium-up);
- Default is no push classes
==============================================================================*/
/*============================================================================
Flickity Slider

- If upgrading Flickity's styles, use the following variables/functions
==============================================================================*/
/*================ Flickity Slider SCSS ================*/
.flickity-enabled {
  position:relative;
}

.flickity-enabled:focus{ outline:none; }

.flickity-viewport {
  overflow:hidden;
  position:relative;
  transition:height 0.6s;
  height:100%;
}

.announcement-slider .flickity-viewport {
  transition: none;
}

.flickity-slider {
  position:absolute;
  width:100%;
  height:100%;
}

.flickity-enabled.is-draggable {
  -webkit-user-select:none;
  user-select:none;
}

.flickity-enabled.is-draggable .flickity-viewport {
  cursor:move;
  cursor:grab;
}

.flickity-enabled.is-draggable .flickity-viewport.is-pointer-down {
  cursor:grabbing;
}

.flickity-button {
  position:absolute;
  border:none;
  color: rgb(var(--color-button-text));
    background: rgba(var(--color-button),var(--alpha-button-background));

      border-radius:50%
      }

.upsellproducts .flickity-button {
  color: rgba(var(--color-button),var(--alpha-button-background));
  background: transparent
}
.hero .flickity-button{
  /*color:#2a2b2f;
  color:var(--colorTextBody);
    background-color:#ffffff;
    background-color:var(--colorBody);*/
      box-shadow:0 5px 5px rgba(0, 0, 0, 0.1)
      }

.product-slideshow .flickity-button {
  position:absolute;
  border:none;
  color:#2a2b2f;
  color:var(--colorTextBody);
background:rgb(var(--color-background));
    border-radius:50%
    }

.product-slideshow .flickity-button .flickity-button-icon {
    stroke: rgba(var(--color-foreground), 1)!important;
}

.flickity-button:hover{
  cursor:pointer;
  opacity:1;
}

/*.product-slideshow .flickity-button:hover {
  position:absolute;
  border:none;
  color:#2a2b2f;
  color:var(--colorTextBody);
    background-color:#ffffff;
    background-color:var(--colorBody);
      border-radius:50%
}

.product-slideshow .flickity-button:hover .flickity-button-icon {
    stroke: var(--colorTextBody)!important;
}*/

.product-slideshow .flickity-next {
  margin-right: 10px;
}

.product-slideshow .flickity-previous {
  margin-left: 10px;
}

.flickity-button:disabled{
  display:none;
  cursor:auto;
  pointer-events:none;
}

.flickity-prev-next-button{
  top:50%;
  width:40px;
  height:40px;
  transform:translateY(-50%)
}

@media only screen and (max-width:589px){

  .flickity-prev-next-button{
    width:33px;
    height:33px
  }
}

.flickity-prev-next-button:active{
  transform:translateY(-50%) scale(1);
  transition:transform 0.05s ease-out;
}

.flickity-previous{ left:0px; }
.flickity-next{ right:0px; }


.splitted-text-slider .flickity-previous{ left:-50px; }
.splitted-text-slider .flickity-next{ right:-50px; }
/*.splitted-text-slider .flickity-button{
  background: var(--colorBody);
  color: var(--colorTextBody);
}*/

.boxed-text .splitted-text-slider .flickity-previous{ left:-40px; }
.boxed-text .splitted-text-slider .flickity-next { right:-40px; }

@media only screen and (max-width:589px){
  .splitted-text-slider .flickity-previous{ left:0px; display: none}
  .splitted-text-slider .flickity-next{ right:0px; display: none}
  .splitted-text-slider .flickity-previous{ left:-15px; }
  .splitted-text-slider .flickity-next{ right:-15px; }
  
  .boxed-text .splitted-text-slider .flickity-previous{ left:-30px; }
  .boxed-text .splitted-text-slider .flickity-next { right:-30px; }

.feature-row__item .flickity-previous{ left:5px; }
.feature-row__item  .flickity-next{ right:5px; }
}

.upsellproducts .flickity-previous{ left:-40px; }
.upsellproducts .previous{ left:-40px; }
.upsellproducts .flickity-next{ right:-40px; }
.upsellproducts .next{ left:-40px; }
/*.upsellproducts .flickity-button {
  color:#2a2b2f;
  color:var(--colorTextBody);
  background-color:#ffffff;
  background-color:var(--colorDrawers);
}*/

.upsellproducts .flickity-button:hover {
  background: var(--colorBtnPrimaryHover);
  filter: brightness(var(--colorBtnFilterBrightness));
     color:var(--colorBody);
}
.upsellproducts.mobile-nav__link{
  padding-bottom: 10px;
}
.upsellproducts.mobile-nav__link:after{
  border-bottom: 0px;
}
.flickity-rtl .flickity-previous{
  left:auto;
  right:10px;
}

.flickity-rtl .flickity-next{
  right:auto;
  left:10px;
}

.flickity-button-icon{
  position:absolute;
  left:35%;
  top:35%;
  width:30%;
  height:30%;
  fill:currentColor;
  stroke-width:calc(var(--iconWeight)*3)!important;
    stroke:currentColor !important;
    stroke-linecap:var(--iconLinecaps)!important;
      stroke-linejoin:var(--iconLinecaps)!important;
        }

/*.product-slideshow .flickity-button-icon{
  position:absolute;
  left:22.5%;
  top:22.5%;
  width:55%;
  height:55%;
  fill:currentColor;

  stroke-width:calc(var(--iconWeight)*2)!important;
    stroke:currentColor !important;
    stroke-linecap:var(--iconLinecaps)!important;
      stroke-linejoin:var(--iconLinecaps)!important;
        }*/
.product-slideshow .flickity-button-icon{
  stroke:var(--colorBody)!important;
}


.hero .flickity-page-dots{
  bottom:20px;
  color:#fff
}

.flickity-rtl .flickity-page-dots{ direction:rtl; }

.flickity-enabled.is-fade .flickity-slider>*{
  pointer-events:none;
  z-index:0;
}

.flickity-enabled.is-fade .flickity-slider>.is-selected{
  pointer-events:auto;
  z-index:1;
}

.flickity-page-dots {
  position: absolute;
  width: 100%;
  left: 0;
  bottom: -25px;
  padding: 0;
  margin: 0;
  list-style: none;
  text-align: center;
  line-height: 1;
  color: currentColor;
}

@media only screen and (max-width:589px){
  .background-media-text__overlay .flickity-page-dots {
    position: unset
  }
}

.flickity-page-dots .dot {
  display: inline-block;
  width: var(--flickity-dots);
  height: var(--flickity-dots);
  margin: 0 5px;
  border-radius: 100%;
  cursor: pointer;
  vertical-align: middle;
  background-color: currentColor;
  opacity: 0.15;
}
.flickity-page-dots .dot.is-selected {
  opacity: 1;
  vertical-align: middle;
  background-color: currentColor;
  width: var(--flickity-dots-selected)!important;
  height: var(--flickity-dots-selected)!important;
}

[data-slider-bars=true].hero .flickity-page-dots{
    bottom:5px;
    height:26px;
  height: auto;
    line-height:var(--flickity-dots);
    z-index:2;
  }

@media only screen and (max-width:768px){
  [data-slider-bars=true].hero .flickity-page-dots{
    height:12px;
  }
}
[data-slider-bars=true] .flickity-page-dots .dot{
    position:relative;
    border-radius:var(--buttonRadius);
    /*width:120px!important;*/
  
      max-width: 20%!important;
    min-width: 10%!important;
    height:var(--flickity-dots)!important;
    border:0;
    opacity:1;
    vertical-align:top;
    background:none;
    overflow:hidden
  }

@media only screen and (max-width:589px){
  [data-slider-bars=true] .flickity-page-dots .dot{
    width:12%!important;
  }
}
@media only screen and (max-width:768px){

[data-slider-bars=true] .flickity-page-dots .dot{
      width:45px
  }
    }

[data-slider-bars=true] .flickity-page-dots .dot:after,[data-slider-bars=true] .flickity-page-dots .dot:before{
      content:"";
      display:block;
      position:absolute;
      left:0;
      height:100%;
      width:100%;
    }

[data-slider-bars=true] .flickity-page-dots .dot:before{
      height: var(--flickity-dots);
      opacity:0.1;
      background-color:#000;
    }

[data-slider-bars=true] .flickity-page-dots .dot:hover:before{
      opacity:0.2;
    }

[data-slider-bars=true] .flickity-page-dots .dot:after{
  top: 0;
  height: var(--flickity-dots);
      transform:translateX(-101%);
      transition:none;
    }

[data-slider-bars=true] .flickity-page-dots .dot.is-selected:after{
      animation:slideshowBars 0s linear forwards;
    }

    .flickity-page-dots .btn {
          height: 65.02px;
      margin-bottom: 10px!important;
    }

.slideshow-wrapper.slideshow__dots-wrapper {
  padding-bottom: 100px
}

.slideshow__dots-wrapper .flickity-page-dots {
  bottom: -70px;
  color: rgba(var(--color-button),var(--alpha-button-background));
}

.slideshow__dots-wrapper .flickity-page-dots .dot {
  width: 39px;
  height: 39px;
  margin: 0 10px;
  vertical-align: middle;
}

.slideshow__dots-wrapper .flickity-page-dots .dot.is-selected {
  width: 39px!important;
  height: 39px!important
}

.page-width.slideshow__dots-wrapper .flickity-viewport{
  border-radius: var(--imageRadius);
}

/*================ GLOBAL ================*/
/*================ #Keyframes =========================*/
@keyframes placeholder-shimmer {
  0%{
    background-position:-150% 0;
  }
  to{
    background-position:150% 0;
  }
}

@keyframes spin {
  0%{
    transform:rotate(0deg);
  }

  to{
    transform:rotate(360deg);
  }
}

@keyframes page-fade-in-up {
  0%{
    opacity:0;
  }
  to{
    opacity:1;
  }
}

@keyframes page-fade-in-up-out {
  0%{
    opacity:1;
  }
  to{
    opacity:0;
  }
}

@keyframes page-slide-reveal-across {
  0%{
    transform:translateX(0);
  }
  to{
    transform:translateX(100vw);
  }
}

@keyframes page-slide-reveal-across-out {
  0%{
    transform:translateX(-100vw);
  }
  to{
    transform:translateX(0);
  }
}

@keyframes page-slide-reveal-down {
  0%{
    transform:translateY(0);
  }
  to{
    transform:translateY(110vh);
  }
}

@keyframes page-slide-reveal-down-out {
  0%{
    transform:translateY(110vh);
  }
  to{
    transform:translateY(0);
  }
}

@keyframes overlay-on {
  0%{ opacity:0; }
  to{ opacity:0.6; }
}

@keyframes overlay-off {
  0%{ opacity:0.6; }
  to{ opacity:0; }
}

@keyframes full-overlay-on {
  0%{ opacity:0; }
  to{ opacity:1; }
}

@keyframes full-overlay-off {
  0%{ opacity:1; }
  to{ opacity:0; }
}

@keyframes modal-open {
  0%{
    opacity:0;
    transform:translateY(30px);
  }
  to{
    opacity:1;
    transform:translateY(0);
  }
}

@keyframes modal-closing {
  0%{
    opacity:1;
    transform:scale(1);
  }
  to{
    opacity:0;
    transform:scale(0.9);
  }
}

@keyframes rise-up {
  0%{
    opacity:1;
    transform:translateY(120%);
  }
  to{
    opacity:1;
    transform:translateY(0%);
  }
}

@keyframes rise-up-out {
  0%{
    opacity:1;
    transform:translateY(0%);
  }
  to{
    opacity:1;
    transform:translateY(-120%);
  }
}

@keyframes paint-across {
  0%{
    transform:scale(1.1);
    opacity:1;
    -webkit-clip-path:polygon(0% 0%, 0% 0%, 0% 100%, 0% 100%);
    clip-path:polygon(0% 0%, 0% 0%, 0% 100%, 0% 100%);
  }
  to{
    transform:scale(1);
    opacity:1;
    -webkit-clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
    clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
  }
}

@keyframes paint-across-small {
  0%{
    opacity:1;
    transform:scale(1.1);
    -webkit-clip-path:polygon(0% 0%, 0% 0%, 0% 100%, 0% 100%);
    clip-path:polygon(0% 0%, 0% 0%, 0% 100%, 0% 100%);
  }
  to{
    opacity:1;
    transform:scale(1);
    -webkit-clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
    clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
  }
}

@keyframes paint-across-out {
  0%{
    opacity:1;
    -webkit-clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
    clip-path:polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
  }
  to{
    opacity:1;
    -webkit-clip-path:polygon(100% 0%, 100% 0%, 100% 100%, 100% 100%);
    clip-path:polygon(100% 0%, 100% 0%, 100% 100%, 100% 100%);
  }
}

@keyframes fade-in {
  0%{
    opacity:0;
  }
  to{
    opacity:1;
  }
}

@keyframes fade-in-small {
  0%{
    opacity:0;
  }
  to{
    opacity:1;
  }
}

@keyframes fade-in-out {
  0%{
    opacity:1;
  }
  to{
    opacity:0;
  }
}

@keyframes fade-out {
  0%{
    opacity:1;
  }
  to{
    opacity:0;
  }
}

@keyframes zoom-fade {
  0%{
    opacity:0;
    transform:scale(1.25);
  }
  to{
    opacity:1;
    transform:scale(1);
  }
}

@keyframes zoom-fade-password {
  0%{
    opacity:0;
    transform:scale(1.4);
  }
  5%{
    opacity:1;
    transform:scale(1);
  }
  to{
    opacity:1;
    transform:scale(1.2);
  }
}

@keyframes zoom-fade-small {
  0%{
    opacity:0;
    transform:scale(1.1);
  }
  10%{
    opacity:0.1;
  }
  20%{
    opacity:0.2;
  }
  to{
    opacity:1;
    transform:scale(1);
  }
}

@keyframes kenburns {
  0%{
    opacity:0;
    transform:scale(1.27);
    animation-timing-function:cubic-bezier(0.12, 0.63, 0.6, 0.74);
  }
  10%{
    opacity:1;
    transform:scale(1.2);
    animation-timing-function:linear;
  }
  to{
    opacity:1;
    transform:scale(1);
  }
}

@keyframes kenburns-out {
  0%{
    opacity:1;
    transform:scale(1);
    animation-timing-function:ease-out;
  }
  50%{
    opacity:0;
  }
  to{
    transform:scale(1.1);
  }
}

@keyframes preloading {
  0%{   transform-origin:0% 50%; transform:scaleX(0); opacity:0; }
  40%{  transform-origin:0% 50%; transform:scaleX(1); opacity:1; }
  41%{  transform-origin:100% 50%; transform:scaleX(1); opacity:1; }
  to{ transform-origin:100% 50%; transform:scaleX(0); opacity:1; }
}

@keyframes product__loading {
  0%{ opacity:1; }
  3%{ opacity:0; }
  10%{ opacity:0; }
  20%{ opacity:0.2; }
  30%{ opacity:0; }
  40%{ opacity:0.2; }
  50%{ opacity:0; }
  60%{ opacity:0.2; }
  70%{ opacity:0; }
  80%{ opacity:0.2; }
  90%{ opacity:0; }
  98%{ opacity:0.2; }
  to{ opacity:1; }
}

@keyframes slideshowBars{
  0%{ transform:translateX(-100%); }
  to{ transform:translateX(0); }
}

@keyframes announcement-scroll--left{
  0%{
    transform:translateX(-1%);
  }
  to{
    transform:translateX(-51%);
  }
}

@keyframes announcement-scroll--right{
  0%{
    transform:translateX(-51%);
  }
  to{
    transform:translateX(-1%);
  }
}

@keyframes moves-disapear-left {
    0% {
        transform: translateX(0px);
        margin: 0 5px;
    }
    
    99.99% {
        margin: 0 0 0 -5px;
        width: 0px;
        height: 0px;
    }
    
    100% {
        transform: translateX(-2px);
        display: none;
        width: 0px;
        height: 0px;
    }
}

@keyframes coming-left {
    0% {
        width: 0px;
        height: 0px;
    }
    to {
        width: 6px;
        height: 6px;
    }
}

@keyframes moves-left {
    0% {
        transform: translateX(0px);
    }
    to {
        transform: translateX(0px);
    }
}

@keyframes coming-right {
    0% {
        width: 0px;
        height: 0px;
    }
    to {
        width: 6px;
        height: 6px;
    }
}

@keyframes moves-disapear-right {
    0% {
        margin: 0 5px;
        transform: translateX(0px);
    }
    
    99.99% {
        margin: 0 0 0 -5px;
        width: 0px;
        height: 0px;
    }
    
    100% {
        transform: translateX(2px);
        display: none;
        width: 0px;
        height: 0px;
    }
}

@keyframes moves-right {
    0% {
        transform: translateX(0px);
    }
    to {
        transform: translateX(0px);
    }
}

*,:after,:before,input{
  box-sizing:border-box;
}

body,html{
  padding:0;
  margin:0;
}

/*@media only screen and (max-width:768px){
  .supports-touch.lock-scroll{
    overflow:hidden;
  }
}*/

.lock-scroll body {
  overflow: hidden;
}

article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary{
  display:block;
}

audio,canvas,progress,video{
  display:inline-block;
  vertical-align:baseline;
}

input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{
  height:auto;
}

input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{
  -webkit-appearance:none;
}

.clearfix:after{content:"";display:table;clear:both;}

html:not(.tab-outline) :focus{
  outline:none;
}
/*================= Animation Classes and Keyframes ============================*/
.is-transitioning{
  display:block !important;
  visibility:visible !important;
}

.display-table{
  display:table;
  table-layout:fixed;
  width:100%;
}

.display-table-cell{
  display:table-cell;
  vertical-align:middle;
  float:none !important;
}

@media only screen and (min-width:590px){
  .medium-up--display-table{
    display:table;
    table-layout:fixed;
    width:100%;
  }

  .medium-up--display-table-cell{
    display:table-cell;
    vertical-align:middle;
    float:none;
  }
}

/*================ #Helper Classes ================*/
.visually-hidden{
  clip:rect(0, 0, 0, 0);
  overflow:hidden;
  position:absolute;
  height:1px;
  width:1px;
}

.visually-invisible{
  opacity:0 !important;
}

/*============================================================================
Skip to content button
- Overrides .visually-hidden when focused
==============================================================================*/
.skip-link:focus{
  clip:auto;
  width:auto;
  height:auto;
  margin:0;
  color:#2a2b2f;
  color:var(--colorTextBody);
    background-color:#ffffff;
    background-color:var(--colorBody);
      padding:10px;
      opacity:1;
      z-index:10000;
      transition:none;
      }

/*============================================================================
#Grid
==============================================================================*/
/*============================================================================
Grid Setup
1. Allow the grid system to be used on lists.
2. Remove any margins and paddings that might affect the grid system.
3. Apply a negative `margin-left` to negate the columns' gutters.
==============================================================================*/
/*============================================================================
Reversed grids allow you to structure your source in the opposite
order to how your rendered layout will appear.
==============================================================================*/
.grid:after{content:"";display:table;clear:both;}

.grid{
  list-style:none;
  margin:0;
  padding:0;
  margin-left:-30px
}

@media only screen and (max-width:589px){

  .grid{
    margin-left:-22px
  }

  html[dir=rtl] .grid{
    margin-left:0;
    margin-right:-22px
  }
}

html[dir=rtl] .grid{
  margin-left:0;
  margin-right:-30px
}

.grid--small{
  margin-left:-10px
}

.grid--small .grid__item{
  padding-left:10px;
}

.grid__item{
  float:left;
  padding-left:30px;
  width:100%;
  min-height:1px
}

@media only screen and (max-width:589px){

  .grid__item{
    padding-left:22px
  }

  html[dir=rtl] .grid__item{
    padding-left:0;
    padding-right:22px
  }
}

.grid__item[class*="--push"]{
  position:relative;
}

.grid__item--all-auto {
  height:auto!important
}

@media only screen and (max-width:589px){
  .grid__item--mobile-auto {
    height:auto!important
  }
}

@media only screen and (min-width:590px){
  .grid__item--desktop-auto {
    height:auto!important
  }
}

html[dir=rtl] .grid__item{
  float:right;
  padding-left:0;
  padding-right:30px
}

html[dir=rtl] .grid__item[class*="--push"]{
  position:static;
}

.grid--no-gutters{
  margin-left:0
}

.grid--no-gutters .grid__item{
  padding-left:0;
  position: relative
}

.grid--small-gutters{
  margin-left:-10px;
  margin-bottom:-10px
}

.grid--small-gutters .grid__item{
  padding-left:10px;
  padding-bottom:10px;
}

html[dir=rtl] .grid--small-gutters{
  margin-left:0;
  margin-right:-10px
}

html[dir=rtl] .grid--small-gutters .grid__item{
  padding-left:0;
  padding-right:10px;
}

.grid--full{
  margin-left:0
}

.grid--full>.grid__item{
  padding-left:0;
}

@media only screen and (min-width:590px){
  .grid--table-large{
    display:table;
    width:100%;
    table-layout:fixed
  }

  .grid--table-large>.grid__item{
    display:table-cell;
    vertical-align:middle;
    float:none;
  }
}

.upsellproducts .grid__item {
  /*padding-left: 15px;
  padding-right: 15px;
  padding-bottom: 15px;*/
  padding: 15px
  /*width: calc(100% + 30px);*/
}



/*============================================================================
Grid Columns
- Create width classes, prepended by the breakpoint name.
==============================================================================*/
/*================ Grid push classes ================*/
/*================ Clearfix helper on uniform grids ================*/
/*================ Build Base Grid Classes ================*/
.one-whole{width:100%;}

.one-half{width:50%;}

.one-third{width:33.33333%;}

.two-thirds{width:66.66667%;}

.one-quarter{width:25%;}

.two-quarters{width:50%;}

.three-quarters{width:75%;}

.one-fifth{width:20%;}

.two-fifths{width:40%;}

.three-fifths{width:60%;}

.four-fifths{width:80%;}

.one-sixth{width:16.66667%;}

.two-sixths{width:33.33333%;}

.three-sixths{width:50%;}

.four-sixths{width:66.66667%;}

.five-sixths{width:83.33333%;}

.one-eighth{width:12.5%;}

.one-twentieth{width:5%;}

.two-eighths{width:25%;}

.three-eighths{width:37.5%;}

.four-eighths{width:50%;}

.five-eighths{width:62.5%;}

.six-eighths{width:75%;}

.seven-eighths{width:87.5%;}

.one-tenth{width:10%;}

.two-tenths{width:20%;}

.three-tenths{width:30%;}

.four-tenths{width:40%;}

.five-tenths{width:50%;}

.six-tenths{width:60%;}

.seven-tenths{width:70%;}

.eight-tenths{width:80%;}

.nine-tenths{width:90%;}

.one-twelfth{width:8.33333%;}

.two-twelfths{width:16.66667%;}

.three-twelfths{width:25%;}

.four-twelfths{width:33.33333%;}

.five-twelfths{width:41.66667%;}

.six-twelfths{width:50%;}

.seven-twelfths{width:58.33333%;}

.eight-twelfths{width:66.66667%;}

.nine-twelfths{width:75%;}

.nine-twentieth{width:45%;}

.ten-twelfths{width:83.33333%;}

.eleven-twelfths{width:91.66667%;}

/*@media only screen and (max-width:589px){
  .grid-mobile.medium-up--one-sixth, .grid-mobile.medium-up--one-quarter{width:50%;}
  .grid-mobile.medium-up--one-third{width:33.33333%;}
  }*/

/*================ Build Responsive Grid Classes ================*/
@media only screen and (max-width:589px){.small--one-whole{width:100%;}.small--one-half{width:50%;}.small--one-third{width:33.33333%;}.small--two-thirds{width:66.66667%;}.small--one-quarter{width:25%;}.small--two-quarters{width:50%;}.small--three-quarters{width:75%;}.small--one-fifth{width:20%;}.small--two-fifths{width:40%;}.small--three-fifths{width:60%;}.small--four-fifths{width:80%;}.small--one-sixth{width:16.66667%;}.small--two-sixths{width:33.33333%;}.small--three-sixths{width:50%;}.small--four-sixths{width:66.66667%;}.small--five-sixths{width:83.33333%;}.small--one-eighth{width:12.5%;}.small--two-eighths{width:25%;}.small--three-eighths{width:37.5%;}.small--four-eighths{width:50%;}.small--five-eighths{width:62.5%;}.small--six-eighths{width:75%;}.small--seven-eighths{width:87.5%;}.small--one-tenth{width:10%;}.small--two-tenths{width:20%;}.small--three-tenths{width:30%;}.small--four-tenths{width:40%;}.small--five-tenths{width:50%;}.small--six-tenths{width:60%;}.small--seven-tenths{width:70%;}.small--eight-tenths{width:80%;}.small--nine-tenths{width:90%;}.small--one-twelfth{width:8.33333%;}.small--two-twelfths{width:16.66667%;}.small--three-twelfths{width:25%;}.small--four-twelfths{width:33.33333%;}.small--five-twelfths{width:41.66667%;}.small--six-twelfths{width:50%;}.small--seven-twelfths{width:58.33333%;}.small--eight-twelfths{width:66.66667%;}.small--nine-twelfths{width:75%;}.small--ten-twelfths{width:83.33333%;}.small--eleven-twelfths{width:91.66667%;}.grid--uniform .small--five-tenths:nth-of-type(odd),.grid--uniform .small--four-eighths:nth-of-type(odd),.grid--uniform .small--four-twelfths:nth-of-type(3n+1),.grid--uniform .small--one-eighth:nth-of-type(8n+1),.grid--uniform .small--one-fifth:nth-of-type(5n+1),.grid--uniform .small--one-half:nth-of-type(odd),.grid--uniform .small--one-quarter:nth-of-type(4n+1),.grid--uniform .small--one-sixth:nth-of-type(6n+1),.grid--uniform .small--one-third:nth-of-type(3n+1),.grid--uniform .small--one-twelfth:nth-of-type(12n+1),.grid--uniform .small--six-twelfths:nth-of-type(odd),.grid--uniform .small--three-sixths:nth-of-type(odd),.grid--uniform .small--three-twelfths:nth-of-type(4n+1),.grid--uniform .small--two-eighths:nth-of-type(4n+1),.grid--uniform .small--two-sixths:nth-of-type(3n+1),.grid--uniform .small--two-twelfths:nth-of-type(6n+1){clear:both;}}

@media only screen and (min-width:590px){.medium-up--one-whole{width:100%;}.medium-up--one-half{width:50%;}.medium-up--one-third{width:33.33333%;}.medium-up--two-thirds{width:66.66667%;}.medium-up--one-quarter{width:25%;}.medium-up--two-quarters{width:50%;}.medium-up--three-quarters{width:75%;}.medium-up--one-fifth{width:20%;}.medium-up--two-fifths{width:40%;}.medium-up--three-fifths{width:60%;}.medium-up--four-fifths{width:80%;}.medium-up--one-sixth{width:16.66667%;}.medium-up--two-sixths{width:33.33333%;}.medium-up--three-sixths{width:50%;}.medium-up--four-sixths{width:66.66667%;}.medium-up--five-sixths{width:83.33333%;}.medium-up--one-eighth{width:12.5%;}.medium-up--two-eighths{width:25%;}.medium-up--three-eighths{width:37.5%;}.medium-up--four-eighths{width:50%;}.medium-up--five-eighths{width:62.5%;}.medium-up--six-eighths{width:75%;}.medium-up--seven-eighths{width:87.5%;}.medium-up--one-tenth{width:10%;}.medium-up--two-tenths{width:20%;}.medium-up--three-tenths{width:30%;}.medium-up--four-tenths{width:40%;}.medium-up--five-tenths{width:50%;}.medium-up--six-tenths{width:60%;}.medium-up--seven-tenths{width:70%;}.medium-up--eight-tenths{width:80%;}.medium-up--nine-tenths{width:90%;}.medium-up--one-twelfth{width:8.33333%;}.medium-up--two-twelfths{width:16.66667%;}.medium-up--three-twelfths{width:25%;}.medium-up--four-twelfths{width:33.33333%;}.medium-up--five-twelfths{width:41.66667%;}.medium-up--six-twelfths{width:50%;}.medium-up--seven-twelfths{width:58.33333%;}.medium-up--eight-twelfths{width:66.66667%;}.medium-up--nine-twelfths{width:75%;}.medium-up--ten-twelfths{width:83.33333%;}.medium-up--eleven-twelfths{width:91.66667%;}.grid--uniform .medium-up--five-tenths:nth-of-type(odd),.grid--uniform .medium-up--four-eighths:nth-of-type(odd),.grid--uniform .medium-up--four-twelfths:nth-of-type(3n+1),.grid--uniform .medium-up--one-eighth:nth-of-type(8n+1),.grid--uniform .medium-up--one-fifth:nth-of-type(5n+1),.grid--uniform .medium-up--one-half:nth-of-type(odd),.grid--uniform .medium-up--one-quarter:nth-of-type(4n+1),.grid--uniform .medium-up--one-sixth:nth-of-type(6n+1),.grid--uniform .medium-up--one-third:nth-of-type(3n+1),.grid--uniform .medium-up--one-twelfth:nth-of-type(12n+1),.grid--uniform .medium-up--six-twelfths:nth-of-type(odd),.grid--uniform .medium-up--three-sixths:nth-of-type(odd),.grid--uniform .medium-up--three-twelfths:nth-of-type(4n+1),.grid--uniform .medium-up--two-eighths:nth-of-type(4n+1),.grid--uniform .medium-up--two-sixths:nth-of-type(3n+1),.grid--uniform .medium-up--two-twelfths:nth-of-type(6n+1){clear:both;}}

@media only screen and (min-width:1050px){.widescreen--one-whole{width:100%;}.widescreen--one-half{width:50%;}.widescreen--one-third{width:33.33333%;}.widescreen--two-thirds{width:66.66667%;}.widescreen--one-quarter{width:25%;}.widescreen--two-quarters{width:50%;}.widescreen--three-quarters{width:75%;}.widescreen--one-fifth{width:20%;}.widescreen--two-fifths{width:40%;}.widescreen--three-fifths{width:60%;}.widescreen--four-fifths{width:80%;}.widescreen--one-sixth{width:16.66667%;}.widescreen--two-sixths{width:33.33333%;}.widescreen--three-sixths{width:50%;}.widescreen--four-sixths{width:66.66667%;}.widescreen--five-sixths{width:83.33333%;}.widescreen--one-eighth{width:12.5%;}.widescreen--two-eighths{width:25%;}.widescreen--three-eighths{width:37.5%;}.widescreen--four-eighths{width:50%;}.widescreen--five-eighths{width:62.5%;}.widescreen--six-eighths{width:75%;}.widescreen--seven-eighths{width:87.5%;}.widescreen--one-tenth{width:10%;}.widescreen--two-tenths{width:20%;}.widescreen--three-tenths{width:30%;}.widescreen--four-tenths{width:40%;}.widescreen--five-tenths{width:50%;}.widescreen--six-tenths{width:60%;}.widescreen--seven-tenths{width:70%;}.widescreen--eight-tenths{width:80%;}.widescreen--nine-tenths{width:90%;}.widescreen--one-twelfth{width:8.33333%;}.widescreen--two-twelfths{width:16.66667%;}.widescreen--three-twelfths{width:25%;}.widescreen--four-twelfths{width:33.33333%;}.widescreen--five-twelfths{width:41.66667%;}.widescreen--six-twelfths{width:50%;}.widescreen--seven-twelfths{width:58.33333%;}.widescreen--eight-twelfths{width:66.66667%;}.widescreen--nine-twelfths{width:75%;}.widescreen--ten-twelfths{width:83.33333%;}.widescreen--eleven-twelfths{width:91.66667%;}.grid--uniform .widescreen--five-tenths:nth-of-type(odd),.grid--uniform .widescreen--four-eighths:nth-of-type(odd),.grid--uniform .widescreen--four-twelfths:nth-of-type(3n+1),.grid--uniform .widescreen--one-eighth:nth-of-type(8n+1),.grid--uniform .widescreen--one-fifth:nth-of-type(5n+1),.grid--uniform .widescreen--one-half:nth-of-type(odd),.grid--uniform .widescreen--one-quarter:nth-of-type(4n+1),.grid--uniform .widescreen--one-sixth:nth-of-type(6n+1),.grid--uniform .widescreen--one-third:nth-of-type(3n+1),.grid--uniform .widescreen--one-twelfth:nth-of-type(12n+1),.grid--uniform .widescreen--six-twelfths:nth-of-type(odd),.grid--uniform .widescreen--three-sixths:nth-of-type(odd),.grid--uniform .widescreen--three-twelfths:nth-of-type(4n+1),.grid--uniform .widescreen--two-eighths:nth-of-type(4n+1),.grid--uniform .widescreen--two-sixths:nth-of-type(3n+1),.grid--uniform .widescreen--two-twelfths:nth-of-type(6n+1){clear:both;}}

@media only screen and (min-width:590px){.medium-up--push-one-half{left:50%;}.medium-up--push-one-third{left:33.33333%;}.medium-up--push-two-thirds{left:66.66667%;}.medium-up--push-one-quarter{left:25%;}.medium-up--push-two-quarters{left:50%;}.medium-up--push-three-quarters{left:75%;}.medium-up--push-one-fifth{left:20%;}.medium-up--push-two-fifths{left:40%;}.medium-up--push-three-fifths{left:60%;}.medium-up--push-four-fifths{left:80%;}.medium-up--push-one-sixth{left:16.66667%;}.medium-up--push-two-sixths{left:33.33333%;}.medium-up--push-three-sixths{left:50%;}.medium-up--push-four-sixths{left:66.66667%;}.medium-up--push-five-sixths{left:83.33333%;}.medium-up--push-one-eighth{left:12.5%;}.medium-up--push-two-eighths{left:25%;}.medium-up--push-three-eighths{left:37.5%;}.medium-up--push-four-eighths{left:50%;}.medium-up--push-five-eighths{left:62.5%;}.medium-up--push-six-eighths{left:75%;}.medium-up--push-seven-eighths{left:87.5%;}.medium-up--push-one-tenth{left:10%;}.medium-up--push-two-tenths{left:20%;}.medium-up--push-three-tenths{left:30%;}.medium-up--push-four-tenths{left:40%;}.medium-up--push-five-tenths{left:50%;}.medium-up--push-six-tenths{left:60%;}.medium-up--push-seven-tenths{left:70%;}.medium-up--push-eight-tenths{left:80%;}.medium-up--push-nine-tenths{left:90%;}.medium-up--push-one-twelfth{left:8.33333%;}.medium-up--push-two-twelfths{left:16.66667%;}.medium-up--push-three-twelfths{left:25%;}.medium-up--push-four-twelfths{left:33.33333%;}.medium-up--push-five-twelfths{left:41.66667%;}.medium-up--push-six-twelfths{left:50%;}.medium-up--push-seven-twelfths{left:58.33333%;}.medium-up--push-eight-twelfths{left:66.66667%;}.medium-up--push-nine-twelfths{left:75%;}.medium-up--push-ten-twelfths{left:83.33333%;}.medium-up--push-eleven-twelfths{left:91.66667%;}}

.show{display:block !important;}

.hide{display:none !important;}

.text-left{text-align:left !important;}

.text-right{text-align:right !important;}

.text-center{text-align:center !important;}

.text-underline{text-decoration: underline;}

.content-left{justify-content:left !important;}

.content-right{justify-content:right !important;}

.content-center{justify-content:center !important;}

.content-height{height:100%!important}

@media only screen and (min-width:590px){
  .medium-up--height{height:100%!important}
}

@media only screen and (max-width:589px){.small--show{display:block !important;}.small--hide{display:none !important;}.small--text-left{text-align:left !important;}.small--text-right{text-align:right !important;}.small--text-center{text-align:center !important;} .small--content-center{justify-content:center !important;}}

@media only screen and (max-width:768px){.medium-down--show{display:block !important;}.medium-down--hide{display:none !important;}.medium-down--text-left{text-align:left !important;}.medium-down--text-right{text-align:right !important;}.medium-down--text-center{text-align:center !important;}}

@media only screen and (min-width:590px){.medium-up--show{display:block !important;}.medium-up--hide{display:none !important;}.medium-up--text-left{text-align:left !important;}.medium-up--text-right{text-align:right !important;}.medium-up--text-center{text-align:center !important;}}

@media only screen and (min-width:769px){.large-up--show{display:block !important;}.large-up--hide{display:none !important;}.large-up--text-left{text-align:left !important;}.large-up--text-right{text-align:right !important;}.large-up--text-center{text-align:center !important;}}

@media only screen and (min-width:1050px){.widescreen--show{display:block !important;}.widescreen--hide{display:none !important;}.widescreen--text-left{text-align:left !important;}.widescreen--text-right{text-align:right !important;}.widescreen--text-center{text-align:center !important;}}

.new-grid{
  display:flex;
  flex-wrap:wrap;
  margin-left:-10px;
  margin-right:-10px;
  word-break:break-word;
}

.new-grid--center{
  justify-content:center;
}

.grid--slidebar-wrapper {
  padding-top:10px
}

[data-view=scrollable] .grid__item{
  flex:0 0 16.66667%;
  max-width:250px
}

[data-view=scrollable-7] .grid__item{
  flex:0 0 14.28571%
}

[data-view=scrollable-5] .grid__item{
  flex:0 0 20%
}

[data-view=xsmall] .grid__item{
  flex:0 0 20%
}

[data-view=medium] .grid__item{
  flex:0 0 33.33333%
}

[data-view=large] .grid__item{
  flex:0 0 50%
}

[data-view=full] .grid__item, [data-view=calendar] .grid__item{
  flex:0 0 100%
}

[data-view="6-3"] .grid__item{
  flex:0 0 16.66667%
}

[data-view="6-2"] .grid__item{
  flex:0 0 16.66667%
}

[data-view="3-1"] .grid__item{
  flex:0 0 33.33333%
}

@media only screen and (min-width:590px){[data-view=small] .medium-up--one-whole{width:100%;}[data-view=small] .medium-up--one-half{width:50%;}[data-view=small] .medium-up--one-third{width:33.33333%;}[data-view=small] .medium-up--two-thirds{width:66.66667%;}[data-view=small] .medium-up--one-quarter{width:25%;}[data-view=small] .medium-up--two-quarters{width:50%;}[data-view=small] .medium-up--three-quarters{width:75%;}[data-view=small] .medium-up--one-fifth{width:20%;}[data-view=small] .medium-up--two-fifths{width:40%;}[data-view=small] .medium-up--three-fifths{width:60%;}[data-view=small] .medium-up--four-fifths{width:80%;}[data-view=small] .medium-up--one-sixth{width:16.66667%;}[data-view=small] .medium-up--two-sixths{width:33.33333%;}[data-view=small] .medium-up--three-sixths{width:50%;}[data-view=small] .medium-up--four-sixths{width:66.66667%;}[data-view=small] .medium-up--five-sixths{width:83.33333%;}[data-view=small] .medium-up--one-eighth{width:12.5%;}[data-view=small] .medium-up--two-eighths{width:25%;}[data-view=small] .medium-up--three-eighths{width:37.5%;}[data-view=small] .medium-up--four-eighths{width:50%;}[data-view=small] .medium-up--five-eighths{width:62.5%;}[data-view=small] .medium-up--six-eighths{width:75%;}[data-view=small] .medium-up--seven-eighths{width:87.5%;}[data-view=small] .medium-up--one-tenth{width:10%;}[data-view=small] .medium-up--two-tenths{width:20%;}[data-view=small] .medium-up--three-tenths{width:30%;}[data-view=small] .medium-up--four-tenths{width:40%;}[data-view=small] .medium-up--five-tenths{width:50%;}[data-view=small] .medium-up--six-tenths{width:60%;}[data-view=small] .medium-up--seven-tenths{width:70%;}[data-view=small] .medium-up--eight-tenths{width:80%;}[data-view=small] .medium-up--nine-tenths{width:90%;}[data-view=small] .medium-up--one-twelfth{width:8.33333%;}[data-view=small] .medium-up--two-twelfths{width:16.66667%;}[data-view=small] .medium-up--three-twelfths{width:25%;}[data-view=small] .medium-up--four-twelfths{width:33.33333%;}[data-view=small] .medium-up--five-twelfths{width:41.66667%;}[data-view=small] .medium-up--six-twelfths{width:50%;}[data-view=small] .medium-up--seven-twelfths{width:58.33333%;}[data-view=small] .medium-up--eight-twelfths{width:66.66667%;}[data-view=small] .medium-up--nine-twelfths{width:75%;}[data-view=small] .medium-up--ten-twelfths{width:83.33333%;}[data-view=small] .medium-up--eleven-twelfths{width:91.66667%;}[data-view=small] .grid--uniform .medium-up--five-tenths:nth-of-type(odd),[data-view=small] .grid--uniform .medium-up--four-eighths:nth-of-type(odd),[data-view=small] .grid--uniform .medium-up--four-twelfths:nth-of-type(3n+1),[data-view=small] .grid--uniform .medium-up--one-eighth:nth-of-type(8n+1),[data-view=small] .grid--uniform .medium-up--one-fifth:nth-of-type(5n+1),[data-view=small] .grid--uniform .medium-up--one-half:nth-of-type(odd),[data-view=small] .grid--uniform .medium-up--one-quarter:nth-of-type(4n+1),[data-view=small] .grid--uniform .medium-up--one-sixth:nth-of-type(6n+1),[data-view=small] .grid--uniform .medium-up--one-third:nth-of-type(3n+1),[data-view=small] .grid--uniform .medium-up--one-twelfth:nth-of-type(12n+1),[data-view=small] .grid--uniform .medium-up--six-twelfths:nth-of-type(odd),[data-view=small] .grid--uniform .medium-up--three-sixths:nth-of-type(odd),[data-view=small] .grid--uniform .medium-up--three-twelfths:nth-of-type(4n+1),[data-view=small] .grid--uniform .medium-up--two-eighths:nth-of-type(4n+1),[data-view=small] .grid--uniform .medium-up--two-sixths:nth-of-type(3n+1),[data-view=small] .grid--uniform .medium-up--two-twelfths:nth-of-type(6n+1){clear:both;}}


@media only screen and (max-width:768px){

  [data-view=xsmall] .grid__item{
    flex:0 0 50%
  }

  [data-view=small] .grid__item{
    flex:0 0 50%
  }

  [data-view=medium] .grid__item{
    flex:0 0 100%
  }

  [data-view=large] .grid__item, [data-view=full] .grid__item, [data-view=calendar] .grid__item{
    flex:0 0 100%
  }

  [data-view=subcollections] .grid__item{
    flex:0 0 28%
  }

  [data-view="6-3"] .grid__item{
    flex:0 0 33.33333%
  }

  [data-view="6-2"] .grid__item{
    flex:0 0 50%
  }

  [data-view="3-1"] .grid__item{
    flex:0 0 100%
  }
}




html{
  touch-action:manipulation;
}

html[dir=rtl]{
  direction:rtl;
}

/*body,html{
  background-color:#ffffff;
  background:var(--colorBody);
    color:#2a2b2f;
    color:var(--colorTextBody);
      }*/

/*================ #Basic Styles ================*/
.page-width{
  max-width:var(--max-content-width);
  margin:0 auto;
}

.page-full {
  padding:0 20px;
  padding:0 15px
}

.page-narrow{
  max-width:1000px;
  margin:0 auto;
}

.page-narrow,.page-width{
  padding:0 20px;
  padding:0 15px
}

@media only screen and (min-width:590px){
  .page-full {
    padding:0
  }
  .page-narrow,.page-width{
    padding:0 40px
  }
}

.page-content,.shopify-email-marketing-confirmation__container,.shopify-policy__container{
  padding-top:30px;
  padding-bottom:30px
}

@media only screen and (min-width:590px){

  .page-content,.shopify-email-marketing-confirmation__container,.shopify-policy__container{
    padding-top:60px;
    padding-bottom:60px
  }
  .product-section .page-content,.shopify-email-marketing-confirmation__container {
    padding-top:55px;
  }
}

.shopify-email-marketing-confirmation__container{
  text-align:center;
}

.page-content--with-blocks{
  padding-bottom:0;
}

@media only screen and (max-width:589px){

  .product-section .page-content{
    padding-top: 0px;
    padding-bottom: 0px;
  }

  .product-section:has(.calendarWrapper) {
    margin-bottom: 55px
  }
}

/*=============== Critical CSS ===================*/
.main-content{
  display:block;
  min-height:300px
}

@media only screen and (min-width:590px){

  .main-content{
    min-height:700px
  }
}

.template-challange .main-content{
  min-height:0
}

.hr--large,.hr--medium,.hr--small,hr{
  height:1px;
  border:0;
  border-top:calc(var(--dividerWeight)/2) solid;
  /*border-top-color:#e8e8e1;
  border-top-color:var(--colorBorder);*/
  border-top-color: rgba(var(--color-border), 1)
    }

.hr--small{
  margin:15px auto;
}

.hr--medium{
  margin:25px auto
}

@media only screen and (min-width:590px){

  .hr--medium{
    margin:35px auto
  }
}

.hr--large{
  margin:40px auto
}

@media only screen and (min-width:590px){

  .hr--large{
    margin:60px auto
  }
}

.page-blocks+.hr--large,.page-blocks+[data-section-type=recently-viewed] .hr--large{
    margin-top:0
}

.page-blocks .hr--large{
  margin-top:0;
}

.hr--clear{
  border:0;
}

@media only screen and (max-width:589px){
  .table--responsive thead{
    display:none;
  }

  .table--responsive tr{
    display:block;
  }

  .table--responsive td,.table--responsive tr{
    float:left;
    clear:both;
    width:100%;
  }

  .table--responsive td,.table--responsive th{
    display:block;
    text-align:right;
    padding:15px;
  }

  .table--responsive td:before{
    font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
      font-weight:var(--fontHeaderWeight);
        letter-spacing:var(--fontHeaderSpacing);
          line-height:var(--fontHeaderLineHeight);
            }

  
  [data-header-capital=true] .table--responsive td:before{
    text-transform:uppercase;
  }

  [data-label-capital=true] .table--responsive td:before{
    text-transform:uppercase;
    letter-spacing:0.2em;
  }

  .table--responsive td:before{
    content:attr(data-label);
    float:left;
    font-size: var(--fontSmall);
    padding-right:10px;
  }
}

@media only screen and (max-width:589px){
  .table--small-hide{
    display:none !important;
  }

  .table__section+.table__section{
    position:relative;
    margin-top:10px;
    padding-top:15px
  }

  .table__section+.table__section:after{
    content:"";
    display:block;
    position:absolute;
    top:0;
    left:15px;
    right:15px;
    border-bottom:1px solid;
    border-bottom-color:#e8e8e1;
    border-bottom-color:var(--colorBorder);
      }
}

/*================ Typography ================*/
body,button,input,p,select,textarea{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          font-size:var(--fontBaseSize);
            -webkit-font-smoothing:antialiased;
            -webkit-text-size-adjust:100%;
            text-rendering:optimizeSpeed;
            }

body{
  font-weight:400;
}

p{
  margin:0 0 20px 0
}

p:empty {
  display: none;
}

p img{
  margin:0;
}

em{
  font-style:italic;
}

b,strong{
  font-weight:700;
}

p[data-spam-detection-disclaimer],small{
  font-size:0.9em;
  font-size: var(--fontSmall);
}

sub,sup{
  position:relative;
  font-size:60%;
  vertical-align:baseline;
}

sup{
  top:-0.5em;
}

sub{
  bottom:-0.5em;
}

span:empty {
  display: none;
}

.rte blockquote,blockquote{
  margin:0;
  padding:20px 40px 40px
}

.rte blockquote p,blockquote p{
  font-size:calc(var(--fontBaseSize) + 2px)
    }

.rte blockquote p,blockquote p{
  margin-bottom:0
}

.rte blockquote p+cite,blockquote p+cite{
  margin-top:20px;
}

.rte blockquote cite,blockquote cite{
  display:block
}

.rte blockquote cite:before,blockquote cite:before{
  content:"\2014 \0020";
}

code,pre{
  background-color:#faf7f5;
  font-family:Consolas,monospace;
  font-size:1em;
  border:0 none;
  padding:0 2px;
  color:#51ab62;
}

pre{
  overflow:auto;
  padding:20px;
  margin:0 0 40px;
}

.label,label{
  font-size:calc(var(--fontBaseSize))
}

[data-header-font=true] .label, [data-header-font=true] .product-form__controls-group label:not(.option) {
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
  letter-spacing:var(--fontHeaderSpacing);
  font-weight:var(--fontHeaderWeight);
  line-height:var(--fontHeaderLineHeight);
}

.footer__title{
  font-weight:var(--fontHeaderWeight);
  letter-spacing:var(--fontHeaderSpacing);
  line-height:var(--fontHeaderLineHeight)
}

[data-header-font=true] .footer__title{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
}

[data-header-capital=true] .footer__title, [data-header-capital=true] .drawer__title, [data-header-capital=true] .cart__subtotal, [data-header-capital=true] .cart__item--headers{
  text-transform:uppercase;
}

[data-header-capital=true] .type-advanced .rte--strong, [data-header-capital=true] .type-product .rte--strong, [data-header-capital=true] .type-sale_collection .rte--strong{
  text-transform:uppercase;
}

[data-header-capital=true] .quotes-slider__text cite{
  text-transform:uppercase
}

[data-label-capital=true] .footer__title{
  text-transform:uppercase;
  letter-spacing:0.2em;
  font-size:var(--fontBaseSize)!important
    }

[data-label-capital=true] .label, [data-label-capital=true] .product-form__controls-group label{
    text-transform:uppercase;
}

[data-label-capital=true] .cart__item--headers, .cart__subtotal{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.collapsible-content .label,.collapsible-content label{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
  font-weight:var(--fontBaseWeight);
  letter-spacing:var(--fontBaseSpacing);
  line-height:var(--fontBaseLineHeight);
  text-transform:none;
  font-size:var(--fontBaseSize);
}

@media only screen and (max-width:768px){
  .collapsible-content .label,.collapsible-content label{
    font-size: var(--fontBaseSize);
  }
}

#comments .label,#comments label{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          text-transform:none;
          font-size:calc(var(--fontBaseSize) - 2px)
            }

.ajaxcart__note .label,.ajaxcart__note label{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          text-transform:none;
          font-size:calc(var(--fontBaseSize) - 2px)
            }

.template-page .contact-form .label,.template-page .contact-form label, .contact-form label, .cart-page-form label, .cart-drawer-form label{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          text-transform:none;
          font-size:var(--fontBaseSize)
            }

label{
  display:block;
  margin-bottom:10px;
}

.label-info{
  display:block;
  margin-bottom:10px;
}

/*================ Headings ================*/
.h1,.h2,.h3,.h4,.h5,.h6,h1,h2,h3,h4,h5,h6{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
  font-weight:var(--fontHeaderWeight);
  letter-spacing:var(--fontHeaderSpacing);
  line-height:var(--fontHeaderLineHeight);
  word-break: normal;
}

[data-header-capital=true] :is(.h1,.h2,.h3,.h4,.h5,.h6,h1,h2,h3,h4,h5,h6){
  text-transform:uppercase
}

.h1,.h2,.h3,.h4,.h5,.h6,h1,h2,h3,h4,h5,h6{
  display:block;
  margin:0 0 10px
}

.h1 a,.h2 a,.h3 a,.h4 a,.h5 a,.h6 a,h1 a,h2 a,h3 a,h4 a,h5 a,h6 a{
  text-decoration:none;
  font-weight:inherit;
}

@media only screen and (min-width:590px){

  .h1,.h2,.h3,.h4,.h5,.h6,h1,h2,h3,h4,h5,h6{
    margin:0 0 20px
  }
}

.h1,h1{
  font-size:calc(var(--fontHeaderSize)*0.85)
    }

@media only screen and (min-width:590px){

  .h1,h1{
    font-size:var(--fontHeaderSize)
      }
}

.h2,h2{
  font-size:calc(var(--fontHeaderSize)*0.9*0.85)
    }

@media only screen and (min-width:590px){

  .h2,h2{
    font-size:calc(var(--fontHeaderSize)*0.9)
      }
}

.h3,h3{
  font-size:calc(var(--fontHeaderSize)*0.85*0.85)
    }

@media only screen and (min-width:590px){

  .h3,h3{
    font-size:calc(var(--fontHeaderSize)*0.85)
      }
}

.h4,h4{
  font-size:calc(var(--fontHeaderSize)*0.61*0.85)
    }

@media only screen and (min-width:590px){

  .h4,h4{
    font-size:calc(var(--fontHeaderSize)*0.61)
      }
}

.h5 h6,.h6,h5{
  font-size:calc(var(--fontHeaderSize)*0.5);
    }

.text-spacing{
  margin-bottom:20px;
}

/*.h1 span,.h2 span,.h3 span,.h4 span,.h5 span,.h6 span,h1 span,h2 span,h3 span,h4 span,h5 span,h6 span{
  background: var(--colorHeaderHighlight);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-box-decoration-break: clone;
}*/

/*================ RTE headings ================*/
.rte h1{
  font-size:calc(var(--fontHeaderSize)*0.85)
    }

@media only screen and (min-width:590px){

  .rte h1{
    font-size:var(--fontHeaderSize)
      }
}

.rte h2{
  font-size:calc(var(--fontHeaderSize)*0.85*0.85)
    }

@media only screen and (min-width:590px){

  .rte h2{
    font-size:calc(var(--fontHeaderSize)*0.85)
      }
}

.rte h3{
  font-size:calc(var(--fontHeaderSize)*0.69*0.85)
    }

@media only screen and (min-width:590px){

  .rte h3{
    font-size:calc(var(--fontHeaderSize)*0.69)
      }
}

.rte h4, h4 p{
  font-size:calc(var(--fontHeaderSize)*0.59*0.85)
    }

@media only screen and (min-width:590px){

  .rte h4, h4 p{
    font-size:calc(var(--fontHeaderSize)*0.59)
      }
}

.collapsible-content .rte table{
  font-size:calc(var(--fontBaseSize) - 2px)
    }

.collapsible-content .rte table td,.collapsible-content .rte table th{
  padding:6px 8px;
}

@media only screen and (max-width:589px){

  .rte table{
    font-size:calc(var(--fontBaseSize) - 2px)
      }
  .rte table td,.rte table th{
    padding:6px 8px;
  }
}

.larger-text .h2{
  font-size:calc(var(--fontHeaderSize)*0.85)
    }

@media only screen and (min-width:590px){

  .larger-text .h2{
    font-size:var(--fontHeaderSize)
      }
}

.larger-text p{
  font-size:calc(var(--fontBaseSize) + 2px)
    }

@media only screen and (min-width:590px){

  .larger-text p{
    font-size:calc(var(--fontBaseSize) + 2px)
      }
}

.larger-text .subtitle{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-family:var(--fontSubHeaderPrimary),var(--fontBaseFallback);
      font-weight:var(--fontBaseWeight);
        letter-spacing:var(--fontBaseSpacing);
          line-height:var(--fontBaseLineHeight);
            }

[data-body-capital=true] .larger-text .subtitle{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.larger-text .subtitle{
  font-size:var(--fontSubheaderSize);
    margin-bottom:5px
    }

[data-body-capital=true] .larger-text .subtitle{
  font-size:calc(var(--fontSubheaderSize)*0.85)
    }

@media only screen and (min-width:590px){

  .larger-text .subtitle{
    font-size:calc(var(--fontSubheaderSize)*1.13)
      }

  
  [data-body-capital=true] .larger-text .subtitle{
    font-size:var(--fontSubheaderSize)
      }
}

.date{
  display:inline-block;
  line-height:1.7;
  margin-bottom:5px;
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    font-weight:var(--fontHeaderWeight);
      letter-spacing:var(--fontHeaderSpacing);
        line-height:var(--fontHeaderLineHeight)
          }

[data-header-capital=true] .date{
  text-transform:uppercase
}

[data-label-capital=true] .date{
  text-transform:uppercase;
  letter-spacing:0.2em
}

@media only screen and (min-width:590px){

  .date{
    margin-bottom:0
  }
}

.section-header .date:last-child{
  margin-bottom:40px;
}

[data-body-capital=true] .article__date{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.comment-author{
  margin-bottom:0;
  font-size:16px;
}

.comment-date{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight)
          }



[data-body-capital=true] .comment-date{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.comment-date{
  font-size: var(--fontSmall);
  display:block;
  margin-top:3px
}

@media only screen and (max-width:589px){

  .comment-date{
    font-size:10px;
    margin-bottom:20px
  }
}

.cart__product-name{
  font-size:var(--fontBaseSize)
    }

@media only screen and (min-width:590px){

  .cart__product-name{
    font-size:calc(var(--fontBaseSize)*1.13)
      }
}

.cart__product-meta{
  font-size:calc(var(--fontBaseSize)*0.85)
    }

@media only screen and (min-width:590px){

  .cart__product-meta{
    font-size:var(--fontBaseSize)
      }
}

.ajaxcart__product-name{
  font-size:calc(var(--fontBaseSize)*1.13*0.85);
    line-height:1.3
    }

@media only screen and (min-width:590px){

  .ajaxcart__product-name{
    font-size:calc(var(--fontBaseSize)*1.13)
      }
}

.ajaxcart__product-meta{
  font-size:calc(var(--fontBaseSize)*0.9*0.85);
    line-height:1.3
    }

@media only screen and (min-width:590px){

  .ajaxcart__product-meta{
    font-size:calc(var(--fontBaseSize)*0.9)
      }
}

.ajaxcart__subtotal{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    font-family: var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontHeaderWeight);
      letter-spacing:var(--fontHeaderSpacing);
        letter-spacing:normal;
        line-height:var(--fontHeaderLineHeight);
          text-transform: none!important;
          }

[data-header-capital=true] .ajaxcart__subtotal{
  text-transform:uppercase
}

[data-label-capital=true] .ajaxcart__subtotal{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.ajaxcart__subtotal{
  font-size:var(--fontCartSize);
    /*margin-bottom:10px;*/
    letter-spacing: normal!important;
    }

.ajaxcart__price{
  line-height:1.2;
  margin-bottom:10px
}

@media only screen and (max-width:589px){

  .ajaxcart__price{
    font-size:calc(var(--fontBaseSize)*0.85)
      }
}

.ajaxcart__note{
  font-size:11px;
  /*opacity:0.8;*/
  margin-bottom:20px;
  margin-top:5px
}

@media only screen and (min-width:590px){

  .ajaxcart__note{
    font-size:13px
  }
}

.ajaxcart__note--terms{
  margin-top:12px
}

.ajaxcart__note--terms input{
  vertical-align:middle;
}

.ajaxcart__note--terms label{
  display:inline;
}

.ajaxcart__note--terms a{
  text-decoration:underline;
}

.ajaxcart__savings,.cart__savings{
  font-size:20px;
}

/*.media__title{
  font-size:calc(var(--fontHeaderSize)*0.46);
    font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
      font-weight:var(--fontHeaderWeight);
        letter-spacing:var(--fontHeaderSpacing);
          line-height:var(--fontHeaderLineHeight)
            }*/

[data-header-capital=true] .media__title{
  text-transform:uppercase
}

@media only screen and (min-width:590px){

  .media__title{
    font-size:calc(var(--fontHeaderSize)*0.71)
      }
}

.rte .enlarge-text{
  margin:0
}

.rte .enlarge-text p{
  font-size:calc(var(--fontBaseSize) + 2px)!important
    }

.rte .enlarge-text p:last-child{
  margin-bottom:0;
}

html[dir=rtl] .text-left{
  text-align:right !important;
}

html[dir=rtl] .text-right{
  text-align:left !important;
}

ol,ul{
  margin:0 0 20px 40px;
  padding:0;
  text-rendering:optimizeLegibility;
}

ol ol{
  list-style:lower-alpha;
}

ol{ list-style:decimal; }

ol ol,ol ul,ul ol,ul ul{ margin:4px 0 5px 20px; }

li{ margin-bottom:0.25em; }

ul.square{ list-style:square outside; }

ul.disc{ list-style:disc outside; }

ol.alpha{ list-style:lower-alpha outside; }

.no-bullets{
  list-style:none outside;
  margin-left:0;
}

.inline-list{
  padding:0;
  margin:0
}

.inline-list li{
  display:inline-block;
  margin-bottom:0;
  vertical-align:middle;
}

/*================ #Tables ================*/
table{
  width:100%;
  border-spacing:1px;
  position:relative;
  border:0 none;
  /*background:#e8e8e1;
  background:var(--colorBorder);*/
    }

.table-wrapper{
  max-width:100%;
  overflow:auto;
  -webkit-overflow-scrolling:touch;
}

td,th{
  border:0 none;
  text-align:left;
  padding:10px 15px;
  /*background:#ffffff;
  background:var(--colorBody)*/
    }

html[dir=rtl] td,html[dir=rtl] th{
  text-align:right
}

th{
  font-weight:700;
}

.table__title,th{
  font-weight:700;
}

.text-link,a{
  /*color:#2a2b2f;
  color:var(--colorTextBody);*/
  text-decoration:none;
  background:none;
}

p a {
  pointer-events: all;
  text-decoration: underline;
}

.text-link:hover,a:not(.btn, .btn--outlined, .image-wrap, .media__item-content, .product__meta-link, .site-nav__link, .site-nav__dropdown-link):hover, a:hover .product__title-header, .collapsible-trigger-btn:hover{
  /*color:#2a2b2f;
  color:var(--colorTextBody);*/
  filter: brightness(var(--colorBtnFilterBrightness))
}

a:not(.btn, .btn--outlined):has(.product__image-mask):hover, a.image-wrap:hover, a:has(.placeholder-svg) {
  filter: unset!important
}

.customers a{
  text-decoration:none;
  /*border-bottom:2px solid;
  border-bottom-color:rgba(42, 43, 47, 0.1);*/
  position:relative
}

.text-link{
  display:inline;
  border:0 none;
  background:none;
  padding:0;
  margin:0;
}

/*.rte a,.shopify-email-marketing-confirmation__container a,.shopify-policy__container a{
  color:#2a2b2f;
  color:var(--colorLink);
    }*/
.drawer-product__btn  {
  text-decoration: underline;
  color: rgba(var(--color-foreground), 1);
}

.drawer__scrollable table {
  margin-left: -20px;
  width: calc(100% + 40px);
  border-spacing: calc(var(--dividerWeight)/2);
  display: grid;
  overflow: auto;
  height: 100%;
}

@media only screen and (max-width:589px){
  .drawer__scrollable table {
    margin-left: -5px;
    width: calc(100% + 5px);
  }
}

.drawer__scrollable th {
  text-align: left;
  background-color: #006699;
  color: #fff;				
  text-align: left;
  padding: 25px;
}

.drawer__scrollable tr {
  position: relative
}

.drawer__scrollable tr td {
  white-space: nowrap;
}

.drawer__scrollable td p {
  margin-bottom: 0;
}

/*.drawer__scrollable table tr:first-child td {
  color: rgb(var(--color-button-text));
    background: rgba(var(--color-button),var(--alpha-button-background));
}*/

.drawer__scrollable table tr:nth-child(even) td {
  background-color: rgba(var(--color-foreground), 0.08);
}

/*.drawer__scrollable table tr:nth-child(odd):after {
  content: "";
  display: grid;
  position: absolute;
  background-color: rgba(var(--color-foreground), 0.08);
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
}*/

.drawer__scrollable table tr:nth-child(even) td {
  position: relative;
  z-index: 1
}
/*================ Buttons ================*/
button{
  overflow:visible;
}

button[disabled],html input[disabled]{
  cursor:default;
}

.btn--outlined{
  /*background:transparent;
  border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorBtnPrimaryOutlined);
  color: var(--colorBtnPrimaryText);
*/
  --color-button: var(--color-secondary-button);
  --color-button-text: var(--color-secondary-button-text);
}

.btn:not(.btn--outlined, .btn--no-animate, .btn--tertiary, .btn--static):hover:not([disabled]){
  --border-offset: 0.3px; /* Default is 1.3px as defined above in this file. This removes 1px to prevent the border from growing on buttons when this effect is on.  */
  box-shadow: 0 0 0 calc(var(--buttons-border-width) + var(--border-offset))
  rgba(var(--color-button-text), var(--border-opacity)),
    0 0 0 var(--buttons-border-width) rgba(var(--color-button), var(--alpha-button-background));
}

.btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded, .btn--subscribed{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
  font-weight:var(--buttonWeight);
  letter-spacing:var(--fontHeaderSpacing);
  line-height:var(--fontHeaderLineHeight)
}

[data-header-capital=true] :is(.btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded){
  text-transform:uppercase
}

.btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded, .btn--subscribed{
  display:inline-block;
  justify-content: center;
  align-items: center;
  display: inline-flex;
  
  margin:0;
  margin-bottom:1px!important;
  width:auto;
  min-width:90px;
  min-width:47.99px;
  line-height:1.42;
  font-size:var(--buttonTextSize);
  text-decoration:none;
  text-align:center;
  vertical-align:middle;
  white-space:normal;
  align-items: center;
  justify-content: center;
  display: inline-flex;
  height: var(--buttonSize);
  padding: 10px 20px;
  cursor:pointer;
  -webkit-user-select:none;
  user-select:none;
  -webkit-appearance:none;
  -moz-appearance:none;
  border-radius:var(--buttonRadius);
  transition:padding-right 0.3s,background 0.3s,opacity 1s;
  color: rgb(var(--color-button-text));
  background: rgba(var(--color-button),var(--alpha-button-background));
}

/*[data-button-capital=true] .btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded, .btn--subscribed{
  text-transform: uppercase;
}*/

@media only screen and (max-width:589px){

  .btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded{
    font-size:var(--buttonTextSize);
    padding:14.07px 18px;
    padding:14.07px 13px;
    height: var(--buttonSize);
  }
}

.btn:hover,.product-reviews .spr-button:hover,.product-reviews .spr-summary-actions a:hover,.rte .btn:hover,.shopify-payment-button .shopify-payment-button__button--unbranded:hover, .flickity-prev-next-button:hover, .drawer__close-button:hover {
  filter: brightness(var(--colorBtnFilterBrightness));
  transition:opacity 0.1s ease;
}

.btn:active,.product-reviews .spr-button:active,.product-reviews .spr-summary-actions a:active,.rte .btn:active,.shopify-payment-button .shopify-payment-button__button--unbranded:active{
  /*opacity:0.6;*/
  filter: brightness(1);
  transition:opacity 0.1s ease;
}

.btn.disabled,.btn[disabled],.product-reviews .spr-button.disabled,.product-reviews .spr-button[disabled],.product-reviews .spr-summary-actions a.disabled,.product-reviews .spr-summary-actions a[disabled],.rte .btn.disabled,.rte .btn[disabled],.shopify-payment-button .shopify-payment-button__button--unbranded.disabled,.shopify-payment-button .shopify-payment-button__button--unbranded[disabled], .quick-product__btn[disabled]{
  cursor:default;
  pointer-events: none;   
  color:#444;
  background-color:#f6f6f6 !important;
  
  color: #777!important;
  color: var(--colorBtnUnavailable)!important;
  background-color: var(--colorBtnUnavailable);
  transition:none
}

.btn.disabled:hover,.btn[disabled]:hover,.product-reviews .spr-button.disabled:hover,.product-reviews .spr-button[disabled]:hover,.product-reviews .spr-summary-actions a.disabled:hover,.product-reviews .spr-summary-actions a[disabled]:hover,.rte .btn.disabled:hover,.rte .btn[disabled]:hover,.shopify-payment-button .shopify-payment-button__button--unbranded.disabled:hover,.shopify-payment-button .shopify-payment-button__button--unbranded[disabled]:hover{
  color:#444;
  background-color:#f6f6f6;
}



  

.shopify-payment-button .shopify-payment-button__button--unbranded:hover:not([disabled]){
  color:#ffffff;
  color:var(--colorBtnPrimaryText);
    background-color:#2a2b2f;
    background-color:var(--colorBtnPrimary);
      }

.shopify-payment-button__more-options{
  color:inherit;
}

.btn--no-animate{
  background-image:none;
  transition:opacity 1s;
}

/*.btn:not(.btn--no-animate, .btn--tertiary, .btn--static, .btn--outlined){
  background:var(--colorBtnPrimary);
  border: none;
  color: var(--colorBtnPrimaryText);

  background:rgba(var(--color-button),var(--alpha-button-background));
  border: none;
  color: rgb(var(--color-button-text));
}*/

.btn--outlined:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static){
  /*background:transparent;
  border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorBtnPrimaryOutlined);
  color: var(--colorBtnPrimaryText);
*/
  /*--color-button: transparent;
  --color-button-text: var(--color-secondary-button-text);*/
}

@media only screen and (max-width:589px){
#MenuMobileHeader {
  height: var(--drawer-height)
}
}
/*.drawer__contents .btn:not(.btn--no-animate, .btn--discount, .btn--tertiary, .btn--static) {
  background:var(--colorBtnPrimary );
}*/

/*.drawer__contents .sct--upsell {

    border-color: var(--colorTextBody );
    color: var(--colorTextBody   )!important
}*/

.btn:not(.btn--no-animate, .btn--tertiary, .btn--static):hover:not([disabled]).product-form--full, .drawer__contents .btn:not(.btn--no-animate, .btn--tertiary, .btn--static, .btn--discount):hover:not([disabled]) {
  min-width: 100px;
    
    /*border: none;
      background: var(--colorBtnPrimaryHover)*/
    }

/*.btn--outlined:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static):hover:not([disabled]){
  background:var(--colorBtnPrimaryOutlined);
    border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorBtnPrimaryOutlined);
    color: white!important;
    }*/


/*.color-inverse .btn:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static):not(.btn--upsell):hover:not([disabled]){
  background:transparent !important;
  color: var(--colorInverse)!important;
  border-color: var(--colorInverse);
  
  background-color: #ffffffe3!important;
    color: var(--colorTextBody)!important;
    border-color: transparent!important;
    }*/

/*.color-inverse .btn--outlined:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static):hover:not([disabled]){
  background:var(--colorInverse) !important;
  color: var(--colorBtnPrimaryOutlined)!important;
  border-color:var(--colorInverse)!important;
    }*/

.hero__text-content.color-inverse, .hero__text-content.color-inverse a {
  background: transparent;
  color: var(--colorInverse)!important
}

.shopify-payment-button__button--unbranded:hover:not([disabled]){
  background:var(--colorBtnPrimaryHover)!important;
    transition: padding-right .3s,background .3s,opacity 1s;
    }

.btn:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static):hover:not([disabled]).variant-input-wrap label {
  min-width: 100px;
  color: var(--colorBtnPrimaryText)!important;
    background-color: var(--colorBtnPrimary)!important;
      
      }

.btn--tertiary,.rte .btn--tertiary{
  background-color:transparent;
  border:1px solid;

    color:var(--colorTextBody);
      font-weight:400;
      font-size: var(--fontSmall);
      padding:6px 10px
      }

@media only screen and (min-width:590px){

  .btn--tertiary,.rte .btn--tertiary{
    font-size:var(--buttonTextSize)
  }
}

.btn--tertiary:hover,.rte .btn--tertiary:hover{
  background-color:transparent;
  color:#2a2b2f;
  color:var(--colorTextBody);
    border-color:#2a2b2f;
    border-color:var(--colorTextBody);
      transition:border 0.25s ease;
      }

.btn--tertiary.disabled,.btn--tertiary[disabled],.rte .btn--tertiary.disabled,.rte .btn--tertiary[disabled]{
  cursor:default;
  color:#444;
  background-color:#f6f6f6;
}

.btn--small,.collapsibles-wrapper .spr-button,.collapsibles-wrapper .spr-summary-actions a{
  padding:8px 13px;
  font-size:var(--fontBaseSize);
  background-position:150% 45%;
  min-width:80px
}

@media only screen and (max-width:589px){

  .btn--small,.collapsibles-wrapper .spr-button,.collapsibles-wrapper .spr-summary-actions a{
    padding:7px 12px
  }
}

.btn--upsell{
  width:100%;
  font-size:var(--buttonTextSize);
  margin-top: 11px;
  margin-bottom: 0!important
}

.sct--upsell{
  height:var(--buttonSize);
    background-color: transparent;
    border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorTextBody);
      color: var(--colorTextBody)!important;
        }

.btn--large{
  padding:15px 20px;
  font-size:18px;
}

.btn--full{
  width:100%;
  font-size:var(--buttonTextSize);
}

/*.btn--inverse:not(.btn--inverse--outlined), .color-inverse .btn:not(.btn--upsell):not(.btn--inverse--outlined){
  background-color:var(--colorInverse) !important;
  background:var(--colorInverse) !important;
  color:var(--colorTextBody)!important;
  border-color: var(--colorInverse)!important;
    }


.btn--inverse--outlined, .color-inverse .btn--outlined {
  background-color:transparent !important;
  background:transparent !important;
  border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorInverse)!important;
  color:var(--colorInverse)!important;
}*/

@media only screen and (max-width:589px){
  .btn--inverse--outlined, .color-inverse .btn--outlined {
    padding: 11.1px 20px;
  }
}

/*.btn--inverse--outlined:active,.btn--inverse--outlined:hover, .color-inverse .btn--outlined:active,.color-inverse .btn--outlined:hover  {
  background-color:var(--colorInverse) !important;
  color:#000 !important;
  transition:all 0.5s ease;
}*/

/*.hero__link .btn--inverse--outlined {
  color:var(--colorHeroText);
    border-color:var(--colorInverse);
    border-color:var(--colorHeroText)
      }*/

.btn--body{
  background-color:var(--colorBody);
    color:#2a2b2f;
    color:var(--colorTextBody)
      }

.btn--body:active,.btn--body:hover{
  background-color:var(--colorBody);
    color:#2a2b2f;
    color:var(--colorTextBody);
      }

.btn--circle{
  padding:14px!important;
  border-radius:50%;
  min-width:0;
  line-height:1;
  width: var(--buttonSize);
}

@media only screen and (max-width:589px){
  .btn--circle{
  min-width:var(--buttonSize);
  max-width:var(--buttonSize);
    padding: 9px!important;
    padding-top: 10px!important;
}
}

.btn--circle .icon{
  width:20px;
  height:20px;
}

.btn--circle:after,.btn--circle:before{
  content:none;
  background:none;
  width:auto;
}

.btn--circle.btn--large .icon{
  width:30px;
  height:30px;
}

.btn--circle.btn--large{
  padding:15px;
}

.btn--loading{
  position:relative;
  /*text-indent:-9999px;*/
  /*background-color:var(--colorBtnPrimaryDim);*/
  color:#1e1f22;
  color:var(--colorBtnPrimaryDim)!important;
  color: transparent!important
}

.btn--loading:active,.btn--loading:hover{
  /*background-color:var(--colorBtnPrimaryDim);*/
    color:var(--colorBtnPrimaryDim);
      background-image:none !important;
      }

.btn--loading:after{
  content:"";
  display:block;
  width:24px;
  height:24px;
  position:absolute;
  left:50%;
  top:50%;
  margin-left:-12px;
  margin-top:-12px;
  border-radius:50%;
  border:3px solid;
  border-color:rgb(var(--color-button-text));
  border-top-color:transparent;
  animation:spin 1s linear infinite;
}

.calDivs.btn--loading{
  background: transparent;
  height: 100%;
  bottom: 25px
}

.calDivs.btn--loading:after, .inverse.btn--loading:after{
  border-color:var(--colorTextBody);
  border-top-color:transparent;
}

/*.btn--loading.btn--secondary-accent{
  background-color:transparent;
  border-color:var(--colorBorder);
  color:var(--colorTextBody)
}

.btn--loading.btn--secondary-accent:active,.btn--loading.btn--secondary-accent:hover{
  background-color:transparent;
  color:var(--colorTextBody);
}*/

.btn--loading .icon-quick-add {
  display: none
}

.btn--outlined {
  border: calc(var(--buttonOutlineWeight) / 2) solid;
  background: transparent
}

.btn--back-to-top {
  position: fixed;
  top: auto;
  display: none;
  bottom: 20px;
  width: 44.4px;
  height: 44.4px;
  border-radius: 50%;
  /*color: var(--colorBtnPrimaryText);
    background: var(--colorBtnPrimary);*/
  color: rgb(var(--color-button-text));
    background: rgba(var(--color-button),var(--alpha-button-background));
  z-index: 150;
  border:1px solid var(--colorBtnPrimary);
    border: calc(var(--buttonOutlineWeight) / 2) solid transparent;
      -webkit-transform: translate(0px, 0px) rotate(270deg);
    -moz-transform: translate(0px, 0px) rotate(270deg);
    -o-transform: translate(0px, 0px) rotate(270deg);
    -ms-transform: translate(0px, 0px) rotate(270deg);
    transform: translate(0px, 0px) rotate(270deg);
}

.btn--back-to-top.left {
  left: 20px;
}

.btn--back-to-top.right {
  right: 20px;
}

.btn--back-to-top:hover {
  filter: brightness(var(--colorBtnFilterBrightness));
}

.btn--back-to-top__padding {
      transition: all 0.5s;
   -webkit-transform: translate(0px, -40px) rotate(270deg);
    -moz-transform: translate(0px, -40px) rotate(270deg);
    -o-transform: translate(0px, -40px) rotate(270deg);
    -ms-transform: translate(0px, -40px) rotate(270deg);
    transform: translate(0px, -40px) rotate(270deg);
}

.btn--disabled, .btn[disabled], .color-inverse .btn.btn--disabled:not(.btn--upsell) {
  pointer-events: none!important;
  cursor: default!important;
  opacity: 0.5
  /*color: #36373e50 !important;
  background-color: #36373e10 !important;
  border-color: transparent !important;*/
}

/*.btn--outlined.btn--disabled {
  background-color: transparent !important;
  border-color: #36373e08 !important;
}
*/
.btn--inverse--outlined.btn--disabled, .color-inverse .btn--outlined.btn--disabled{
  background-color: transparent !important;
  border-color: #ffffff21 !important;
  color: #ffffff21!important
}

.btn--loading.btn--disabled {
  pointer-events: none!important;
  cursor: default!important;
  background: transparent!important
}

.btn--pause {
  display: none
}

.playing .btn--pause {
  display: block
}

.btn--loading .btn--play, .playing .btn--play {
  display: none
}

.color-alert.btn--outlined {
  color: var(--colorAlert)!important;
  border-color: var(--colorAlert)!important;
}

.color-alert.btn--outlined:not(.btn--no-animate):not(.btn--tertiary):not(.btn--static):hover:not([disabled]){
  background:var(--colorAlert)!important;
    border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorAlert);
    color: white!important;
    }

.return-link{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    font-weight:var(--fontHeaderWeight);
      letter-spacing:var(--fontHeaderSpacing);
        line-height:var(--fontHeaderLineHeight)
          }

[data-header-capital=true] .return-link{
  text-transform:uppercase
}

.return-link{
  font-size:18px;
  text-align:center
}

@media only screen and (min-width:590px){

  .return-link{
    font-size:20px
  }
}

.return-link .icon{
  width:27px;
  margin-right:8px;
}

.collapsible-trigger-btn{
  display:block;
  width:100%;
  text-align:left;
  padding:20px 0;
}

@media only screen and (max-width:589px){

  .collapsible-trigger-btn{
    padding:20px 0
  }
}

.collapsible-trigger-btn.btn--tertiary{
  padding:6px 10px;
  width:auto;
}

.collapsible-trigger-btn--borders{
  border-top:1px solid;
  border-top-color:#e8e8e1;
  border-top-color:var(--colorBorder)
    }

.collapsible-trigger-btn--borders:first-child{
  border-top:none;
}

.collapsible-content+.collapsible-trigger-btn--borders{
  margin-top:-1px
}

.collapsible-trigger-btn--borders+.collapsible-content .collapsible-content__inner{
  padding-bottom:20px;
}

@media only screen and (max-width:768px){
  .collection-mobile-filters .collapsible-trigger-btn{
    padding-right:17px;
    padding-left:17px;
  }

  .collection-mobile-filters .collapsible-trigger__icon{
    right:17px;
  }
}

.collapsible-content__inner{
  padding:0 0 15px
}

@media only screen and (max-width:768px){
  /*.collapsible-content__inner{
    padding:0 17px 12px
  }*/
  .site-footer .collapsible-content__inner{
    padding:0 0px 12px
  }
}

.shopify-payment-button{
  margin-top:10px;
}

[data-button-capital=true] .shopify-payment-button{
  text-transform:uppercase!important
}

.shopify-payment-button .shopify-payment-button__button--unbranded{
  display:block;
  width:100%;
  transition:none
}

.shopify-payment-button .shopify-payment-button__button--unbranded:hover,.shopify-payment-button .shopify-payment-button__button--unbranded:hover:not([disabled]){
  background-position:150% 35%;
  padding:11px 20px;
}

.product-form__item--payment-button .btn,.product-form__item--payment-button .shopify-payment-button,.product-form__item--payment-button .shopify-payment-button__button--unbranded{
  min-height:var(--buttonSize);
  font-family: var(--fontButtonPrimary),var(--fontHeaderFallback);
}

.product-form__item--payment-button .btn--secondary-accent{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    /*font-weight:var(--fontHeaderWeight);*/
      letter-spacing:var(--fontHeaderSpacing);
        line-height:var(--fontHeaderLineHeight)
          }

[data-header-capital=true] .product-form__item--payment-button .btn--secondary-accent{
  text-transform:uppercase
}

.product-form__item--payment-button .btn--secondary-accent{
  /*background-color:var(--colorBtnPrimary);

      color:var(--colorBtnPrimaryText);*/
        font-size: var(--buttonTextSize);
        padding:11px 20px;
        border-radius:var(--buttonRadius);
        
        border: calc(var(--buttonOutlineWeight) / 2) solid transparent;
        }

@media only screen and (max-width:589px){

  .product-form__item--payment-button .btn--secondary-accent{
    font-size:var(--buttonTextSize);
    padding:8px 18px
  }
}

.btn--secondary-accent.newsletter__input {
  border-color: #e8e8e1;
  border-color: var(--colorTextBodyBorder);
}

.shopify-payment-button__button--hidden{
  display:none !important;
}

/*================ #Images and Iframes ================*/
img{
  border:0 none;
}

svg:not(:root){
  overflow:hidden;
}

iframe,img{
  max-width:100%;
}

img[data-sizes=auto]{
  display:block;
  width:100%;
}
/*
.lazyload{
  opacity:0
}

.no-js .lazyload{
  display:none
}

.lazyloaded{
  opacity:1;
  transition:opacity 0.4s ease;
}
*/
.video-wrapper{
  position:relative;
  overflow:hidden;
  max-width:100%;
  padding-bottom:56.25%;
  height:0;
  height:auto
}

.video-wrapper iframe,.video-wrapper video{
  position:absolute;
  top:0;
  left:0;
  width:100%;
  height:100%;
}

.video-wrapper--modal{
  width:1000px;
}

.grid__image-ratio{
  position:relative;
  background-color:#ffffff;
  background-color:var(--colorBody)
    }

.grid__image-ratio img{
  position:absolute;
  top:0;
  left:0;
  width:100%;
  height:100%;
  -o-object-fit:cover;
  object-fit:cover
}

.grid__image-ratio img.grid__image-contain{
  -o-object-fit:contain;
  object-fit:contain;
}

.grid__image-ratio:before{
  content:"";
  display:block;
  height:0;
  width:100%;
}

.grid__image-ratio--wide:before{
  padding-bottom:56.25%;
}

.grid__image-ratio--landscape:before{
  padding-bottom:75%;
}

.grid__image-ratio--square:before{
  padding-bottom:100%;
}

.grid__image-ratio--portrait:before{
  padding-bottom:150%;
}

.image-fit{
  position:relative;
  width:100%;
  height:100%;
  -o-object-fit:cover;
  object-fit:cover;
  font-family:"object-fit: cover";
  z-index:1;
}

.image-fit--contain{
  position:relative;
  width:100%;
  height:100%;
  -o-object-fit:contain;
  object-fit:contain;
  font-family:"object-fit: contain";
  z-index:1;
}

.image-wrap .video-div {
  width: 100%;
    height: 100%;
    position: absolute;
  top: 0;
}


/*================ Forms ================*/
form{
  margin:0;
}

.form-vertical{
  margin-bottom:20px;
}

.inline{
  display:inline;
}

@media only screen and (max-width:768px){
  input,textarea{
    font-size:16px;
  }
}

button,input,textarea{
  -webkit-appearance:none;
  -moz-appearance:none;
}

button{
  background:none;
  border:none;
  display:inline-block;
  cursor:pointer;
}

fieldset{
  border:1px solid;
  border-color:#e8e8e1;
  border-color:var(--colorBorder);
    padding:20px;
    }

legend{
  border:0;
  padding:0;
}

button,input[type=submit]{
  cursor:pointer;
}

input, select, textarea {
  color: rgba(var(--color-foreground), 1);
  border:1px solid;
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 1)!important;
  max-width:100%;
  padding:11px 10px;
  /*padding: 11px calc(var(--buttonOutlineWeight) / -2 + 11px);*/
  border-radius:var(--buttonRadius);
  height: var(--buttonSize);
  background-color:transparent;
}

[data-bottom-border=true] :is(input, select) {
  padding:11px calc(var(--buttonOutlineWeight) / 2 + 10px)
}

[data-bottom-border=true] textarea {
  padding:11px calc(var(--buttonOutlineWeight) / -2 + 11px);
  padding: 11px 10px;
}

input.button {
  border: none!important;
}

.spr-form-input-textarea {
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 1)!important;
}

textarea {
  border: none!important;
}

[data-bottom-border=true] input:not(.site-header__search-input) {
  border: none!important;
  border-bottom: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 1)!important;
  border-radius: 0!important;
}


/*[data-bottom-border=true] input:not(:focus, .site-header__search-input) {
  margin-left: var(--buttonOutlineWeight) / 2
  
}*/

[data-bottom-border=true] .grid__item--textarea {
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 0)!important;
}

[data-bottom-border=true] .grid__item--border:has(.grid__item--textarea):after {
  content: "";
  display: grid;
  border-bottom: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
  height: 100%;
  bottom: 17px;
  position: relative;
}

[data-bottom-border=true] .grid__item--border:has(textarea:focus):after {
  content: "";
  display: grid;
  width: 0px;
  position: relative;
}

/*[data-bottom-border=true] .grid__item--textarea:not(:focus) {
  padding:11px calc(var(--buttonOutlineWeight) / 2 - 0.5px);
}

[data-bottom-border=true] .grid__item--textarea:has(textarea:focus){
  padding:11px calc(var(--buttonOutlineWeight) / 2 - 3.5px);
}*/

.site-header__search-input {
 padding: 16px 10.5px;
  border-color: transparent!important
}

.search__page-search-bar .site-header__search-input {
  padding: 16px 9.5px;
}

.product__quantity input{
  border-color:#e8e8e1;
  border-color:var(--colorTextBodyBorder);
}

input.disabled,input[disabled],select.disabled,select[disabled],textarea.disabled,textarea[disabled]{
  cursor:default;
  border-color:#b6b6b6;
}

input.input-full,select.input-full,textarea.input-full{
  width:100%;
  height: 100%
}

.product-form__item--submit .input-full {
  height:var(--buttonSize);
}

textarea {
  min-height:100px;
  padding: 7px 10px;
  padding-top: calc(var(--buttonSize) * 0.25);
}

input[type=checkbox],input[type=radio]{
  margin:-2px 6px 0 0;
  padding:0;
  width:auto;
  width: 18px;
  height: 18px!important;
  border-radius: 5px;
}

input[type=checkbox]{
  -webkit-appearance:checkbox;
  -moz-appearance:checkbox;
}

input[type=radio]{
  -webkit-appearance:radio;
  -moz-appearance:radio;
}

input[type=image]{
  padding-left:0;
  padding-right:0;
}




input[type=checkbox] {
         position: relative;
           cursor: pointer;
    }
input[type=checkbox]:before {
   content: "";
   display: block;
   position: absolute;
   width: 18px;
   height: 18px;
   top: 0;
   left: 0;
  border-radius: 5px;
}
input[type=checkbox]:checked:before {
  content: "";
  display: block;
  position: absolute;
  width: 18px;
  height: 18px;
  top: 0;
  left: 0;
  border-radius: 2px;
  background: rgba(var(--color-button),var(--alpha-button-background));
  /*display: none*/
}
input[type=checkbox]:checked:after {
  content: "";
  display: block;
  width: 5px;
  height: 10px;
  border: solid rgb(var(--color-button-text));
  border-width: 0 2px 2px 0;
  -webkit-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  transform: rotate(45deg);
  position: absolute;
  top: 3px;
  left: 6px;
}

.tag--swatch input[type=checkbox], .tag__checkbox-wrapper input[type=checkbox]{
  position: absolute;
}


select{
  -webkit-appearance:none;
  appearance:none;
  background-position:100%;
  display:inline-block;
  vertical-align:middle;
  padding-right:38px!important;
  text-indent:0.01px;
  cursor:pointer;
  color:inherit;
  padding-top: 9px;
    padding-bottom: 9px;
  font-family: var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight: var(--fontBaseWeight);
    letter-spacing: var(--fontBaseSpacing);
    line-height: var(--fontBaseLineHeight);
    font-size: var(--fontBaseSize);
    -webkit-font-smoothing: antialiased;
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeSpeed;
  text-overflow: unset;
}

optgroup{
  font-weight:700;
}

option{
  color:#000;
  background-color:#fff
}

option[disabled]{
  color:#ccc;
}

select::-ms-expand{
  display:none;
}

.hidden-label{
  clip:rect(0, 0, 0, 0);
  overflow:hidden;
  position:absolute;
  height:1px;
  width:1px;
}

label[for]{
  cursor:pointer;
}

.form-vertical input,.form-vertical select,.form-vertical textarea{
  display:block;
  margin-bottom:30px;
  margin-bottom:12px;
  height: var(--buttonSize)
}

.form-vertical textarea {
  height: 100%
}

.form-vertical .btn:not(.btn--outlined),.form-vertical input[type=checkbox],.form-vertical input[type=radio]{
  display:inline-block;
}

.form-vertical .btn:not(:last-child){
  margin-bottom:30px;
}

small{
  display:block;
}

input.error,textarea.error{
  border-color:#d02e2e;
  background-color:#fff6f6;
  color:#d02e2e;
}

label.error{
  color:#d02e2e;
}

.discount-code-error, .datepicker-error {
  width: 100%;
  display: inline-flex;
  margin-top: 6px;
  margin-bottom: 4px;
  text-transform: none;
  color: var(--colorAlert)!important
}

.selector-wrapper label{
  margin-right:10px;
}

.selector-wrapper+.selector-wrapper{
  margin-top:20px;
}

.selector-wrapper select {
  width: 100%
}

.input-group{
  display:flex
}

.input-group input::-moz-focus-inner{
  border:0;
  padding:0;
  margin-top:-1px;
  margin-bottom:-1px;
}

.input-group-btn,.input-group-field{
  margin:0;
}

.input-group .input-group-field{
  flex:1 1 auto;
  min-width:0;
}

.input-group-btn{
  flex:0 1 auto;
  padding:0
}

.input-group-btn .icon{
  vertical-align:baseline;
  vertical-align:initial;
}

.input-group-btn .btn{
  margin-top:0;
  height:100%;
  background-image:none !important
}

.input-group-btn .btn:hover{
  padding-right:20px !important;
}

.icon{
  display:inline-block;
  width:24px;
  height:24px;
  vertical-align:middle;
  fill:currentColor
}

.icon.icon-chevron-down{
      width: 10px!important;
    margin-right: 9px;
    height: 11px!important;
    vertical-align: middle;
}

.no-svg .icon{
  display:none
}

.icon--full-color{
  fill:initial;
}

.grid__item-draggable .grid__text {
    cursor: pointer;
    height: 100%;
    width: calc(100% - 10px);
    align-items: center;
    display: flex;
    margin: 5px;
    position: relative;
    color: rgba(var(--color-foreground),.5);
}

.grid__item-draggable.active .grid__text {
    color: transparent;
    background: rgba(var(--color-border),0.2);
}


.grid__item-draggable {
    position:relative;
  border: 1px dashed red;
  border: calc(var(--buttonOutlineWeight) / 2) dashed rgba(var(--color-border),0.2);
  height: calc(var(--buttonSize) * 1.5);
  width: calc(var(--buttonSize) * 1.5);
  border-radius: calc(var(--buttonRadius)*0.9);
  font-weight:400;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  margin-bottom: 12px;
}

.grid__item-draggable.active {
    border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
}

.grid__item-draggable img {
  height: 100%;
  width: 100%;
  object-fit: contain;
  border-radius: calc(var(--buttonRadius) * 0.45);
}

/*================ #Icons ================*/
svg.icon:not(.icon--full-color) circle,svg.icon:not(.icon--full-color) ellipse,svg.icon:not(.icon--full-color) g,svg.icon:not(.icon--full-color) line,svg.icon:not(.icon--full-color) path,svg.icon:not(.icon--full-color) polygon,svg.icon:not(.icon--full-color) polyline,svg.icon:not(.icon--full-color) rect,symbol.icon:not(.icon--full-color) circle,symbol.icon:not(.icon--full-color) ellipse,symbol.icon:not(.icon--full-color) g,symbol.icon:not(.icon--full-color) line,symbol.icon:not(.icon--full-color) path,symbol.icon:not(.icon--full-color) polygon,symbol.icon:not(.icon--full-color) polyline,symbol.icon:not(.icon--full-color) rect{
  fill:inherit;
  /*stroke:inherit;*/
  stroke: currentColor
}

.icon-social path{
  stroke: none!important
}

.icon-bag-minimal circle,.icon-bag-minimal ellipse,.icon-bag-minimal g,.icon-bag-minimal line,.icon-bag-minimal path,.icon-bag-minimal polygon,.icon-bag-minimal polyline,
.icon-bag-minimal rect,.icon-bag circle,.icon-bag ellipse,.icon-bag g,.icon-bag line,.icon-bag path,.icon-bag polygon,.icon-bag polyline,.icon-bag rect,.icon-cart circle,
.icon-cart ellipse,.icon-cart g,.icon-cart line,.icon-cart path,.icon-cart polygon,.icon-cart polyline,.icon-cart rect,.icon-chevron-down circle,.icon-chevron-down ellipse,
.icon-chevron-down g,.icon-chevron-down line,.icon-chevron-down path,.icon-chevron-down polygon,.icon-chevron-down polyline,.icon-chevron-down rect,.icon-close circle,
.icon-close ellipse,.icon-close g,.icon-close line,.icon-close path,.icon-close polygon,.icon-close polyline,.icon-close rect,.icon-email circle,.icon-email ellipse,
.icon-email g,.icon-email line,.icon-email path,.icon-email polygon,.icon-email polyline,.icon-email rect,.icon-hamburger circle,.icon-hamburger ellipse,.icon-hamburger g,
.icon-hamburger line,.icon-hamburger path,.icon-hamburger polygon,.icon-hamburger polyline,.icon-hamburger rect,.icon-search circle,.icon-search ellipse,.icon-search g,
.icon-search line,.icon-search path,.icon-search polygon,.icon-search polyline,.icon-search rect,.icon-login circle,.icon-login ellipse,.icon-login g,.icon-login line,
.icon-login path,.icon-login polygon,.icon-login polyline,.icon-login rect, .icon-phone path, .icon-pin path, .icon-mail path {
  fill:none !important;
  stroke-width:var(--iconWeight)!important;
    stroke:currentColor !important;
    stroke-linecap:var(--iconLinecaps)!important;
      stroke-linejoin:var(--iconLinecaps)!important;
        }

.icon-minus circle,.icon-minus ellipse,.icon-minus g,.icon-minus line,.icon-minus path,.icon-minus polygon,.icon-minus polyline,.icon-minus rect,.icon-plus circle,.icon-plus ellipse,.icon-plus g,.icon-plus line,.icon-plus path,.icon-plus polygon,.icon-plus polyline,.icon-plus rect {
  fill:none !important;
  stroke-width: calc(var(--iconWeight) * 3)!important;
  stroke:currentColor !important;
  stroke-linecap:var(--iconLinecaps)!important;
  stroke-linejoin:var(--iconLinecaps)!important;
}
.icon-bag-minimal path {
  stroke-width: calc(var(--iconWeight) * 0.24)!important;
}
.icon-chevron-down path {
  stroke-width: calc(var(--iconWeight) * 0.9)!important;
}
.icon-phone path {
  stroke-width: calc(var(--iconWeight) * .8)!important;
}
.icon-pin path {
  stroke-width: calc(var(--iconWeight) * 0.5)!important;
}
.icon-plus path, .icon-minus path {
  stroke-width: calc(var(--iconWeight) * 2.5)!important;
}
.icon-mail path {
  stroke-width: calc(var(--iconWeight) * 5)!important;
}

.icon-play, .icon-pause {
  stroke-width: calc(var(--iconWeight) - 5.9px)!important;
  stroke: currentColor
}

.icon-play path, .icon-pause path{
  fill: currentColor
}

.icon-play:hover, .icon-pause:hover {
  filter: brightness(var(--colorBtnFilterBrightness));
}
/*.color-inverse :is(.icon-play, .icon-pause) {
  stroke: var(--colorBtnPrimaryText)!important;
}*/
.icon-check {
  stroke-width: calc(var(--iconWeight) - 3.7px)!important;
}

/*.btn .icon-arrow-send path{
  stroke: var(--colorBtnPrimaryText)!important;
}*/

.color-inverse .icon-arrow-send path {
  stroke: var(--colorTextBody)!important;
}
.cart__discount-wrapper .icon-close {
  width: 22px;
  height: 22px;
}

.price-grid__item .icon-close {
  width: 30px;
  height: 30px;
}
.cart__discount-wrapper .icon-close path {
  stroke: var(--colorTextBody) !important;
  opacity: var(--colorCartLabel);
}
.icon-cart circle{
  fill:currentColor !important;
}

.icon-mode path{
  stroke-width: calc(var(--iconWeight)*1.1);
    stroke:currentColor !important;
    stroke-linecap:var(--iconLinecaps)!important;
      stroke-linejoin:var(--iconLinecaps)!important;
}

.icon-login circle,.icon-login path {
  stroke-width: calc(var(--iconWeight)*0.6)!important;
}

.icon-search circle, .icon-search line{
  stroke-width: calc(var(--iconWeight)*0.45)!important;
}


.icon__fallback-text{
  clip:rect(0, 0, 0, 0);
  overflow:hidden;
  position:absolute;
  height:1px;
  width:1px;
}

.icon-filling {
  stroke-width: 0!important;
}

.icon-selector path {
  fill: currentColor
}

.cart__remove-wrapper path {
  stroke: var(--colorBody)!important
}

.field__caret {
  display: block;
  width: 30px;
  height: calc(var(--buttonSize) - 50%);
    /*border-left: 1px solid var(--colorBorder);*/
      pointer-events: none;
      position: absolute;
      top: 50%;
      right: 0;
      -webkit-transform: translate(0%, -50%);
      transform: translate(0%, -50%);
}

.icon-facebook:not(.payment-icons--greyscale .icon-facebook) {
  color: #1877f2;
}

.icon-pinterest:not(.payment-icons--greyscale .icon-pinterest) {
  color: #bd081c;
}

.icon-instagram:not(.payment-icons--greyscale .icon-instagram) {
  color: #e4405f;
}

.icon-snapchat:not(.payment-icons--greyscale .icon-snapchat) {
  color: #FFFC00;
}

.icon-tumblr:not(.payment-icons--greyscale .icon-tumblr) {
  color: #36465D;
}

.icon-linkedin:not(.payment-icons--greyscale .icon-linkedin) {
  color: #0072b1;
}

.icon-youtube:not(.payment-icons--greyscale .icon-youtube) {
  color: #FF0000;
}

.icon-vimeo:not(.payment-icons--greyscale .icon-vimeo) {
  color: #86c9ef;
}

.icon-spotify:not(.payment-icons--greyscale .icon-spotify) {
  color: #1DB954;
}

.icon-whatsapp:not(.payment-icons--greyscale .icon-whatsapp, .icon-contact) {
  color: #25D366;
}

.icon-wechat:not(.payment-icons--greyscale .icon-wechat) {
  color: #5cc928
}

/*================ CART ================*/

.placeholder-svg{
  fill:rgba(42, 43, 47, 0.35);
  background-color:#f4f4f4;
  width:100%;
  height:100%;
  max-width:100%;
  max-height:100%;
  display:block;
  /*padding:30px 0;*/
}

.placeholder-noblocks{
  padding:40px;
  text-align:center;
}

.placeholder-content{
  overflow:hidden;
  animation:placeholder-shimmer 1.3s linear infinite;
  background-size:400% 100%;
  margin-bottom:20px;
  border-radius:4px
}

@media only screen and (max-width:589px){

  .placeholder-content{
    margin-left:auto;
    margin-right:auto
  }
}

.animation-cropper{
  overflow:hidden;
  display:inline-flex;
}

.appear-delay-20{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.3s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.4s;}

.appear-delay-19{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.24s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.34s;}

.appear-delay-18{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.18s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.28s;}

.appear-delay-17{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.12s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.22s;}

.appear-delay-16{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.06s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.16s;}

.appear-delay-15{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.1s;}

.appear-delay-14{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.94s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 1.04s;}

.appear-delay-13{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.88s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.98s;}

.appear-delay-12{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.82s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.92s;}

.appear-delay-11{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.76s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.86s;}

.appear-delay-10{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.7s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.8s;}

.appear-delay-9{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.64s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.74s;}

.appear-delay-8{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.58s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.68s;}

.appear-delay-7{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.52s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.62s;}

.appear-delay-6{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.46s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.56s;}

.appear-delay-5{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.4s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.5s;}

.appear-delay-4{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.34s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.44s;}

.appear-delay-3{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.28s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.38s;}

.appear-delay-2{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.22s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.32s;}

.appear-delay-1{transition:transform 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.1s,opacity 1s cubic-bezier(0.165, 0.84, 0.44, 1) 0.2s;}

.image-wrap{
  overflow:hidden;
  border-radius: var(--imageRadius);
  margin-bottom: -1px;
}

[data-view=list] .image-wrap {
  border-radius: calc(var(--imageRadius)*.5);
}

.product-single__thumbnail-item .image-wrap{
  border-radius: calc(var(--imageRadius)*0.35);
}

.image-wrap img:not([role=presentation]){
  display:block;
    
}

.no-js .image-wrap img:not([role=presentation]).lazyload{
  display:none
}

.type-sale-images .image-wrap, .type-banner__image .image-wrap{
  border-radius: 0;
}

.appear-animation{
  opacity:0;
  transform:translateY(40px)
}

@media only screen and (min-width:590px){

  .appear-animation{
    transform:translateY(60px)
  }
}

.js-drawer-open .appear-animation{
  opacity:1;
  transform:translateY(0px);
}

.js-drawer-closing .appear-animation{
  transition-duration:0s;
  transition-delay:0.5s;
}

/*.spr-badge-starrating,.spr-icon-star-empty,.spr-icon-star-hover,.spr-icon-star-hover:hover,.spr-starrating,.spr-starratings{
  color:#f3c200;
}*/

.spr-star path {
  fill: currentColor
}

.spr-icon{
  font-size:14px !important;
  vertical-align:text-bottom;
}

.spr-header-title{
  font-size:calc(var(--fontHeaderSize)*0.85*0.85) !important
    }

@media only screen and (min-width:590px){

  .spr-header-title{
    font-size:calc(var(--fontHeaderSize)*0.85) !important
      }
}

.spr-container.spr-container{
  padding:0;
  border:0;
}

.product-reviews #shopify-product-reviews{
  margin:0;
}

.product-reviews .spr-summary-actions-newreview{
  float:none;
}

.product-reviews .spr-form-label,.product-reviews .spr-review-content-body{
  font-size:var(--fontBaseSize);
    line-height:1.563;
    }

.product-reviews .spr-review-header-byline{
  font-size:11px;
  opacity:1
}

.product-reviews .spr-review-header-byline strong{
  font-weight:400;
}

.product-reviews .spr-form-label{
  display:block;
  text-align:left;
}

.product-reviews .spr-summary-actions,.product-reviews .spr-summary-caption{
  display:block;
}

.product-reviews .spr-summary-actions{
  margin-top:10px;
}

@media only screen and (min-width:590px){
  .product-reviews--full .spr-reviews{
    display:flex;
    flex-wrap:wrap;
  }

  .product-reviews--full .spr-review:first-child{
    margin-top:0;
  }

  .product-reviews--full .spr-review{
    flex:1 1 40%;
    padding:20px;
    border:1px solid;
    border-color:#e8e8e1;
    border-color:var(--colorBorder);
      margin-left:30px;
      margin-bottom:30px
      }

  .product-reviews--full .spr-review:nth-child(odd){
    margin-left:0;
  }

  .product-reviews--full .spr-review:last-child{
    padding-bottom:20px;
  }
}

.grid-product .spr-badge{
  margin-top:6px;
}

.grid-product .spr-badge[data-rating="0.0"]{
  display:none;
}

.grid-product .spr-badge-starrating{
  font-size:11px
}

@media only screen and (min-width:590px){

  .grid-product .spr-badge-starrating{
    font-size: var(--fontSmall)
  }
}

.grid-product .spr-icon{
  margin-right:1px;
  font-size: var(--fontSmall) !important;
}

.grid-product .spr-badge-caption{
  font-size:11px;
  margin-left:4px
}

@media only screen and (min-width:590px){

  .grid-product .spr-badge-caption{
    font-size: var(--fontSmall)
  }
}

.product-reviews--tab .collapsible-trigger .spr-badge-caption{
  margin-left:0;
}

.product-reviews--tab .collapsible-trigger .spr-badge-starrating{
  font-size:13px;
  margin-right:10px
}

@media only screen and (min-width:590px){

  .product-reviews--tab .collapsible-trigger .spr-badge-starrating{
    font-size:14px
  }
}

.product-reviews--tab .collapsible-trigger .spr-badge[data-rating="0.0"] .spr-starrating{
  display:none;
}

.product-reviews--tab .spr-icon{
  margin-right:1px;
}

.product-reviews--tab .spr-badge-caption{
  margin-left:4px;
}

.product-reviews--tab .spr-header-title,.product-reviews--tab .spr-summary-caption,.product-reviews--tab .spr-summary-starrating{
  display:none !important;
}

.product-reviews--tab .spr-button,.product-reviews--tab .spr-summary-actions a{
  margin-top:0 !important;
}

.product-reviews--tab .spr-button-primary{
  float:none;
}

@media only screen and (max-width:480px){
  .product-reviews--tab .spr-summary{
    text-align:left;
  }
}

.product-reviews--tab .spr-form-title{
  display:none;
}

.product-reviews--tab .spr-form-label{
  font-size:13px !important;
}

.product-reviews--tab .spr-review-header .spr-starratings{
  font-size:14px;
}

.spr-pagination{
  flex:1 1 100%;
}

.multi-selectors{
  display:flex;
  justify-content:center;
  flex-wrap:wrap;
  position: absolute;
  position: relative
}

@media only screen and (min-width:590px){
.text-right .multi-selectors {
  right:0;
}
}

.multi-selectors__item{
  margin:0 10px;
}

.single-option-selector::first-letter {
  font-size: 200%;
  color: #8A2BE2;
}

/*================ Flag Icons ================*/
.flag-icons{
  -webkit-user-select:none;
  user-select:none;
  cursor:default;
  -webkit-flex-direction: row;
    -webkit-flex-wrap: wrap;
}
.flag-icons li{
  margin:0 0px 0px;
  padding-top:4px;
  padding-top:0px;
  width: 40px;
  height: 30px;
  background-repeat: no-repeat;
}

.flag-icons--big li{
  margin:0 3px 20px;
  padding-top:4px;
  width: 107px;
  font-weight: var(--fontHeaderWeight);
  text-transform: uppercase;
}
.flag-icons--greyscale{
  filter:grayscale(1);
}
.flag-icon {
  cursor: pointer;
}
.flag-icon.disabled {
  pointer-events: none;
  cursor: default;
  opacity: 0.6;
}

/*================ Payment Icons ================*/
.payment-icons{
  -webkit-user-select:none;
  user-select:none;
  cursor:default
}

.payment-icons li{
  cursor:default;
  margin:0 4px 0;
  /*padding-top:3.5px;*/
}

.payment-icons--greyscale{
  filter:grayscale(1);
}

.payment-icon{
  height: 34px;
}

.payment-icon--cod {
  display: flex;
  min-width:38px;
  height: 24px;
  background-color: white;
  color: #000000;
  border-radius: 3px;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  -khtml-border-radius: 3px;
  justify-content: center;
  align-items: center;
  -webkit-text-stroke: 0.5px #22391A;
  margin-right: -3.5px
}

.payment-icon--cod {
  height: 22px;
  color: #000000;
  border-radius: 2px;
  -webkit-border-radius: 2px;
  -moz-border-radius: 2px;
  -khtml-border-radius: 2px;
  margin-top: 1px;
  outline: 1px solid #000000;

}

.cart__checkout-wrapper .payment-icon--cod {
  margin-right: 6.5px
}

.payment-icon--cod span{
  font-size: 10px;
  font-style: italic;
  font-weight: 900;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  padding: 0 3px;
  padding-bottom: 1px
}

.payment-icon.large-icon {
  height: 51px;
  margin-left: 2px
}



.large-icon .payment-icon--cod {
    min-width: 65px;
    height: 38px;
    border-radius: 645px;
    -webkit-border-radius: 3.5px;
    -moz-border-radius: 3.5px;
    -khtml-border-radius: 3.5px;
    outline: 1.5px solid #000000;
    -webkit-text-stroke: 0.7px #22391A;
  margin-top: 1.5px;
}

.large-icon .payment-icon--cod span{
  font-size: 15px;
}

.payment-icon.large-icon svg {
    min-width: 65px !important;
    min-height: 41px !important;
}

.errors,.note{
  border-radius:var(--buttonRadius);
  padding:6px 12px;
  margin-bottom:20px;
  border:1px solid transparent;
  text-align:left
}

.errors ol,.errors ul,.note ol,.note ul{
  margin-top:0;
  margin-bottom:0;
}

.errors li:last-child,.note li:last-child{
  margin-bottom:0;
}

.errors p,.note p{
  margin-bottom:0;
}

.note{
  border-color:#e8e8e1;
  border-color:var(--colorBorder);
    }

.errors ul{
  list-style:disc outside;
  margin-left:20px;
}

.note--success{
  color: #56ad6a;
  background-color: #ecfef0 ! important;
  border-color: #56ad6a;
  height: 41px!important;
  padding: 9px;
}

.note--success a{
  color:#56ad6a;
  text-decoration:underline
}

.note--success a:hover{
  text-decoration:none;
}

.errors,.form-error{
  color:var(--colorAlert);
  background-color:var(--colorWarningLight);
  border-color:var(--colorAlert);
}

.errors a,.form-error a{
  color:#d02e2e;
  text-decoration:underline
}

.errors a:hover,.form-error a:hover{
  text-decoration:none;
}

.pagination{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          margin:0;
          padding:80px 0 0;
          text-align:center;
          font-size:15px;
          padding-bottom: 30px;
          }

@media only screen and (max-width:589px){

  .pagination{
    font-size:14px
  }
}

.pagination>span{
  display:inline-block;
  line-height:1;
}

.pagination a{
  display:inline-block;
}

.pagination .page.current,.pagination a{
  padding:8px 12px;
}

.pagination .page.current{
  opacity:0.3;
}

.pagination .next,.pagination .prev{
  color:#ffffff;
  color:var(--colorBtnPrimaryText);
    background:#2a2b2f;
    background:var(--colorBtnPrimary);
      width:43px;
      height:43px;
      line-height:27px;
      border-radius:43px;
      margin:0 10px;
      transition:transform 0.05s ease-out
      }

@media only screen and (max-width:589px){

  .pagination .next,.pagination .prev{
    width:35px;
    height:35px;
    line-height:19px
  }
}

.pagination .next .icon,.pagination .prev .icon{
  color:#ffffff;
  color:var(--colorBtnPrimaryText);
    width:13px;
    height:13px
    }

@media only screen and (max-width:589px){

  .pagination .next .icon,.pagination .prev .icon{
    width:12px;
    height:12px
  }
}

.infinite-scroll-wrap {
  margin-bottom: 40px;
  text-align: center;
}
.infinite-scroll-wrap img {
  max-width: 25px;
}

.pagination-row {
  position:relative;
  display: inline-block;
  padding: 0 60px;
}

.infinite-scroll-wrap button {
  min-width: 150px;
}

/*================ #Rich Text Editor ================*/
.rte:after{content:"";display:table;clear:both;}

.rte+.rte{
  margin-top:40px;
}

.rte img{
  height:auto;
}

.rte ol,.rte p,.rte table,.rte ul{
  margin-bottom:25px;
}

.rte ul ul{
  margin-bottom:0;
}

.rte a:not(.rte__image, .list-item--btn):not(.btn){
  text-decoration:none;
  border-bottom:calc(var(--buttonOutlineWeight) / 3) solid;
  border-bottom-color:rgba(var(--color-button),var(--alpha-button-background));
  position:relative;
}

.rte h1,.rte h2,.rte h3,.rte h4,.rte h5,.rte h6{
  margin-top:2.5em;
  margin-bottom:1em;
}

.rte h1:first-child,.rte h2:first-child,.rte h3:first-child,.rte h4:first-child,.rte h5:first-child,.rte h6:first-child{
  margin-top:0;
}

.rte h1 a,.rte h2 a,.rte h3 a,.rte h4 a,.rte h5 a,.rte h6 a{
  text-decoration:none;
}

.rte meta:first-child+h1,.rte meta:first-child+h2,.rte meta:first-child+h3,.rte meta:first-child+h4,.rte meta:first-child+h5,.rte meta:first-child+h6{
  margin-top:0;
}

.rte>div{
  margin-bottom:20px;
}

.rte li{
  margin-bottom:0;
}


[data-list-items=true] .text-center .rte ol, .text-center .rte ul {
  text-align: left
}

[data-list-items=true] .rte ol {
  list-style: none;
  counter-reset: my-awesome-counter;
}
[data-list-items=true] .rte ol li {
  counter-increment: my-awesome-counter;
  margin: 0 0 10px 2.5em;
  position: relative;
  margin-left: 0
}
[data-list-items=true] .rte ol li::before {
  content: counter(my-awesome-counter);
  position: absolute;
  top: -3px;
  left: calc(var(--fontBaseSize)*-2 - 8px);
  width: calc(var(--fontBaseSize)*2);
  height: calc(var(--fontBaseSize)*2);
  border-radius: 50%;
  display: inline-block;
  line-height: calc(var(--fontBaseSize)*2);
  text-align: center;
  font-weight: 700;
  padding-right: 0.5px;
  color: rgb(var(--color-button-text));
  background: rgba(var(--color-button),var(--alpha-button-background));
}

[data-list-items=true] .rte ul:not(.inline-list) {
  list-style: none;
  counter-reset: my-awesome-counter;
}
[data-list-items=true] .rte ul:not(.inline-list) li {
  counter-increment: my-awesome-counter;
  margin: 0 0 10px 2.5em;
  position: relative;
  margin-left: 0;
}
[data-list-items=true] .rte ul:not(.inline-list) li::before {
  content: "";
  background: rgba(var(--color-button),var(--alpha-button-background));
      position: absolute;
    top: 6.5px;
  left: calc(var(--fontBaseSize)*-1.4 - 7px);
  width: calc(var(--fontBaseSize)*0.7);
  height: calc(var(--fontBaseSize)*0.7);
  border-radius: 50%;
  display: inline-block;
  text-align: center;
  font-weight: 700;
}

.article__content aside {
  -webkit-font-smoothing: antialiased;
  border-radius: 0.5rem;
  background-color: #ffffff29;
  padding: 10px 15px;
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.rte>p:last-child{
  margin-bottom:0;
}

.rte table{
  table-layout:fixed;
}

.rte--block{
  margin-bottom:20px;
}

.rte-setting>p:last-child{
  margin-bottom:0;
}

.text-center.rte ol,.text-center .rte ol,.text-center.rte ul,.text-center .rte ul{
  list-style-position:inside;
  margin-left:0;
}

.rte--nomargin{
  margin-bottom:0;
}

.rte--indented-images img:not([style]):not(.rte__no-indent),.rte--indented-images img[style="float: none;"]:not(.rte__no-indent){
  max-width:100vw;
  margin:0 -22px;
  margin-left: 0;
  display:block
}

@media only screen and (min-width:590px){

  .rte--indented-images img:not([style]):not(.rte__no-indent),.rte--indented-images img[style="float: none;"]:not(.rte__no-indent){
    max-width:100%;
    width: 100%;
    margin:40px -15%;
    margin-left: 0;
  }
}

.rte--indented-images p[style] img{
  display:inline;
  margin:0;
  max-width:100%;
}

.header-layout{
  display:flex;
  justify-content:space-between;
}

.header-layout--center{
  align-items:center;
}

.grid__item-header{
  display:flex;
  align-items:center;
  flex:0 1 auto;
}

.grid__item-header--logo{
  flex:0 0 auto;
  padding: 5px 0;
}

.grid__item-header--icons{
  justify-content:flex-end;
  flex:0 1 auto;
  padding: 5px 0;
}

.grid__item-header--left.grid__item-header--icons{
  justify-content:flex-start;
}

.header-layout--left-center .grid__item-header--icons,.header-layout--left-center .grid__item-header--logo{
  flex:0 0 200px;
  max-width:50%
}

@media only screen and (min-width:590px){

  .header-layout--left-center .grid__item-header--icons,.header-layout--left-center .grid__item-header--logo{
    min-width:130px
  }
}

@media only screen and (min-width:590px){
  .header-layout[data-logo-align=center] .grid__item-header--logo{
    margin:0 26.66667px
  }
}

.header-layout[data-logo-align=center] .grid__item-header--icons,.header-layout[data-logo-align=center] .grid__item-header--navigation{
  flex:1 1 170px;
}

.header-layout[data-logo-align=left] .site-header__logo{
  margin-right:13.33333px;
}

.grid__item-header--logo-split{
  display:flex;
  justify-content:center;
  align-items:center;
  flex:1 1 100%
}

.grid__item-header--logo-split .grid__item-header:not(.grid__item-header--logo){
  text-align:center;
  flex:1 1 20%;
}

.grid__item-header--logo, .header-layout--left-center .grid__item-header--logo, .header-layout--left-center .grid__item-header--icons {
  -webkit-box-flex: 0 1 auto;
  -ms-flex: 0 1 auto;
  flex: 0 1 auto;
}

@media only screen and (min-width: 769px) {
  .grid__item-header--logo, .header-layout--left-center .grid__item-header--logo, .header-layout--left-center .grid__item-header--icons {
    -webkit-box-flex: 0 0 auto;
    -ms-flex: 0 0 auto;
    flex: 0 0 auto
  }
}

.grid__item-header--split-left{
  justify-content:flex-end;
}

.grid__item-header--left .site-nav{
  margin-left:-12px;
  margin-right: 15px
}

@media only screen and (max-width:589px){

  .grid__item-header--left .site-nav{
    margin-left:-10px;
    margin-right: 0px
  }
}

.grid__item-header--icons .site-nav{
  margin-right:-12px
}

@media only screen and (max-width:589px){

  .grid__item-header--icons .site-nav{
    margin-right:-10px
  }
}

/*======================= SEARCH BAR =======================*/

.site-page__search {
  position: absolute;
  width: 100%;
  top: 0px;
  z-index: 28;
  /*background-color:#ffffff;
  background-color:var(--colorBody);
  color:#2a2b2f;
  color:var(--colorTextBody);*/
}

.search__page-search-bar {
  position: relative;
  max-width: 1300px;
  margin: 0 auto;
  padding-right: 26px;
  padding-left: 30px;
  height: 100%
}
@media only screen and (max-width:589px){
  .search__page-search-bar {
    padding-right: 0px;
    padding-left: 0px;
  }
}

.header-item--search .search__page-search-bar {
  margin: 0 auto;
  margin-right: 26px;
  margin-left: 30px;
  padding: 0;
  background: #afafaf2e
}

.header-item--search .site-page__search {
  background-color:transparent;
}
.search-toolbar {
  padding-top: 30px;
  padding-left: 40px;
  padding-right: 40px;
}

.search-toolbar .filters-toolbar {
  position: static;
}

@media only screen and (max-width:589px){
  .site-page__search {
    z-index: 200;
    padding-right: 0px!important;
    padding-left: 0px!important;
  }
  
  .search-toolbar {
    padding-top: 0px;
    padding-left: 0px;
    padding-right: 0px;
  }
}

.site-header__search-container{
  visibility:visible;
  position:absolute;
  left:0;
  right:0;
  bottom:0;
  height:100%;
  z-index:29;
  transition:visibility 0.3s cubic-bezier(0, 0, 0.38, 1);
  opacity: 0;
  visibility: visible;
  bottom: 100%;
}

.site-header__search-container.is-active{
  visibility:visible;
  top: 0;
  opacity: 1;
}

.site-header__search{
  position:absolute;
  top:0;
  left:0;
  bottom:0;
  right:0;
  z-index:28;
  display:flex;
  transform:translate3d(0, -110%, 0);
  background-color: rgb(var(--color-background));
   /* color:#2a2b2f;
    color:var(--colorTextBody);*/
      transition:transform 0.2s cubic-bezier(0, 0, 0.38, 1)
      }

.site-header__search .page-width{
  flex:1 1 100%;
  display:flex;
  align-items:stretch;
  padding-right: 32px;
    padding-left: 30px;
}

@media only screen and (max-width:589px){

  .site-header__search .page-width{
    padding:0
  }
}

.is-active .site-header__search{
  transform:translateZ(0)
}

.site-header__search .icon, .site-page__search .icon{
  width:30px;
  height:30px
}

@media only screen and (max-width:589px){

  .site-header__search .icon, .site-page__search .icon{
    width:27px;
    height:27px
  }
}

.site-header__search-form{
  flex:1 1 auto;
  display:flex;
}

.site-header__search-input{
  border:0;
  width:100px;
  height:100%;
  flex:1 1 auto;
  margin-top: 0px;
  font-size:20px;
    margin-top: auto;
  margin-bottom: auto;
}

@media only screen and (min-width:590px){

  .site-header__search-input{
    font-size:24px;
  }
}

.site-header__search-input:focus{
  border:0;
  outline:0;
  border-color: transparent!important
}

.site-header__close-btn, .site-header__search-btn{
  padding:0 10px 0 10px;
  color: rgba(var(--color-foreground), 1);
}

.predictive-results{
  position:absolute;
  top:100%;
  left:0;
  right:0;
  background-color:#ffffff;
  background-color:var(--colorBody);
    color:#2a2b2f;
    color:var(--colorTextBody);
      max-height:70vh;
      max-height:calc(88vh - 100%);
      overflow:auto;
      margin-top:-1px;
      z-index:28
      }

@media only screen and (min-width:590px){

  .predictive-results{
    padding-top:20px;
    max-height:calc(100vh - 100% - 30px)
  }
}

.active_search--background {
  background: var(--colorBody)!important
}





.search__results{
  /*background:var(--colorBody);
  color:var(--colorTextBody);*/
  container-type:inline-size;
  display:none;
  margin-top:0px;
  overflow-y:auto;
  padding:0;
  position:absolute;
  /*top:15%;*/
  left:0;
  width:100%;
  height: 100vh;
  z-index: 28;
padding-top: 20px;
    position:fixed;
}

@media only screen and (max-width:768px){

  .search__results{
    margin-left:calc(var(--pageWidthPadding, 17px)*-1);
    margin-right:calc(var(--pageWidthPadding, 17px)*-1);
    width:calc(100% + var(--pageWidthPadding, 15px));
    padding-left: 17px;
    padding-top: 0px;
  }

  .template-search .page-content .search__results{
    max-height:50vh;
  }
}

.results{
  display:block;
  padding:0 15px 20px;
  width:100%;
}

.results ul{
  list-style:none;
  margin:0;
  padding:0;
}

.results li{
  margin-bottom:0;
  padding:10px 0;
  transition:background 0.2s ease;
}

.results li:hover{
  text-decoration: underline;
}

.results li a{
  align-items:center;
  display:flex;
}

.results--queries span{
  font-weight:bolder;
}

.results--queries mark{
  background-color:transparent;
  font-weight:400;
  color:var(--colorTextBody);
}

.results-products__image{
  height:80px;
  max-width:80px;
  min-width:80px;
  width:100%;
}

.results-products__info{
  display:flex;
  flex-direction:column;
  padding-left: 10px
}

.predictive-search-results{
  display:flex;
  flex-direction:column;
  flex-wrap:wrap;
  overflow-y:auto;
  /*padding:40px 0 0;*/
}

@media only screen and (max-width:589px){
  .predictive-search-results {
    padding: 0 15px;
    padding-right: 0;
    padding-left: 0;
    flex-direction:row;
    height: 100%;
    padding-bottom: var(--buttonSize);
  }
}

.predictive-search-results h3{
  border-bottom:1px solid;
  border-color:var(--colorBorder);
  margin-bottom:10px;
  padding-bottom:10px;
}

.predictive-search-results--none{
  padding-top:20px;
}

.predictive-search__no-results{
  display:block;
  width:100%;
}

.predictive-search__no-results:hover{
  opacity:0.5;
}

.results__group-1{
  flex:100% 0 0;
}

.results__group-1>div:not(.results--queries){
  display:none;
}

.results__group-2{
  flex:100% 0 0;
}

.results__search-btn{
  border:1px solid;
  position: sticky;
  border-color:var(--colorBorder);
  padding:10px;
  transition:background-color 0.2s ease;
  width:100%;
}

.results__search-btn:hover{
  background-color:rgba(0,0,0,.05);
}

@container (min-width: 800px){
  .predictive-search-results{
    flex-direction:row;
    flex-wrap:nowrap;
    height: 100%
  }

  .results{
    padding:0 40px 40px;
  }

  .results__group-1{
    flex:1 0 0;
  }

  .results__group-1 div:not(.results--queries){
    display:block;
  }

  .results__group-2{
    flex:2 0 0;
  }

  .results__group-2>div:not(.results--products){
    display:none;
  }

  .results__search-btn{
    padding:10px 0 10px 40px;
    text-align:left;
  }
}

.predictive-overflow-hidden{
  overflow:hidden;
}

@media only screen and (max-width:768px){

  .predictive-overflow-hidden{
      overflow:auto;
  }
}
  
.btn--search {
  /*color: var(--colorBtnPrimary)!important;
  background: var(--colorBody)!important;*/
  position: fixed;
  bottom: 25px;
  /*left: 0*/
  left: 30px;
  width: calc(100% - 55px);
  z-index: 1;
}

.btn--search.results__search-btn {
  position: sticky;
  margin-top: 30px
}

@media only screen and (max-width:768px){

  .btn--search {
    left: 15px;
    width: calc(100% - 30px);
  }
}
/*======================= SITE HEADER =======================*/
.footer__social li {
  display: inline-block;
}

.site-header{
  position:relative;
  z-index: 28;
  padding:0px 0;
  /*background:var(--colorNav); */
}

@media only screen and (max-width:589px){
  .site-header{
    z-index: 200;
    z-index: 30;
  }
}

.site-header-container:has(.site-header--sticky) {
  position: sticky;
  top: 0;
  z-index: 29
}

@media only screen and (max-width:589px){
  .site-header-container:has(.site-header--sticky) {
    z-index: 30
  }
}

.site-toolbar {
  position: relative;
  z-index: 29
}

.is-light .site-header:not(.active_search--background), .is-light .site-toolbar {
  background:transparent!important;
}

.is-light .site-header:has(.site-nav--has-dropdown:hover) {
  background: rgb(var(--color-background))!important;
  background: var(--gradient-background)!important;
}

.site-toolbar select {
  border-color: transparent!important
}
.site-toolbar .selector-wrapper {
  flex: none;
  min-width: 0!important;
}
.site-toolbar .social__icons {
  display: flex;
  align-items: center;
  padding: 0 10px;
}
.social__icons--separator {
  display: flex;
  width: 20px
}
.site-toolbar-nav__social {
  margin: 0;
  display:inline-block;
}
.site-toolbar-nav__social li {
  margin: 0 15px 0px 0!important;
  float: left
}
/*.site-toolbar-nav__social li a:has(.icon){
  padding: 9px 0;
}*/

.site-toolbar-nav__social li a .icon:not(.icon-contact) {
  height: var(--buttonSize);
}



.site-toolbar__item .site-header__logo-link {
  height: 44px; 
  padding: 10px 0;
}

.site-toolbar__item .site-header__logo-link img {
  object-fit: contain!important;
  width:auto
}
              
.site-header--sticky{
    left: 0;
    right: 0;
    top: 0;
    z-index: 28;
}

.site-header:not(.site-header--sticky):has(.search__results.is-active) {
  /*position: fixed!important;
  width: 100%*/
}

.header-wrapper--overlay .site-header{
  position:absolute;
    left: 0;
    right: 0;
    z-index: 28;
}

.site-header--remove{
  transform:translate3d(0, -100%, 0);
  transition:transform 0.4s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.js-drawer-open--search .site-header--sticky{
  z-index:28;
}

@media only screen and (max-width:589px){
  .site-header--sticky{
    z-index:200;
  }
}

.site-header--opening{
  transform:translateZ(0)!important;
  transition:transform 0.4s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.site-header__logo{
  margin:13.33333px 0;
  display:block
}

.header-layout[data-logo-align=center] .site-header__logo{
  margin:0;
  margin:13.33333px 0;
}

.site-header__logo.disabled {
  pointer-events: none!important;
  cursor: default!important;
  color: #36373e50!important;
  opacity: 0.6;
  -webkit-filter: grayscale(100%);
  filter: grayscale(100%);
}

@media only screen and (min-width:590px){

  .text-center .site-header__logo{
    padding-right:0;
    margin:13.33333px auto
  }
}

.header-layout[data-logo-align=center] .site-header__logo{
  margin-left:auto;
  margin-right:auto;
  text-align:center
}

.site-header__logo a,.site-header__logo a:hover{
  text-decoration:none;
}

.site-header__logo img{
  display:block;
}

.header-layout--center .site-header__logo img{
  margin:0 auto
}

.site-header__logo-link{
  display:flex;
  align-items:center;
  /*color:#000000;
  color:var(--colorNavText)*/
}

a.site-header__logo-link:not(.site-header__logo-default, .site-header__logo-white) {
  max-width: fit-content!important;
}

/*.site-header__logo-link:hover{
  color:#000000;
  color:var(--colorNavText);
    }*/

@media only screen and (max-width:589px){

  .site-header__logo-link{
    margin:0 auto
  }

  .site-header__logo-link.a:not(.site-header__logo-default, .site-header__logo-white) {
    max-width: unset;
  }
}

.header-wrapper--overlay{
  position:relative;
  top:0;
  left:0;
  right:0;
  z-index:6;
  z-index:12;
  background:none;
  background:linear-gradient(180deg, rgba(0, 0, 0, 0.3) 0%,transparent)
}
@media only screen and (max-width:589px){
  .header-wrapper--overlay{
    z-index:200;
  }
}

.header-wrapper--overlay .site-header:not(.site-header--sticky){
  background:none;
}

.js-drawer-open--search .header-wrapper--overlay{
  z-index:28
}

.site-header__drawers{
  height:0;
  overflow:visible
}

@media only screen and (max-width:768px){

.site-header__drawers{
    padding:0
}
  }

.site-header__drawers-container{
  position:relative;
}

.site-header__drawer{
  display:none;
  position:absolute;
  top:1px;
  padding:20px;
  width:100%;
  max-height:75vh;
  max-height:var(--maxDrawerHeight);
  overflow-y:auto;
  background-color:#ffffff;
  background-color:var(--colorBody);
  color:#2a2b2f;
  color:var(--colorTextBody);
  box-shadow:0 10px 25px rgba(0, 0, 0, 0.15);
  z-index:5;
  transition:all 0.25s cubic-bezier(0.165, 0.84, 0.44, 1);
  transform:translateY(-100%)
}

@media only screen and (max-width:768px){

.site-header__drawer{
    top:0;
    z-index:3
}
  }

.site-header__drawer.is-active{
    display:block;
    transform:translateY(0);
  }

.header-wrapper--compressed .site-header__drawer{
    top:0
}

.site-header__drawer-animate{
  transform:translateY(-20px);
  opacity:0
}

.is-active .site-header__drawer-animate{
    opacity:1;
    transform:translateY(0px);
    transition:opacity 0.3s ease 0.15s,transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1) 0.15s
}

.site-header__drawer-animate.is-empty form{
    display:none;
  }

.site-header__mobile-nav{
  left:0;
  right:0;
  overflow-x:hidden;
}

.site-header-blurred {    
  max-width: 1240px!important;
  margin: auto;
  top: 20px;
  margin-bottom: 40px;
  border-radius: calc(var(--imageRadius)*0.9)
}

.header-wrapper:has(.site-header-blurred)  {
  padding: 0 0px;
  padding-bottom: 0px
}

@media only screen and (min-width:1240px){
  .header-wrapper:has(.site-header-blurred) {
    padding: 0 20px;
  }
}

.site-header-blurred {
  -webkit-backdrop-filter: saturate(180%) blur(10px);
  backdrop-filter: saturate(180%) blur(10px);
  background-color: rgba(var(--color-background), 0.8)!important;
}

.site-header-blurred .site-nav__dropdown {
  background-color: rgba(var(--color-background), 1)!important;
}

.site-header-blurred .site-toolbar {
  background-color: rgba(var(--color-background), 0.8)!important;
  border-radius: calc(var(--imageRadius)*.9);
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

.site-header-blurred:has(.is-focused), .site-header-blurred:has(.search__results.is-active), .site-header-blurred:has(.site-nav--has-dropdown:hover) {
  background-color: rgba(var(--color-background), 1)!important;
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

.site-header-blurred .site-header__search-container .site-page__search, .site-header-blurred .site-header__search {
  background-color: rgba(var(--color-background), 0.8)!important;
  border-radius: calc(var(--imageRadius)*.9);
}

.site-header-blurred .site-header__search-container .site-page__search:has(.search__results.is-active), .site-header-blurred .site-header__search:has(.search__results.is-active) {
  border-radius: calc(var(--imageRadius)*.9);
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

@media only screen and (max-width:1240px){
  .site-header-blurred {
    margin-left: 20px;
    margin-right: 20px
  }
}

.site-header-blurred .search__results {
  border-radius: calc(var(--imageRadius)*.9);
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

@media only screen and (max-width:768px){
  .site-header-blurred .search__results {
    margin-left: calc(var(--pageWidthPadding, 17px)*0);
    padding: 0;
    margin-top: 10px;
    width: calc(100% + 30px);
    left: -15px;
    position: absolute
  }

  .site-header-blurred.active_search--background {
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
  }
}

.js-drawer-closing .predictive-results{
  opacity:0;
  transition:opacity 0.1s ease-in
}

.predictive__label{
  border-bottom:1px solid;
  border-bottom-color:#e8e8e1;
  border-bottom-color:var(--colorBorder);
    padding-bottom:5px;
    margin-bottom:20px;
    }

.predictive-result__layout>div{
  margin-bottom:40px
}

.predictive-result__layout>div:last-child{
  margin-bottom:0;
}

.predictive-result__layout .grid__item{
  margin-bottom:20px;
  opacity:0;
  animation:fade-in 0.5s ease forwards

}

.predictive-result__layout .grid__item:last-child{
  margin-bottom:0;
}

.predictive-result__layout .grid__item:nth-child(2){
  animation-delay:150ms;
}

.predictive-result__layout .grid__item:nth-child(3){
  animation-delay:300ms;
}

.predictive-result__layout .grid__item:nth-child(4){
  animation-delay:450ms;
}

.predictive-result__layout .grid__item:nth-child(5){
  animation-delay:600ms;
}

.predictive-result__layout .grid__item:nth-child(6){
  animation-delay:750ms;
}

.predictive-result__layout .grid__item:nth-child(8){
  animation-delay:900ms;
}

.predictive__result{
  display:flex;
  align-items:center;
}

.predictive__result-image{
  flex:0 1 60px;
  width:60px;
  height:60px;
  margin-right:20px
}

@media only screen and (min-width:590px){

  .predictive__result-image{
    flex:0 1 100px;
    width:100px;
    height:100px
  }
}

.predictive__result-meta{
  flex:1 1 auto;
}

.predictive-results__footer{
  padding:30px 0 30px
}

@media only screen and (min-width:590px){

  .predictive-results__footer{
    padding:20px 0 40px
  }
}

.search-bar{
  max-width:100%;
}

.search-bar--page{
  max-width:300px;
  margin-top:-20px;
}

.search-bar--drawer{
  margin-bottom:20px;
  padding-bottom:20px;
  border-bottom:1px solid;
  border-bottom-color:#e8e8e1;
  border-bottom-color:var(--colorDrawerBorder)
    }

.search-bar--drawer input{
  border:0;
}

.search-bar .icon{
  width:24px;
  height:24px;
  vertical-align:middle;
}

.section-header{
  margin-bottom:30px
}

@media only screen and (min-width:590px){

  .section-header{
    margin-bottom:40px
  }

  [data-style=slider] .section-header {
    margin-bottom: 0px;
  }
}

.section-header--hero{
  position:relative;
  flex:1 1 100%;
  color:#ffffff;
  color:var(--colorHeroText);
    margin-bottom:20px
    }

.section-header--hero .breadcrumb a {
  color: var(--colorHeroText);
}

@media only screen and (min-width:590px){

  .section-header--hero{
    margin-bottom:40px
  }
}

.section-header__rte{
  margin-top:20px;
}

.section-header__title{
  margin-bottom:0;
}

.section-header__title--big{
  font-size:40px
}

@media only screen and (min-width:590px){

  .section-header__title--big{
    font-size:80px
  }
}

.section-header__title--medium{
  font-size:32px
}

@media only screen and (min-width:590px){

  .section-header__title--medium{
    font-size:60px
  }
}

.section-header__link{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          font-weight:400;
          font-size:var(--fontBaseSize);
            padding-top:6px;
            float:right
            }

@media only screen and (max-width:589px){

  .section-header__link{
    display:none
  }
}

.section-header--404{
  margin-bottom:0;
  padding:60px 0;
}

.section-header select{
  margin-left:20px;
}

.section-header .btn{
  float:right;
  margin:0;
}

.section-header .rte .btn{
  float:none;
}

.site-nav{
  margin:0;
  /*height: 100%;*/
  display: flex;
  flex-flow: wrap;
}

.text-center .site-navigation{
  margin:0 auto;
  justify-content: center
}

.header-layout--left .site-navigation{
  padding-left:13.33333px
}

.header-layout--left {
  
}

.header-layout--left .grid__item-header--icons {
  flex: 1 0 auto;
}

.site-nav--icons{
  display:flex;
  align-items:center;
}

.site-nav__icons{
  white-space:nowrap;
  font-size:0;
}

.site-nav__item{
  position:relative;
  display:inline-block;
  margin:0;
}

.site-footer__linklist .site-nav__item{
  display: list-item
}

.site-nav__item li{
  display:block;
}

.site-nav__item .icon-chevron-down{
  width:10px;
  height:10px;
}

/*.site-nav__link{
  display:inline-block;
  vertical-align:middle;
  text-decoration:none;
  padding:16px 20px;
  margin: auto 0;
  white-space:nowrap;
  color:#000000;
  color:var(--colorNavText)
}*/

.site-nav__link{
  display:inline-block;
  vertical-align:middle;
  text-decoration:none;
  padding:14px 20px;
  margin: -6px 0;
  white-space:nowrap;
  /*color:#000000;
  color:var(--colorNavText)*/
}

.site-nav__item .site-nav__link{
  height: 100%;
  margin: 0 auto;
  gap: 5px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.site-footer__linklist .site-nav__item .site-nav__link, .site-toolbar__item .site-nav__item .site-nav__link {
  
  justify-content: left;
}


.site-header--heading-style .site-nav__link{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    font-weight:var(--fontHeaderWeight);
      letter-spacing:var(--fontHeaderSpacing);
        line-height:var(--fontHeaderLineHeight)
          }

[data-header-capital=true] .site-header--heading-style .site-nav__link{
  text-transform:uppercase
}

/*.site-nav__link:hover{
  color:#000000;
  color:var(--colorNavText);
  color:var(--colorTextBodyHover);
    }*/

/*.is-light .site-nav__link{
  color:var(--colorNavText);
}*/

/*.is-light .site-nav__link:hover{
  color:var(--colorTextBodyHover);
}*/

/*.is-light [data-overlay=true] .site-nav__link{
  color:var(--colorNavTextOverlay);
}*/

[data-theme="dark"] [data-overlay=true] .logo--has-inverted {
  opacity: 0;
  display: none;
  visibility: unset;
    overflow: hidden;
}

[data-theme="dark"] [data-overlay=true] .logo--inverted {
    opacity: 1;
    display: flex!important;
    align-items: center;
    visibility: unset;
    overflow: visible;
}

/*.is-light [data-overlay=true] .site-nav__link:hover{
  color:var(--colorTextBodyHoverOverlay);
}*/

.site-nav--has-dropdown>.site-nav__link{
  position:relative;
  z-index:6
}

.site-nav__link .icon-chevron-down{
  margin-left:5px;
}

@media only screen and (max-width:768px){

  .site-nav__link{
    padding:10px
  }
}

.site-nav__link--spacing-bottom {
  margin-bottom: 5px;
}

.site-nav__link,
.site-nav__dropdown-link:not(.site-nav__dropdown-link--top-level){
  font-size: var(--fontMenuSize)!important;
    }
.megamenu__colection-title {
  font-size: var(--fontMenuSize)!important;
    }
.megamenu__colection-featured-title {
  font-size: var(--fontMenuSize)!important;
    }

.site-nav--has-dropdown{
  z-index:6;
}

.site-nav--has-dropdown.is-focused,.site-nav--has-dropdown:hover{
  z-index:7;
}

.site-nav--has-dropdown.is-focused>a,.site-nav--has-dropdown:hover>a, .site-nav__item:hover, .site-nav__item:hover a.site-nav__link:not(.megamenu__collection, .product__meta-link) {
  /*color:var(--colorTextBody) !important;
  background-color:var(--colorBody);*/
  opacity:1;
  transition:none;
  z-index: 29;
  /*text-decoration: underline;*/
}

.site-nav--has-dropdown>a:after,
.site-nav--has-dropdown>a:after, 
.site-nav__item:not(.site-nav--is-megamenu):after, 
.site-nav__item a.site-nav__link:not(.megamenu__collection, .product__meta-link):after {
  display: block;
  position: absolute;
  content: "";
  bottom: 9px;
  top: auto;
  left: 19px;
  right: 100%;
  background: rgba(var(--color-button),var(--alpha-button-background));
  height: calc(var(--buttonOutlineWeight));
   transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  width: 0rem
}

.media__text:has(a.h2.product-single__title:not(:hover)) span:after {
  display: block;
  position: absolute;
  content: "";
  bottom: 0px;
  top: auto;
  left: 2px;
  background: rgba(var(--color-button),var(--alpha-button-background));
  height: calc(var(--buttonOutlineWeight) / 1.5);
   transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  width: 0rem
}

@media only screen and (max-width:768px){
  .site-nav--has-dropdown>a:after, .site-nav__item:not(.site-nav--is-megamenu):after, .site-nav__item a.site-nav__link:not(.megamenu__collection,.product__meta-link):after {
    left: 10px
  }
}

.site-nav--has-dropdown.is-focused>a:after,
.site-nav--has-dropdown:hover>a:after, 
.site-nav__item:has(a):hover:not(.site-nav--is-megamenu):after, 
.site-nav__item:hover a.site-nav__link:not(.megamenu__collection, .product__meta-link, :has(.icon)):after,
.media__text a.h2.product-single__title:hover span:after {
  transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  width: 1.5rem
}

.site-nav__item:has(.icon):not(.site-nav--is-megamenu):after {
  left: 29px;
  bottom: 0
}

.site-nav--has-dropdown.is-focused>a:before,.site-nav--has-dropdown:hover>a:before {
  content:"";
  position:absolute;
  left:15px;
  right:45px;
  bottom:-5px;
  display:block;
  background-color:var(--colorBorder);
  height:0px;
  z-index:6;
}

.site-nav__link--icon{
  font-size:0;
  padding-left:12px;
  padding-right:12px;
  cursor: pointer;
  color: currentColor
}

@media only screen and (max-width:589px){

  .site-nav__link--icon{
    padding-left:10px;
    padding-right:10px
  }
}

.site-nav__link--icon .icon, .icon--big{
  width:30px;
  height:30px;
  margin-bottom: 3px;
}

@media only screen and (max-width:589px){

  .site-nav__link--icon .icon{
    width:27px;
    height:27px;
  }
}

.mobile-nav-trigger path,.site-nav__compress-menu path{
  transition:all 0.3s cubic-bezier(0.18, 0.77, 0.58, 1);
}

.mobile-nav-trigger.is-active path:first-child,.site-nav__compress-menu.is-active path:first-child{
  transform:rotate(45deg);
  transform-origin:20% 30%;
}

.mobile-nav-trigger.is-active path:nth-child(2),.site-nav__compress-menu.is-active path:nth-child(2){
  opacity:0;
}

.mobile-nav-trigger.is-active path:nth-child(3),.site-nav__compress-menu.is-active path:nth-child(3){
  transform:rotate(-45deg);
  transform-origin:15% 66%;
}

/*@media only screen and (max-width:768px){
  .mobile-nav-open .site-nav__link:not(.mobile-nav-trigger){
    display:none;
  }
}*/

.site-nav__dropdown{
  position:absolute;
  left:0;
  margin:0;
  z-index:27;
  display:block;
  visibility:hidden;
  /*background-color:#ffffff;
  background-color:var(--colorBody);*/
  min-width:100%;
  width: 100%;
  padding:10px 0 5px;
  box-shadow:0px 10px 20px rgba(0, 0, 0, 0.09);
  transform:translate3d(0px, -3%, 0px)
}

.is-focused>.site-nav__dropdown,.site-nav--has-dropdown:hover .site-nav__dropdown{
  display:block;
  visibility:visible;
  transform:translateZ(0px);
  transition:all 500ms cubic-bezier(0.2, 0.06, 0.05, 0.95)
}

.is-focused>.site-nav__dropdown li,.site-nav--has-dropdown:hover .site-nav__dropdown li{
  opacity:1;
  transform:translateY(0);
  transition:opacity 0.5s ease,transform 0.5s cubic-bezier(0.2, 0.06, 0.05, 0.95)
}

.site-nav__dropdown li{
  margin:0;
  opacity:0;
  transform:translateY(15px);
  transition:none;
}

.site-nav__dropdown>li{
  position:relative
}

.site-nav__dropdown>li>a{
  position:relative;
  z-index:6;
}

.site-nav__dropdown a{
  /*background-color:#ffffff;
  background-color:var(--colorBody);*/
    padding-right:40px;
    }

.site-nav__deep-dropdown{
  background-color:#ffffff;
  background-color:var(--colorBody);
    box-shadow:0px 10px 20px rgba(0, 0, 0, 0.09);
    position:absolute;
    top:0;
    left:100%;
    margin:0;
    visibility:hidden;
    opacity:0;
    z-index:5;
    transform:translate3d(-12px, 0px, 0px)
    }

.header-layout--center .site-nav__item:last-child .site-nav__deep-dropdown{
  left:auto;
  right:100%
}

.is-focused>.site-nav__deep-dropdown,.site-nav__deep-dropdown-trigger:hover .site-nav__deep-dropdown{
  visibility:visible;
  opacity:1;
  transform:translateZ(0px);
  transition:all 300ms cubic-bezier(0.2, 0.06, 0.05, 0.95)
}

.site-nav__deep-dropdown:before{
  content:"";
  display:block;
  position:absolute;
  top:0;
  left:0;
  bottom:0;
  width:10px;
  background-image:linear-gradient(90deg, rgba(0, 0, 0, 0.09), transparent);
  pointer-events:none
}

.header-layout--center .site-nav__item:last-child .site-nav__deep-dropdown:before{
  left:auto;
  right:0;
  background-image:linear-gradient(270deg, rgba(0, 0, 0, 0.09), transparent)
}

.site-nav__deep-dropdown-trigger:hover .site-nav__dropdown-link--has-children,.site-nav__dropdown-link--has-children:focus,.site-nav__dropdown-link--has-children:hover{
  padding:8px 35px 8px 25px
}

.site-nav__deep-dropdown-trigger .icon-chevron-down{
  position:absolute;
  top:50%;
  right:10px;
  width:10px;
  height:10px;
  transform:rotate(-90deg) translateX(50%);
}

.mobile-nav{
  margin:-20px -20px 0 -20px
}

.mobile-nav li{
  margin-bottom:0;
  list-style:none;
}

.mobile-nav__search{
  padding:20px;
}

.mobile-nav__footer {
  bottom: 0px;
    position: absolute;
  width: 100%;
  background-color: white;
  background-color: var(--colorDrawers);;
  left: 0;
  padding-left: 15px;
  padding-right: 15px;
}

.mobile-nav__social{
  margin:5px 0 -5px -5px;
  
}

.mobile-nav__social li{
  margin-right:10px;
  display:inline-block;
}

.mobile-nav__social a{
  padding:10px 5px;
}

.mobile-nav__social .icon{
  width:24px;
  height:24px;
}

.icon-on-shop {
  margin-right: 0px;
  position: relative;
  top: 2px;
}


@media only screen and (min-width:590px){
  .site-nav__link--icon .icon{
    width:28px;
    height:28px;
  }

  .icon-on-shop {
    margin-right: -20px;
  }
}

.cart-link{
  position:relative;
  display:inline-block;
  line-height:1;
}

.cart-link__bubble {
  display: none;
  position: absolute;
  top: 50%;
  right: -4px;
  font-size: 11px;
  line-height: 1;
  font-weight: 700;
  letter-spacing: 0;
  text-align: center;
}

.cart-link__bubble-num {
  position: relative;
  color: #fff;
  top: 4px;
}

.cart-link__bubble--visible{
  display:block;
  position:absolute;
  top:0px;
  right:-4px;
  width:20px;
  height:20px;
  background-color:var(--colorAlert);
      border-radius:50%
      }

[data-icon=bag] .cart-link__bubble--visible{
  top:50%;
  right:-5px;
}

[data-icon=bag-minimal] .cart-link__bubble--visible{
  top:50%;
  right:-5px;
}

.breadcrumb{
  font-size:13px;
  margin-bottom:10px
}

.cart-page-form .breadcrumb{
  margin-bottom:20px;
    padding-top: 15px;
}

@media only screen and (max-width:589px){

  .breadcrumb{
    font-size:11px;
  }
}

.breadcrumb__divider{
  color:currentColor;
}

.breadcrumb-icon {
  position: unset;
  height: 10px;
    width: 21px;
}

.btn-icon {
  position: unset;
  height: 15px;
    width: 15px;
  margin-right: 7px;
}

.modal{
  display:none;
  bottom:0;
  left:0;
  opacity:1;
  overflow:hidden;
  position:fixed;
  right:0;
  top:0;
  z-index: 151;
  color:#fff;
  align-items:center;
  justify-content:center
}

.modal a,.modal a:hover{
  color:inherit;
}

/*.modal .btn:not([disabled]),.modal .btn:not([disabled]):hover{
  color:#ffffff;
  color:var(--colorBtnPrimaryText);
    }
*/
.modal.modal--quick-shop{
  align-items:flex-start;
}

.modal-open .modal .modal__inner{
  animation:modal-open 0.5s forwards;
  animation:modal-open 0s forwards;
}

.modal-open .modal:before{
  content:"";
  position:fixed;
  top:0;
  left:0;
  width:100%;
  height:100%;
  background-color:#000000;
  background-color:var(--colorModalBg);
    animation:overlay-on 0.5s forwards;
    cursor:pointer;
    }

.modal-closing .modal .modal__inner{
  animation:modal-closing 0.5s forwards;
}

.modal-closing .modal:before{
  content:"";
  position:fixed;
  top:0;
  left:0;
  width:100%;
  height:100%;
  background-color:#000000;
  background-color:var(--colorModalBg);
    animation:overlay-off 0.5s forwards;
    }

.modal-open--solid .modal:before{
  background-color:#000000;
  background-color:var(--colorModalBg)
    }

.modal-open .modal--solid:before{
  background-color:#000;
  animation:full-overlay-on 0.5s forwards;
}

.modal-closing .modal--solid:before{
  background-color:#000;
  animation:full-overlay-off 0.5s forwards;
}

.modal--is-closing{
  display:flex !important;
  overflow:hidden;
}

.modal--is-active{
  display:flex !important;
  overflow:hidden;
}

.modal-full.modal--is-active {
  display:block!important;
}
  
@media only screen and (min-width:590px){
  .modal-open {
    overflow:hidden;
  }
}

.modal__inner{
  transform-style:preserve-3d;
  flex:0 1 auto;
  margin:20px;
  max-width:100%;
  display:flex;
  align-items:center;
  box-shadow: 0px 10px 20px rgb(0 0 0 / 9%);
}

.modal-full .modal__inner {
  display: block;
}

@media only screen and (min-width:590px){

  .modal__inner{
    margin:40px
  }
}

/*.modal--square .modal__inner{
  background:var(--colorDrawers);
    color:#2a2b2f;
    color:var(--colorTextBody)
      }
*/
.modal__inner img:not(.product__image){
  display:block;
  max-height:90vh;
}

.modal__inner .image-wrap img{
  max-height:none;
}

.modal__centered{
  position:relative;
  flex:0 1 auto;
  min-width:1px;
  max-width:100%;
}

.modal-locator {
  width: 410px;
}

@media only screen and (max-width:589px){
  .modal-locator {
    width: 100%;
    margin: 50px 0;
    text-align: center;
  }
}

.modal--square .modal__centered-content{
  max-height:80vh;
  padding:30px;
  min-width:200px;
  min-height:190px;
  overflow:auto;
  -webkit-overflow-scrolling:touch
}


@media only screen and (min-width:590px){
  .modal--square .modal__centered-content{
    padding:60px;
    max-height:90vh;
  }
}

.modal__close{
  border:0;
  padding:20px;
  position:fixed;
  top: 15px;
    right: 15px;
    background: yellow;
    border-radius: 50%;
  background: var(--colorDrawers);
    background: transparent;
  color:#fff
}

@media only screen and (min-width:590px){

  .modal__close{
    padding:40px
  }
}

.modal__close .icon{
  width:28px;
  height:28px;
}

.modal__close:focus,.modal__close:hover{
  color:#fff;
}

.modal--square .modal__close{
  position:absolute;
  color: rgba(var(--color-foreground), 1);
  background: rgb(var(--color-background));
  padding:13.33333px;
  padding:6px
    }

.modal--square .modal__close:focus,.modal--square .modal__close:hover{
  /*color:#2a2b2f;
  color:var(--colorTextBody);*/
  filter: brightness(var(--colorBtnFilterBrightness));
    }

.modal .page-content,.modal .page-width{
  padding:0;
}

.popup-cta{
  margin-bottom:20px;
}


/*================== POPUP ========================*/
.modal--unlocked{
  top:auto;
  bottom:0;
  overflow:auto;
  justify-content: flex-end;
}

.modal--unlocked.modal--square .modal__centered-content{
  padding:20px 20px 0;
}

.modal-full .modal__centered-content {
  min-height:80px;
  padding:20px 20px 20px!important;
}

.modal--unlocked.modal--is-active{
  overflow:auto;
}
.modal-open .modal--unlocked:before{
  display:none;
}
.modal-closing .modal--unlocked:before{
  display:none;
}

.modal--unlocked .modal__inner{
  margin:10px;
  box-shadow:0 10px 20px rgba(0, 0, 0, 0.3);
  max-width: 480px;
}

.modal-full.modal--unlocked .modal__inner {
  margin: 0px;
  margin-top: 10px;
  max-width: 100%;
}

.modal--unlocked .h1{
  padding-right:25px;
}

.modal--unlocked input{
  font-size:16px !important;
}

.modal--unlocked .input-group{
  margin:0 auto 20px;
}

.modal--unlocked .btn{
  min-width:auto;
}

.modal--unlocked .text-close{
  display:none;
}

@media only screen and (max-width:589px){
  .modal--mobile-friendly{
    bottom:0;
    overflow:auto;
    justify-content: center;
  }

  .custom-popup.modal--mobile-friendly{
    z-index: 200;
    top:10px
  }
  .modal--mobile-friendly.modal--square .modal__centered-content{
    padding:20px 20px 0;
  }

  .modal--mobile-friendly.modal--is-active{
    overflow:auto;
  }

  .modal-open .modal--mobile-friendly:before{
    display:none;
  }
  .custom-popup.modal--mobile-friendly:before{
    display:block;
  }
  
  .modal-closing .modal--mobile-friendly:before{
    display:none;
  }
  
  .custom-popup.modal-open .modal:before{
  content:"";
  position:fixed;

  top:0;
  left:0;
  width:100%;
  height:100%;
  background-color:#000000;
  background-color:var(--colorModalBg);
    animation:overlay-on 0.5s forwards;
    cursor:pointer;
    }

  .modal--mobile-friendly .modal__inner{
    margin:0px;
    box-shadow:0 10px 20px rgba(0, 0, 0, 0.3);
  }

  .modal--mobile-friendly .h1{
    padding-right:25px;
  }

  .modal--mobile-friendly input{
    font-size:16px !important;
  }

  .modal--mobile-friendly .input-group{
    margin:0 auto 20px;
  }

  .modal--mobile-friendly .btn{
    min-width:auto;
  }

  .modal--mobile-friendly .text-close{
    display:none;
  }
}

.js-qty__wrapper{
  position:relative;
  max-width:100px;
  min-width:75px;
  overflow:visible;
  border-radius: var(--buttonRadius);
  height: var(--buttonSize);
  background-color:#ffffff;
  background-color:var(--colorInputBg);
    background-color:transparent;
    color:#2a2b2f;
    color:var(--colorInputText);
      }

.text-center .js-qty__wrapper {
  margin-left: auto;
  margin-right: auto
}

@media only screen and (max-width:589px){
  .small--text-center .js-qty__wrapper {
    margin-left: auto;
    margin-right: auto
  }
}

.js-qty__num{
  display:block;
  background:none;
  text-align:center;
  width:100%;
  padding:8px 25px;
      padding-top: 13.75px;
    padding-bottom: 13.75px;
  margin:0;
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
  z-index:1;
}

.js-qty__adjust{
  cursor:pointer;
  position:absolute;
  display:block;
  top:0;
  bottom:0;
  border:0 none;
  background:none;
  text-align:center;
  overflow:hidden;
  padding:0 5px;
  line-height:1;
  -webkit-user-select:none;
  user-select:none;
  -webkit-backface-visibility:hidden;
  backface-visibility:hidden;
  transition:background-color 0.1s ease-out;
  z-index:2;
  /*fill:#2a2b2f;
  fill:var(--colorInputText);
    color:#2a2b2f;
  color:var(--colorInputText);*/
    padding-left: 12px;
    padding-right: 12px;

  color: rgba(var(--color-foreground), 1);
    }


.js-qty__adjust .icon{
  display:block;
  font-size:8px;
  vertical-align:middle;
  width:13px;
  height:13px;
  fill:inherit;
  margin: auto
}

.js-qty__adjust:hover{
  color: rgb(var(--color-background));
  background: rgba(var(--color-border),1);
}

.js-qty__adjust:active{
  opacity: 0.6;
  color: rgb(var(--color-button-text));
    background: rgba(var(--color-button),var(--alpha-button-background));
      filter: brightness(1);
    }

.js-qty__num:active~.js-qty__adjust,.js-qty__num:focus~.js-qty__adjust{
  border-color:#2a2b2f;
  border-color:rgba(var(--color-border),1);
    }

.js-qty__adjust--plus{
  right:0;
  padding-right:12px;
  border-top-right-radius: var(--buttonRadius);
        border-bottom-right-radius: var(--buttonRadius);
}

.js-qty__adjust--minus{
  left:0;
  padding-left:13px!important;
  
  border-top-left-radius: var(--buttonRadius);
        border-bottom-left-radius: var(--buttonRadius);
}

/* contact form */
.js-qty__full-width .js-qty__wrapper {
  width: 100%;
  max-width: unset;
      margin-bottom: 12px;
}

.js-qty__full-width .js-qty__wrapper label {
  left: calc(var(--buttonOutlineWeight) / 2 + 10px)!important
}

.js-qty__full-width .js-qty__num {
  padding-left: 11px
}

[data-bottom-border=true] .js-qty__full-width .js-qty__num {
  padding: 11px calc(var(--buttonOutlineWeight) / 2 + 10px);
}

.js-qty__full-width .js-qty__adjust--minus {
  width: var(--buttonSize);
  right: var(--buttonSize);
  left: unset;
  border-radius: 0;
}

.contact-form .js-qty__full-width .js-qty__num {
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 1);
  border-radius: var(--buttonRadius);
}

[data-bottom-border=true] .contact-form .js-qty__wrapper:has(.js-qty__adjust:hover) .js-qty__num {
  padding-top: 15px!important
}

.js-qty__full-width .js-qty__adjust--plus {
  width: var(--buttonSize);
}

.drawer .js-qty__wrapper{
  background-color:transparent;
  border-color:#e8e8e1;
  border-color:var(--colorDrawerBorder);
    max-width:100px;
    }

.drawer .js-qty__num{
  /*color:#2a2b2f;
  color:var(--colorTextBody );*/
    border-color:#e8e8e1;
    border-color:var(--colorTextBodyBorder );
      padding-top:13.75px;
      padding-bottom:13.75px;
      }

.drawer .js-qty__adjust{
  border-color:#e8e8e1;
  border-color:var(--colorBtnPrimary );
    /*color:#2a2b2f;
    color:var(--colorInputText  );
      fill:#2a2b2f;
      fill:var(--colorTextBody )*/
        }

.drawer .js-qty__adjust:hover{
  /*background-color:#e8e8e1;
  background-color:var(--colorTextBody );
    color:#ffffff;
    color:var(--colorDrawers);
      fill:#ffffff;
      fill:var(--colorDrawers);*/
        }

.drawer .js-qty__num:active~.js-qty__adjust,.drawer .js-qty__num:focus~.js-qty__adjust{
  border-color:#e8e8e1;
  border-color:var(--colorTextBodyBorder );
    }

.currency-flag{
  position:relative;
  display:inline-block;
  vertical-align:middle;
  width:var(--buttonSize);
  height:var(--buttonSize);
  overflow:hidden;
  border-radius:50%;
  box-shadow:inset 0 0 1px 0 rgba(0, 0, 0, 0.3)
}

.currency-flag:after,.currency-flag:before{
  content:"";
  display:block;
  position:absolute;
  top:0;
  left:0;
  right:0;
  bottom:0;
  border-radius:50%;
}

.currency-flag:before{
  content:attr(data-flag);
  font-size:16px;
  margin:1px;
  background-color:#000;
  color:#fff;
  text-align:center;
  font-weight:700;
  line-height:49px;
}

.currency-flag--small{
  width:20px;
  height:20px
}

.currency-flag--small:before{
  display:none;
}

.currency-options__label{
  display:inline-block;
  vertical-align:middle;
  width:100px
}

.currency-options__label span{
  border-bottom:2px solid transparent
}

.is-active .currency-options__label span{
  border-bottom:2px solid currentColor
}

.currency-options__label--inline{
  display:inline;
  width:auto;
  padding-left:5px;
}

.disclosure{
  position:relative;
}

.disclosure__toggle{
  white-space:nowrap;
}

.disclosure-list{
  background-color:#ffffff;
  background-color:var(--colorBody);
    color:#2a2b2f;
    color:var(--colorTextBody);
      bottom:100%;
      padding:10px 0px;
      margin:0;
      position:absolute;
      display:none;
      min-height:92px;
      max-height:60vh;
      overflow-y:auto;
      border-radius:0;
      box-shadow:0px 0px 20px rgba(0, 0, 0, 0.09)
      }

.modal-locator .disclosure-list {
  width: 230px;
}

.disclosure-list a{
  color:currentColor;
}

.disclosure-list--visible{
  display:block;
}

.disclosure-list__item{
  white-space:nowrap;
  padding:5px 15px 4px;
  text-align:left
}

.disclosure-list__item .currency-options__label{
  border-bottom:1px solid transparent;
}

.disclosure-list__option:focus .currency-options__label,.disclosure-list__option:hover .currency-options__label{
  border-bottom:1px solid currentColor;
}

.disclosure-list__item--current .currency-options__label{
  border-bottom:1px solid currentColor;
}

.shopify-model-viewer-ui .shopify-model-viewer-ui__controls-area{
  opacity:1;
  background:#ffffff;
  background:var(--colorBody);
    border-color:rgba(42, 43, 47, 0.05);
    border-radius:50px;
    }

.shopify-model-viewer-ui .shopify-model-viewer-ui__button{
  color:#2a2b2f;
  color:var(--colorTextBody);
    }

.shopify-model-viewer-ui .shopify-model-viewer-ui__button--control:hover{
  color:#2a2b2f;
  color:var(--colorTextBody);
    }

.shopify-model-viewer-ui .shopify-model-viewer-ui__button--control.focus-visible:focus,.shopify-model-viewer-ui .shopify-model-viewer-ui__button--control:active{
  color:#2a2b2f;
  color:var(--colorTextBody);
    background-color:rgba(42, 43, 47, 0.05);
    }

.shopify-model-viewer-ui .shopify-model-viewer-ui__button--control:not(:last-child):after{
  border-color:rgba(42, 43, 47, 0.05);
}

.shopify-model-viewer-ui .shopify-model-viewer-ui__button--poster{
  background-color:#2a2b2f;
  background-color:var(--colorTextBody);
    color:#ffffff;
    color:var(--colorBody);
      border-radius:100%;
      border:1px solid;
      border-color:rgba(28, 29, 29, 0.05)
      }

.shopify-model-viewer-ui .shopify-model-viewer-ui__button--poster:focus,.shopify-model-viewer-ui .shopify-model-viewer-ui__button--poster:hover{
  color:#ffffff;
  color:var(--colorBody);
    }

.product-single__view-in-space{
  display:block;
  color:#2a2b2f;
  color:var(--colorTextBody);
    background-color:rgba(42, 43, 47, 0.08);
    width:100%;
    padding:5px 10px 10px;
    border-radius: var(--buttonRadius);
      margin: 40px 0 10px;
    }

.product-single__view-in-space[data-shopify-xr-hidden]{
  display:none;
}

.product-single__view-in-space-text{
  display:inline-block;
  vertical-align:middle;
  margin-left:5px;
  font-size:0.9em;
}

.shopify-model-viewer-ui,.shopify-model-viewer-ui model-viewer{
  display:block;
  position:absolute;
  top:0;
  left:0;
  width:100%;
  height:100%;
}

.shopify-model-viewer-ui__button[hidden]{
  display:none;
}

.product-single__close-media{
  position:absolute;
  top:10px;
  right:10px;
  z-index:2;
}

.collapsibles-wrapper--border-bottom{
  border-bottom:calc(var(--dividerWeight)/2) solid;
  /*border-bottom-color:#e8e8e1;
  border-bottom-color:var(--colorBorder);*/
  border-bottom-color: rgba(var(--color-border), 1)
    }

.collapsible-trigger{
  color:inherit;
  position:relative;
}

.mobile-nav__toggle button{
  padding:20px 30px;
}

.collapsible-trigger__icon{
  display:block;
  position:absolute;
  right:0;
  top:50%;
  width:12px;
  height:12px;
  transform:translateY(-50%)
}

@media only screen and (max-width:589px){

  .collapsible-trigger__icon{
    width:10px;
    height:10px
  }
}

.selector-wrapper .collapsible-trigger__icon, .contact-selector .collapsible-trigger__icon, .product__bundle--wrapper .collapsible-trigger__icon, .variant-wrapper--dropdown .collapsible-trigger__icon { 
  right: 20px
}

.mobile-nav__has-sublist .collapsible-trigger__icon{
  right:20px
}

.collapsible-trigger__icon .icon{
  display:block;
  width:12px;
  height:12px;
  transition:all 0.2s ease-in
}

@media only screen and (max-width:589px){

  .collapsible-trigger__icon .icon{
    width:10px;
    height:10px
  }
}

.collapsible-trigger--inline{
  font-weight:700;
  padding:11px 0 11px 20px
}

.collapsible-trigger--inline .collapsible-trigger__icon{
  right:auto;
  left:0;
}

.collapsible-trigger__icon--circle{
  border:1px solid;
  border-color:#e8e8e1;
  border-color:var(--colorBorder);
    border-radius:50%;
    width:24px;
    height:24px;
    text-align:center
    }

.collapsible-trigger__icon--circle .icon{
  position:absolute;
  top:50%;
  left:50%;
  transform:translate(-50%, -50%);
}

.collapsible-trigger.is-open .collapsible-trigger__icon>.icon-chevron-down{
  transform:rotate(180deg);
}

.collapsible-trigger .collapsible-trigger__icon .icon-minus,.collapsible-trigger.is-open .collapsible-trigger__icon .icon-plus{
  display:none;
}

.collapsible-trigger.is-open .collapsible-trigger__icon .icon-minus{
  display:block;
}

.collapsible-trigger__layout{
  display:flex;
  align-items:center;
  justify-content:space-between
}

.collapsible-trigger__layout>span{
    display:block;
    padding-right:10px;
  }

.collapsible-trigger__layout--inline{
  position:relative;
  justify-content:flex-start
}

.collapsible-trigger__layout--inline>span{
    padding-right:15px;
  }

.collection-mobile-filters .collapsible-trigger__layout--inline{
    justify-content:space-between
}

.collapsible-trigger__layout--inline .collapsible-trigger__icon{
    position:static;
    transform:none;
  }

.collapsible-content{
  transition:opacity 0.3s cubic-bezier(.25,.46,.45,.94),height 0.3s cubic-bezier(.25,.46,.45,.94)
}

.collapsible-content.is-open{
  overflow:unset;
  position:relative;
  visibility:visible;
  opacity:1;
  transition:opacity 1s cubic-bezier(.25,.46,.45,.94),height 0.5s cubic-bezier(.25,.46,.45,.94);
}

.flickity-viewport .collapsible-content, .flickity-viewport .collapsible-content.is-open{
  transition:none!important
}

.collapsible-content--all{
  visibility:hidden;
  overflow:hidden;
  -webkit-backface-visibility:hidden;
  backface-visibility:hidden;
  opacity:0;
  height:0
}

@media only screen and (min-width:590px){
  .filters-toolbar__item .collapsible-content--all:has(.color-swatch){
    width: 240px
  }
}

.collapsible-content--all .collapsible-content__inner{
  transform:translateY(40px);
}

.collapsible-content--all .collapsible-content__inner--no-translate{
  transform:translateY(0);
}

@media only screen and (max-width:589px){
  .collapsible-content--small{
    overflow:hidden;
    visibility:hidden;
    -webkit-backface-visibility:hidden;
    backface-visibility:hidden;
    opacity:0;
    height:0
  }

  .collapsible-content--small .collapsible-content__inner{
    transform:translateY(40px);
  }

  .collapsible-content--small .collapsible-content__inner--no-translate{
    transform:translateY(0);
  }
}

.collapsible-content__inner{
  transition:transform 0.3s cubic-bezier(.25,.46,.45,.94)
}

.is-open .collapsible-content__inner{
  transform:translateY(0);
  transition:transform 0.5s cubic-bezier(.25,.46,.45,.94)
}

.rte.collapsible-content__inner--faq{
  padding-bottom:40px;
}

.collapsible-trigger[aria-expanded=true] .collapsible-label__closed{
  display:none
}

.collapsible-label__open{
  display:none
}

.collapsible-trigger[aria-expanded=true] .collapsible-label__open{
  display:inline-block
}

::-webkit-input-placeholder{
  color:inherit;
  opacity:0.5;
}

:-moz-placeholder{
  color:inherit;
  opacity:0.5;
}

:-ms-input-placeholder{
  color:inherit;
  opacity:0.5;
}

::-ms-input-placeholder{
  color:inherit;
  opacity:1;
}

/*input,select,textarea{
  background-color:transparent;
  color: rgba(var(--color-foreground), 1);
}*/

input.disabled,input[disabled],select.disabled,select[disabled],textarea.disabled,textarea[disabled]{
  background-color:#f6f6f6;
  border-color:transparent;
}

input:active,input:focus,select:active,select:focus,textarea:active,textarea:focus{
  border: calc(var(--buttonOutlineWeight) / 2) solid;
  border-color:#2a2b2f;
  border-color:var(--colorTextBody);
    }

input[type=number]{
  font-size:16px;
}

input[type=image]{
  background-color:transparent;
}

.media-grid--pagination .media__item{
  height:120px
}

@media only screen and (min-width:590px){

  .media-grid--pagination .media__item{
    height:350px
  }
}

.collection--square-small,.collection--wide{
  height:150px
}

@media only screen and (min-width:590px){

  .collection--square-small,.collection--wide{
    height:300px
  }
}

.collection--square-large,.collection--tall{
  height:300px
}

@media only screen and (min-width:590px){

  .collection--square-large,.collection--tall{
    height:600px
  }
}

.collections-list .collection--square-small{
  margin-bottom:22px
}

@media only screen and (min-width:590px){

  .collections-list .collection--square-small{
    margin-bottom:30px
  }
}

.social-sharing{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight)
          }

@media only screen and (max-width:589px){

  .social-sharing{
    text-align:center
  }
}

.social-sharing .icon{
  height:18px;
  width:18px;
}

.social-sharing__link{
  display:inline-block;
  color:currentColor;
  border-radius:2px;
  font-size:calc(var(--fontBaseSize) - 1px);
  margin:0 18px 0 0;
  text-decoration:none;
  font-weight:400;
  min-height: 44px;
}

.social-sharing__link:last-child{
  margin-right:0;
}

.social-sharing__title{
  display:inline-block;
  vertical-align:middle;
  padding-right:15px;
  padding-left:3px
}

@media only screen and (max-width:589px){

  .social-sharing__title{
    font-size:0.9em
  }
}

.grid-search{
  margin-bottom:40px;
}

.grid-search__product{
  position:relative;
  text-align:center;
}

.grid-search__page-link,.grid-search__product-link{
  height:280px;
}

.grid-search__page-link{
  display:block;
  background-color:#fafafa;
  padding:20px;
  color:#2a2b2f;
  color:var(--colorTextBody);
    border:1px solid;
    border-color:#e8e8e1;
    border-color:var(--colorBorder);
      overflow:hidden;
      text-overflow:ellipsis
      }

.grid-search__page-link:focus,.grid-search__page-link:hover{
  background-color:#f5f5f5;
}

.grid-search__page-content{
  display:block;
  height:100%;
  overflow:hidden;
}

.grid-search__page-content img{
  display:block;
  margin-bottom:10px;
}

.grid-search__image{
  display:block;
  padding:20px;
  margin:0 auto;
  max-height:100%;
  max-width:100%
}

@media only screen and (min-width:590px){

  .grid-search__image{
    position:absolute;
    top:50%;
    left:50%;
    transform:translate(-50%, -50%)
  }
}

.index-section{
  padding:34px 0;
}

.index-section--small{
  padding:24px 0;
}

.index-section+.index-section,.index-section+.index-section--hidden{
  margin-top:0;
}

.index-section--flush{
  margin:0;
}

.index-section--faq{
  margin-bottom:20px;
}

.section--divider--line{
  border-top:1px solid;
  border-top-color:rgba(var(--color-border), 1);
  padding-top:30px;
  padding-bottom:30px;
    }

.section--divider--clear{
  padding-top:30px;
  padding-bottom:30px;
}

.quotes-section .section--divider--clear, .quotes-section .section--divider--line {
  padding-top:60px;
    padding-bottom:60px;
}

.index-section--faq{
  margin-bottom:40px;
}

.product-reviews--full{
  border-top:1px solid;
  border-top-color:#e8e8e1;
  border-top-color:var(--colorBorder);
    padding:34px 0;
    margin-bottom:0;
    }

.newsletter-section{
  padding:34px 0;
}

.newsletter-section--with-divider{
  border-top:1px solid;
  border-top-color:#e8e8e1;
  border-top-color:var(--colorBorder);
    }

.quotes-section{
  padding:34px 0;
}

.quotes-section--with-divider{
  border-top:calc(var(--dividerWeight)/2) solid;
  border-top-color:#e8e8e1;
  border-top-color:var(--colorBorder);
    }

.section--divider-all{
    padding-top:60px;
    padding-bottom:60px;
  }
  
  .section--divider-top{
    padding-top:60px;
  }
  
  .section--divider-bottom{
    padding-bottom:60px;
  }

@media only screen and (min-width:590px){
  .index-section{
    padding: 70px 0;
  }

  .index-section--small{
    padding:40px 0;
  }

  .index-section--faq{
    padding:40px 0 20px;
  }

  .section--divider{
    padding-top:100px;
  }
  
  .section--divider-all{
    padding-top:100px;
    padding-bottom:100px;
  }
  
  .section--divider-top{
    padding-top:100px;
  }
  
  .section--divider-bottom{
    padding-bottom:100px;
  }

  .product-reviews--full{
    padding:60px;
  }

  .newsletter-section{
    padding:60px 0;
  }

  .quotes-section{
    padding:100px 0;
  }

  .quotes-section--no-padding{
    padding: 0px 0 !important;
  }
}

.page-blocks--flush .page-width{
  padding:0;
}

.page-blocks>div:first-child .index-section{
  margin-top:0;
}

.feature-row{
  margin:0 auto;
  display:flex;
  justify-content:space-between;
  align-items:center
}

@media only screen and (min-width:1050px){

  .feature-row{
    margin:0 6%
  }
}

@media only screen and (max-width:768px){

  .feature-row{
    flex-direction:column;
    margin:0
  }
}

.feature-row .image-wrap.splitted-media-text__container, .feature-row .image-wrap.background-media-text__container{
  position: relative;
  background-repeat: no-repeat;
  background-size: contain;
  background-position: 50%;
  overflow: visible
}
.feature-row .image-wrap.splitted-media-text__container:before, .feature-row .image-wrap.background-media-text__container:before{
  padding-bottom:100%;
  content: "";
  display: block;
  height: 0;
  width: 100%;
}
.feature-row .image-wrap.splitted-media-text__container img, .feature-row .image-wrap.background-media-text__container img{
  opacity: 1;
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  -o-object-fit: cover;
  object-fit: cover;
}

.feature-row.feature-row-full-width .custom__item-inner--image{

  display:block !important;
}

@media only screen and (max-width:768px){
  .feature-row--small-none{
    display:block;
  }
}

.feature-row__item{
  min-width:30%;
  flex:0 1 50%;
  border-radius: var(--imageRadius)
}

@media only screen and (max-width:768px){

  .feature-row__item{
    flex:1 1 auto;
    max-width:100%;
    min-width:100%
  }
}

.feature-row__image{
  display:block;
  margin:0 auto
}

@media only screen and (max-width:768px){

  .feature-row__image{
    order:1
  }
}

.feature-row__text{
  padding-top:40px;
  padding-bottom:40px
}

@media only screen and (max-width:768px){

  .feature-row__text{
    order:2;
    padding-top:34px;
    padding-bottom:34px;
  }
}

.feature-row__text .rte{
  margin:0;
}

.feature-row__text .btn{
  margin:20px 13.33333px 0 0;
  margin:20px 0px 0 0;
}

@media only screen and (min-width:768px){
  .feature-row__text--left{
    padding-left:80px;
  }

  .feature-row__text--right{
    padding-right:80px;
  }
}

.feature-row-full-width {
  margin: 0 0%;
}
@media only screen and (max-width:590px){
  .feature-row-full-width {
    -ms-flex-direction: column;
    flex-direction: column;
    margin: 0;
  }
}

.index-section-full {
  margin-top: -34px!important;
  margin-top: 0px!important;
  margin-bottom: 0!important;
}

@media only screen and (min-width:590px){
  .index-section-full {
    margin: 0px 0!important;
  }
  .feature-row--arrows {
    padding-left: 60px;
    padding-right: 60px;
  }
}

/*@media only screen and (max-width:589px){
.feature-row--arrows [data-watchcss=false].splitted-text-slider{
    padding-left: 60px;
    padding-right: 60px;
  }
}*/

/*==============================================================================
 HERO position
==============================================================================*/

.hero__text-content.vertical-top {
  vertical-align: top;
}

.hero__link-break {
  display: list-item!important;
  list-style: none;
  padding-top: 20px;
}
.hero-title_font-style {
  font-family: var(--fontBasePrimary),var(--fontBaseFallback)!important;
    font-weight: 400!important;
    letter-spacing: 2px;
    line-height: var(--fontBaseLineHeight)!important;
      }

.hero .slideshow__slide{
  height:100%;
  float: right;
  position: absolute
}

/*.hero__text-shadow:after {
  background: radial-gradient(rgba(0,0,0,.3) 0, transparent 60%);
  bottom: 0;
  content: "";
  display: block;
  left: 0;
  margin: -100px -200px -100px -200px;
  position: absolute;
  right: 0;
  top: 0;
  z-index: -1;
}*/

/*.slideshow-wrapper .hero {
  background-color: #ffffff;
  background-color: var(--colorBody);
}*/

.slideshow-wrapper{
  position:relative;
}

.slideshow__pause:focus{
  clip:auto;
  width:auto;
  height:auto;
  margin:0;
  color:#ffffff;
  color:var(--colorBtnPrimaryText);
    background-color:#2a2b2f;
    background-color:var(--colorBtnPrimary);
      padding:10px;
      z-index:10000;
      transition:none
      }

.video-is-playing .slideshow__pause:focus{
  display:none
}

.slideshow__pause-stop{
  display:block
}

.slideshow__pause--is-paused .slideshow__pause-stop{
  display:none
}

.slideshow__pause-play{
  display:none
}

.slideshow__pause--is-paused .slideshow__pause-play{
  display:block
}

.slideshow__slide{
  display:none;
  width:100%;
  position:relative;
  overflow:hidden
}

.slideshow__slide:first-child{
  display:block;
}

.flickity-slider .slideshow__slide{
  display:block
}

.slideshow__slide:after{
  content:"";
  position:absolute;
  background:#ffffff;
  background:var(--colorBody);
    bottom:0;
    height:0px
    width:100%;
    z-index:1;
    transform:scaleX(0) translateZ(0);
    transition:transform 0s linear 0s;
    transform-origin:0 50%;
    }

.slideshow__slide.is-selected:after{
  transform:scaleX(1);
}

.hero{
  position:relative;
  overflow:visible;
  /*background:#111111;
  background:var(--colorBody);*/
    }

.hero .flickity-viewport, .hero .slideshow__slide {
  overflow:hidden;
}

.hero__image-wrapper,.hero__media{
  position:absolute;
  top:0;
  right:0;
  height:100%;
  width:100%
}

.hero__image-wrapper:before,.hero__media:before{
  content:"";
  position:absolute;
  top:0;
  right:0;
  bottom:0;
  left:0;
  z-index:3
}

.video-interactable .hero__image-wrapper:before,.video-interactable .hero__media:before{
  pointer-events:none
}

.hero__image-wrapper--no-overlay:before{
  content:none;
}

.hero__image{
  position:relative;
  width:100%;
  height:100%;
  z-index:1;
  -o-object-fit:cover;
  object-fit:cover;
}

.hero__media iframe,.hero__media video{
  width:100%;
  height:100%;
  pointer-events:none
}

.video-interactable .hero__media iframe,.video-interactable .hero__media video{
  pointer-events:auto
}

.video-parent-section.loading .hero__media iframe,.video-parent-section.loading .hero__media video{
  opacity:0.01
}

.video-parent-section.loaded .hero__media iframe,.video-parent-section.loaded .hero__media video{
  opacity:1
}

.hero__media video{
  position:relative;
  -o-object-fit:cover;
  object-fit:cover;
  font-family:"object-fit: cover";
}

.hero__media iframe{
  position:absolute;
  top:0;
  left:0;
  width:300%;
  left:-100%;
  max-width:none
}

@media screen and (min-width:1140px){

  .hero__media iframe{
    width:100%;
    height:300%;
    left:auto;
    top:-100%
  }
}

.hero__slide-link{
  display:block;
  position:absolute;
  height:100%;
  width:100%;
  color:#ffffff;
  color:var(--colorHeroText);
    }

.hero__text-wrap{
  position:relative;
  height:100%;
 /* color:#ffffff;
  color:var(--colorHeroText)*/
    }

.video-interactable .hero__text-wrap{
  pointer-events:none
}

.hero__text-wrap .page-width{
  display:table;
  width:100%;
  height:100%;
}

.hero__text-wrap--absolute{
  position:absolute;
  width:100%;
}

.hero__text-content{
  position:relative;
  padding:20px 0;
  z-index:4;
}

@media only screen and (min-width:590px){

  .hero__text-content{
    padding:60px 0
  }
  
  .collection-hero .hero__text-content {
    padding:40px 0
  }
  
  .customers-hero .collection-hero {
    min-height: 700px
  }
}

.expand_column.media__title.hero__text-content {
  padding: 0px;
}

.hero__title{
  display:block;
  margin-bottom:0;
  font-size:40px
}

@media only screen and (min-width:590px){

  .hero__title{
    font-size:80px
  }
}

.hero__title h1 {
  font-size: unset!important
}

.hero__subtext{
  margin-top:20px;
}

.hero__subtitle{
  display:inline-block;
  vertical-align:middle;
  margin-right:20px;
  margin-top:5px;
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight)
          }

[data-body-capital=true] .hero__subtitle{
  text-transform:uppercase;
  letter-spacing:0.2em
}


.hero__subtitle{
  font-size:var(--fontBaseSize)
    }

[data-body-capital=true] .hero__subtitle{
  font-size:calc(var(--fontBaseSize)*0.85)
    }

@media only screen and (min-width:590px){

  .hero__subtitle{
    font-size:calc(var(--fontBaseSize)*1.3)
      }

  [data-body-capital=true] .hero__subtitle{
    font-size:calc(var(--fontBaseSize)*1.18)
      }
}

.hero__link{
  display:inline-block
}

.video-interactable .hero__link{
  pointer-events:auto
}

.hero__link .btn{
  margin-top:10px
}

@media only screen and (min-width:590px){

  .hero__link .btn{
    margin-top:5px
  }
}

.hero__link .btn .icon-play{
  position:relative;
  top:-2px;
  width:16px;
  height:16px;
  margin-right:15px
}

@media only screen and (max-width:589px){

  .hero__link .btn .icon-play{
    margin-right:10px
  }
}

.hero__link .btn--small .icon-play{
  width:14px;
  height:14px;
  margin-right:10px;
}

.hero__text-content{
  display:table-cell
}

.hero__text-content.horizontal-left, .section-header.horizontal-left{
  text-align:left;
}

.hero__text-content.horizontal-center, .section-header.horizontal-center{
  text-align:center;
}

.hero__text-content.horizontal-right, .section-header.horizontal-right{
  text-align:right;
}

.hero__text-content.vertical-center{
  vertical-align:middle
}

.hero__text-content.vertical-center .hero__subtitle{
  margin-right:0;
}

.hero__text-content.vertical-center .hero__link{
  display:block
}

@media only screen and (min-width:590px){

  .hero__text-content.vertical-center .hero__link{
    margin-top:20px
  }
}

.hero__text-content.vertical-center .hero__link .btn{
  margin-left:0;
}

.hero__text-content.vertical-bottom{
  vertical-align:bottom;
}

.hero--350px{
  height:227.5px;
}

.hero--450px{
  height:292.5px;
}

.hero--550px{
  height:357.5px;
}

.hero--650px{
  height:422.5px;
}

.hero--750px{
  height:487.5px;
}

.hero--100vh{
  height:100vh;
}

@media only screen and (min-width:590px){
  .hero--natural[data-natural]{
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
  }
  .hero--350px{
    height:350px;
  }
  .hero--450px{
    height:450px;
  }
  .hero--550px{
    height:550px;
  }
  .hero--650px{
    height:650px;
  }
  .hero--750px{
    height:750px;
  }
  .hero--16-9{
    height:0;
    padding-bottom:56.25%;
  }
}

@media only screen and (max-width:589px){
  .hero--mobile--auto[data-mobile-natural=true]{
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
    height:auto;
  }
  .hero--16-9.hero--mobile--auto{
    height:0;
    padding-bottom:56.25%;
  }
  .hero--natural[data-mobile-natural=false]{
    height:500px;
  }
  .hero--mobile--250px[data-mobile-natural=false]{
    height:250px;
  }
  .hero--mobile--300px[data-mobile-natural=false]{
    height:300px;
  }
  .hero--mobile--400px[data-mobile-natural=false]{
    height:400px;
  }
  .hero--mobile--500px[data-mobile-natural=false]{
    height:500px;
  }
  .hero--mobile--100vh[data-mobile-natural=false]{
    height:90vh;
  }
}

.slideshow__slide .animation-cropper{
  opacity:0;
}

.slideshow__slide .animation-contents{
  opacity:0;
  transform:translateY(15px);
  transition:none;
}

.slideshow__slide .hero__image{
  opacity:0;
  transition:none;
}

.hero__image-wrapper{
  transform:scale(1.15);
  opacity:0;
  transition:none;
}

.slideshow__slide.is-selected .animation-cropper{
  opacity:1;
}

.slideshow__slide .hero__title .animation-contents{
  transform:translateY(15px);
}

.slideshow__slide.is-selected .hero__title .animation-contents{
  animation:0.8s cubic-bezier(0.26, 0.54, 0.32, 1) 0.3s forwards;
  animation-name:fade-in;
  transition-delay:0.3s!important;
  transform:translateY(0px);
  transition:opacity 0.4s ease,transform 0.6s cubic-bezier(0.26, 0.54, 0.32, 1);
}

.slideshow__slide.is-selected .hero__subtitle .animation-contents{
  animation:1s cubic-bezier(0.26, 0.54, 0.32, 1) 0.7s forwards;
  animation-name:fade-in;
  transition-delay:0.7s!important;
  transform:translateY(0px);
  transition:opacity 0.4s ease,transform 0.6s cubic-bezier(0.26, 0.54, 0.32, 1);
}

.slideshow__slide.is-selected .hero__link .animation-contents{
  animation:fade-in 1.2s ease 1.2s forwards;
  transition-delay:1.2s!important;
  transform:translateY(0px);
  transition:opacity 0.4s ease,transform 0.6s cubic-bezier(0.26, 0.54, 0.32, 1);
}

.slideshow__slide.is-selected .hero__image--svg, .slideshow__slide.is-selected .hero__image, .slideshow__slide.is-selected .hero__media{
  animation:2.5s cubic-bezier(0.26, 0.54, 0.32, 1) 0s forwards;
  animation-name:zoom-fade;
  opacity: 1
}

.slideshow__slide.is-selected .hero__image-wrapper{
  opacity:1;
  transform:scale(1);
  transition:transform 1.5s cubic-bezier(0.26, 0.54, 0.32, 1),opacity 1.5s cubic-bezier(0.26, 0.54, 0.32, 1);
}

.animate-out .animation-cropper{
  opacity:0;
}

.animate-out .hero__image-wrapper{
  opacity:0;
  transform:translateX(-60px);
  transition:transform 0.25s ease-in 0.08s,opacity 0.25s ease-in 0.08s;
}

.animate-out .hero__image{
  opacity:1;
}

@media only screen and (max-width:589px){
  .hero__text-content.horizontal-left--mobile, .section-header.horizontal-left--mobile {
    text-align:left;
  }
  
  .hero__text-content.horizontal-center--mobile, .section-header.horizontal-center--mobile {
    text-align:center;
  }
  
  .hero__text-content.horizontal-right--mobile, .section-header.horizontal-right--mobile {
    text-align:right;
  }
  
  .hero__text-content.vertical-center--mobile {
    vertical-align:middle
  }

  .hero__text-content.vertical-bottom--mobile {
    vertical-align:bottom;
  }

  .hero__text-content.vertical-top--mobile {
    vertical-align: top;
  }
}
/*====================
Collection Switcher
========================*/

.tab-switcher__title{
  display:inline-block;
  margin-bottom:15px;
  margin-left:0px;
  margin-right:20px;
}

.tab-switcher__trigger{
  position:relative;
  display:block;
  overflow:visible;
  color: var(--colorTextBodyBorder);
  text-decoration: none;
}

.tab-switcher__trigger:after{
  content:"";
  position:absolute;
  bottom:-2px;
  left:0;
  width:100%;
    }

.tab-switcher__trigger.is-active{
  color: rgba(var(--color-foreground), 1);
}

.tab-switcher__collection {
  position:relative;
}

@media only screen and (max-width:589px){
  .tab-switcher__collection--slider {
    margin-top: 25px
  }
}

.tab-switcher__collection .flickity-next {
  z-index: 1;
  right: 20px;
}

.tab-switcher__collection .flickity-previous {
  z-index: 1;
  left: 20px;
}

.tab-switcher__collection-grid{
  margin-bottom:40px
}

.tab-switcher__collection-grid .grid__item{
  display:none;
  opacity:0
}

.tab-switcher__collection-grid .grid__item:first-child{
  display:block;
}

.tab-switcher__collection-grid .flickity-slider .grid__item{
  display:block;
  opacity:1;
}

@media only screen and (min-width:590px){
  .tab-switcher__collection-grid .flickity-slider{
    padding:3px 0;
  }
}

.tab-switcher__collection-grid .flickity-slider{
  padding-top:18px;
}

.tab-switcher__collection-grid .quick-product__btn{
  right:1px;
}

.tab-switcher__collection-grid .flickity-prev-next-button{
  top:calc(50% - 40px);
}

.tab-switcher__collection-grid .flickity-next{ right:-20px; }

.tab-switcher__collection-grid .flickity-previous{
  left:10px;
}

@media only screen and (max-width:589px){
  .tab-switcher__collection-grid .flickity-next{
    right:-10px;
  }

  .tab-switcher__collection-grid .flickity-previous{
    left:10px;
  }

  .tab-switcher__title{
    margin-bottom:10px;
    margin-right:15px
  }
}

.text-switcher__title {
  font-size: 0.8em!important;
  font-size: calc(var(--fontBaseSize) + 2px)!important;
  font-style: normal;
  font-weight: var(--fontBaseWeight);
    line-height: 1.6;
    text-transform: uppercase;
    letter-spacing: 0.2em;
    margin-bottom: 5px;
    display: inline-block;
    margin-bottom: 15px;
    }
@media only screen and (min-width:590px){
  .text-switcher__title {
    margin-bottom: 0px;
  }
}
/*.text-switcher__title+.text-switcher__title{
  margin-left:20px;
}*/
.text-switcher__title{
  margin-right:20px;
}
.text-switcher__title:nth-last-of-type(1) {
  margin-right:00px;
}

.details-section-header {
  margin-bottom: 20px;
}

.section-header.details-section-header {
  margin-top: 20px;
}

.description_full-width .section-header.details-section-header {
  margin-top: 10px;
}

.collection-grid__wrapper .grid__item {
  padding-left: 10px;
  padding-right: 10px;
}

.collection-grid__wrapper .media__item-content{
  position:relative;
  overflow:hidden;
  height:100%;
  width:100%;
  background-color: var(--colorBody);
}

.grid-view-btn{
  display:block;
  padding:10px 8px;
  opacity:0.15;
  color: currentColor
}

.grid-view-btn:hover{
    opacity:0.4;
  }

.grid-view-btn.is-active{
    opacity:1;
  }

.grid-view-btn svg{
    display:block;
  }

@media only screen and (max-width:768px){
    .grid-view-btn svg{
      width:18px;
      height:18px;
    }
  }

.logo--inverted, .is-light .header-inverse-overlay .header-layout .logo--has-inverted, .header-inverse .header-layout .logo--has-inverted{
  opacity:0;
  display: none!important;
  visibility:hidden;
  overflow:hidden;
  height:0
}

.is-light .header-inverse-overlay .header-layout .logo--inverted, .header-inverse .header-layout .logo--inverted{
  opacity:1;
  display: block!important;
  visibility:visible;
  height:auto
}

.is-light .site-toolbar-inverse-overlay .logo--has-inverted, .site-toolbar-inverse .logo--has-inverted{
  opacity:0;
  display: none!important;
  visibility:hidden;
  overflow:hidden;
}

.is-light .site-toolbar-inverse-overlay .logo--inverted, .site-toolbar-inverse .logo--inverted{
  opacity:1;
  display: block!important;
  visibility:visible;
}
  

.site-header__logo{
  font-size:20px
}

@media only screen and (min-width:590px){

  .site-header__logo{
    text-align:left;
    font-size:25px
  }
}

/*.header-logo a,.site-header__logo a{
  color:var(--colorNavText)
    }*/

/*.is-light .header-logo a,.is-light .site-header__logo a{
  color:#fff
}

.is-light .header-logo a:hover,.is-light .site-header__logo a:hover{
  color:#fff;
}*/

.is-light .site-header{
  border-bottom:none;
}

.site-nav__dropdown-link{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback)
  display:block;
  white-space:normal;
  padding:0px 20px 0px;
  padding-left: 0;
  font-size:16px;
  transition:padding 0.2s ease;
  width: max-content;
}

.site-nav__dropdown-link.h5{
  font-weight: 600
}

.site-nav__dropdown-link:hover{
  /*text-decoration: underline;*/
  cursor:pointer
}

div>.site-nav__dropdown-link, li>.site-nav__dropdown-link {
  position:relative
}
.site-nav__dropdown-link:after, .tag__text:after {
  display: block;
  position: absolute;
  content: "";
  bottom: -8px;
  top: auto;
  left: 0px;
  transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  background: rgba(var(--color-button),var(--alpha-button-background));
  height: calc(var(--buttonOutlineWeight));
  width: 0rem
}

.site-nav__dropdown-link:hover:after, .tag__text:hover:after {
  transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  width: 84%
}

.footer__collapsible .site-nav__dropdown-link:after {
  bottom: -1px
}

.footer__collapsible .site-nav__dropdown-link:hover:after {
  transition: right .25s ease-in-out;
  transition: width .25s ease-in-out;
  width: 100%
}

.tag>.tag__checkbox-wrapper {
  position:relative;
  cursor: pointer;
}

.tag__checkbox-wrapper {
  width: max-content;
}

.tags a{
  font-size:16px;
  display:block;
  transition:padding 500ms cubic-bezier(0.2, 0.06, 0.05, 0.95);
  padding-bottom:5px
}

.tags a:hover{
  padding-left:10px;
}

@media only screen and (min-width:590px){

  .tags a{
    font-size:19px;
    padding-bottom:10px
  }
}

.tags--vertical{
  list-style:none outside;
  margin:0;
  padding:0;
}

.tags--article a{
  padding-right:20px;
  padding-left:0;
}

.tags__title{
  margin-right:20px;
}

.tag--active{
  font-weight:700
}

.tag--active a{
  padding-left:0
}

.tags-toggle{
  margin-top:10px
}

@media only screen and (min-width:590px){

  .tags-toggle{
    margin-top:20px
  }
}

/*================================================
FEATURED COLLECTION
================================================*/

.variant-input-wrap{
  border:0;
  padding:0;
  margin:0 0 26.66667px;
  position:relative;
}

.variant-input-wrap input, .selling-plan-input-wrap input{
  clip:rect(0, 0, 0, 0);
  overflow:hidden;
  position:absolute;
  height:1px;
  width:1px;
}

.variant-input-wrap label, .selling-plan-input-wrap label{
  position:relative;
  display:inline-block;
  display: flex;
  line-height:1;
  font-weight:400;
  min-height: var(--buttonSize);
  padding:11.46px 15px 11.46px;
  padding:5.46px 15px 5.46px;
  margin:0 4px 12px 0px;
  font-style:normal;
  font-size:var(--fontBaseSize);
  background-color:var(--colorBody);
  background-color: transparent;
  border:1px solid;
  border: calc(var(--buttonOutlineWeight) / 2) solid;
  border-radius: var(--buttonRadius);
  transition:all 0.25s ease;
  overflow:hidden;
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
  font-weight:var(--fontBaseWeight);
  letter-spacing:var(--fontBaseSpacing);
  line-height:var(--fontBaseLineHeight);
  border-color:transparent;
}

@media only screen and (max-width:589px){

  .variant-input-wrap label, .selling-plan-input-wrap label{
    font-size:calc(var(--fontBaseSize) - 1px);
    /*padding:12.305px 15px 12.304px;*/
  }
}

.variant-input-wrap label.disabled{
  color:#e8e8e1;
  color:var(--colorTextBodyBorder);
    }

.variant-input-wrap label.disabled:after{
  position:absolute;
  content:"";
  left:50%;
  top:0;
  bottom:-1px;
  border-left:1px solid;
  border-color:var(--colorTextBodyBorder);
  transform:rotate(45deg);
    z-index: 5;
    }

.selling-plan-input-wrap input[type=radio]:checked+label, .color-swatch--small.is-active{
  box-shadow: inset 0 0 1px 1px #0000000d,var(--colorTextBodyBorder) 0 0 0 1.5px,var(--colorBody) 0 0 0 3px,var(--colorTextBody) 0 0 0 4.5px;
  box-shadow: inset 0 0 1px 1px #0000000d,var(--colorTextBodyBorder) 0 0 0 0,var(--colorBody) 0 0 0 2px,var(--colorTextBody) 0 0 0 3.5px
}

.variant-input-wrap input[type=radio]:focus-visible+label, .selling-plan-input-wrap input[type=radio]:checked+label {
  border: calc(var(--buttonOutlineWeight) / 2) solid;
  border-color: #c9c9c8;
  box-shadow: 0 0 0 0px var(--colorBody), 0px 0px 0px 2px #275DC5;
}

.variant-wrapper--dropdown .variant-input-wrap {
  margin: 0 0 17.66667px;
}

.variant-input{
  display:inline-block;
  vertical-align: top;
}

select .variant-input{
  display:block
}

.variant-wrapper{
  margin-bottom:-12px
}

.no-js .variant-wrapper{
  display:none
}

.variant-wrapper--dropdown{
  display:inline-block;
  max-width:100%;
}

.variant__label{
  display:block;
  margin-bottom:10px;
  cursor:default;
  font-style:normal;
}

.variant__label-info{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          text-transform:none;
          font-weight:400;
          }

.collection-grid{
  margin-top: 20px;
  margin-bottom:60px;
  margin-bottom:var(--indexSectionPadding);
}

[data-grid-style*=grey] .collection-grid,[data-grid-style=simple] .collection-grid{
    padding-top:10px;
}

[data-view="calendar"].product-grid {
  width: 100%;
  margin: 0 auto;
}

@media only screen and (max-width:590px){
  .medium-up--one-whole [data-view="calendar"].product-grid {
    width: 100%
  }
}

@media only screen and (min-width:589px){
  .medium-up--one-whole [data-view="calendar"].product-grid {
    width: 65%
  }
}
  
@media only screen and (min-width:769px){
  .medium-up--one-whole [data-view="calendar"].product-grid {
    width: 60%
  }
}
  
@media only screen and (min-width:1050px){
  .medium-up--one-whole [data-view="calendar"].product-grid {
    width: 55%
  }
}

 
[data-grid-style*=gridlines] .product-grid{
    margin-left:0;
    margin-right:0;
}

@media only screen and (max-width:768px){
    [data-grid-style*=gridlines] .product-grid{
      margin-left:-40px;
      margin-left:calc(var(--pageWidthPadding)*-1);
      margin-right:-40px;
      margin-right:calc(var(--pageWidthPadding)*-1);
      padding:var(--gridThickness) calc(40px + var(--gridThickness));
      padding:var(--gridThickness) calc(var(--pageWidthPadding) + var(--gridThickness));
    }
    [data-grid-style*=gridlines] .collection-grid{
      padding:0;
    }
}


.product__content{
  position:relative;
  display: flex;
  flex-direction: column;
  margin-bottom:20px;
  text-align:left
}

[data-view=full] .product__content, [data-view=calendar] .product__content {
  flex-direction: row;
  flex-wrap: nowrap;
  margin-bottom: 0;
}

@media only screen and (max-width:768px){
  [data-view=calendar] .product__content{
    flex-wrap: wrap;
  }
}

.grid-product__overlay .product__content {
  /*height: calc(100% - 20px);*/
  height: 100%;
  border-radius: var(--imageRadius);
}

.grid-product__overlay .product__meta {
  padding-left: 10px;
  padding-right: 10px
}

.collection-grid__wrapper .grid__item:has(.product__meta--embedded):has(:not(.color-background-1)) {
  padding-bottom: 20px
}

[data-view=full] .product__image-link {
  flex: 1 1 45%;
  flex: 1 1 auto;
  max-width: 150px;
}

[data-view=calendar] .product__image-link {
  flex: 1 1 45%;
  flex: 0 1 auto;
  display: inline-flex;
  max-width: 60px;
    padding: 20px
}


html[dir=rtl] .product__content{
  text-align:right
}

@media only screen and (min-width:590px){

  .product__content{
    margin-bottom:40px
  }

  .product__content.small {
    margin-bottom:20px
  }
}

.product-single__related .product__content{
  margin-bottom:0
}

.product-single__title{
  font-size:calc(var(--fontHeaderSize)*0.85)!important;
  margin-bottom:10px;
  word-wrap:break-word
}

@media only screen and (min-width:590px){
  .product-single__title{
    font-size:calc(var(--fontHeaderSize) * .9)!important;
    margin-bottom:20px
  }
}

.product__link{
  display:block;
}

.product__image-mask{
  position:relative;
  overflow:hidden;
  border-radius: var(--imageRadius);
}

.product__image{
  display:block;
  margin:0 auto;
  width:100%;
}

.product__secondary-image{
  position:absolute;
  top:-1px;
  left:-1px;
  right:-1px;
  bottom:-1px;
  background-color:var(--colorBody);
    opacity:0
    }

.product__secondary-image img{
  width:100%;
  height:100%;
  -o-object-fit:cover;
  object-fit:cover;
}

.product__secondary-image img.grid__image-contain{
  -o-object-fit:contain;
  object-fit:contain;
}

.product__content:hover .product__secondary-image{
  opacity:1
}

@media only screen and (max-width:589px){

  .supports-touch .product__secondary-image.small--hide{
    display:none
  }
}

.product__meta{
  position:relative;
  padding:10px 0 6px 0;
  padding:10px 0 10px 0;
  line-height:calc(var(--fontBaseLineHeight) - 0.1)
    }

.product__meta-secondary{
  margin-top:5px;
}

[dir=ltr] [data-grid-style=simple] .collection-grid__items:not([data-view=list]) .product__meta{
    margin-left:0;
  }

[dir=rtl] [data-grid-style=simple] .collection-grid__items:not([data-view=list]) .product__meta{
    margin-right:0;
  }

[data-view=full] .product__meta {
  flex: 1 1 55%;
  flex: 1 1 auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 30px;
}

[data-view=calendar] .product__meta {
  flex: 1 1 75%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 30px;
}
  
.product__btn {
  flex: 1 1 35%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

@media only screen and (max-width:768px){
  .product__btn {
    margin-bottom: 10px
  }

  [data-view=calendar] .product__btn {
    padding: 0 20px 10px;
  }
}

.product__swatch-image {
  position:absolute;
  top:-1px;
  left:-1px;
  right:-1px;
  bottom:-1px;
  background-repeat:no-repeat;
  background-size:cover;
  background-position:50%;
  opacity:0;
  background-color:var(--colorBody)
}

.product__swatch-image.is-active{
    opacity:1;
  }

.product__policies{
  font-size:0.85em;
}

.shopify-payment-terms{
  margin:15px 0
}

.shopify-payment-terms:empty{
    display:none;
  }

.product__title{
  font-size:calc(var(--fontBaseSize));
}

.product__title, .footer__title {
  font-weight: var(--fontLabelWeight);
}

.cart .product__title{
  font-size:calc(var(--fontBaseSize));
    font-weight: 600;
}

[data-label-capital=true] .product__title, .grid-product__title {
  text-transform:uppercase;
}

.product__vendor{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight);
          }

[data-body-capital=true] .product__vendor{
  text-transform:uppercase;
  letter-spacing:0.2em;
}

.product__vendor{
  font-size:11px;
  margin-top:3px;
}

.product__price--listing{
  margin-top:6px;
  font-size:var(--fontBaseSize)
    }

.product__price--listing--original{
  text-decoration:line-through;
  margin-right:5px;
}

.product__tag{
  font-family:var(--fontBasePrimary),var(--fontBaseFallback);
    font-weight:var(--fontBaseWeight);
      letter-spacing:var(--fontBaseSpacing);
        line-height:var(--fontBaseLineHeight)
          }

[data-body-capital=true] .product__tag{
  text-transform:uppercase;
  letter-spacing:0.2em
}

.product__tag{
  position:absolute;
  top:0;
  right:0;
  font-size:10px;
  line-height:1.4;
  padding:6px 6px 6px 8px;
  /*background-color:var(--colorSaleTag);
    color:var(--colorSaleTagText);*/
  background-color: rgb(var(--color-badge-background));
    border-color: rgba(var(--color-badge-border),var(--alpha-badge-border));
    color: rgb(var(--color-badge-foreground));
      z-index:4;
      transition:opacity 0.4s ease
      }

@media only screen and (min-width:590px){

  .product__tag{
    font-size:var(--fontSmall);
    padding:7px 8px 7px 10px
  }
}

.product__tag--bundle{
  z-index: 2;
  background-color:var(--colorSaleTag);
  color:var(--colorSaleTagText);
  text-transform: none!important;
}

[data-view=calendar] .product__tag {
  display: none
}

.product__bundle {
  width: 100%;
}
.product__bundle--wrapper {
  position: relative;
  bottom: 0px;
}
.image__bundle {
  position:relative;
  margin-bottom: 0px
}

.product__bundle .product__price--listing {
  margin-top: 10px;
  padding-bottom: 10px
}

.product__bundle .sale-price {
  position: absolute;
  margin-top: 0px;
}

.bundle__separator {
  text-align: center;
  font-size: 24px;display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  z-index: 2;
  padding-bottom: 30%
}
@media only screen and (max-width:589px){
  
.bundle__separator {
  padding-bottom: 28%
}
}

.product__bundle--icon {
  display: block;
  font-size: 8px;
  vertical-align: middle;
  width: 15px;
  height: 15px;
  fill: inherit;
  stroke-width: calc(var(--iconWeight) * 2)!important
}

.product__bundle img{
  border-radius: var(--imageRadius);
}

.bundle-variant-input-wrap {
  width: 100%;
}

@media only screen and (max-width:589px){
  .grid-overflow-wrapper{
    overflow:hidden;
    overflow-x:scroll;
    -webkit-overflow-scrolling:touch;
    padding-bottom:26.66667px;
  }

  .grid-overflow-wrapper .grid{
    white-space:nowrap;
    display:flex;
  }

  .grid-overflow-wrapper .grid__item{
    width:66vw;
    flex:0 0 66vw;
    display:inline-block;
    float:none;
    white-space:normal
  }

  .grid-overflow-wrapper .grid__item:first-child{
    margin-left:20px;
  }

  .grid-overflow-wrapper .grid__item:last-child:after{
    content:"";
    display:inline-block;
    width:100%;
    height:1px;
    margin-right:20px;
  }

  .grid-overflow-wrapper .grid__item--view-all{
    align-self:center;
  }

  .grid-overflow-wrapper .product__content{
    margin-bottom:0;
  }
}

.product__see-all{
  display:inline-block;
  padding:20px;
  text-align:center;
  border:1px solid;
  border-color:var(--colorBorder);
    margin-top:-60px;
    }

.grid-product.aos-animate.unload .product__image{
  opacity:0;
  transition-duration:0.3s;
  animation:product__loading 15s ease infinite !important;
}

.grid-product.aos-animate.unload .product__meta{
  opacity:0;
  transition-duration:0.3s;
}

.grid-product.aos-animate.unload .product__colors{
  opacity:0;
  transition-duration:0.3s;
}

.grid-product.aos-animate.unload .product__tag{
  opacity:0;
  transition-duration:0.3s;
}

.product__colors{
  display:flex;
  flex-wrap:wrap;
  align-items:center;
  line-height:14px;
  margin-top:5px;
  margin-bottom: 5px
}

@media only screen and (min-width:590px){

  .product__colors{
    line-height:17px
  }
}

[data-view=list] .product__media_header .product__title {
  display: none
}

.collection-grid__items:not([data-view=list]) .product__content:has(.product__media_header) {
  margin-bottom: 0
}

.collection-grid__items:not([data-view=list]) .product__meta--embedded :is(.product__meta-link, .product__vendor, .product__price--listing, .product__price--listing, .shopify-product-reviews-badge)  {
  display: none
}

.collection-grid__items:not([data-view=list]) .product__media_header {
  position:absolute;
  bottom: 0px;
  z-index: 10;
  display: flex;
  padding: 0 11px;
  width: 100%;
  height: 100%
}

.collection-grid__items:not([data-view=list]) .product__media_header .product__btn {
  position:relative;
  flex: 0 0 var(--buttonSize);
  margin-bottom:-11px;
}

.collection-grid__items:not([data-view=list]) .product__media_header .product__title {
  position: relative;
  flex: 0 0 calc(100% - var(--buttonSize) - 10px);
  align-self: flex-end;
  padding-bottom:10px
}

@media only screen and (max-width:589px){
  .collection-grid__items:not([data-view=list]) .product__media_header {
    width: calc(100% + 20px);
    width:100%;
    height: calc(100% - 10px);
    padding: 0;
    bottom: 10px;
    margin-left: 11px;
    margin-bottom: -10px;
    margin-left: 0px;
    padding-bottom:0px
  }

  .collection-grid__items:not([data-view=list]) .product__media_header .product__title {
    margin-bottom: 0px;
    padding-left:11px
  }

  .collection-grid__items:not([data-view=list], [data-view=large]) .product__media_header .product__btn {
    margin-bottom: -5px;
    margin-left:20px;
    left:0
  }

  [data-view=large] .product__media_header .product__btn {
    margin-bottom: 15px;
    margin-left: 0px;
    left: 0;
  }
}

/*============= Color swatch ======================*/

.size-swatch {
  display: flex!important;
  flex-direction: column;
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
}
.size-swatch-item {
  font-size: .8em
}
.color-swatch{
  position:relative;
  display:block;
  text-indent:-9999px;
  overflow-y:hidden;
  margin:0 4px 4px;
  background-position:50%;
  background-size:cover;
  background-repeat:no-repeat;
  width:2.5em;
  /*box-shadow:0 0 0 1px #e8e8e1;
  box-shadow:0 0 0 1px var(--colorBorder);*/
    box-shadow:0 0 0 1px rgba(var(--color-border), 1);
    transition:box-shadow 0.2s ease
    }

.color-swatch:before{
  content:"";
  position:absolute;
  top:0;
  left:0;
  right:0;
  bottom:0;
  z-index:2;
  border-radius: 100px;
  box-shadow: inset 0 0 1px 1px rgb(0 0 0 / 8%);
}

.variant-input-wrap input[type=radio]:checked+.color-swatch:before {
  box-shadow: #00f 0 0 0 1.5px, #ff0 0 0 0 5px, #0ff 0 0 0 7px;
  box-shadow: inset 0 0 1px 1px rgb(0 0 0 / 5%), var(--colorTextBodyBorder) 0 0 0 1.5px, var(--colorBody) 0 0 0 5px, var(--colorTextBody) 0 0 0 7px;
  box-shadow: inset 0 0 1px 1px rgb(0 0 0 / 5%), var(--colorTextBodyBorder) 0 0 0 1.5px, var(--colorBody) 0 0 0 calc(var(--buttonOutlineWeight) / 2), rgba(var(--color-foreground), 1) 0 0 0 calc(var(--buttonOutlineWeight));
  box-shadow: inset 0 0 1px 1px #0000000d,var(--colorTextBodyBorder) 0 0 0 1.5px,var(--colorBody) 0 0 0 0,rgba(var(--color-border),1) 0 0 0 calc(var(--buttonOutlineWeight)*.7)
}
/*calc(var(--buttonOutlineWeight) / 2) solid*/
.color-swatch--small{
  width:14px;
  height:14px
}

.site-nav__dropdown .color-swatch--small{
  padding-right:0px;
}

@media only screen and (min-width:590px){

  .color-swatch--small{
    width:17px;
    height:17px
  }
}

.color-swatch--small:before{
  border:2px solid;
  border-color:var(--colorBody);
    }

.color-swatch {
  position: relative;
  display: block;
  text-indent: -9999px;
  overflow: visible;
  margin: 0 1px 4px;
  background-position: 50%;
  background-size: cover;
  background-repeat: no-repeat;
  width: 2.5em;
  transition: box-shadow 0.2s ease;
  border-radius: 100px;
}

.color-swatch:not(.disabled):after {
  content: "";
  position: absolute;
  top: -3px;
  left: -3px;
  right: -3px;
  bottom: -3px;
  z-index: 3;
    }

.color-swatch.color-swatch--white {
  border: 1px solid;
  color: #00000015
}

.variant-input-wrap label.color-swatch{
  position:relative;
  display:inline-block;
  min-height: calc(var(--buttonSize) - 14px);
  height: calc(var(--buttonSize) - 14px);
    width: calc(var(--buttonSize) - 14px);
  line-height:1;
  font-weight:400;
  padding:12px 15px;
  margin: 5px 10px 10px 0px;
  font-style:normal;
  text-transform:none;
  border: none!important;
  border-radius:100px;
  color:var(--colorTextBody);
      overflow: visible;
      box-shadow: var(--colorTextBodyBorder) 0px 0px 0px 1.5px;
      }

.splitted-text-slider .variant-input-wrap label.color-swatch {
  margin:5px 6px 12px 6px;
}

.splitted-text-slider .product-single__thumbnails {
  display: none!important
}

.splitted-text-slider__product {
  bottom: 3px; 
  position: relative; 
}

@media only screen and (min-width:590px){
  .splitted-text-slider__product {
    padding: 0 32.75px
  }
}

.color-swatch--small {
  height: 22px;
  width: 22px;
  margin: 0px 9px 9px 0px;
}
.color-swatch--small:before {
  border: 0px solid;
}

.color-swatch--small:not(.disabled):after {
  content: "";
  position: absolute;
  top: 0px;
  left: 0px;
  right: 0px;
  bottom: 0px;
  z-index: 3;
  border-radius: 100px;
}

.variant-input-wrap label.color-swatch {
  border: none!important;
  box-shadow: var(--colorTextBodyBorder) 0px 0px 0px 1.5px;
}

.variant-input-wrap input[type=radio]:checked+label, .activeDate.active {
  border-color: rgba(var(--color-border), 1)!important;
    /*margin-bottom: 9px*/
}

.variant-input label {
  text-transform: none;
}

.media__item{
  overflow:hidden;
}

.media__item-content{
  position:relative;
  overflow:hidden;
  height:100%;
  width:100%;
  background:var(--colorBody)
}

.product-navigation .media__item-content{
  background:var(--colorBody)
}

.media__item-content .icon-arrow-left{
  width:33px;
  margin-right:8px;
}

.media__item-content .icon-arrow-right{
  width:33px;
  margin-left:8px;
}

.media__item-content .media__overlay{
  position:relative;
  transition:transform 2s ease-out 0s
}

.media__item-content .media__overlay:after{
  content:"";
  position:absolute;
  top:0;
  right:0;
  bottom:0;
  left:0;
  z-index:1;
  transition:opacity 0.5s ease;
}

.media__item-content .media__overlay:before{
  content:"";
  position:absolute;
  top:0;
  right:0;
  bottom:0;
  left:0;
  z-index:auto;
  background:#000;
  opacity:0;
  transition:opacity 0.5s ease;
}

.media__link{
  display:block;
  width:100%;
  height:100%;
}

.media__overlay{
  display:block;
  overflow:hidden;
  height:100%;
  width:100%;
  z-index: 1
}

.media__title{
  position:absolute;
  bottom:0;
  left:0;
  margin:0 12.5px 12.5px 12.5px;
  pointer-events:none;
  /*color:var(--colorTextBody);*/
  transition:transform 0.5s ease
}

@media only screen and (min-width:590px){

  .media__title{
    margin:0 25px 25px 25px
  }
}

.media__title--right{
  left:auto;
  right:0;
}

.media__underline-me{
  position:relative;
  display:inline-block;
}

[data-aos=media__animation] .media__overlay{
  transform:scale(1.1);
  opacity:0;
  transition:opacity 1s cubic-bezier(0.44, 0.13, 0.48, 0.87),transform 1.5s cubic-bezier(0.44, 0.13, 0.48, 0.87)
}

.no-js [data-aos=media__animation] .media__overlay{
  opacity:1
}

[data-aos=media__animation] .media__overlay:after{
  opacity:0
}

.no-js [data-aos=media__animation] .media__overlay:after{
  opacity:1
}

[data-aos=media__animation].aos-animate .media__overlay{
  opacity:1;
  transform:scale(1);
}

[data-aos=media__animation].aos-animate .media__overlay:after{
  opacity:1;
  transition-duration:1.5s;
}

.media__item:nth-child(2) .media__overlay{ transition-delay:0.25s; }

.media__item:nth-child(3) .media__overlay{ transition-delay:0.5s; }

.media__item:nth-child(4) .media__overlay{ transition-delay:0.13s; }

.media__item:nth-child(5) .media__overlay{ transition-delay:0.38s; }

.media__item:nth-child(6) .media__overlay{ transition-delay:0.63s; }

.media__item:nth-child(7) .media__overlay{ transition-delay:0.25s; }

.media__item:nth-child(8) .media__overlay{ transition-delay:0.5s; }

.media__item:nth-child(9) .media__overlay{ transition-delay:0.13s; }

.media__item:nth-child(10) .media__overlay{ transition-delay:0.38s; }

.media__item:nth-child(11) .media__overlay{ transition-delay:0.63s; }

.media__item:nth-child(12) .media__overlay{ transition-delay:0.45s; }

.custom-content{
  display:flex;
  align-items:stretch;
  flex-wrap:wrap;
  width:auto;
  margin-bottom:-30px;
  margin-left:-30px
}

@media only screen and (max-width:589px){

  .custom-content{
    margin-bottom:-22px;
    margin-left:0px
  }
}

.custom__item{
  flex:0 0 auto;
  margin-bottom:30px;
  padding-left:30px;
  max-width:100%
}

@media only screen and (max-width:589px){

  .custom__item{
    flex:0 0 auto;
    padding-left:0px;
    margin-bottom:22px
  }

  .custom__item.small--one-half{
    flex:1 0 50%;
    max-width:400px;
    margin-left:auto;
    margin-right:auto;
  }
}

.custom__item img{
  display:block;
}

.custom__item-inner{
  position:relative;
  display:inline-block;
  text-align:left;
  max-width:100%;
  width:100%;
}

.custom__item-inner--html,.custom__item-inner--video{
  display:block;
}

.custom__item-inner--image{
  width:100%;
}

.custom__item-inner--html img{
  display:block;
  margin:0 auto;
}

.custom__item-inner--placeholder-image{
  width:100%;
}

@media only screen and (min-width:590px){
  .custom__item-inner--price-list {
    margin-left:15px;
  }
}

.customers-hero .custom__item-inner {
  min-height: 700px
}

.customers__membership-overlay {
  width:100%;
  height:150px;
  top:-150px; 
  left:0; 
  position:absolute; 
  background-image: linear-gradient(to bottom, rgba(var(--color-background),0), rgba(var(--color-background),1))
}

.customers__membership-cover {
  position:absolute; 
  top:0; 
  left: 0; 
  width: 100%; 
  height: 100%;
}

.customers__membership-wrapper {
  position: relative;
  background: transparent!important;
  background-color: transparent!important
}

.btn--standalone {
  display: inline-flex!important;
  font-size: var(--buttonTextSize);
  background: transparent!important;
  margin-right:auto;
  letter-spacing: normal;
  color: rgba(var(--color-foreground), 1)!important;
  height: 56px;
  margin: 0!important;
}

.align--top-middle{
  text-align:center;
}

.align--top-right{
  text-align:right;
}

.align--middle-left{
  align-self:center;
}

.align--center{
  align-self:center;
  text-align:center;
}

.align--middle-right{
  align-self:center;
  text-align:right;
}

.align--bottom-left{
  align-self:flex-end;
}

.align--bottom-middle{
  align-self:flex-end;
  text-align:center;
}

.align--bottom-right{
  align-self:flex-end;
  text-align:right;
}



.logo-bar{
  text-align:center;
  margin-bottom:-40px;
  display:flex;
  align-items:center;
  justify-content:center;
  flex-wrap:wrap;
}

.logo-bar__item{
  flex:0 1 110px;
  vertical-align:middle;
  margin:0 20px 26.66667px;
}

@media only screen and (min-width:590px){

  .logo-bar__item{
    flex:0 1 160px;
    margin:0 26.66667px 40px
  }
}

.logo-bar__image{
  display:block;
  margin:0 auto;
}

.logo-bar__link{
  display:block;
}

.opening-hours :is(td, th) {
  padding: 5px
}

.opening-hours table {
  margin-left: -5px;
  overflow: hidden; 
  text-overflow: ellipsis;
  white-space: nowrap;
}

.larger-text .opening-hours table {
  font-size: calc(var(--fontBaseSize) + 2px);
}

.background-media-text{
  position:absolute;
  width:100%;
  overflow:hidden;
  background:var(--colorBody);
    }

.background-media-text__video{
  position:absolute;
  top:0;
  left:0;
  bottom:0;
  width:100%;
  z-index:0;
}

@media only screen and (max-width:589px){

  .background-media-text__video{
    width:300%;
    left:-100%
  }
}

.background-media-text__video iframe,.background-media-text__video video{
  position:absolute;
  top:0;
  left:0;
  height:100%;
  width:100%;
  pointer-events:none
}

@media only screen and (min-width:590px){

  .background-media-text__video iframe,.background-media-text__video video{
    height:120%;
    max-width:none;
    left:-100%;
    height:150%;
    width:300%
  }
}

@media screen and (min-width:1140px){

  .background-media-text__video iframe,.background-media-text__video video{
    width:100%;
    height:300%;
    left:auto;
    top:-100%
  }
}

.video-parent-section.video-interactable iframe,.video-parent-section.video-interactable video{
  pointer-events:auto;
}

.background-media-text__inner{
  position:absolute;
  z-index:1;
  width:100%
}

.video-interactable .background-media-text__inner{
  pointer-events:none
}

.background-media-text__text{
  text-align:left;
  /*background:var(--colorBody);*/
    padding:40px;
    width:380px;
    }

.background-media-text__text .btn{
  margin-top:20px;
}

@media only screen and (min-width:590px){
  .background-media-text--right .animation-cropper{
    float:right;
  }
}

.background-media-text__container, .splitted-media-text__container {
  position:absolute;
  top:0;
  left:0;
  right:0;
  bottom:0;
}

@media only screen and (max-width:589px){
  .background-media-text{
    position:relative;
  }
  .background-media-text__inner{
    position:relative;
  }

  .background-media-text__aligner{
    margin:-11px -11px 11px;
    overflow:hidden;
  }
  .background-media-text__aligner{
    margin-right:0;
  }
  .background-media-text__text{
    padding: 34px!important;
    width: calc(100% - 30px)!important;
    margin: 0 0!important;
    left: 5px;
    margin-left: 15px!important;
  }
  .background-media-text.loading:after,.background-media-text.loading:before{
    top:117px;
  }
}

.background-media-text__overlay-wrapper{
  position:relative;
  width: 100%;
}
@media only screen and (min-width:590px){
  .background-media-text__overlay-wrapper{
    position:absolute;
  }
}

.background-media-text__overlay{
  position:relative;
  display:inline-block;
  padding:30px;
  margin:30px;
  width:85%;
  z-index:3;
  border-radius: 0;
  border-top-left-radius: var(--imageRadius);
  border-top-right-radius: var(--imageRadius);
  background-color: rgb(var(--color-background));
  z-index: 5;
}

[data-theme="dark"] .background-media-text__overlay{
  background-color:var(--colorBody)!important;
}

.background-media-text__overlay .flickity-page-dots {
  left: 0;
}

.slideshow-wrapper .background-media-text__overlay {
   border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
}

@media only screen and (min-width:590px){

  .background-media-text__overlay{
    padding:40px;
    position:absolute;
    left:60px;
    top:50%;
    transform:translateY(-50%);
    margin:0;
    width:380px;
    padding:0px;
    border-radius: var(--imageRadius);
  }

  .background-media-text__overlay .images-slide {
    padding: 40px;
  }
  
  .slideshow-wrapper .background-media-text__overlay{
    padding:40px;
    position:absolute;
    left:0px;
    top:50%;
    transform:translateY(-50%);
    margin:0;
    width:auto;
    padding:40px 40px 40px 20px;
    border-radius: var(--imageRadius);
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    z-index: 4;
  }
  
  .background-media-text__overlay .flickity-previous {
    left: 10px;
  }
  
  .background-media-text__overlay .flickity-next {
    right: 10px
  }
  
  .background-media-text__overlay .flickity-page-dots {
    bottom: 15px
  }
  
  .background-media-text__overlay--arrows {
    padding-left:20px;
    padding-right:20px;
  }
}

/*================== ANNOUNCEMENT BAR ==================*/
.announcement{
  position:relative;
  overflow:hidden;
  background-color:var(--colorAnnouncement);
    color:var(--colorAnnouncementText);
      max-height:100px;
      transition:max-height 0.3s cubic-bezier(0, 0, 0.38, 1);;
      transform:translateZ(0);
      z-index:29;
      }

.site-announcement-container{
  position: relative;
  z-index: 27
}
.site-announcement-container:has(.announcement-bar--sticky) {
  position: sticky;
  top: 0;
  z-index: 27
}

.announcement--closed{
  max-height:0 !important;
  padding: 0 !important;
}

.announcement__text{
  display:block;
  padding:5px 20px 4px;
  font-size:14px;
  transition:opacity 0.75s ease;
  text-align:center;
  height:36px
}

@media only screen and (min-width:590px){

  .announcement__text{
    padding:6px 20px 5px;
    font-size:16px
  }
}

.announcement--closed .announcement__text{
  opacity:0
}

.announcement__link{
  display:block;
  color:var(--colorAnnouncementText)
    }

.announcement__link:hover{
  color:var(--colorAnnouncementText);
    }

.announcement__close{
  display:block;
  position:absolute;
  top:50%;
  right:0;
  transform:translateY(-50%);
  padding:0 10px;
  transition:opacity 1s ease 0.8s;
  cursor:pointer;
  z-index:2
}

.announcement--closed .announcement__close{
  opacity:0
}

.announcement__close .icon{
  color:var(--colorAnnouncementText);
    width:20px;
    height:20px;
    vertical-align:middle
    }

@media only screen and (min-width:590px){

  .announcement__close .icon{
    width:22px;
    height:22px
  }
}

.announcement-bar {
  font-size:calc(var(--fontBaseSize));
    position:relative;
    text-align:center;
      z-index: 201;
        padding:0px 0;
        border-bottom-color:#e8e8e1;
        border-bottom-color:var(--colorBorder);
}

@media only screen and (max-width:589px){
  .announcement-bar {
    z-index: 201;
  }
}

.announcement-slider__slide{
  position:relative;
  overflow:hidden;
  padding:0 5px;
  width:100%;
  height:100%;
  display: table;

  display: flex!important;
  justify-content: center;
  align-items: center;
}

.announcement-link{
  display:block;
  color:var(--colorAnnouncementText);
  vertical-align: middle;
  text-align: center;
}

.announcement-link:active,.announcement-link:hover{
  color:var(--colorAnnouncementText);
    }

.announcement-text{
  font-weight:700;
  display:block;
  text-transform:uppercase;
  letter-spacing:0.2em;
  font-size:0.85em;
}

.announcement-link-text{
  display:block
}

.announcement-link .announcement-link-text{
  text-decoration:underline
}

.announcement-link-text a{
  color:inherit;
}

@media only screen and (min-width:769px){
  .announcement-slider[data-compact=true] .announcement-slider__slide{
    display:none;
  }

  .announcement-slider[data-compact=true] .announcement-slider__slide:first-child{
    display:block;
  }
  .announcement-slider[data-compact=true] .announcement-link-text,.announcement-slider[data-compact=true] .announcement-text{
    display:inline;
  }

  .announcement-slider[data-compact=true] .announcement-text+.announcement-link-text{
    padding-left:5px;
  }

  .announcement-slider[data-compact=true].flickity-enabled .announcement-slider__slide{
    display:block;
  }
  .announcement-slider[data-compact=false]{
    display:flex
  }

  .announcement-slider[data-compact=false] .announcement-slider__slide{
    flex:1 1 33%;
  }
}

#AnnouncementWrapper {
  position:relative;
}

@media only screen and (max-width:589px){
  #AnnouncementWrapper {
    z-index: 201
  }
}

/*.announcement-bar {
  position: fixed;
  width: 100%
}*/

.announcement-bar--sticky{
  left:0;
  right:0;
  top:0;
  transform:translate3d(0, 0%, 0);
  transition:transform 0.4s cubic-bezier(0.165, 0.84, 0.44, 1);
  z-index:29;
  z-index: 201
}

.js-drawer-open--search .announcement-bar--sticky{
  z-index:33
}
@media only screen and (min-width:590px){

  .announcement-bar--sticky {
    z-index: 29
  }
}

.announcement-bar--opening{
  transform: translate3d(0px, -100%, 0px);
  transition:transform 0.4s cubic-bezier(0.165, 0.84, 0.44, 1);
  z-index: 201;
}

.fading-images-overlay__overlay:before{
  content:"";
  position:absolute;
  top:0;
  right:0;
  bottom:0;
  left:0;
  z-index:3;
}

.fading-images__item{
  position:absolute;
  width:100%;
  height:100%;
  left:0;
  opacity:0;
  -o-object-fit:cover;
  object-fit:cover
}

.no-js .fading-images__item{
  opacity:1
}

.fading-images-overlay__titles{
  opacity:0;
  margin:0
}

.fading-images-overlay__titles.active-titles,.fading-images-overlay__titles.finished-titles{
  opacity:1;
}

.fading-images-overlay__titles--heading-style{
  font-family:var(--fontHeaderPrimary),var(--fontHeaderFallback);
    font-weight:var(--fontHeaderWeight);
      letter-spacing:var(--fontHeaderSpacing);
        line-height:var(--fontHeaderLineHeight);
          }

[data-header-capital=true] .fading-images-overlay__titles--heading-style{
  text-transform:uppercase;
}

.fading-images-overlay__titles .animation-cropper{
  display:block;
}

.fading-images-overlay__title{
  display:inline-block;
  -webkit-box-decoration-break:clone;
  box-decoration-break:clone;
  padding:4px 15px;
  margin:2px 0;
}

.active-titles .fading-images-overlay__title{
  animation:1.2s forwards;
  animation-name:rise-up;
}

.finished-titles .fading-images-overlay__title{
  animation:0.75s forwards;
  animation-name:rise-up-out;
}

.active-image.lazyloaded{
  animation:kenburns 7s linear forwards;
  z-index:2;
}

.finished-image.lazyloaded{
  animation:kenburns-out 1s linear forwards;
  z-index:1;
}


.image-row:after{content:"";display:table;clear:both;}

.image-row__placeholder{
  float:left;
  width:33.33%
}

.image-row--gutters .image-row__placeholder{
  width:32%;
  margin:0 1% 2%
}

.image-row--gutters .image-row__placeholder:first-child{
  margin-left:0;
}

.image-row--gutters .image-row__placeholder:last-child{
  margin-right:0;
}

.image-row__image{
  position:relative;
  min-height:1px;
  float:left
}

.image-row__image:after{
  content:"";
  display:block;
  height:0;
  width:100%;
}

.image-row__image img{
  display:block;
  position:absolute;
  top:0;
  left:0;
}

.image-row__image .js-photoswipe__zoom{
  cursor:zoom-in;
}

.image-row__image a .js-photoswipe__zoom{
  cursor:pointer;
}

.product__photo-zoom{
  position:absolute;
  bottom:0;
  right:0;
  cursor:zoom-in;
  display: block;
  opacity:0;
  width:100%;
  height: 100%;
  top:0;
  left:0;
  margin:0;
  border-radius:0;
  max-width: none
}
/*.product__photo-zoom{
  position:absolute;
  bottom:0;
  right:0;
  cursor:zoom-in
}

@media only screen and (min-width:590px){
  .product__photo-zoom{
    display: block;
    opacity:0;
    width:100%;
    height: 100%;
    top:0;
    left:0;
    margin:0;
    border-radius:0
  }
}*/

/*============ #new label ================*/
.new-product-label {
  display: inline-block;
  line-height: 1;
  font-weight: 600;
  padding: 3px 10px 3px;
  margin: 0 0px 12px 0;
  font-style: normal;
  font-size: var(--fontSmall);
  text-transform: none;
  background-color: var(--shopify-editor-setting-color_body_bg);
    font-family: 'Futura', sans-serif;
    letter-spacing: 0.025em;
    line-height: 1.6;
    border: 1px solid var(--colorBorder);
      border: calc(var(--buttonOutlineWeight) / 2) solid;
        border-radius: calc(var(--buttonRadius)*0.5);
      }
.new-product-label-collection {
  display: inline-block;
  line-height: 1;
  font-weight: 600;
  padding: 2px 6px 2px;
  margin: 0 0px 0px 0;
  font-style: normal;
  font-size: 11px;
  text-transform: none;
  letter-spacing: 0.025em;
  line-height: 1.6;
  /*background-color: var(--colorSaleTag);
  color: var(--colorSaleTagText);*/
}


/*============= #featured Images ============*/
@media screen and (min-width: 590px) {
  .grid-item-left {
    flex: 1;
    margin-top: -20em;
  }
  .grid-item-right {
    flex: 1;
    margin-top: -20em;
    margin-left: 25%;
  }
}

@media only screen and (max-width:589px){
  .collection--wide-tall--200 {
    height: 200px;
  }
  .collection--wide-tall--250 {
    height: 250px;
  }
  .collection--wide-tall--300 {
    height: 300px;
  }
  .collection--wide-tall--350 {
    height: 350px;
  }
  .collection--wide-tall--400 {
    height: 400px;
  }
  .collection--wide-tall--450 {
    height: 450px;
  }
  .collection--wide-tall--500 {
    height: 500px;
  }
}
@media only screen and (min-width:590px){
  .collection--wide-tall {
    height:380px
  }
}

@media only screen and (min-width:590px){
  .collection--square-large-flat {
    height:410px
  }
}
.middle-alignment {
  text-align: center;
  width: 100%;
  height: 100%;
  top: 37%;
  margin: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding: 40px!important;
  height: auto;
  bottom: auto;
  top: auto;
  left: auto;
  display: inline-block;
  vertical-align: middle;
}
.middle-alignment-top {
  bottom: auto;
  top: 0;
}
.middle-alignment-center {
  bottom: auto;
  top: auto;
}
.middle-alignment-bottom {
  bottom: 0;
  top: auto;
}

.middle-alignment-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.media__overlay-hide .media__overlay:after {
  opacity:0;
}
[data-aos=media__animation].aos-animate .media__overlay-hide:after {
  opacity:0;
}
.media__overlay-hide .media__item-content .media__overlay:after {
  opacity:0;
}

.media__title-full_width{
  margin:0;
  padding-left: 12.5px;
  padding-right: 12.5px;
  width: 100%;
}

@media only screen and (max-width:589px){.small--one-half-full{width:100%!important;}}

/*================ featured collection scroll ====================*/
.collection-scroll-header {
  display:block!important;
  margin-bottom: 30px;
}
.grid__item-title {
  display:none!important;
}

@media only screen and (min-width:590px){
  .collection-scroll-header {
    margin-bottom: 40px;
  }
  [data-style=slider] .collection-scroll-header {
    display:none!important;
  }
  .grid__item-title {
    display:block!important;
  }
}

@media only screen and (max-width:589px){
  .featured-row__slider {
    margin-top: -20px;
  }
}

.more_button-width {
  flex: 0 0 30vw!important;
}
.product__see-all {
  margin-top: 0;
}

.product-inline {
  width: 65%;
  width: 190px;
}
.product-inline--left {
  margin-right: auto;
}
.product-inline--center {
  margin-left: auto;
  margin-right: auto;
}
.product-inline__meta {
  padding-left: 15px;
  padding-right: 45px;
  margin-left: auto;
  margin-right: auto;

}
.product-inline__meta .social-sharing{
  margin-top: 20px;
}

.product-full_width {
  padding: 0 20px;
  padding-right: 0px;
}
.product_image-full_width {
  padding-left: 0px;
  padding-right: 0px;
}
.product_details-full_width {
  padding: 0 20px;
}

.product_button-title_row {
  text-align: center;
  padding-top: 30px;
  padding-bottom: 20px;
}

@media only screen and (min-width:590px){
  .product-inline__meta .social-sharing {
    margin-top: $gutter;
  }
  .product-full_width {
    padding-right: 40px;
  }
  .product_button-title_row {
    display:none!important;
  }
}

@media only screen and (max-width:589px){
  .product-inline__meta{
    padding-left: 20px;
    padding-right: 20px;
    margin-top: $gutter / 2;
  }
}

.grid-overflow-wrapper{
  overflow:visible;
  overflow-x:scroll;
  -webkit-overflow-scrolling:touch;
  padding-bottom:26.66667px;
  padding-bottom:0px;
}

.grid-overflow-wrapper .grid{
  white-space:nowrap;
  display:flex;
}

.grid-overflow-wrapper .grid__item{
  width:66vw;
  flex:0 0 66vw;
  display:inline-block;
  float:none;
  white-space:normal
}

@media only screen and (min-width:590px){
  .grid-overflow-wrapper .grid__item-1 {
    width:66vw;
    flex:0 0 66vw!important;
  }
  .grid-overflow-wrapper .grid__item-2 {
    width:36vw;
    flex:0 0 36vw!important;
  }
  .grid-overflow-wrapper .grid__item-3 {
    width:266vw;
    flex:0 0 26vw!important;
  }
  .grid-overflow-wrapper .grid__item-4 {
    width: 19vw;
    flex:0 0 19vw!important;
  }
  .grid-overflow-wrapper .grid__item-5 {
    width: 17vw;
    flex:0 0 17vw!important;
  }
}

.grid-overflow-wrapper .grid__item:first-child{
  margin-left:20px;
}

.grid-overflow-wrapper .grid__item:last-child:after{
  content:"";
  display:inline-block;
  width:100%;
  height:1px;
  margin-right:20px;
}

.grid-overflow-wrapper .grid__item--view-all{
  align-self:center;
}

.grid-overflow-wrapper .product__content{
  margin-bottom:0;
}

div::-webkit-scrollbar:vertical {
  display: none;
}
div::-webkit-scrollbar:horizontal {
  display: none;
}

.images-slide{
  width:100%;
}

@media only screen and (min-width:769px){
  .images-slide{
    display:none
  }

  .images-slide:first-child{
    display:flex;
    flex-direction: column;
  }
  
  .flickity-enabled .images-slide{
    display:flex;
  }

  .grids-slide.images-slide{
    display:block;
  }
}

/*================ Font family modification ===================*/
.product-form__item--payment-button .btn--secondary-accent{
  font-family:var(--fontButtonPrimary),var(--fontHeaderFallback);
    }
.btn,.product-reviews .spr-button,.product-reviews .spr-summary-actions a,.rte .btn,.shopify-payment-button .shopify-payment-button__button--unbranded, .btn--subscribed, .btn.cart__discount{
  font-family:var(--fontButtonPrimary),var(--fontHeaderFallback);
    }

/*===================== Parallax ===================*/

.parallax-container {
  position: absolute;
  top: -30%;
  left: 0;
  min-height: 650px;
  height: 110%;
  width: 100%;
}

.parallax-image {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 100%;
  background-size: cover;
  background-position: 50% 50%;
  background-repeat: no-repeat;
}

.parallax-image {
  background-attachment: fixed;
}
.js-ipad .parallax-image {
  background-attachment: initial;
}

/*================ #Smooth scroll =========================*/
html {
  scroll-behavior: smooth;
}


/*================ #Image grid =========================*/

.flex-grid{
  display:flex;
  flex-wrap:wrap;
  flex:1 1 100%;
}

[data-center-text=true] .flex-grid{
  justify-content:center
}

.flex-grid--center{
  align-items:center;
}

.flex-grid__item{
  flex:0 1 100%;
  display:flex;
  align-items:stretch
}

.flex-grid__item>*{
  flex:1 1 100%;
}

.flex-grid__item--stretch{
  flex:1 1 100%
}

.flex-grid__item--stretch:first-child{
  min-width:250px;
  padding-left: 30px;
}

@media only screen and (min-width:769px){
  .flex-grid__item--33{
    flex-basis:33.33%;
  }

  .flex-grid__item--50{
    flex-basis:50%;
  }
}

@media only screen and (max-width:768px){
  .flex-grid__item--mobile-second{
    order:2;
  }
}


/*=================== #Rich text =======================*/
.collapsible-trigger--inline_right {
  font-weight:700;
  padding:11px 0 11px 0px
}

.collapsible-title_right {
  margin-left: auto;
  margin-right: 0px;
  text-align: right;
  padding-right: 20px;
}

.column-grid {
  display: inline-block;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: row;
  flex-wrap:wrap;
  align-items: stretch;
  margin-top: 60px;
  margin-bottom: 20px;
}

.media__overlay .column-grid, .media__overlay .column-grid__item {
  margin: 0
}

.column-grid__logo {
  display: inline-block;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: row;
  flex-wrap:wrap;
  align-items: stretch;
  margin-top: 60px;
  margin-bottom: 20px;
  text-align: center;
  margin-bottom: -30px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
}

.column-grid__item {
  display: inline-block;
  vertical-align: top;
  padding-left: 10px;
  padding-right: 10px;
  margin-bottom: 30px;
  overflow: hidden;
}

.column-grid__item--middle {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  align-content: center;
  flex-wrap: wrap;
}

.column-grid__logo .column-grid__item {
  flex: 0 1 160px;
  margin: 0 20px 30px; 
  vertical-align: middle;
}
 
.column-layout__separator {
  height: 108%;
  width: 70%;
  position: absolute;
  top: 45%;
  left: 50%;
  -webkit-transform:translate(-50%,-50%);
  transform:translate(-50%,-50%);
}

@media only screen and (max-width:589px){
  .separator-line {
    border-bottom: calc(var(--dividerWeight)/2) solid;/*1px solid;*/
    border-bottom-color: #e8e8e1;
    border-bottom-color: var(--colorBorder);
    z-index: 1
  }
}

@media only screen and (min-width:590px){
  .column-layout__separator {
    height: 70%;
    width: 100%;
    position: absolute;
    top: 50%;
    left:50%;
    -webkit-transform:translate(-50%,-50%);
    transform:translate(-50%,-50%);
  }
  .separator-line {
    border-right: calc(var(--dividerWeight)/2) solid;/*1px solid;*/
    border-right-color: #e8e8e1;
    border-right-color: var(--colorBorder);
    z-index: 1
  }
  .column-grid {
    margin-top: 0px;
    margin-bottom: 0px;
  }

  .column-grid__logo {
  }
  
  .column-grid__item {
    margin-bottom: 0px;
  }
}

@media screen and ( max-width: 768px ) {
  .column-grid__logo .column-grid__item {
    flex:0 1 110px;
    margin:0 15px 20px
  }
}

.text-wrapper {
  border-radius: var(--imageRadius);
}

.boxed-section {
  border-radius: var(--imageRadius);
  overflow: visible;
}

.boxed-section .feature-row__text {
  overflow: hidden;
}

.boxed-section-align {
  padding-top: 60px;
  padding-bottom: 60px;
  padding-left: 60px;
  padding-right: 60px;
  margin: 0 auto;
  max-width: calc(var(--max-content-width) + 40px)
}
@media only screen and (max-width:589px){
  .boxed-section-align {
    padding-left: 25px;
    padding-right: 25px;
  }
}
  
.boxed-text {
  padding-left: 40px;
  padding-right: 40px
}
@media only screen and (max-width:589px){
  .boxed-text {
  padding-left: 30px;
  padding-right: 30px
}
}

.boxed-image {
  margin-left: 0px;
}

.boxed-section .image-wrap, .feature-row-full-width .image-wrap {
  border-radius: 0;
}

.upsellproducts .product__image-mask {
  box-shadow: none
}
/*=================== #Image with text =======================*/
.main-carousel:after {
  content: ''!important;
}

@media screen and ( max-width: 768px ) {
  .main-carousel:after {
    content: 'flickity'!important;
    display: none!important;
  }
}

.featured-row__number {
  width: 100%;
  color: var(--colorTextBody);
  flex: 0 0 60px;
  text-align: center;
  line-height: 111px;
  position: absolute;
  z-index: -1;
  font-size: 200px;
  top: 22px;
  overflow: visible;
  opacity:0.2
}
.list-item--wrapper {
  display: flex;
  flex-direction: column;
  padding-top: 0!important
}

.list-item--wrapper :is(.h3, .h4, .subtitle, .feature-row__item) {
  flex: 100%;
  padding-left: calc(var(--buttonSize) + 0px)
}

.list-item--wrapper .featured-row__subtext {
  position: relative;
  left: -30px;
}

@media only screen and (max-width:768px){
  .list-item--wrapper :is(.h3, .h4, .subtitle, .feature-row__item) {
    padding-left: calc(var(--buttonSize) + 30px)
  }

  .list-item--wrapper .featured-row__subtext {
    left: 0px;
  }

  .list-item--btn {
    margin-top: auto;
    margin-bottom: auto;
    position: relative;
    bottom: 20px
  }
}

.featured-row__subtext:has(.list-item--btn) {
  display: flex
}
.list-item--btn {
  font-size: calc(var(--buttonTextSize)*2);
  height: var(--buttonSize);
  width: var(--buttonSize);
  background: var(--colorBtnPrimaryHover);
  color: rgba(var(--color-button),var(--alpha-button-background))!important;
  border-radius: 30px;
  margin-right: 30px;
  flex: 0 0 var(--buttonSize);
  text-align:center;
  line-height:var(--buttonSize);
  cursor: pointer;
  font-weight: 700;
}

.list-item--btn.is-selected {
  background: var(--colorBtnPrimary);
  background: rgba(var(--color-button),var(--alpha-button-background));
  color: rgb(var(--color-button-text))!important;
}

@media only screen and (max-width:589px){
    .list-item--btn {
      background: var(--colorBtnPrimary);
      background: rgba(var(--color-button),var(--alpha-button-background));
      color: rgb(var(--color-button-text))!important;
    }
}
/*@media only screen and (max-width:768px){
  .list-item--btn {
    background: var(--colorBtnPrimary);
  }
}*/
  
/*=================== Product template ====================*/
@media only screen and (min-width:590px){
  .full-expander-spacing {
    margin-top: 50px;
  }
}
.page-width-bottom-removed {
  padding-bottom: 0px!important;
}
.page-content-bottom-removed {
  padding-bottom: 0px!important;
}
.description_full-width {
  width: calc(100% + 35px);
  margin-left: -20px;
}

.description_full-width :is(h1,h2,h3,h4,h5,h6) {
  margin-top: 40px;
  margin-bottom: 20px;
}

.description-row__text, .description-row__text--small{
  /*padding-top:40px;
  padding-bottom:60px;
  padding-left: 22px;
  padding-right: 22px;*/
  padding: 0px 22px 40px;
}
@media only screen and (min-width:590px){
  .description-row__text{
    /*padding-top:80px;
    padding-bottom:80px;
    padding-left: 60px;
    padding-right: 40px;*/
    padding: 10px 55px 50px 60px
  }

  .description-row__text--small{
    padding-top:20px;
    padding-bottom:20px;
    padding-left: 60px;
    padding-right: 40px;
  }

  .description-row__text--single {
    padding: 0px 40px 0px 60px
  }
  
  .description_full-width {
    /*margin-left: -15px;
    width: 50vw;*/
    margin-left: -15px;
    width: calc(100% + 55px);
    height: 100%;
    height: var(--product_sidebar_padding);
    overflow: auto;
  }
  
  .product-form__controls-group .description_full-width {
    margin-left: -60px;
  }
}
.product-single__thumbnails--beside{
  margin-left:0px;
  margin-right:20px;
}

.product-single__media-group{
  direction:ltr
}

.product-single__media-group a:not(.btn) {
  display:block;
  max-width:100%;
}

.product-single__media-group img{
  display:block;
  margin:0 auto;
  max-width:100%;
  width:100%;
}

.product-single__main-photos{
  position:relative;
  flex:1 1 auto;
  border-radius: var(--imageRadius);
}

.product-single__main-photos img{
  display:none;
}

.product-single__main-photos .flickity-page-dots{
  display:none;
}

.product-dots__enabled {
  padding-top: 55px;
}

.product-dots__padding {
  padding-bottom: 20px
}
@media only screen and (min-width:590px){
  .product-dots__enabled {
    padding-top: 0px;
  }
  .product-dots__padding {
    padding-bottom: 0px
  }
}
@media only screen and (max-width:768px){
  .product-single__main-photos{
    margin-bottom:30px;
    margin-left:-17px;
    margin-right:-17px;
    margin-top: 25px;
    margin-left: 2px;
    margin-right: 2px;

    margin-top: 0px;
  }

  .product-single__main-photos .flickity-page-dots{
    display:block;
  }
}

.product-single_main-slide:not(.is-selected) button,.product-single_main-slide:not(.is-selected) iframe,.product-single_main-slide:not(.is-selected) model-viewer,.product-single_main-slide:not(.is-selected) video{
  display:none;
}

.product-single_main-slide{
  display:none;
  width:100%;
  overflow:hidden;
}

.product-single_main-slide:first-child{
  display:block;
}

.product-slideshow.flickity-enabled .product-single_main-slide{
  display:none;
}

@media only screen and (max-width:768px){
  .grid--product-images--partial .flickity-slider .product-single_main-slide{
    width:75%;
    margin-right:4px
  }
}

.hide-thumbnail{
  display: none
}
@media only screen and (max-width:768px){
  .hide-thumbnail-mobile{
    display: none
  }
}

.product-single__variants{
  display:none
}

.no-js .product-single__variants{
  display:block;
  margin-bottom:40px
}

.product-single__meta .product__price--listing {
  display: block;
}

[data-body-capital=true] .product-single__vendor{
  text-transform:uppercase;
  letter-spacing:0.2em;
}

.product__price{
  font-size:calc(var(--fontBaseSize) + 2px)
}

@media only screen and (min-width:590px){
  .product__price {
    font-size:calc(var(--fontBaseSize) + 4px)
  }
}

.product__price.enlarge-text {
  font-size: calc(var(--fontHeaderSize) - 5px);
}

/*================= #Quick shop =====================*/
.logo-bar{
  display: inline-block;
  width: 100%;
}

.slick-list.draggable{
  overflow: hidden !important
}

.quick-product__btn{
  position:absolute;
  top:-15px;
  right:-15px;
  z-index:2;
  border-radius:50%;
  width:var(--buttonSize);
  height:var(--buttonSize);
  margin-top: 10px;
  margin-right: 10px;
  color: rgb(var(--color-button-text));
  background: rgba(var(--color-button),var(--alpha-button-background));
  overflow:hidden;
  font-size:calc(var(--fontBaseSize));
  line-height:1.2;
  cursor:pointer;
  opacity:0;
  transform:translateY(5px);
  transition:opacity 0.15s ease,transform 0.15s ease-out,background 0.15s ease
}

.quick-product__btn .icon-quick-add path {
  stroke: rgb(var(--color-button-text));
}
.no-js .quick-product__btn{
  display:none
}

.product__content:hover :is(.quick-product__btn, .quick-play__btn){
  opacity:1;
}

.product__content:has(.quick-product__btn:hover) .media__overlay:before, .product__content:has(.quick-product__btn-wrapper select:hover) .media__overlay:before {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  z-index: 3;
  background-color: #000;
  opacity: 0.4;
}

.product__content:hover .quick-product__btn:active{
  transform:none;
}

.quick-product__btn--not-ready{
  pointer-events:none;
}

.quick-product__label{
  position:relative;
  display:block;
  text-align:center;
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
}

.product__has-quick-shop .product__content:hover .product__tag{
  opacity:0;
  transition-duration:0.2s;
}

.quick-product__btn{
  top:0em !important;
  z-index:9;
  display: flex;
  right: 1px;
  justify-content: center;
  align-content: center;
  align-items: baseline;
  transform: translateY(0);
}

.quick-product__btn-wrapper {
  display:flex;
  align-items:flex-end
}

.quick-product__btn-wrapper select{
  top:0em !important;
  position: absolute !important;
  z-index: 9 !important;
  right: 1px !important;
  height: -moz-fit-content;
  width: -moz-fit-content;
  height:fit-content;
  padding: 0;
  font-size: 0;
  width: var(--buttonSize) !important;
  border-radius: var(--buttonSize) !important;
  height: var(--buttonSize) !important;
  border:none!important;
  margin-top: 10px;
  margin-right: 10px;
  -webkit-appearance: none !important;
  -moz-appearance: none !important;
  appearance: none !important;
  cursor: pointer;
}

.quick-product__btn-select:hover + .quick-product__btn {
  filter: brightness(var(--colorBtnFilterBrightness));
}
  
.quick-product__btn-wrapper select:hover, .quick-product__btn:hover, .quick-play__btn:hover {
  filter: brightness(var(--colorBtnFilterBrightness));
}


@media only screen and (max-width:589px){
  [data-view=small] .quick-product__btn, [data-view=small] .quick-product__btn-wrapper select, [data-view=list] .quick-product__btn, [data-view=list] .quick-product__btn-wrapper select {
    margin-top: -10px;
    margin-right: -10px;
  }
}

.quick-product__btn-wrapper{
  color:transparent!important;
  opacity:0;
}

.grid__item.grid-product.product__has-quick-shop:hover .quick-product__btn-wrapper{
  opacity:1
}
@media only screen and (max-width:768px){
  .announcement-slider .announcement-slider__slide{
    display:none
  }

  .announcement-slider .announcement-slider__slide:first-child{
    display:block;
  }

  .announcement-slider.flickity-enabled .announcement-slider__slide{
    display:block;
  }
  .quick-product__btn{
    opacity:1 !important;
    top:0em !important;
    right:0;
    padding: 0
  }
  .quick-product__btn-wrapper select{
    top:0em !important;
    right:0!important;
    opacity:1
  }
  .quick-product__btn-wrapper {
    opacity:1
  }
}
@media only screen and (max-width:450px){
  .quick-product__btn{
    top:0em !important
  }
  .quick-product__btn-wrapper select{
    top:0em !important
  }
}

.next {
  right:10px;
}


.product-link--button {
  position: absolute;
  display: block;
  width: 16px!important;
  height: 16px!important;
  padding:0px;
  margin: -8px 0 0 -8px;
  background: #ffffff;
  border-radius: 100%;
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.25);
  cursor: pointer;
  z-index: 3;
  transform: scale(1);
  transition: transform 0.25s ease-in-out;
}
.product-link--button::after {
  position: absolute;
  content: "";
  width: 40px;
  height: 40px;
  left: -12px;
  top: -12px;
  border-radius: 100%;
  background: rgba(255, 255, 255, 0.4);
  -webkit-animation: 1.4s shopTheLookDotKeyframe ease-in-out infinite;
  animation: 1.4s shopTheLookDotKeyframe ease-in-out infinite;
}

.product-link--button--dark {
  background: #000000;
}
.product-link--button--dark::after {
  background: rgba(0, 0, 0, 0.4);
}

.product-link--button.is-active, .product-link--button:hover {
  transform: scale(1.5);
}

@media only screen and (max-width:589px){
  .product-link--button {
    transform: scale(1.5);
  }
  .product-link--button.is-active, .product-link--button:hover {
    transform: scale(1.7);
  }
}

.drawer{
  display:none;
  position:fixed;
  overflow:hidden;
  -webkit-overflow-scrolling:touch;
  top:0;
  bottom:0;
  max-width:100%;
  z-index:201;
  transition:transform 0.25s cubic-bezier(0.43, 0.01, 0.44, 1)
}

.mobile-nav__wrapper{
  position:relative;
  overflow:hidden
}

.mobile-nav__wrapper:after{
  content:"";
  display:block;
  position:absolute;
  top:0;
  right:0;
  bottom:0;
  left:0;
  z-index:1;
  pointer-events:none;
  border-radius:var(--roundness);
}

.mobile-nav{
  margin:0;
  list-style:none;
  transition:transform 0.25s cubic-bezier(0.165, 0.84, 0.44, 1)
}

[data-level="2"] .mobile-nav{
  transform:translateX(-100%)
}

[data-level="3"] .mobile-nav{
  transform:translateX(-200%)
}

.mobile-nav__button{
  display:block;
  background:none;
  border:0;
  padding:0;
  width:100%;
  text-align:left;
}

.mobile-nav__link{
  position:relative;
  display:flex;
  width:100%;
  padding:20px 40px;
  padding:20px 0px;
  align-items:center;
  justify-content:space-between;
  font-size:var(--fontMenuSize);
  color: rgba(var(--color-foreground), 1);
}

@media only screen and (max-width:589px){
  .mobile-nav__link{
    padding:15.53px 15px;
    padding:15.53px 0px;
  }
}

.mobile-nav__link>span{
    display:block;
    flex:1 1 auto;
  }

.mobile-nav__link .icon{
    width:11px;
    height:11px;
    margin-left:10px;
  }

.mobile-nav__image{
  position:relative;
  width:auto;
  height:40px;
}

.mobile-nav__image img{
  -o-object-fit:cover;
  object-fit:cover;
  width:100%;
  height:100%;
  max-width: 65px;
  }

.mobile-nav__image+span{
  padding-left:5px;
}

.mobile-nav__link--back{
  font-weight:700;
  justify-content:flex-start
}

.mobile-nav__link--back>span{
  text-underline-offset:2px;
}

.mobile-nav__link--back .icon{
  margin-right:15px;
  margin-left:0;
}

.mobile-nav__item{
  display:block;
  width:100%;
  margin:0
}

.mobile-nav__item:last-child{
    padding-bottom:10px;
}

.mobile-nav__link:after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0px;
  right: 0px;
  border-bottom: calc(var(--dividerWeight)/2) solid;
  border-bottom-color: rgba(var(--color-border), 1);
  color: rgba(var(--color-border), 1);
}

.mobile-nav__dropdown{
  display:none;
  visibility:hidden;
  position:absolute;
  width:100%;
  top:0;
  right:-100%;
  margin:0;
  opacity:0;
  transition:all 0.25s cubic-bezier(0.165, 0.84, 0.44, 1)
}

.mobile-nav__dropdown.is-active{
  display:block;
  visibility:visible;
  opacity:1;
  transition:all forwards 0.55s cubic-bezier(0.165, 0.84, 0.44, 1) 0.1s;
}

.product-form select {
  -moz-appearance:none !important;
  -webkit-appearance: none !important;
  appearance: none !important;
  padding-right: 2rem !important;
}

@media only screen and (max-width:589px){
  .upsellproducts-cartdrawer {
    padding: 0 0px;
  }
  .drawer__mobile-nav .upsellproducts-cartdrawer {
    padding: 0 15px;
    padding: 0;
  }
}

.product-form select:focus {
  border-color:var(--colorTextBody);
}

.product__bundle--selected {
  border-color:var(--colorTextBody);
}

.SelectBundle{
  width:100%
}

[data-section-type="blog"] .grid {
  display:flex;
  flex-wrap:wrap;
  margin-bottom: -40px
}

[data-section-type="blog"] .grid a.h3.article__h3{
  word-break: break-word;
}

.contact-form .grid__item, .contact-form .input-group  {
  position: relative;
}

.contact-form label, .cart-page-form label, .cart-drawer-form label {
  position: absolute;
  pointer-events: none;
  left: 22px;
  left: calc(var(--buttonOutlineWeight) / 2 + 20px);
  top: 0.5px;
  height: var(--buttonSize);
  align-items: center;
  justify-content: center;
  display: inline-flex!important;
  transition: 0.1s ease all;
  font-size: var(--fontBaseSize);
  color: rgba(var(--color-foreground), 0.5)!important
}

.floating-label label {
  left: 24px;
  top: 2px;
}

.contact-selector label {
  position: absolute;
  pointer-events: none;
 /*left: 22px;*/
  top: -8px;
  height: var(--buttonSize);
  align-items: center;
  justify-content: center;
  display: inline-flex;
  transition: .1s ease all;
  font-family: var(--fontBasePrimary),var(--fontBaseFallback);
  text-transform: inherit;
  font-size: var(--fontSmall);
  color: var(--colorBorderInput);
}

.contact-selector select {
  padding: 15px 36px 0px 9px;
  background-position: calc(100% - 0.75rem) center!important;
  -moz-appearance: none!important;
  -webkit-appearance: none!important;
  appearance: none!important;

  padding: 15px 36px 0px 10px;
  
  padding-right: 3rem!important;

  
}

[data-bottom-border=true] .contact-selector select {
  border: none!important;
  border-bottom: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
  border-radius: 0!important;
      padding-left: calc(var(--buttonOutlineWeight) / 2 + 10px);
}

[data-bottom-border=true] .contact-selector select:focus {
  border-radius: calc(var(--buttonRadius) * .75)!important;
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
  padding-top: calc(var(--buttonOutlineWeight) / -2 + 15px);
  padding-left: 10px
}

.contact-form .medium-up--one-whole label {
  width: 100%;
  left:0;
}

.contact-form .footer__newsletter__input-group label {
  color: rgba(var(--color-foreground), 0.5);
}

.newsletter__input-group label, .discount__input-group label{
  margin-left: -10px;
  margin-top: 0px;
  padding-top: 0px;
}

.footer__newsletter__input-group {
  background: transparent;
}

.contact-form input:not(#SearchInModal):not(#SearchPageInput):not(:placeholder-shown) {
  padding-top: 15px;
  padding-bottom: 0px;
}

[data-bottom-border=true] .contact-form input:not(#SearchInModal):not(#SearchPageInput):not(:placeholder-shown):not(:focus) {
  padding-top: calc(var(--buttonOutlineWeight) / 2 + 15px);
}



.contact-form input:not(#SearchInModal):not(#SearchPageInput):not(:placeholder-shown) ~ label:not([for="SearchInModal"]), .contact-form textarea:not(:placeholder-shown) ~ label, .contact-form input:not(#SearchInModal):not(#SearchPageInput):not(:placeholder-shown):-webkit-autofill ~ label:not([for="SearchInModal"]), .contact-form textarea:not(:placeholder-shown):-webkit-autofill ~ label {
  top:-8px;
  left:21px;
  font-size: var(--fontSmall)!important;
  top: calc(var(--buttonSize) * -0.2);
  left: calc(var(--buttonOutlineWeight) / 2 + 20px);
}

.contact-form textarea:not(:placeholder-shown) ~ label {
  left:0px;
}

input:not([value=""]).input-full ~ label {
  top: -8px!important;
  left: 21px!important;
  font-size: var(--fontSmall)!important;
  padding-left: 0!important
}

.grid__item--textarea{
  border: calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border), 1);
  border-radius: calc(var(--buttonRadius) * 0.75);
  width:100%;
  height:125px;
  overflow: hidden;
  position: relative;
  margin-bottom: 15px;
}

.cart-drawer-form .grid__item--textarea, .cart-page-form .grid__item--textarea {
  height: 110px
}

.product-form__controls-group .contact-form .subscribe-form input:not(:placeholder-shown) ~ label {
  left:0px!important;
}

.contact-form textarea:not(:placeholder-shown) ~ label, .cart-page-form textarea:not(:placeholder-shown) ~ label, .cart-drawer-form textarea:not(:placeholder-shown) ~ label {
  top: calc(var(--buttonOutlineWeight) / 2 - 2px);
  left: calc(var(--buttonOutlineWeight) / 2 + -6px);
  width: calc(100% - 15px);
  padding-left: 10px;
  z-index: 0;
  font-size: var(--fontSmall)!important;
  justify-content:left;
  height:30px;
}

.contact-form textarea:not(:placeholder-shown) ~ .floating-label--wrapper, .cart-page-form textarea:not(:placeholder-shown) ~ .floating-label--wrapper, .cart-drawer-form textarea:not(:placeholder-shown) ~ .floating-label--wrapper  {
  height: 20px
}

.contact-form input:not(#SearchInModal):not(#SearchPageInput):not(.error):focus {
  border:calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-foreground), 1)!important
}

.grid__item--textarea:focus-within {
  border:calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-foreground), 1)!important;
}

[data-bottom-border=true] .contact-form input:not(#SearchInModal):not(#SearchPageInput):not(.error):focus,
[data-bottom-border=true] .grid__item--textarea:focus-within,
[data-bottom-border=true] .contact-form .js-qty__wrapper:has(.js-qty__adjust:hover) .js-qty__num {
  border:calc(var(--buttonOutlineWeight) / 2) solid rgba(var(--color-border),1)!important;
  border-radius: calc(var(--buttonRadius) * .75)!important;
      padding-left: calc(var(--buttonOutlineWeight) / -2 + 11px);
  padding-left: 10px
}

[data-bottom-border=true] .grid__item--textarea:focus-within {
  padding-left: 0;
}

.contact-form textarea:not(:placeholder-shown), .cart-page-form textarea:not(:placeholder-shown), .cart-drawer-form textarea:not(:placeholder-shown) {
  padding-top: 0px!important;
  bottom: 0px!important;
  position: absolute;
  margin: 0;
  height: calc(100% - 30px)!important;
  height: 100%!important;
  border-radius: 0;
  border-top: 22px solid rgb(var(--color-background))!important;
}

/*[data-bottom-border=true] .contact-form textarea:not(:placeholder-shown):focus,
[data-bottom-border=true] .cart-page-form textarea:not(:placeholder-shown):focus,
[data-bottom-border=true] .cart-drawer-form textarea:not(:placeholder-shown):focus {
  border-top: calc(var(--buttonOutlineWeight) / -2 + 22px) solid rgb(var(--color-background))!important;
}*/

.grid__item--textarea label{
  left: calc(var(--buttonOutlineWeight) / 2 + 5px);
  top:  -5px;
      left: 10px;

      
      
}

[data-bottom-border=true] .grid__item--textarea label{
  top: calc(var(--buttonOutlineWeight) / -2 - 5px);
  /*left: calc(var(--buttonOutlineWeight) / 2 + 9px);*/
}

/*[data-bottom-border=true] .grid__item--textarea:has(textarea:not(:focus)) label{
  left: calc(var(--buttonOutlineWeight) / 2 + 8.5px);
  top: calc(var(--buttonOutlineWeight) / 2 - 2.5px);
  -webkit-transition: none !important;
  -moz-transition: none !important;
  -o-transition: none !important;
  transition: none !important;
  transition: 0s ease all!important;
}*/

/*[data-bottom-border=true] .grid__item--textarea:has(textarea:not(:focus)) label:not(:focus){
  left: calc(var(--buttonOutlineWeight) + 5px);
}
*/
/*[data-bottom-border=true] .grid__item--textarea:has(textarea:focus) label {
  left: calc(var(--buttonOutlineWeight)/-2 + 5px);
}*/

.contact-form textarea:not(:placeholder-shown) ~ label {
  top: 0;
  left: 0
}

[data-bottom-border=true] .contact-form textarea:not(:placeholder-shown) ~ label {
  top: calc(var(--buttonOutlineWeight) / 2 - 5px);
  left: calc(var(--buttonOutlineWeight) / 2 + -5px);
  left: 0
}

/*[data-bottom-border=true] .contact-form textarea:not(:placeholder-shown) ~ label {
  top: calc(var(--buttonOutlineWeight) / 2 - 5px);
  left: calc(var(--buttonOutlineWeight) + -6px);
transition: 0s ease all!important;
}*/

input#SearchInModal:placeholder-shown + label[for="SearchInModal"], input#SearchPageInput:placeholder-shown + label[for="SearchPageInput"] {
  left: 41px;
  top: 0px;
  height: 100%;
  display: flex;
  align-items: center;
  font-size: 24px;
  font-size: var(--fontBaseSize);
}

label[for="SearchInModal"], label[for="SearchPageInput"] {
  position: absolute;
  pointer-events: none;
  font-family: var(--fontBasePrimary),var(--fontBaseFallback);
  text-transform: none;
  letter-spacing: var(--fontBaseSpacing);
  transition: 0.2s ease all;
  font-size: 17px;
  font-weight: normal;
  left: 41px;
  top: -30px;
  height: 100%;
  display: flex;
  align-items: center;
  font-size: var(--fontBaseSize)
}

input#SearchInModal:not(:placeholder-shown), input#SearchPageInput:not(:placeholder-shown) {
  transform: translateY(10px);
  border-color: transparent!important
}

label[for="SearchInModal"], label[for="SearchPageInput"] {
  top: -15px;
  opacity: 0.5
}


@media only screen and (max-width:768px){
  input#SearchInModal:not(:placeholder-shown), input#SearchPageInput:not(:placeholder-shown) {
    transform: translateY(10px);
  }

  label[for="SearchInModal"], label[for="SearchPageInput"] {
    top: -10px;
  }
}

@media only screen and (max-width:589px){
  input#SearchInModal:placeholder-shown + label[for="SearchInModal"], input#SearchPageInput:placeholder-shown + label[for="SearchPageInput"] {
    left: 10px;
  }
  label[for="SearchInModal"], label[for="SearchPageInput"] {
    left: 10px;
  }
}

.header-item--search {
  width: 100%;
  height: 44px;
  position: relative;
}

.header-item--search .search__page-search-bar {
  /*left: -30px;*/
  left: -20px;
  width: 100%
}

.header-item--search :is(.site-header__search-input, input#SearchInModal:placeholder-shown+label[for=SearchInModal]) {
  font-size: var(--fontMenuSize)!important;
}
.header-item--search.small--show :is(input#SearchInModal:placeholder-shown+label[for=SearchInModal], label[for=SearchInModal]) {
  left: 40px
}

.header-item--search.small--show .predictive-search-results {
  padding: 0;
  padding-top: 20px;
  padding-bottom: 100px
}

.header-item--search label[for=SearchInModal] {
  font-size: var(--fontSmall)!important;
  margin-left: -30px;
  top: -9px
}
@media only screen and (min-width:590px){
  .header-item--search label[for=SearchPageInput] {
    font-size: var(--fontSmall)!important;
    margin-left: -30px;
    top: -9px
  }
}

.header-item--search input#SearchInModal:not(:placeholder-shown) {
  transform: translateY(5px);
  padding-top: 22px;
}

.header-item--search .predictive-close-results  {
  margin: 0!important
}

.header-item--search .site-header__close-btn {
  display: none
}

.header-item--search .is-active .site-header__close-btn  {
  display: inline-block
}

.header-item--search .site-header__search-btn {
  display: inline-block
}

.header-item--search .is-active .site-header__search-btn  {
  display: none
}

/*==================== #Dark mode ====================*/
.is-light .header-inverse-overlay .site-nav__link, .is-light .header-inverse-overlay .site-header__logo-link,
.is-light .site-toolbar-inverse-overlay a, .is-light .site-toolbar-inverse-overlay select {
  color: var(--colorInverse)!important
}

.is-light .site-toolbar-inverse-overlay .icon path {
  stroke: var(--colorInverse)!important
}

.is-light .site-toolbar-inverse-overlay .icon-selector path {
  fill: var(--colorInverse)!important
}
 
/*.header-inverse .site-nav--has-dropdown:hover>a, .is-light .header-inverse-overlay .site-nav--has-dropdown:hover>a {
  color:var(--colorTextBody)!important;
}*/

.color-inverse input,.color-inverse select,.color-inverse textarea {
  border: calc(var(--buttonOutlineWeight) / 2) solid var(--colorBorderInverse);
    color:var(--colorInverse)!important
}

.is-light .header-inverse-overlay .field__caret {
  color: var(--colorInverse)
}

.is-light .header-inverse-overlay:has(.site-nav--has-dropdown:hover) .site-nav__link, .is-light .header-inverse-overlay:has(.site-nav--has-dropdown:hover) .site-header__logo-link {
  color: rgba(var(--color-foreground), 1)!important;
}

.shopify-cleanslate li div, .shopify-payment-button__button--branded,.shopify-payment-button__button--branded .shopify-cleanslate{
  height: var(--buttonSize)!important;
  border-radius:var(--buttonRadius)!important;
  overflow: auto;
}

.shopify-payment-button__button--branded .shopify-cleanslate > div:focus {
  border: 2px solid #275DC5!important;
  border-radius: var(--buttonRadius)!important;
}

.shopify-payment-button__button--branded span {
  font-size: var(--buttonTextSize);
}
  
.loading{
  width: 100%;
  height: 100%;
  animation:placeholder-shimmer 1.3s linear 0.5s infinite;
  background-size:300% 100%!important;
  background: #00000008;
  background-image: linear-gradient(122deg,#ffffff00 40%,#ffffffeb 63%,#ffffff00 79%)!important;
}

[data-theme="dark"] .loading{
  background-image: linear-gradient(122deg,#ffffff00 40%,#ffffff0d 63%,#ffffff00 79%)!important;
}

.loading.loaded{
  animation:none;
  background-image:none;
}

.loading--delayed:before{
  animation-delay:0.8s !important;
  animation-duration:1s !important;
}

.loading--delayed:after{
  animation-delay:1.3s !important;
}

.main-swacthes-container {
  overflow: hidden;
  display: flex;
  width: calc(100% - 32px);
  margin: auto;
  margin: 0;
}

.scroll-content {
  display: flex;
  gap: var(--ColorSwatchSlider);
  padding-left: var(--ColorSwatchSlider);
  padding-right: var(--ColorSwatchSlider);
  padding: 0
}

.slider__padding-left {
  padding-left: var(--ColorSwatchSlider);
}

.slider__padding-right {
  padding-right: var(--ColorSwatchSlider);
}

.btn-arrows {
  width: 10%;
  width: 32px;
  opacity: 0.6
}

.btn--arrows .btn-icon {
  width: 10px;
  height: 15px;
  margin: 0;
  stroke: var(--colorTextBody)!important;
}

div#main-parent-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.btn--arrows {
  border: 1.5px solid var(--colorBorder);
  width: 29px;
  height: 29px;
  border-radius: 100%;
  line-height: 0;
  font-size: 0;
  vertical-align: top;
  margin-top: -3px;
  margin: 1.5px;
}

.div-width-calculation {
  display: block;
  width: 100%;
}

.color-swatch--small {
  margin: 5px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.color-swatch--empty {
  width: 32px;
}
{"name":"Header","type":"header","sections":{"announcement-bar":{"type":"announcement-bar","blocks":{"announcement_7kJMKN":{"type":"announcement","settings":{"text":"WELCOME TO PIMP CITY™ CLOTHING LLC","link_text":"It's the way we wear our clothes","link":"shopify:\/\/collections"}}},"block_order":["announcement_7kJMKN"],"settings":{"enable_sticky_bar":false,"announcement_compact":true,"color_scheme":"accent-1"}},"header":{"type":"header","blocks":{"toolbar_HagkQM":{"type":"toolbar","settings":{"show_social_icons":true,"show_language_selector":true,"show_currency_country_selector":"currency","align_text":"right","color_scheme":"accent-1","greyscale_icons":true}},"b879239a-aa52-4343-8e09-44b6a6d7300a":{"type":"search","settings":{"display_style":"field"}},"logo_YbFn6H":{"type":"logo","settings":{"logo":"shopify:\/\/shop_images\/photostudio_1704526762464-1.jpg","logo_white":"shopify:\/\/shop_images\/photostudio_1706838105937.jpg","desktop_logo_width":400,"mobile_logo_width":200}}},"block_order":["toolbar_HagkQM","b879239a-aa52-4343-8e09-44b6a6d7300a","logo_YbFn6H"],"settings":{"main_menu_alignment":"center-split","enable_sticky_bar":false,"main_menu_link_list":"main-menu","mega_menu_images":true,"mobile_menu_images":true,"mega_menu_column":4,"boxed_content":true,"overlay_home":false,"overlay_collection":false,"overlay_product":false,"overlay_pages":false,"overlay_article":false,"overlay_all":false,"color_scheme":"accent-1","color_scheme_drawer":"accent-1","inverse_text_color":true,"inverse_text_color_scroll":true}},"custom_liquid_pmyDxR":{"type":"custom-liquid","settings":{"custom_liquid":"","color_scheme":"","section_id":""}}},"order":["announcement-bar","header","custom_liquid_pmyDxR"]}

{"sections":{},"order":[]}

{"sections":{},"order":[]}
<script type="module" src="{{ 'popup.js' | asset_url }}" defer="defer"></script>
{{ 'footer.css' | asset_url | stylesheet_tag }}

<footer class="site-footer color-{{section.settings.color_scheme}} gradient" data-section-id="{{ section.id }}" data-section-type="footer-section">
  <div class="page-width" id="{{ section.settings.section_id }}">
    <div class="grid">
      {%- assign row_width = 0 -%}
      {%- for block in section.blocks -%}
        {% assign row_width = row_width | plus: block.settings.container_width %}
        {% assign grid_item_width = 'medium-up--' | append: block.settings.size_width %}
        <style>

  
          .footer__logo a {
            max-width: {{ block.settings.mobile_logo_width }}px;
          }
          
          .is-light .footer__logo .logo--inverted {
            max-width: {{ block.settings.mobile_logo_width }}px;
          }
          
          @media only screen and (min-width: 769px) {
            .footer__logo a {
              max-width: {{ block.settings.desktop_logo_width }}px;
            }
        
            .is-light .footer__logo .logo--inverted {
              max-width: {{ block.settings.desktop_logo_width }}px;
            }
          }
        
          .footer__logo--width {
            width: {{block.settings.desktop_logo_width}}px
          }
        
          @media only screen and (max-width: 589px) {
            .footer__logo--width {
              width: {{block.settings.mobile_logo_width}}px
            }
          }
        </style>
  
        {%- case block.type -%}
          {%- when 'logo_social' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            <div class="footer__logo-social {% if block.settings.greyscale_icons %}payment-icons--greyscale{% endif %}">
              <div class="h2 footer__logo site-header__logo">
              {%- render 'header-logo-block', section: block, style: 'width: 100%; object-fit: cover; height: 100%;' -%}
              </div>
            </div>
          </div>
  
          {%- when 'social' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {% if block.settings.greyscale_icons %}payment-icons--greyscale{% endif %} {{ grid_item_width }} text-{{ block.settings.align_text }}">
            {% liquid 
              assign enable_follow_on_shop = false
              if shop.features.follow_on_shop? and block.settings.enable_follow_on_shop
                assign enable_follow_on_shop = true
              endif
            %}
            {% if enable_follow_on_shop or block.settings.show_social_icons %}
              <ul class="no-bullets footer__social">
                {% render 'social-links', block: block %}
              </ul>   
            {%endif%}
          </div>
          
          {%- when 'custom' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-custom-text', block: block -%}
          </div>
          {%- when 'newsletter' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid-newsletter grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-newsletter', block: block -%}
          </div>
          {%- when 'menu' -%}
          <div {{ block Pimp-City _attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-menu', block: block -%}
          </div>
          {%- when 'currency' -%}
          <script type="module" src="{{ 'country-flags.js' | asset_url }}" defer="defer"></script>
          <div {{ block.shopify_attributes }} class="grid__item grid-locator grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-currency', block: block -%}
          </div>
          {%- when 'payment_icons' -%}
          <script type="module" src="{{ 'country-flags.js' | asset_url }}" defer="defer"></script>
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- unless shop.enabled_payment_types == empty -%}
            {% if block.settings.title != blank %}
            <p class="footer__title small--hide text-{{ block.settings.align_text }}">{{ block.settings.title }}</p>
            <span class="footer__title collapsible-trigger-btn medium-up--hide">
              {{ block.settings.title }}
            </span>
            {% endif %}
            <ul class="inline-list payment-icons site-footer__bottom-block{% if block.settings.greyscale_payment_icons  %} payment-icons--greyscale{% endif %} text-{{ block.settings.align_text }}" style="{% if block.settings.title == blank %}padding-top:23px{% endif %}">
              {% if settings.cart_payment_cod_text %}
              <li class="payment-icon {%if block.settings.enlarge_icons%}large-icon{%endif%}">
                <div class="payment-icon--cod">
                  <span>{{ 'cart.general.cash' | t }}</span>
                </div>
              </li>
              {% endif %}
              {%- for type in shop.enabled_payment_types -%}
              <li class="payment-icon {%if block.settings.enlarge_icons%}large-icon{%endif%}">
                {{ type | payment_type_svg_tag }}
              </li>
              {%- endfor -%}
            </ul>
            <script>
              setTimeout(function(){
                var footerPaymentContainer = document.querySelector('.site-footer__bottom-block')    
                footerPaymentContainer.querySelectorAll('[id^="pi-"]').forEach(el => {
                  el.id = el.id + '-ftr'
                })
              },1); 
            </script>
            {%- endunless -%}
          </div>
        {%- endcase -%}
      {%- endfor -%}
    </div>

    <div class="site-footer__bottom" id="FooterPaymentIcons">
      <div class="footer__copyright site-footer__bottom-block">
        {%- if section.settings.show_copyright -%}
        <span>
          &copy; {{ 'now' | date: '%Y' }} {{ shop.name }}
          {%- if section.settings.copyright_text != blank -%}
          {{ section.settings.copyright_text }}
          {%- endif -%}
        </span>
        {%- endif -%}
        {%- if section.settings.copyright_text == blank and section.settings.show_copyright-%}
        {{powered_by_link}}
        {%endif%}
      </div>
    </div>

  </div>
</footer>

{% schema %}
  {
    "name": "Footer",
    "max_blocks": 12,
    "settings": [
      {
        "type": "header",
        "content": "Additional footer content"
      },
      {
        "type": "checkbox",
        "id": "show_copyright",
        "label": "Show copyright"
      },
      {
        "type": "text",
        "id": "copyright_text",
        "label": "Additional copyright text"
      },
	  {
        "type": "header",
        "content": "Layout"
      },
      {
        "type": "color_scheme",
        "id": "color_scheme",
        "label": "Color scheme",
        "default": "background-1"
      },
	  {
        "type": "header",
        "content": "Navigation"
      },
	  {
        "type": "text",
        "id": "section_id",
        "label": "Section ID"
      }
    ],
    "blocks": [
      {
        "type": "logo_social",
        "name": "Logo",
        "limit": 1,
        "settings": [
          {
            "type": "image_picker",
            "id": "logo",
            "label": "Logo"
          },
          {
            "type": "image_picker",
            "id": "logo_white",
            "label": "White logo"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "range",
            "id": "desktop_logo_width",
            "label": "Desktop logo width",
            "default": 200,
            "min": 40,
            "max": 400,
            "step": 10,
            "unit": "px"
          },
          {
            "type": "range",
            "id": "mobile_logo_width",
            "label": "Mobile logo width",
            "default": 90,
            "min": 40,
            "max": 200,
            "step": 10,
            "unit": "px",
            "info": "Set as a max-width, may appear smaller"
          },  
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }         
        ]
      },
      {
        "type": "social",
        "name": "Social",
        "limit": 1,
        "settings": [
          {
            "type": "text",
            "id": "title",
            "label": "Title",
            "default": "Follow our journey",
            "info": "To display your social media accounts, link them in your [theme settings](\/editor?context=theme&category=social%20media)."
          },
		  {
            "type": "checkbox",
            "id": "show_social_icons",
            "label": "Show social icons",
			"default": true
          },
          {
            "type": "header",
            "content": "Follow on shop",
            "info": "To allow customers to follow your store on the Shop app from your storefront, Shop Pay must be enabled. [Learn more](https:\/\/help.shopify.com\/manual\/online-store\/themes\/customizing-themes\/follow-on-shop)"
          },
          {
            "type": "checkbox",
            "id": "enable_follow_on_shop",
            "default": true,
            "label": "Enable Follow on Shop",
            "info": "You have to add Shop app to your store to make this visible. [Learn more](https://apps.shopify.com/shop)"
          },
		  {
            "type": "header",
            "content": "Layout"
          },   
          {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "left"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
          {
            "type": "checkbox",
            "id": "greyscale_icons",
            "label": "Greyscale social icons"
          }
        ]
      },
      {
        "type": "menu",
        "name": "Menu",
        "settings": [
          {
            "type": "checkbox",
            "id": "show_footer_title",
            "label": "Show title",
            "default": true
          },
		  {
            "type": "text",
            "id": "title",
            "label": "Heading"
          },
          {
            "type": "link_list",
            "id": "menu",
            "label": "Choose a menu",
            "default": "footer",
            "info": "This menu won't show dropdown items"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      },
      {
        "type": "newsletter",
        "name": "Newsletter",
        "limit": 1,
        "settings": [
          {
            "type": "checkbox",
            "id": "show_footer_title",
            "label": "Show title",
            "default": true
          },
          {
            "type": "paragraph",
            "content": "Any customers who sign up will have an account created for them in Shopify. [View customers](/admin/customers)."
          },
          {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Sign up and save"
          },
          {
            "type": "richtext",
            "id": "richtext",
            "label": "Text",
            "info": "Optional",
            "default": "<p>Offer special discounts or once-in-a-lifetime deals.</p>"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      },
	  {
        "type": "currency",
        "name": "Language and currency",
        "limit": 2,
        "settings": [
		  {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Language and currency"
          },
          {
            "type": "richtext",
            "id": "richtext",
            "label": "Text"
          },
		  {
            "type": "header",
            "content": "Language selector",
            "info": "To add a language, go to your [language settings.](/admin/settings/languages)"
          },
          {
            "type": "select",
            "id": "display_style",
            "label": "Display style",
            "default": "dropdown",
            "options": [
			  {
                "value": "none",
                "label": "None"
              },
              {
                "value": "dropdown",
                "label": "Dropdown"
              },
              {
                "value": "buttons",
                "label": "Flags"
              }
            ]
          },
		  {
        	"type": "text",
        	"id": "flag_name",
        	"label": "Button label",
			"info": "Add custom region, like EU. You can upload its flag to Files like eu.svg"
      	  },
		  {
        	"type": "text",
        	"id": "flag_link",
        	"label": "Button link",
			"info": "Region URL like 'https://example.eu'"
      	  },
		  {
            "type": "header",
            "content": "Country/currency",
            "info": "To add a currency, go to your [currency settings.](/admin/settings/payments). Select 'Primary domain only' to get the best experience in Markets > Domains and languages"
          },
          {
            "type": "select",
            "id": "show_currency_country_selector",
            "label": "Display style",
            "default": "both",
            "options": [
			  {
                "value": "none",
                "label": "None"
              },
              {
                "value": "country",
                "label": "Country dropdown"
              },
              {
                "value": "currency",
                "label": "Currency dropdown"
              },
              {
                "value": "both",
                "label": "Country and currency dropdown"
              },
			  {
                "value": "buttons",
                "label": "Flags"
              }
            ]
          },
          {
            "type": "header",
            "content": "layout"
          },
		  {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "center"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
		  {
            "type": "checkbox",
            "id": "greyscale_flag_icons",
            "label": "Greyscale flag icons"
          }
        ]
      },
      {
        "type": "payment_icons",
        "name": "Payment options",
        "limit": 1,
        "settings": [
		  {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "We accept"
          },		  
          {
            "type": "header",
            "content": "layout"
          },
		  {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "center"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
		  {
            "type": "checkbox",
            "id": "greyscale_payment_icons",
            "label": "Greyscale payment icons"
          },
          {
            "type": "checkbox",
            "id": "enlarge_icons",
            "label": "Enlarge icons",
            "default": false
		  }
        ]
      },
      {
        "type": "custom",
        "name": "Rich text",
        "settings": [
          {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Rich text"
          },
          {
            "type": "richtext",
            "id": "text",
            "label": "Text",
            "default": "<p>Add your own custom text here.</p>"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      }
    ],
    "default": {
      "settings": {},
      "blocks": [
        {
          "type": "menu",
          "settings": {}
        },
        {
          "type": "logo_social",
          "settings": {}
        }
      ]
    }
  }
{% endschema %}

{"sections":{"main":{"type":"page","settings":{}}},"order":["main"]}


<script type="module" src="{{ 'popup.js' | asset_url }}" defer="defer"></script>
{{ 'footer.css' | asset_url | stylesheet_tag }}

<footer class="site-footer color-{{section.settings.color_scheme}} gradient" data-section-id="{{ section.id }}" data-section-type="footer-section">
  <div class="page-width" id="{{ section.settings.section_id }}">
    <div class="grid">
      {%- assign row_width = 0 -%}
      {%- for block in section.blocks -%}
        {% assign row_width = row_width | plus: block.settings.container_width %}
        {% assign grid_item_width = 'medium-up--' | append: block.settings.size_width %}
        <style>

  
          .footer__logo a {
            max-width: {{ block.settings.mobile_logo_width }}px;
          }
          
          .is-light .footer__logo .logo--inverted {
            max-width: {{ block.settings.mobile_logo_width }}px;
          }
          
          @media only screen and (min-width: 769px) {
            .footer__logo a {
              max-width: {{ block.settings.desktop_logo_width }}px;
            }
        
            .is-light .footer__logo .logo--inverted {
              max-width: {{ block.settings.desktop_logo_width }}px;
            }
          }
        
          .footer__logo--width {
            width: {{block.settings.desktop_logo_width}}px
          }
        
          @media only screen and (max-width: 589px) {
            .footer__logo--width {
              width: {{block.settings.mobile_logo_width}}px
            }
          }
        </style>
  
        {%- case block.type -%}
          {%- when 'logo_social' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            <div class="footer__logo-social {% if block.settings.greyscale_icons %}payment-icons--greyscale{% endif %}">
              <div class="h2 footer__logo site-header__logo">
              {%- render 'header-logo-block', section: block, style: 'width: 100%; object-fit: cover; height: 100%;' -%}
              </div>
            </div>
          </div>
  
          {%- when 'social' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {% if block.settings.greyscale_icons %}payment-icons--greyscale{% endif %} {{ grid_item_width }} text-{{ block.settings.align_text }}">
            {% liquid 
              assign enable_follow_on_shop = false
              if shop.features.follow_on_shop? and block.settings.enable_follow_on_shop
                assign enable_follow_on_shop = true
              endif
            %}
            {% if enable_follow_on_shop or block.settings.show_social_icons %}
              <ul class="no-bullets footer__social">
                {% render 'social-links', block: block %}
              </ul>   
            {%endif%}
          </div>
          
          {%- when 'custom' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-custom-text', block: block -%}
          </div>
          {%- when 'newsletter' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid-newsletter grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-newsletter', block: block -%}
          </div>
          {%- when 'menu' -%}
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-menu', block: block -%}
          </div>
          {%- when 'currency' -%}
          <script type="module" src="{{ 'country-flags.js' | asset_url }}" defer="defer"></script>
          <div {{ block.shopify_attributes }} class="grid__item grid-locator grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- render 'footer-currency', block: block -%}
          </div>
          {%- when 'payment_icons' -%}
          <script type="module" src="{{ 'country-flags.js' | asset_url }}" defer="defer"></script>
          <div {{ block.shopify_attributes }} class="grid__item grid__item--{{ section.id }} {{ grid_item_width }}">
            {%- unless shop.enabled_payment_types == empty -%}
            {% if block.settings.title != blank %}
            <p class="footer__title small--hide text-{{ block.settings.align_text }}">{{ block.settings.title }}</p>
            <span class="footer__title collapsible-trigger-btn medium-up--hide">
              {{ block.settings.title }}
            </span>
            {% endif %}
            <ul class="inline-list payment-icons site-footer__bottom-block{% if block.settings.greyscale_payment_icons  %} payment-icons--greyscale{% endif %} text-{{ block.settings.align_text }}" style="{% if block.settings.title == blank %}padding-top:23px{% endif %}">
              {% if settings.cart_payment_cod_text %}
              <li class="payment-icon {%if block.settings.enlarge_icons%}large-icon{%endif%}">
                <div class="payment-icon--cod">
                  <span>{{ 'cart.general.cash' | t }}</span>
                </div>
              </li>
              {% endif %}
              {%- for type in shop.enabled_payment_types -%}
              <li class="payment-icon {%if block.settings.enlarge_icons%}large-icon{%endif%}">
                {{ type | payment_type_svg_tag }}
              </li>
              {%- endfor -%}
            </ul>
            <script>
              setTimeout(function(){
                var footerPaymentContainer = document.querySelector('.site-footer__bottom-block')    
                footerPaymentContainer.querySelectorAll('[id^="pi-"]').forEach(el => {
                  el.id = el.id + '-ftr'
                })
              },1); 
            </script>
            {%- endunless -%}
          </div>
        {%- endcase -%}
      {%- endfor -%}
    </div>

    <div class="site-footer__bottom" id="FooterPaymentIcons">
      <div class="footer__copyright site-footer__bottom-block">
        {%- if section.settings.show_copyright -%}
        <span>
          &copy; {{ 'now' | date: '%Y' }} {{ shop.name }}
          {%- if section.settings.copyright_text != blank -%}
          {{ section.settings.copyright_text }}
          {%- endif -%}
        </span>
        {%- endif -%}
        {%- if section.settings.copyright_text == blank and section.settings.show_copyright-%}
        {{powered_by_link}}
        {%endif%}
      </div>
    </div>

  </div>
</footer>

{% schema %}
  {
    "name": "Footer",
    "max_blocks": 12,
    "settings": [
      {
        "type": "header",
        "content": "Additional footer content"
      },
      {
        "type": "checkbox",
        "id": "show_copyright",
        "label": "Show copyright"
      },
      {
        "type": "text",
        "id": "copyright_text",
        "label": "Additional copyright text"
      },
	  {
        "type": "header",
        "content": "Layout"
      },
      {
        "type": "color_scheme",
        "id": "color_scheme",
        "label": "Color scheme",
        "default": "background-1"
      },
	  {
        "type": "header",
        "content": "Navigation"
      },
	  {
        "type": "text",
        "id": "section_id",
        "label": "Section ID"
      }
    ],
    "blocks": [
      {
        "type": "logo_social",
        "name": "Logo",
        "limit": 1,
        "settings": [
          {
            "type": "image_picker",
            "id": "logo",
            "label": "Logo"
          },
          {
            "type": "image_picker",
            "id": "logo_white",
            "label": "White logo"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "range",
            "id": "desktop_logo_width",
            "label": "Desktop logo width",
            "default": 200,
            "min": 40,
            "max": 400,
            "step": 10,
            "unit": "px"
          },
          {
            "type": "range",
            "id": "mobile_logo_width",
            "label": "Mobile logo width",
            "default": 90,
            "min": 40,
            "max": 200,
            "step": 10,
            "unit": "px",
            "info": "Set as a max-width, may appear smaller"
          },  
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }         
        ]
      },
      {
        "type": "social",
        "name": "Social",
        "limit": 1,
        "settings": [
          {
            "type": "text",
            "id": "title",
            "label": "Title",
            "default": "Follow our journey",
            "info": "To display your social media accounts, link them in your [theme settings](\/editor?context=theme&category=social%20media)."
          },
		  {
            "type": "checkbox",
            "id": "show_social_icons",
            "label": "Show social icons",
			"default": true
          },
          {
            "type": "header",
            "content": "Follow on shop",
            "info": "To allow customers to follow your store on the Shop app from your storefront, Shop Pay must be enabled. [Learn more](https:\/\/help.shopify.com\/manual\/online-store\/themes\/customizing-themes\/follow-on-shop)"
          },
          {
            "type": "checkbox",
            "id": "enable_follow_on_shop",
            "default": true,
            "label": "Enable Follow on Shop",
            "info": "You have to add Shop app to your store to make this visible. [Learn more](https://apps.shopify.com/shop)"
          },
		  {
            "type": "header",
            "content": "Layout"
          },   
          {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "left"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
          {
            "type": "checkbox",
            "id": "greyscale_icons",
            "label": "Greyscale social icons"
          }
        ]
      },
      {
        "type": "menu",
        "name": "Menu",
        "settings": [
          {
            "type": "checkbox",
            "id": "show_footer_title",
            "label": "Show title",
            "default": true
          },
		  {
            "type": "text",
            "id": "title",
            "label": "Heading"
          },
          {
            "type": "link_list",
            "id": "menu",
            "label": "Choose a menu",
            "default": "footer",
            "info": "This menu won't show dropdown items"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      },
      {
        "type": "newsletter",
        "name": "Newsletter",
        "limit": 1,
        "settings": [
          {
            "type": "checkbox",
            "id": "show_footer_title",
            "label": "Show title",
            "default": true
          },
          {
            "type": "paragraph",
            "content": "Any customers who sign up will have an account created for them in Shopify. [View customers](/admin/customers)."
          },
          {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Sign up and save"
          },
          {
            "type": "richtext",
            "id": "richtext",
            "label": "Text",
            "info": "Optional",
            "default": "<p>Offer special discounts or once-in-a-lifetime deals.</p>"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      },
	  {
        "type": "currency",
        "name": "Language and currency",
        "limit": 2,
        "settings": [
		  {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Language and currency"
          },
          {
            "type": "richtext",
            "id": "richtext",
            "label": "Text"
          },
		  {
            "type": "header",
            "content": "Language selector",
            "info": "To add a language, go to your [language settings.](/admin/settings/languages)"
          },
          {
            "type": "select",
            "id": "display_style",
            "label": "Display style",
            "default": "dropdown",
            "options": [
			  {
                "value": "none",
                "label": "None"
              },
              {
                "value": "dropdown",
                "label": "Dropdown"
              },
              {
                "value": "buttons",
                "label": "Flags"
              }
            ]
          },
		  {
        	"type": "text",
        	"id": "flag_name",
        	"label": "Button label",
			"info": "Add custom region, like EU. You can upload its flag to Files like eu.svg"
      	  },
		  {
        	"type": "text",
        	"id": "flag_link",
        	"label": "Button link",
			"info": "Region URL like 'https://example.eu'"
      	  },
		  {
            "type": "header",
            "content": "Country/currency",
            "info": "To add a currency, go to your [currency settings.](/admin/settings/payments). Select 'Primary domain only' to get the best experience in Markets > Domains and languages"
          },
          {
            "type": "select",
            "id": "show_currency_country_selector",
            "label": "Display style",
            "default": "both",
            "options": [
			  {
                "value": "none",
                "label": "None"
              },
              {
                "value": "country",
                "label": "Country dropdown"
              },
              {
                "value": "currency",
                "label": "Currency dropdown"
              },
              {
                "value": "both",
                "label": "Country and currency dropdown"
              },
			  {
                "value": "buttons",
                "label": "Flags"
              }
            ]
          },
          {
            "type": "header",
            "content": "layout"
          },
		  {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "center"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
		  {
            "type": "checkbox",
            "id": "greyscale_flag_icons",
            "label": "Greyscale flag icons"
          }
        ]
      },
      {
        "type": "payment_icons",
        "name": "Payment options",
        "limit": 1,
        "settings": [
		  {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "We accept"
          },		  
          {
            "type": "header",
            "content": "layout"
          },
		  {
            "type": "text_alignment",
            "id": "align_text",
            "label": "Text alignment",
            "default": "center"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          },
		  {
            "type": "checkbox",
            "id": "greyscale_payment_icons",
            "label": "Greyscale payment icons"
          },
          {
            "type": "checkbox",
            "id": "enlarge_icons",
            "label": "Enlarge icons",
            "default": false
		  }
        ]
      },
      {
        "type": "custom",
        "name": "Rich text",
        "settings": [
          {
            "type": "text",
            "id": "title",
            "label": "Heading",
            "default": "Rich text"
          },
          {
            "type": "richtext",
            "id": "text",
            "label": "Text",
            "default": "<p>Add your own custom text here.</p>"
          },
		  {
            "type": "header",
            "content": "Layout"
          },
          {
            "type": "select",
            "id": "size_width",
            "label": "Width",
            "default": "one-quarter",
            "options": [
			  {
                "label": "16%",
                "value": "one-sixth"
              },
			  {
                "label": "25%",
                "value": "one-quarter"
              },
              {
                "label": "33%",
                "value": "one-third"
              },
              {
                "label": "41%",
                "value": "five-twelfths"
              },
              {
                "label": "50%",
                "value": "one-half"
              },
			  {
                "label": "66%",
                "value": "two-thirds"
              },
			  {
                "label": "75%",
                "value": "three-quarters"
              },
			  {
                "label": "100%",
                "value": "one-whole"
              }
            ]
          }
        ]
      }
    ],
    "default": {
      "settings": {},
      "blocks": [
        {
          "type": "menu",
          "settings": {}
        },
        {
          "type": "logo_social",
          "settings": {}
        }
      ]
    }
  }
{% endschema %}
         
.footer__logo a { max-width: {{ block.settings.mobile_logo_width }}px; }
.is-light .footer__logo .logo--inverted { max-width: {{ block.settings.mobile_logo_width }}px; }
@media only screen and (min-width: 769px) {
.footer__logo a { max-width: {{ block.settings.desktop_logo_width }}px; }
.is-light .footer__logo .logo--inverted { max-width: {{ block.settings.desktop_logo_width }}px; }
}
.footer__logo--width { width: {{block.settings.desktop_logo_width}}px }
@media only screen and (max-width: 589px) {
.footer__logo--width { width: {{block.settings.mobile_logo_width}}px }
}

header {
color-scheme: accent-1;
}
header .announcement-bar {
background-color: accent-1; /* Or corresponding color /
color: white; / Or corresponding text color /
}
header .header {
background-color: accent-1; / Or corresponding color /
color: white; / Or corresponding text color /
}
header .header .toolbar {
filter: grayscale(100%); / For greyscale icons /
}
header .header .logo img {
max-width: 400px; / Desktop logo width /
}
@media (max-width: 767px) {
header .header .logo img {
max-width: 200px; / Mobile logo width */
}
}

pip install google-cloud-firestore

{

  "id": "1",

  "name": "T-Shirt",

  "description": "Cool T-shirt",

  "price": 20,

  "images": ["image1.jpg", "image2.jpg"],

  "sizes": ["S", "M", "L"],

  "colors": ["Red", "Blue"],

  "tier": "basic" // Or "premium", "exclusive"

},

{

  "id": "2",

  "name": "Dress",

  "description": "Elegant dress",

  "price": 50,

  "images": ["image3.jpg", "image4.jpg"],

  "sizes": ["S", "M", "L"],

  "colors": ["Black", "White"],

  "tier": "premium"

}

 o*B**|                                  |         oo++=BB.|                                  |         .+.==.oo|                                  |           ..+.+o|                                  |        S    .o.X|                                  |       .    o o+E|                                  |        .  o = . |                                  |         .. + o  |                                  |        ..o+     |                                  +----[SHA256]-----+

{

  "uid": "user123",

  "email": "user@example.com",

  "tier": "basic",

  "wishlist": ["1", "3"] // Product IDs

}

