
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       profile VARCHAR(255) NOT NULL,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         productname VARCHAR(255) NOT NULL,
                         description TEXT NOT NULL,
                         quantity INT NOT NULL,
                         productvalue NUMERIC(10,2) NOT NULL
);

CREATE TABLE requests (
                          id BIGSERIAL PRIMARY KEY,
                          requesttext TEXT NOT NULL,
                          requestdate TIMESTAMP NOT NULL,
                          responsetext TEXT,
                          userid BIGINT,
                          CONSTRAINT fk_requests_user FOREIGN KEY (userid) REFERENCES users(id)
);

CREATE TABLE requestproduct (
                                id BIGSERIAL PRIMARY KEY,
                                requestid BIGINT NOT NULL,
                                productid BIGINT NOT NULL,
                                quantity INT NOT NULL,
                                CONSTRAINT fk_requestproduct_request FOREIGN KEY (requestid) REFERENCES requests(id),
                                CONSTRAINT fk_requestproduct_product FOREIGN KEY (productid) REFERENCES product(id)
);
