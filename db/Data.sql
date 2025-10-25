BEGIN;

-- ==============================
-- ORDER STATUS
-- ==============================
INSERT INTO restaurant.status (code, display_name, description) VALUES
('PENDING', 'Pending', 'Order received and awaiting confirmation'),
('PREPARING', 'Preparing', 'Order is being prepared'),
('READY', 'Ready', 'Order is ready for pickup or delivery'),
('ON_THE_WAY', 'On the Way', 'Order is out for delivery'),
('DELIVERED', 'Delivered', 'Order successfully delivered'),
('CANCELLED', 'Cancelled', 'Order was cancelled');

-- ==============================
-- BRANCHES
-- ==============================
INSERT INTO restaurant.branches
(branch_name, address, city, country, phone, email, location, opening_hours, is_main_branch, is_active)
VALUES
('Al Olaya Branch', 'Olaya St, Riyadh', 'Riyadh', 'Saudi Arabia',
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
 }', TRUE, TRUE);

 INSERT INTO restaurant.branches
(branch_name, address, city, country, phone, email, location, opening_hours, is_main_branch, is_active)
VALUES
('Al Malqa Branch', 'King Salman Rd, Al Malqa, Riyadh', 'Riyadh', 'Saudi Arabia',
 '+966500000002', 'malqa@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.624 24.789)'),
 '{
   "sunday":{"open":"09:00","close":"22:00"},
   "monday":{"open":"09:00","close":"22:00"},
   "tuesday":{"open":"09:00","close":"22:00"},
   "wednesday":{"open":"09:00","close":"22:00"},
   "thursday":{"open":"09:00","close":"00:00"},
   "friday":{"open":"14:00","close":"01:00"},
   "saturday":{"open":"09:00","close":"22:00"}
 }', FALSE, TRUE);


INSERT INTO restaurant.branches
(branch_name, address, city, country, phone, email, location, opening_hours, is_main_branch, is_active)
VALUES
('Al Nakheel Branch', 'Imam Saud Rd, Al Nakheel, Riyadh', 'Riyadh', 'Saudi Arabia',
 '+966500000003', 'nakheel@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.701 24.746)'),
 '{
   "sunday":{"open":"07:30","close":"22:30"},
   "monday":{"open":"07:30","close":"22:30"},
   "tuesday":{"open":"07:30","close":"22:30"},
   "wednesday":{"open":"07:30","close":"22:30"},
   "thursday":{"open":"07:30","close":"00:00"},
   "friday":{"open":"13:00","close":"00:30"},
   "saturday":{"open":"08:00","close":"22:30"}
 }', FALSE, TRUE);


INSERT INTO restaurant.branches
(branch_name, address, city, country, phone, email, location, opening_hours, is_main_branch, is_active)
VALUES
('Al Rawdah Branch', 'Prince Bandar Rd, Al Rawdah, Riyadh', 'Riyadh', 'Saudi Arabia',
 '+966500000004', 'rawdah@restaurant.com',
 ST_GeogFromText('SRID=4326;POINT(46.785 24.744)'),
 '{
   "sunday":{"open":"09:00","close":"21:00"},
   "monday":{"open":"09:00","close":"21:00"},
   "tuesday":{"open":"09:00","close":"21:00"},
   "wednesday":{"open":"09:00","close":"21:00"},
   "thursday":{"open":"09:00","close":"23:00"},
   "friday":{"open":"14:00","close":"23:30"},
   "saturday":{"open":"09:00","close":"21:00"}
 }', FALSE, FALSE);

-- ==============================
-- MENU SECTIONS
-- ==============================
INSERT INTO restaurant.menu_sections (branch_id, name, description) VALUES
(1, 'Breakfast', 'Morning meals with coffee, eggs & pastries'),
(1, 'Lunch', 'Midday meals with rice, meat & salads'),
(1, 'Dinner', 'Evening grills and sides'),
(1, 'Soups', 'Warm and comforting soups'),
(1, 'Sushi', 'Fresh Japanese sushi rolls');

