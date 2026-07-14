CREATE TABLE user_profiles (
    -- Primary identifiers
    id                  CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    auth_user_id        CHAR(36),

    -- User info
    username            VARCHAR(50) NOT NULL UNIQUE,
    first_name          VARCHAR(100),
    last_name           VARCHAR(100),
    email               VARCHAR(255) NOT NULL UNIQUE,
    role                VARCHAR(20),

    -- Contact info
    phone               VARCHAR(20) UNIQUE,
    city                VARCHAR(100),
    district            VARCHAR(100),
    profile_image       VARCHAR(500),

    -- Ratings & reviews
    rating              DECIMAL(3,2) DEFAULT 0.00,
    review_count        INT DEFAULT 0,

    -- Sales & trades stats
    total_sales         INT DEFAULT 0,
    total_trades        INT DEFAULT 0,

    -- Timestamps
    joined_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Verification flags
    phone_verified      BOOLEAN DEFAULT FALSE,
    email_verified      BOOLEAN DEFAULT FALSE,

    -- Status
    status              VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE INDEX idx_user_city
ON user_profiles(city);

CREATE INDEX idx_user_username
ON user_profiles(username);

CREATE INDEX idx_user_email
ON user_profiles(email);

CREATE INDEX idx_auth_user
ON user_profiles(auth_user_id);