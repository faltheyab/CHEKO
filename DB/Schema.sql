CREATE TABLE restaurant.branches (
    id               SERIAL PRIMARY KEY,
    branch_name      VARCHAR(255) NOT NULL,
    address          TEXT NOT NULL,
    city             VARCHAR(100),
    country          VARCHAR(100),
    location         GEOGRAPHY(Point, 4326),
    phone            VARCHAR(20),
    email            VARCHAR(255),
    opening_hours    JSONB,                     
    is_main_branch   BOOLEAN DEFAULT FALSE,
    is_active        BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMPTZ DEFAULT NOW(),  
    updated_at       TIMESTAMPTZ DEFAULT NOW()   
);

CREATE TABLE restaurant.menu_sections (
    id          SERIAL PRIMARY KEY,
    branch_id   INT REFERENCES restaurant.branches(id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE restaurant.menu_items (
    id           SERIAL PRIMARY KEY,
    section_id   INT REFERENCES restaurant.menu_sections(id) ON DELETE CASCADE,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    price        DECIMAL(10,2) NOT NULL,
    calories     INT CHECK (calories >= 0),
    image_url    TEXT,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE restaurant.customers (
    id          SERIAL PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) UNIQUE,
    phone       VARCHAR(20),
    address     TEXT,
    location    GEOGRAPHY(Point, 4326),
    created_at  TIMESTAMPTZ DEFAULT NOW()
);


CREATE TABLE restaurant.status (
    id           SERIAL PRIMARY KEY,
    code         VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE restaurant.orders (
    id           SERIAL PRIMARY KEY,
    customer_id  INT REFERENCES restaurant.customers(id) ON DELETE SET NULL,
    branch_id    INT REFERENCES restaurant.branches(id) ON DELETE SET NULL,
    status_id    INT REFERENCES restaurant.status(id) ON DELETE SET NULL,
    total_price  DECIMAL(10,2) NOT NULL,
    created_at   TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE restaurant.order_items (
    id            SERIAL PRIMARY KEY,
    order_id      INT REFERENCES restaurant.orders(id) ON DELETE CASCADE,
    menu_item_id  INT REFERENCES restaurant.menu_items(id) ON DELETE CASCADE,
    quantity      INT NOT NULL,
    price         DECIMAL(10,2) NOT NULL
);

CREATE INDEX idx_branches_location      ON restaurant.branches USING GIST (location);
CREATE INDEX idx_branches_opening_hours ON restaurant.branches USING GIN  (opening_hours);
CREATE INDEX idx_customers_location     ON restaurant.customers USING GIST (location);
