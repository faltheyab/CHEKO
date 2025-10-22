BEGIN;
INSERT INTO restaurant.status (code, display_name, description) VALUES
  ('PENDING',    'قيد الانتظار', 'Order received and awaiting confirmation'),
  ('PREPARING',  'قيد التحضير', 'Order is being prepared'),
  ('READY',      'جاهز', 'Order is ready for pickup or delivery'),
  ('ON_THE_WAY', 'قيد التوصيل', 'Order is out for delivery'),
  ('DELIVERED',  'تم التوصيل', 'Order successfully delivered'),
  ('CANCELLED',  'ملغي', 'Order was cancelled');



-- 4 Riyadh Branches
INSERT INTO restaurant.branches
(branch_name, address, city, country, phone, email, location, opening_hours, is_main_branch)
VALUES
('فرع العليا / Al Olaya Branch', 'Olaya St, Riyadh', 'Riyadh', 'Saudi Arabia',
 '+966500000001', 'olaya@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.685 24.711)'),
 '{
   "sunday":{"open":"08:00","close":"23:00"},
   "monday":{"open":"08:00","close":"23:00"},
   "tuesday":{"open":"08:00","close":"23:00"},
   "wednesday":{"open":"08:00","close":"23:00"},
   "thursday":{"open":"08:00","close":"23:00"},
   "friday":{"open":"13:00","close":"01:00"},
   "saturday":{"open":"08:00","close":"23:00"}
 }', TRUE, TRUE),
('فرع الملقا / Al Malqa Branch','Al Malqa, King Salman Rd, Riyadh','Riyadh','Saudi Arabia',
 '+966500000002','malqa@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.625 24.774)'),
 '{
   "sunday":{"open":"08:00","close":"23:00"},
   "monday":{"open":"08:00","close":"23:00"},
   "tuesday":{"open":"08:00","close":"23:00"},
   "wednesday":{"open":"08:00","close":"23:00"},
   "thursday":{"open":"08:00","close":"23:00"},
   "friday":{"open":"13:00","close":"01:00"},
   "saturday":{"open":"08:00","close":"23:00"}
 }',FALSE, FALSE),
('فرع النخيل / Al Nakheel Branch','Al Nakheel, Imam Saud Rd, Riyadh','Riyadh','Saudi Arabia',
 '+966500000003','nakheel@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.698 24.746)'),
 '{
   "sunday":{"open":"08:00","close":"23:00"},
   "monday":{"open":"08:00","close":"23:00"},
   "tuesday":{"open":"08:00","close":"23:00"},
   "wednesday":{"open":"08:00","close":"23:00"},
   "thursday":{"open":"08:00","close":"23:00"},
   "friday":{"open":"13:00","close":"01:00"},
   "saturday":{"open":"08:00","close":"23:00"}
 }',FALSE, FALSE),
('فرع الروضة / Al Rawdah Branch','Al Rawdah, Prince Bandar Rd, Riyadh','Riyadh','Saudi Arabia',
 '+966500000004','rawdah@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.785 24.744)'),
 '{
   "sunday":{"open":"08:00","close":"23:00"},
   "monday":{"open":"08:00","close":"23:00"},
   "tuesday":{"open":"08:00","close":"23:00"},
   "wednesday":{"open":"08:00","close":"23:00"},
   "thursday":{"open":"08:00","close":"23:00"},
   "friday":{"open":"13:00","close":"01:00"},
   "saturday":{"open":"08:00","close":"23:00"}
 }',FALSE, FALSE);

-- 5 Menu Sections for main branch (id = 1)
INSERT INTO restaurant.menu_sections (branch_id,name,description) VALUES
(1,'Breakfast / الإفطار','Morning meals with coffee, eggs & pastries'),
(1,'Lunch / الغداء','Midday meals with rice, meat & salads'),
(1,'Dinner / العشاء','Evening grills and sides'),
(1,'Soups / الشوربة','Warm and comforting soups'),
(1,'Sushi / السوشي','Fresh Japanese sushi rolls');

