package com.maxxcoffee.mobile.util;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class Dummy {

//    private Context context;
//    private StoreController storeGroupController;
//    private StoreItemController storeItemController;
//    private PromoController promoController;
//    private MenuCategoryController menuCategoryController;
//    private MenuController menuItemController;
//    private CardController cardController;
//    private FaqController faqController;
//
//    public Dummy(Context context) {
//        this.context = context;
//        this.storeGroupController = new StoreController(context);
//        this.storeItemController = new StoreItemController(context);
//        this.promoController = new PromoController(context);
//        this.menuCategoryController = new MenuCategoryController(context);
//        this.menuItemController = new MenuController(context);
//        this.cardController = new CardController(context);
//        this.faqController = new FaqController(context);
//
//        setStore();
//        setStoreItem();
//        setPromo();
//        setMenuCategory();
//        setMenuItem();
//        setCard();
//        setFaq();
//    }
//
//    private void setFaq() {
//        FaqModel faq1 = new FaqModel();
//        faq1.setId(1);
//        faq1.setTitle("How can I access my card on phone?");
//        faq1.setDescription("Lorem ipsum dolor sit amet, ne mel possit option, vis assum vidisse te. Vis altera definitiones eu. Ad deleniti intellegebat interpretaris cum, pro ut habeo reprimique, per tritani dolores temporibus et. Quis zril maiestatis quo ad, mea augue recteque te. Vix ex denique interesset.");
//
//        FaqModel faq2 = new FaqModel();
//        faq2.setId(2);
//        faq2.setTitle("How can use my rewards?");
//        faq2.setDescription("Lorem ipsum dolor sit amet, ne mel possit option, vis assum vidisse te. Vis altera definitiones eu. Ad deleniti intellegebat interpretaris cum, pro ut habeo reprimique, per tritani dolores temporibus et. Quis zril maiestatis quo ad, mea augue recteque te. Vix ex denique interesset.");
//
//        FaqModel faq3 = new FaqModel();
//        faq3.setId(3);
//        faq3.setTitle("Will reward expire someday?");
//        faq3.setDescription("Lorem ipsum dolor sit amet, ne mel possit option, vis assum vidisse te. Vis altera definitiones eu. Ad deleniti intellegebat interpretaris cum, pro ut habeo reprimique, per tritani dolores temporibus et. Quis zril maiestatis quo ad, mea augue recteque te. Vix ex denique interesset.");
//
//        FaqModel faq4 = new FaqModel();
//        faq4.setId(4);
//        faq4.setTitle("How to transfer balance from one to another card?");
//        faq4.setDescription("Lorem ipsum dolor sit amet, ne mel possit option, vis assum vidisse te. Vis altera definitiones eu. Ad deleniti intellegebat interpretaris cum, pro ut habeo reprimique, per tritani dolores temporibus et. Quis zril maiestatis quo ad, mea augue recteque te. Vix ex denique interesset.");
//
//        faqController.insert(faq1);
//        faqController.insert(faq2);
//        faqController.insert(faq3);
//        faqController.insert(faq4);
//    }
//
//    private void setCard() {
//        CardModel model1 = new CardModel();
//        model1.setId(1);
//        model1.setName("Card #1");
//        model1.setBalance(100000);
//        model1.setBeans(25);
//        model1.setPoint(120);
//        model1.setImage(R.drawable.sample_card_1);
//
//        CardModel model2 = new CardModel();
//        model2.setId(2);
//        model2.setName("Card #2");
//        model2.setBalance(50000);
//        model2.setBeans(30);
//        model2.setPoint(600);
//        model2.setImage(R.drawable.sample_card_2);
//
//        CardModel model3 = new CardModel();
//        model3.setId(3);
//        model3.setName("Card #3");
//        model3.setBalance(100000);
//        model3.setBeans(40);
//        model3.setPoint(180);
//        model3.setImage(R.drawable.sample_card_3);
//
//        cardController.insert(model1);
//        cardController.insert(model2);
//        cardController.insert(model3);
//    }
//
//    private void setMenuItem() {
//        MenuItemModel item1 = new MenuItemModel();
//        item1.setId(1);
//        item1.setCategoryId(1);
//        item1.setName("Americano");
//        item1.setDescription("Lorem Ipsum Enak BAnget");
//        item1.setPrice(50000);
//        item1.setPoint(60);
//        item1.setImage(R.drawable.bg_coffee_afternoon);
//
//        MenuItemModel item2 = new MenuItemModel();
//        item2.setId(2);
//        item2.setCategoryId(1);
//        item2.setName("Coffee Mocha");
//        item2.setDescription("Lorem Ipsum Enak BAnget");
//        item2.setPrice(50000);
//        item2.setPoint(60);
//        item2.setImage(R.drawable.bg_coffee_evening);
//
//        MenuItemModel item3 = new MenuItemModel();
//        item3.setId(3);
//        item3.setCategoryId(1);
//        item3.setName("Cappucino");
//        item3.setDescription("Lorem Ipsum Enak BAnget");
//        item3.setPrice(50000);
//        item3.setPoint(60);
//        item3.setImage(R.drawable.bg_coffee_morning);
//
//        MenuItemModel item4 = new MenuItemModel();
//        item4.setId(4);
//        item4.setCategoryId(1);
//        item4.setName("Caramel Machiato");
//        item4.setDescription("Lorem Ipsum Enak BAnget");
//        item4.setPrice(50000);
//        item4.setPoint(60);
//        item4.setImage(R.drawable.bg_coffee_afternoon);
//
//        MenuItemModel item5 = new MenuItemModel();
//        item5.setId(5);
//        item5.setCategoryId(2);
//        item5.setName("Cookies and Cream Frape");
//        item5.setDescription("Lorem Ipsum Enak BAnget");
//        item5.setPrice(50000);
//        item5.setPoint(60);
//        item5.setImage(R.drawable.bg_coffee_evening);
//
//        MenuItemModel item6 = new MenuItemModel();
//        item6.setId(6);
//        item6.setCategoryId(2);
//        item6.setName("Greentea Frape");
//        item6.setDescription("Lorem Ipsum Enak BAnget");
//        item6.setPrice(50000);
//        item6.setPoint(60);
//        item6.setImage(R.drawable.bg_coffee_morning);
//
//        menuItemController.insert(item1);
//        menuItemController.insert(item2);
//        menuItemController.insert(item3);
//        menuItemController.insert(item4);
//        menuItemController.insert(item5);
//        menuItemController.insert(item6);
//    }
//
//    private void setMenuCategory() {
//        MenuCategoryModel category1 = new MenuCategoryModel();
//        category1.setId(1);
//        category1.setName("Drink");
//
//        MenuCategoryModel category2 = new MenuCategoryModel();
//        category2.setId(2);
//        category2.setName("Food");
//
//        MenuCategoryModel category3 = new MenuCategoryModel();
//        category3.setId(3);
//        category3.setName("Merchandise");
//
//        menuCategoryController.insert(category1);
//        menuCategoryController.insert(category2);
//        menuCategoryController.insert(category3);
//    }
//
//    private void setPromo() {
//        PromoModel promo1 = new PromoModel();
//        promo1.setId(1);
//        promo1.setTitle("Promo 1");
//        promo1.setImage(R.drawable.sample_promo_1);
//        promo1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        PromoModel promo2 = new PromoModel();
//        promo2.setId(2);
//        promo2.setTitle("Promo 2");
//        promo2.setImage(R.drawable.sample_promo_2);
//        promo2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        PromoModel promo3 = new PromoModel();
//        promo3.setId(3);
//        promo3.setTitle("Promo 3");
//        promo3.setImage(R.drawable.sample_promo_3);
//        promo3.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        PromoModel promo4 = new PromoModel();
//        promo4.setId(4);
//        promo4.setTitle("Promo 4");
//        promo4.setImage(R.drawable.sample_promo_4);
//        promo4.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        PromoModel promo5 = new PromoModel();
//        promo5.setId(5);
//        promo5.setTitle("Promo 5");
//        promo5.setImage(R.drawable.sample_promo_5);
//        promo5.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        PromoModel promo6 = new PromoModel();
//        promo6.setId(6);
//        promo6.setTitle("Promo 6");
//        promo6.setImage(R.drawable.sample_promo_6);
//        promo6.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//
//        promoController.insert(promo1);
//        promoController.insert(promo2);
//        promoController.insert(promo3);
//        promoController.insert(promo4);
//        promoController.insert(promo5);
//        promoController.insert(promo6);
//    }
//
//    private void setStoreItem() {
//        StoreModel child1 = new StoreModel();
//        child1.setId(1);
//        child1.setStoreId(1);
//        child1.setName("Ambon City Center");
//        child1.setAddress("Jl. Wolter Monginsidi");
//        child1.setContact("(0911) 321654");
//        child1.setOpen("10.00 - 22.00");
//
//        StoreModel child2 = new StoreModel();
//        child2.setId(2);
//        child2.setStoreId(2);
//        child2.setName("Batik Semar CF");
//        child2.setAddress("Jl. Tomang Raya 54 Jati Pulo, Jakbar 11430");
//        child2.setContact("(021) 5636961");
//        child2.setOpen("10.00 - 22.00");
//
//        StoreModel child3 = new StoreModel();
//        child3.setId(3);
//        child3.setStoreId(2);
//        child3.setName("Berita Satu Plaza");
//        child3.setAddress("Jl. Gatot Subroto Kav 35-36, Jaksel 12950");
//        child3.setContact("(021) 5279054");
//        child3.setOpen("10.00 - 22.00");
//
//        StoreModel child4 = new StoreModel();
//        child4.setId(4);
//        child4.setStoreId(2);
//        child4.setName("Cibubur Junction");
//        child4.setAddress("Jl. Jambore 1, Cibubur, Ciracas, Jaktim 13720");
//        child4.setContact("(021) 87756481");
//        child4.setOpen("10.00 - 22.00");
//
//        StoreModel child5 = new StoreModel();
//        child5.setId(5);
//        child5.setStoreId(3);
//        child5.setName("J-Walk Plaza");
//        child5.setAddress("Jl. Babarsari");
//        child5.setContact("(021) 123456");
//        child5.setOpen("10.00 - 22.00");
//
//        StoreModel child6 = new StoreModel();
//        child6.setId(6);
//        child6.setStoreId(6);
//        child6.setName("Lippo Plaza Jogja");
//        child6.setAddress("Jl. Solo");
//        child6.setContact("(021) 654321");
//        child6.setOpen("10.00 - 22.00");
//
//        storeItemController.insert(child1);
//        storeItemController.insert(child2);
//        storeItemController.insert(child3);
//        storeItemController.insert(child4);
//        storeItemController.insert(child5);
//        storeItemController.insert(child6);
//
//    }
//
//    private void setStore() {
//
//        StoreGroupModel store1 = new StoreGroupModel();
//        store1.setId(1);
//        store1.setLocation("Ambon");
//
//        StoreGroupModel store2 = new StoreGroupModel();
//        store2.setId(2);
//        store2.setLocation("Jakarta");
//
//        StoreGroupModel store3 = new StoreGroupModel();
//        store3.setId(3);
//        store3.setLocation("Yogyakarta");
//
//        storeGroupController.insert(store1);
//        storeGroupController.insert(store2);
//        storeGroupController.insert(store3);
//    }
}
