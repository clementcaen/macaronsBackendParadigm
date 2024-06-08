--Erase all tables
DROP TABLE IF EXISTS sale_entry;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS sales_status;
DROP TABLE IF EXISTS macaron;

-- Create sales_status table
CREATE TABLE sales_status (
    code VARCHAR(20) PRIMARY KEY
);

-- Create macaron table
CREATE TABLE macaron (
    taste VARCHAR(255) PRIMARY KEY,
    unit_price DECIMAL(10, 2),
    stock INT
);

-- Create sales table
CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    firstname_reservation VARCHAR(255),
    total_price_paid DECIMAL(10, 2),
    date DATE,
    status_code VARCHAR(20),
    FOREIGN KEY (status_code) REFERENCES sales_status(code)
);

-- Create sale_entry table
CREATE TABLE sale_entry (
    sales_id INT,
    macaron_taste VARCHAR(255),
    number_macaron INT,
    PRIMARY KEY (sales_id, macaron_taste),
    FOREIGN KEY (sales_id) REFERENCES sales(id),
    FOREIGN KEY (macaron_taste) REFERENCES macaron(taste)
);

INSERT INTO macaron (taste, unit_price, stock) VALUES
                                                   ('raspberry', 1.50, 100),
                                                   ('blackberry', 1.50, 100),
                                                   ('strawberry', 1.50, 100),
                                                   ('cranberry', 1.50, 100),
                                                   ('redberry', 1.50, 100),
                                                   ('grape', 1.50, 100);

INSERT INTO sales_status (code) VALUES
    ('NOENTRY'),
    ('WAITING'),
    ('CANCELLED'),
    ('PAID'),
    ('COMPLETED');