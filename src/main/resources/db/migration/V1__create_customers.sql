CREATE TABLE customers (
                           id UUID PRIMARY KEY,
                           customer_number VARCHAR(30) NOT NULL,
                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100) NOT NULL,
                           email VARCHAR(150) NOT NULL,
                           phone VARCHAR(20) NOT NULL,
                           national_id VARCHAR(11) NOT NULL,
                           birth_date DATE NOT NULL,
                           status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL,

                           CONSTRAINT uk_customers_customer_number
                               UNIQUE (customer_number),

                           CONSTRAINT uk_customers_email
                               UNIQUE (email),

                           CONSTRAINT uk_customers_national_id
                               UNIQUE (national_id),

                           CONSTRAINT chk_customers_status
                               CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_customers_last_name
    ON customers(last_name);
