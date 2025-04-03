
CREATE TABLE product  (
    productId BIGINT IDENTITY NOT NULL PRIMARY KEY,
    productName VARCHAR(255),
    productPrize numeric,
    categoryId int,
    deleteFlg bit
);