CREATE INDEX idx_user_city
ON user_profiles(city);

CREATE INDEX idx_user_username
ON user_profiles(username);

CREATE INDEX idx_user_email
ON user_profiles(email);

CREATE INDEX idx_auth_user
ON user_profiles(auth_user_id);