-- ==============================
-- BREAKFAST (10 items)
-- ==============================
INSERT INTO restaurant.menu_items (section_id, name, description, price, calories, image_url) VALUES
(1, 'Omelette', 'Fluffy omelette with cheese & vegetables', 18.00, 250, 'https://images.pexels.com/photos/1437267/pexels-photo-1437267.jpeg'),
(1, 'Pancakes', 'Soft pancakes with maple syrup', 22.00, 340, 'https://images.pexels.com/photos/376464/pexels-photo-376464.jpeg'),
(1, 'Avocado Toast', 'Whole grain toast with avocado spread', 19.00, 270, 'https://images.pexels.com/photos/566566/pexels-photo-566566.jpeg'),
(1, 'Shakshuka', 'Eggs poached in spiced tomato sauce', 20.00, 260, 'https://images.pexels.com/photos/2232/vegetables-italian-pizza-restaurant.jpg'),
(1, 'Cheese Manakeesh', 'Lebanese flatbread with cheese', 16.00, 280, 'https://images.pexels.com/photos/4109085/pexels-photo-4109085.jpeg'),
(1, 'Foul Medames', 'Traditional fava beans with olive oil and lemon', 14.00, 290, 'https://images.pexels.com/photos/1109197/pexels-photo-1109197.jpeg'),
(1, 'Labneh Plate', 'Creamy labneh with olive oil and mint', 13.00, 220, 'https://images.pexels.com/photos/1458694/pexels-photo-1458694.jpeg'),
(1, 'Halloumi Sandwich', 'Grilled halloumi with fresh vegetables', 21.00, 310, 'https://images.pexels.com/photos/1640774/pexels-photo-1640774.jpeg'),
(1, 'Oatmeal Bowl', 'Warm oats with banana and honey', 15.00, 250, 'https://images.pexels.com/photos/4109994/pexels-photo-4109994.jpeg'),
(1, 'Arabic Tea & Dates', 'Traditional tea with dates', 10.00, 150, 'https://images.pexels.com/photos/1373915/pexels-photo-1373915.jpeg');

