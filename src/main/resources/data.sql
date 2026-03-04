-- Sample categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices, components, and accessories'),
('Furniture', 'Office and home furniture'),
('Apparel', 'Clothing and accessories'),
('Tools', 'Hand tools and power tools');

-- Sample products
INSERT INTO products (name, sku, description, price, quantity, category_id) VALUES
('Wireless Keyboard', 'ELEC-KB-001', 'Compact wireless Bluetooth keyboard', 49.99, 120, 1),
('USB-C Hub 7-Port', 'ELEC-HUB-002', 'Multiport USB-C hub with 4K HDMI and PD charging', 35.50, 85, 1),
('Laptop Stand Adjustable', 'FURN-LS-001', 'Ergonomic adjustable aluminum laptop stand', 29.99, 200, 2),
('Ergonomic Office Chair', 'FURN-CH-001', 'Lumbar support office chair with armrests', 249.00, 15, 2),
('Polo Shirt - White L', 'APRL-PS-001', 'Premium cotton polo shirt, white, size L', 24.99, 7, 3),
('Safety Goggles', 'TOOL-SG-001', 'ANSI Z87.1 rated impact-resistant safety goggles', 12.00, 0, 4),
('Precision Screwdriver Set', 'TOOL-SD-001', '42-piece magnetic precision screwdriver kit', 18.99, 50, 4);
