CREATE TABLE user_profiles(
    id                  CHAR(36) PRIMARY KEY,
    auth_user_id        CHAR(36) NOT NULL,
    username            VARCHAR(50) NOT NULL UNIQUE,
    first_name          VARCHAR(100),
    last_name           VARCHAR(100),
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone               VARCHAR(20) UNIQUE,
    city                VARCHAR(100),
    district            VARCHAR(100),
    profile_image_url   VARCHAR(500),
    average_rating      DECIMAL(2,1) DEFAULT 0.0,
    review_count        INT DEFAULT 0,
    joined_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);