-- ==============================
-- LUNCH (10 items)
-- ==============================
INSERT INTO restaurant.menu_items (section_id, name, description, price, calories, image_url) VALUES
(2, 'Grilled Chicken Plate', 'Grilled chicken with rice and salad', 35.00, 520, 'https://images.pexels.com/photos/410648/pexels-photo-410648.jpeg'),
(2, 'Beef Kabsa', 'Traditional Saudi rice with spiced meat', 38.00, 610, 'https://images.pexels.com/photos/541040/pexels-photo-541040.jpeg'),
(2, 'Chicken Biryani', 'Indian-style spiced rice with chicken', 33.00, 580, 'https://images.pexels.com/photos/5908042/pexels-photo-5908042.jpeg'),
(2, 'Pasta Alfredo', 'Creamy pasta with chicken and mushroom', 32.00, 540, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg'),
(2, 'Steak Meal', 'Grilled beef steak with mashed potato', 55.00, 700, 'https://images.pexels.com/photos/675951/pexels-photo-675951.jpeg'),
(2, 'Fettuccine', 'White sauce pasta with shrimp', 34.00, 560, 'https://images.pexels.com/photos/3298181/pexels-photo-3298181.jpeg'),
(2, 'Burger Classic', 'Beef burger with fries and cheese', 30.00, 620, 'https://images.pexels.com/photos/1639561/pexels-photo-1639561.jpeg'),
(2, 'Chicken Shawarma', 'Marinated chicken served with garlic sauce', 26.00, 430, 'https://images.pexels.com/photos/7030218/pexels-photo-7030218.jpeg'),
(2, 'Mixed Grill', 'Assorted grilled meats served with bread', 47.00, 720, 'https://images.pexels.com/photos/410648/pexels-photo-410648.jpeg'),
(2, 'Greek Salad', 'Fresh salad with feta, olives and tomato', 23.00, 290, 'https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg');

-- ==============================
-- DINNER (10 items)
-- ==============================
INSERT INTO restaurant.menu_items (section_id, name, description, price, calories, image_url) VALUES
(3, 'Grilled Shrimp', 'Shrimp with butter sauce', 48.00, 460, 'https://images.pexels.com/photos/3298181/pexels-photo-3298181.jpeg'),
(3, 'Ribeye Steak', 'Juicy ribeye with herb butter', 65.00, 750, 'https://images.pexels.com/photos/675951/pexels-photo-675951.jpeg'),
(3, 'Chicken Alfredo Pizza', 'Creamy chicken pizza', 33.00, 590, 'https://images.pexels.com/photos/2619967/pexels-photo-2619967.jpeg'),
(3, 'Mushroom Risotto', 'Creamy Italian risotto with mushrooms', 29.00, 480, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg'),
(3, 'Seafood Pasta', 'Pasta with shrimp and calamari', 38.00, 610, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg'),
(3, 'Fried Chicken', 'Crispy fried chicken with coleslaw', 31.00, 640, 'https://images.pexels.com/photos/410648/pexels-photo-410648.jpeg'),
(3, 'Chicken Quesadilla', 'Mexican tortilla with chicken & cheese', 28.00, 520, 'https://images.pexels.com/photos/3756523/pexels-photo-3756523.jpeg'),
(3, 'Veggie Burger', 'Healthy vegetarian burger', 25.00, 420, 'https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg'),
(3, 'Salmon Teriyaki', 'Salmon with teriyaki sauce', 52.00, 480, 'https://images.pexels.com/photos/3298181/pexels-photo-3298181.jpeg'),
(3, 'Beef Lasagna', 'Baked pasta with meat and cheese', 33.00, 610, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg');

-- ==============================
-- SOUPS (6 items)
-- ==============================
INSERT INTO restaurant.menu_items (section_id, name, description, price, calories, image_url) VALUES
(4, 'Lentil Soup', 'Warm lentil soup with lemon', 14.00, 190, 'https://images.pexels.com/photos/1640773/pexels-photo-1640773.jpeg'),
(4, 'Tomato Soup', 'Creamy tomato soup', 15.00, 170, 'https://images.pexels.com/photos/4109994/pexels-photo-4109994.jpeg'),
(4, 'Chicken Corn Soup', 'Thick chicken and corn soup', 17.00, 230, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg'),
(4, 'Mushroom Soup', 'Creamy mushroom soup', 18.00, 250, 'https://images.pexels.com/photos/539451/pexels-photo-539451.jpeg'),
(4, 'Vegetable Soup', 'Light vegetable soup', 13.00, 160, 'https://images.pexels.com/photos/1640773/pexels-photo-1640773.jpeg'),
(4, 'Seafood Soup', 'Mixed seafood creamy soup', 21.00, 280, 'https://images.pexels.com/photos/628752/pexels-photo-628752.jpeg');

-- ==============================
-- SUSHI (6 items)
-- ==============================
INSERT INTO restaurant.menu_items (section_id, name, description, price, calories, image_url) VALUES
(5, 'California Roll', 'Crab, avocado, cucumber roll', 28.00, 310, 'https://images.pexels.com/photos/2098085/pexels-photo-2098085.jpeg'),
(5, 'Salmon Nigiri', 'Fresh salmon on rice', 26.00, 220, 'https://images.pexels.com/photos/357756/pexels-photo-357756.jpeg'),
(5, 'Tuna Roll', 'Classic tuna sushi roll', 25.00, 230, 'https://images.pexels.com/photos/2098085/pexels-photo-2098085.jpeg'),
(5, 'Shrimp Tempura Roll', 'Fried shrimp roll with sauce', 29.00, 340, 'https://images.pexels.com/photos/3298181/pexels-photo-3298181.jpeg'),
(5, 'Dragon Roll', 'Shrimp tempura topped with avocado', 32.00, 360, 'https://images.pexels.com/photos/3298181/pexels-photo-3298181.jpeg'),
(5, 'Rainbow Roll', 'Mixed fish layered roll', 35.00, 370, 'https://images.pexels.com/photos/2098085/pexels-photo-2098085.jpeg');

-- ==============================
-- CUSTOMER & ORDER
-- ==============================
INSERT INTO restaurant.customers (full_name, email, phone, address, location)
VALUES ('Faisal Altheyab', 'faisal@example.com', '+966512345678',
'Al Olaya, Riyadh', ST_GeogFromText('SRID=4326;POINT(46.685 24.711)'));

INSERT INTO restaurant.orders (customer_id, branch_id, status_id, total_price)
VALUES (1, 1, 1, 150.00);

INSERT INTO restaurant.order_items (order_id, menu_item_id, quantity, price)
VALUES
(1, 1, 1, 18.00),
(1, 2, 1, 22.00),
(1, 6, 1, 14.00),
(1, 8, 1, 21.00),
(1, 11, 1, 35.00);

COMMIT;