-- 20 Breakfast Items
INSERT INTO restaurant.menu_items (section_id,name,description,price,calories,image_url) VALUES
(1,'Omelette / عجة البيض','Fluffy omelette with cheese & vegetables',18.00,250,'https://picsum.photos/seed/omelette/400/300'),
(1,'Pancakes / فطائر','Soft pancakes with maple syrup',22.00,340,'https://picsum.photos/seed/pancakes/400/300'),
(1,'French Toast / توست فرنسي','Cinnamon toast served with honey',20.00,310,'https://picsum.photos/seed/frenchtoast/400/300'),
(1,'Foul Medames / فول مدمس','Fava beans with olive oil & lemon',15.00,280,'https://picsum.photos/seed/foul/400/300'),
(1,'Cheese Manakeesh / منقوشة جبنة','Lebanese flatbread with cheese',14.00,290,'https://picsum.photos/seed/manakeesh/400/300'),
(1,'Labneh Plate / لبنة','Strained yogurt with olive oil & mint',13.00,210,'https://picsum.photos/seed/labneh/400/300'),
(1,'Falafel Plate / فلافل','Crispy falafel with tahini sauce',16.00,320,'https://picsum.photos/seed/falafel/400/300'),
(1,'Shakshuka / شكشوكة','Eggs poached in spiced tomato sauce',18.00,260,'https://picsum.photos/seed/shakshuka/400/300'),
(1,'Avocado Toast / توست الأفوكادو','Whole grain toast with avocado spread',19.00,270,'https://picsum.photos/seed/avocado/400/300'),
(1,'Halloumi Sandwich / ساندويتش حلومي','Grilled halloumi with fresh vegetables',21.00,310,'https://picsum.photos/seed/halloumi/400/300'),
(1,'Mini Pies / فطائر صغيرة','Assorted mini savory pies',17.00,290,'https://picsum.photos/seed/pies/400/300'),
(1,'Oatmeal Bowl / شوفان','Warm oat porridge with honey & banana',14.00,240,'https://picsum.photos/seed/oatmeal/400/300'),
(1,'Cheese Rolls / لفائف جبن','Pastry rolls stuffed with cheese',15.00,270,'https://picsum.photos/seed/cheeserolls/400/300'),
(1,'Egg Wrap / راب بيض','Tortilla wrap with scrambled eggs',18.00,260,'https://picsum.photos/seed/eggwrap/400/300'),
(1,'Fruit Parfait / بارفيه فواكه','Layers of yogurt, granola, fresh fruit',16.00,220,'https://picsum.photos/seed/parfait/400/300'),
(1,'Arabic Tea & Dates / شاي عربي وتمر','Traditional tea with premium dates',12.00,180,'https://picsum.photos/seed/teadates/400/300'),
(1,'Saj Zaatar / صاج زعتر','Thyme flatbread with olive oil',13.00,230,'https://picsum.photos/seed/zaatar/400/300'),
(1,'Peanut Butter Toast / توست زبدة الفول','Toasted bread with peanut butter and banana',15.00,260,'https://picsum.photos/seed/pbtoast/400/300'),
(1,'Iced Latte / لاتيه مثلج','Chilled espresso with milk and ice',16.00,150,'https://picsum.photos/seed/latte/400/300'),
(1,'Hot Chocolate / شوكولاتة ساخنة','Creamy hot chocolate drink',14.00,200,'https://picsum.photos/seed/hotchoco/400/300');


-- 20 Lunch Items
INSERT INTO restaurant.menu_items (section_id,name,description,price,calories,image_url) VALUES
(2,'Grilled Chicken Plate / دجاج مشوي','Grilled chicken with rice and salad',35.00,520,'https://picsum.photos/seed/grilledchicken/400/300'),
(2,'Beef Kabsa / كبسة لحم','Traditional Saudi rice with spiced meat',38.00,610,'https://picsum.photos/seed/kabsa/400/300'),
(2,'Chicken Biryani / برياني دجاج','Indian spiced rice with chicken',33.00,580,'https://picsum.photos/seed/biryani/400/300'),
(2,'Pasta Alfredo / باستا ألفريدو','Creamy pasta with chicken and mushroom',32.00,540,'https://picsum.photos/seed/alfredo/400/300'),
(2,'Steak Meal / ستيك','Grilled beef steak with mashed potato',55.00,700,'https://picsum.photos/seed/steak/400/300'),
(2,'Fettuccine / فيتوتشيني','White sauce pasta with shrimp',34.00,560,'https://picsum.photos/seed/fettuccine/400/300'),
(2,'Burger Classic / برجر كلاسيكي','Beef burger with cheese and fries',30.00,620,'https://picsum.photos/seed/burger/400/300'),
(2,'Grilled Salmon / سالمون مشوي','Salmon fillet served with rice',52.00,480,'https://picsum.photos/seed/salmon/400/300'),
(2,'Caesar Salad / سلطة سيزر','Fresh romaine with chicken and parmesan',24.00,320,'https://picsum.photos/seed/caesar/400/300'),
(2,'Shish Tawook / شيش طاووق','Marinated grilled chicken skewers',31.00,510,'https://picsum.photos/seed/tawook/400/300'),
(2,'Lamb Chops / ريش غنم','Grilled lamb chops with herbs',58.00,740,'https://picsum.photos/seed/lambchops/400/300'),
(2,'Mixed Grill / مشاوي مشكل','Assorted meat platter with bread',47.00,720,'https://picsum.photos/seed/mixedgrill/400/300'),
(2,'Chicken Shawarma Plate / شاورما دجاج','Served with garlic sauce and fries',26.00,430,'https://picsum.photos/seed/shawarma/400/300'),
(2,'Beef Shawarma / شاورما لحم','Classic beef shawarma wrap',28.00,450,'https://picsum.photos/seed/beefshawarma/400/300'),
(2,'Vegetable Curry / كاري خضار','Mild Indian vegetable curry with rice',27.00,390,'https://picsum.photos/seed/vegcurry/400/300'),
(2,'Club Sandwich / ساندويتش كلوب','Turkey and egg sandwich with fries',25.00,510,'https://picsum.photos/seed/clubsandwich/400/300'),
(2,'BBQ Ribs / ضلوع مشوية','Slow cooked ribs in BBQ sauce',54.00,780,'https://picsum.photos/seed/ribs/400/300'),
(2,'Greek Salad / سلطة يونانية','Feta cheese, olives & tomatoes',23.00,290,'https://picsum.photos/seed/greeksalad/400/300'),
(2,'Roast Chicken / دجاج مشوي كامل','Half roast chicken with rice',36.00,540,'https://picsum.photos/seed/roastchicken/400/300'),
(2,'Mandi Rice / رز مندي','Yemeni style rice with chicken pieces',32.00,560,'https://picsum.photos/seed/mandi/400/300');



