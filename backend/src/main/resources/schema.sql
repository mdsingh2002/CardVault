-- ========================================
-- CardVault Database Schema
-- ========================================

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ========================================
-- Table: users
-- Stores user account information
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- Table: card_conditions
-- Lookup table for card conditions
-- ========================================
CREATE TABLE IF NOT EXISTS card_conditions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    value_multiplier DECIMAL(3, 2) DEFAULT 1.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default card conditions
INSERT INTO card_conditions (name, description, value_multiplier) VALUES
    ('Mint', 'Perfect condition, no visible flaws', 1.50),
    ('Near Mint', 'Slight wear on edges or corners', 1.30),
    ('Excellent', 'Light wear but still looks great', 1.10),
    ('Good', 'Moderate wear, some scratches or whitening', 0.90),
    ('Played', 'Heavy wear from gameplay', 0.60),
    ('Poor', 'Significant damage or creasing', 0.30)
ON CONFLICT (name) DO NOTHING;

-- ========================================
-- Table: cards
-- card data (from Pokemon TCG API)
-- ========================================
CREATE TABLE IF NOT EXISTS cards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    api_id VARCHAR(100) UNIQUE,
    name VARCHAR(100) NOT NULL,
    set_name VARCHAR(100),
    set_series VARCHAR(100),
    card_number VARCHAR(50),
    rarity VARCHAR(50),
    card_type VARCHAR(50),
    supertype VARCHAR(50),
    subtypes TEXT,
    hp INTEGER,
    artist VARCHAR(100),
    image_url TEXT,
    small_image_url TEXT,
    market_price DECIMAL(10, 2),
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- Table: user_cards
-- User's card collection inventory
-- ========================================
CREATE TABLE IF NOT EXISTS user_cards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    card_id UUID NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    quantity INTEGER DEFAULT 1 CHECK (quantity > 0),
    condition_id INTEGER REFERENCES card_conditions(id),
    purchase_price DECIMAL(10, 2),
    current_value DECIMAL(10, 2),
    acquisition_date DATE DEFAULT CURRENT_DATE,
    notes TEXT,
    is_graded BOOLEAN DEFAULT FALSE,
    grade_value VARCHAR(20),
    grading_company VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, card_id, condition_id)
);

-- ========================================
-- Table: achievements
-- Badges and milestones
-- ========================================
CREATE TABLE IF NOT EXISTS achievements (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    criteria JSONB,
    points INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default achievements
INSERT INTO achievements (name, description, icon, criteria, points) VALUES
    ('First Card', 'Add your first card to the collection', '🎴', '{"total_cards": 1}', 10),
    ('Collector', 'Reach 50 cards in your collection', '📚', '{"total_cards": 50}', 50),
    ('Master Collector', 'Reach 100 cards in your collection', '🏆', '{"total_cards": 100}', 100),
    ('Rare Hunter', 'Collect 10 rare or higher cards', '💎', '{"rare_cards": 10}', 75),
    ('Set Completionist', 'Complete an entire set', '✨', '{"complete_sets": 1}', 150),
    ('High Roller', 'Collection value exceeds $1000', '💰', '{"collection_value": 1000}', 100),
    ('Vintage Collector', 'Own a card from the Base Set', '🎯', '{"vintage_cards": 1}', 50)
ON CONFLICT (name) DO NOTHING;

-- ========================================
-- Table: user_achievements
-- Track which achievements users have earned
-- ========================================
CREATE TABLE IF NOT EXISTS user_achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    achievement_id INTEGER NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, achievement_id)
);

-- ========================================
-- Table: wishlist
-- Cards users want to acquire
-- ========================================
CREATE TABLE IF NOT EXISTS wishlist (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    card_id UUID NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    priority INTEGER DEFAULT 1 CHECK (priority BETWEEN 1 AND 5),
    max_price DECIMAL(10, 2),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, card_id)
);

-- ========================================
-- Indexes for Performance
-- ========================================
CREATE INDEX IF NOT EXISTS idx_user_cards_user_id ON user_cards(user_id);
CREATE INDEX IF NOT EXISTS idx_user_cards_card_id ON user_cards(card_id);
CREATE INDEX IF NOT EXISTS idx_cards_name ON cards(name);
CREATE INDEX IF NOT EXISTS idx_cards_set_name ON cards(set_name);
CREATE INDEX IF NOT EXISTS idx_cards_rarity ON cards(rarity);
CREATE INDEX IF NOT EXISTS idx_cards_api_id ON cards(api_id);
CREATE INDEX IF NOT EXISTS idx_user_achievements_user_id ON user_achievements(user_id);
CREATE INDEX IF NOT EXISTS idx_wishlist_user_id ON wishlist(user_id);

-- ========================================
-- Views for Analytics
-- ========================================

-- View: User collection summary
CREATE OR REPLACE VIEW v_user_collection_summary AS
SELECT
    u.id AS user_id,
    u.username,
    COUNT(DISTINCT uc.card_id) AS unique_cards,
    SUM(uc.quantity) AS total_cards,
    SUM(uc.current_value * uc.quantity) AS total_value,
    COUNT(DISTINCT c.set_name) AS total_sets
FROM users u
LEFT JOIN user_cards uc ON u.id = uc.user_id
LEFT JOIN cards c ON uc.card_id = c.id
GROUP BY u.id, u.username;

-- View: Card rarity distribution
CREATE OR REPLACE VIEW v_card_rarity_distribution AS
SELECT
    uc.user_id,
    c.rarity,
    COUNT(*) AS card_count,
    SUM(uc.quantity) AS total_quantity,
    SUM(uc.current_value * uc.quantity) AS total_value
FROM user_cards uc
JOIN cards c ON uc.card_id = c.id
GROUP BY uc.user_id, c.rarity;

-- View: Most valuable cards
CREATE OR REPLACE VIEW v_top_valuable_cards AS
SELECT
    uc.user_id,
    c.name,
    c.set_name,
    c.rarity,
    uc.quantity,
    uc.current_value,
    (uc.current_value * uc.quantity) AS total_value,
    cc.name AS condition
FROM user_cards uc
JOIN cards c ON uc.card_id = c.id
LEFT JOIN card_conditions cc ON uc.condition_id = cc.id
ORDER BY total_value DESC;

-- ========================================
-- Functions
-- ========================================

-- Function: Update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cards_updated_at BEFORE UPDATE ON cards
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_cards_updated_at BEFORE UPDATE ON user_cards
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ========================================
-- Schema Created Successfully
-- ========================================