-- 20 Dinner Items
INSERT INTO restaurant.menu_items (section_id,name,description,price,calories,image_url) VALUES
(3,'Grilled Shrimp / روبيان مشوي','Shrimp with butter sauce',48.00,460,'https://picsum.photos/seed/shrimp/400/300'),
(3,'Ribeye Steak / ستيك ريب آي','Juicy steak with herb butter',65.00,750,'https://picsum.photos/seed/ribeye/400/300'),
(3,'Chicken Alfredo Pizza / بيتزا دجاج ألفريدو','Creamy chicken pizza',33.00,590,'https://picsum.photos/seed/alfredopizza/400/300'),
(3,'Mushroom Risotto / ريزوتو الفطر','Creamy Italian rice with mushrooms',29.00,480,'https://picsum.photos/seed/risotto/400/300'),
(3,'Seafood Pasta / باستا مأكولات بحرية','Pasta with shrimp and calamari',38.00,610,'https://picsum.photos/seed/seafoodpasta/400/300'),
(3,'Margherita Pizza / بيتزا مارغريتا','Classic cheese and tomato pizza',27.00,510,'https://picsum.photos/seed/margherita/400/300'),
(3,'Fried Chicken / دجاج مقلي','Crispy fried chicken with coleslaw',31.00,640,'https://picsum.photos/seed/friedchicken/400/300'),
(3,'Chicken Quesadilla / كاساديا دجاج','Mexican tortilla with chicken & cheese',28.00,520,'https://picsum.photos/seed/quesadilla/400/300'),
(3,'Veggie Burger / برجر نباتي','Healthy vegetarian burger',25.00,420,'https://picsum.photos/seed/veggieburger/400/300'),
(3,'Salmon Teriyaki / سالمون تيرياكي','Salmon with teriyaki sauce',52.00,480,'https://picsum.photos/seed/teriyaki/400/300'),
(3,'Chicken Parmesan / دجاج بارميزان','Breaded chicken with cheese sauce',36.00,590,'https://picsum.photos/seed/parmesan/400/300'),
(3,'Fajita Beef / فيجيتا لحم','Sizzling beef fajita',39.00,560,'https://picsum.photos/seed/fajita/400/300'),
(3,'Shawarma Pizza / بيتزا شاورما','Middle Eastern fusion pizza',31.00,550,'https://picsum.photos/seed/shawarmapizza/400/300'),
(3,'Beef Lasagna / لازانيا لحم','Baked pasta with meat and cheese',33.00,610,'https://picsum.photos/seed/lasagna/400/300'),
(3,'Grilled Vegetables / خضار مشوية','Assorted grilled vegetables',21.00,250,'https://picsum.photos/seed/grilledveg/400/300'),
(3,'Shrimp Burger / برجر روبيان','Crispy shrimp burger with tartar sauce',34.00,520,'https://picsum.photos/seed/shrimpburger/400/300'),
(3,'Chicken Skillet / مقلاة دجاج','Pan-seared chicken with herbs',29.00,480,'https://picsum.photos/seed/skillet/400/300'),
(3,'Ramen Bowl / رامن','Japanese noodle soup with egg',32.00,450,'https://picsum.photos/seed/ramen/400/300'),
(3,'Baked Fish / سمك بالفرن','Oven-baked fish with lemon',37.00,430,'https://picsum.photos/seed/bakedfish/400/300'),
(3,'Kofta Platter / كفتة','Grilled beef kofta with tahini sauce',35.00,510,'https://picsum.photos/seed/kofta/400/300');


-- 10 Soups
INSERT INTO restaurant.menu_items (section_id,name,description,price,calories,image_url) VALUES
(4,'Lentil Soup / شوربة عدس','Warm lentil soup with lemon',14.00,190,'https://picsum.photos/seed/lentil/400/300'),
(4,'Tomato Soup / شوربة طماطم','Creamy tomato soup',15.00,170,'https://picsum.photos/seed/tomato/400/300'),
(4,'Chicken Corn Soup / شوربة دجاج وذرة','Thick chicken and corn soup',17.00,230,'https://picsum.photos/seed/chickencorn/400/300'),
(4,'Mushroom Soup / شوربة فطر','Creamy mushroom soup',18.00,250,'https://picsum.photos/seed/mushroom/400/300'),
(4,'Vegetable Soup / شوربة خضار','Light vegetable soup',13.00,160,'https://picsum.photos/seed/vegsoup/400/300'),
(4,'Seafood Soup / شوربة بحرية','Mixed seafood creamy soup',21.00,280,'https://picsum.photos/seed/seafoodsoup/400/300'),
(4,'Pumpkin Soup / شوربة قرع','Smooth pumpkin soup with cream',16.00,200,'https://picsum.photos/seed/pumpkin/400/300'),
(4,'Onion Soup / شوربة بصل','Caramelized onion broth',15.00,180,'https://picsum.photos/seed/onion/400/300'),
(4,'Minestrone / شوربة مينستروني','Italian vegetable soup',17.00,210,'https://picsum.photos/seed/minestrone/400/300'),
(4,'Broccoli Soup / شوربة بروكلي','Creamy broccoli soup',16.00,190,'https://picsum.photos/seed/broccoli/400/300');

-- 10 Sushi
INSERT INTO restaurant.menu_items (section_id,name,description,price,calories,image_url) VALUES
(5,'California Roll / كاليفورنيا رول','Crab, avocado, cucumber roll',28.00,310,'https://picsum.photos/seed/california/400/300'),
(5,'Salmon Nigiri / سلمون نيجيري','Fresh salmon on rice',26.00,220,'https://picsum.photos/seed/nigiri/400/300'),
(5,'Tuna Roll / رول تونة','Classic tuna sushi roll',25.00,230,'https://picsum.photos/seed/tunaroll/400/300'),
(5,'Shrimp Tempura Roll / رول روبيان','Fried shrimp roll with sauce',29.00,340,'https://picsum.photos/seed/tempura/400/300'),
(5,'Avocado Roll / رول أفوكادو','Vegetarian roll with avocado',23.00,210,'https://picsum.photos/seed/avoroll/400/300'),
(5,'Dragon Roll / رول دراجون','Shrimp tempura topped with avocado',32.00,360,'https://picsum.photos/seed/dragon/400/300'),
(5,'Spicy Tuna Roll / رول تونة حار','Tuna with spicy mayo sauce',27.00,280,'https://picsum.photos/seed/spicytuna/400/300'),
(5,'Eel Roll / رول انقليس','Grilled eel sushi with sauce',34.00,390,'https://picsum.photos/seed/eelroll/400/300'),
(5,'Rainbow Roll / رول قوس قزح','Mixed fish layered roll',35.00,370,'https://picsum.photos/seed/rainbow/400/300'),
(5,'Sashimi Set / ساشيمي','Assorted sliced raw fish',42.00,320,'https://picsum.photos/seed/sashimi/400/300');

-- One customer
INSERT INTO restaurant.customers (full_name,email,phone,address,location)
VALUES ('Faisal altheyab / فيصل لذياب','faisal@example.com','+966512345678',
'Al Olaya, Riyadh',ST_GeogFromText('SRID=4326;POINT(46.685 24.711)'));

-- One PENDING order + 3 items
INSERT INTO restaurant.orders (customer_id,branch_id,status_id,total_price)
VALUES (1,1,1,65.00);

INSERT INTO restaurant.order_items (order_id,menu_item_id,quantity,price) VALUES
(1,1,1,18.00),
(1,2,1,22.00),
(1,4,1,15.00);

COMMIT;