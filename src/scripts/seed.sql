
BEGIN;
SET LOCAL search_path TO food_bank, public;

INSERT INTO users (user_id, name, email, phone, password_hash, role) VALUES
(900001, 'Demo Coordinator', 'coordinator.demo@example.com', '604-555-0101', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Coordinator'),
(900002, 'Demo Donor A', 'donor.a.demo@example.com', '604-555-0102', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Donor'),
(900003, 'Demo Volunteer A', 'volunteer.a.demo@example.com', '604-555-0103', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Volunteer'),
(900004, 'Demo Recipient A', 'recipient.a.demo@example.com', '604-555-0104', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Recipient'),
(900005, 'Demo Recipient Pending', 'recipient.pending.demo@example.com', '604-555-0105', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Recipient'),
(900006, 'Demo Volunteer B', 'volunteer.b.demo@example.com', '604-555-0106', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Volunteer'),
(900007, 'Demo Donor B', 'donor.b.demo@example.com', '604-555-0107', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Donor'),
(900008, 'Demo Recipient B', 'recipient.b.demo@example.com', '604-555-0108', '$2a$10$Qzq0tmBI44/RA9QK4HBOD.DqAFDEY54pzIROtFXawLBaI.J41Kh.G', 'Recipient')
ON CONFLICT DO NOTHING;

INSERT INTO recipients (recipient_id, household_size, dietary_restrictions, emergency_contact, approval_status, approved_by, approved_at) VALUES
(900004, 3, 'Vegetarian', 'Demo Contact A: 604-555-0201', 'Approved', 900001, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(900005, 1, NULL, 'Demo Contact Pending: 604-555-0202', 'Pending', NULL, NULL),
(900008, 4, 'No peanuts', 'Demo Contact B: 604-555-0203', 'Approved', 900001, CURRENT_TIMESTAMP - INTERVAL '10 days')
ON CONFLICT DO NOTHING;

INSERT INTO volunteers (volunteer_id, profile_image, availability, created_by) VALUES
(900003, NULL, 'Monday, Wednesday, Friday mornings', 900001),
(900006, NULL, 'Tuesday and Thursday afternoons', 900001)
ON CONFLICT DO NOTHING;

INSERT INTO categories (category_id, name) VALUES
(900101, 'Demo Canned Goods'),
(900102, 'Demo Produce'),
(900103, 'Demo Dairy'),
(900104, 'Demo Grains'),
(900105, 'Demo Household')
ON CONFLICT DO NOTHING;

INSERT INTO donations (donation_id, donor_id, recorded_by, item_name, category_id, quantity, unit, donation_date) VALUES
(900201, 900002, 900003, 'Canned beans', 900101, 30, 'cans', CURRENT_DATE - 12),
(900202, 900007, 900003, 'Rice', 900104, 25, 'kg', CURRENT_DATE - 10),
(900203, 900002, 900006, 'Apples', 900102, 20, 'kg', CURRENT_DATE - 5),
(900204, 900007, NULL, 'Milk cartons', 900103, 18, 'cartons', CURRENT_DATE - 3)
ON CONFLICT DO NOTHING;

INSERT INTO inventory (inventory_id, donation_id, item_name, category_id, quantity, quantity_received, unit, received_date, expiry_date, waste_date, status, updated_by) VALUES
(900301, 900201, 'Canned beans', 900101, 20, 30, 'cans', CURRENT_DATE - 12, CURRENT_DATE + 180, NULL, 'Available', 900003),
(900302, 900202, 'Rice', 900104, 20, 25, 'kg', CURRENT_DATE - 10, CURRENT_DATE + 200, NULL, 'Available', 900003),
(900303, 900203, 'Apples', 900102, 8, 20, 'kg', CURRENT_DATE - 5, CURRENT_DATE + 3, NULL, 'Available', 900006),
(900304, 900204, 'Milk cartons', 900103, 18, 18, 'cartons', CURRENT_DATE - 3, CURRENT_DATE + 2, NULL, 'Available', 900006),
(900305, NULL, 'Wilted lettuce', 900102, 4, 10, 'kg', CURRENT_DATE - 10, CURRENT_DATE - 1, CURRENT_DATE - 1, 'Expired', 900003),
(900306, NULL, 'Damaged packages', 900105, 2, 6, 'packs', CURRENT_DATE - 7, NULL, CURRENT_DATE - 2, 'Discarded', 900003)
ON CONFLICT DO NOTHING;

INSERT INTO hamper_requests (request_id, recipient_id, request_date, pickup_date, status) VALUES
(900401, 900004, CURRENT_DATE - 6, CURRENT_DATE - 4, 'Completed'),
(900402, 900008, CURRENT_DATE - 3, CURRENT_DATE - 1, 'Scheduled'),
(900403, 900004, CURRENT_DATE - 1, CURRENT_DATE + 2, 'Scheduled')
ON CONFLICT DO NOTHING;

INSERT INTO volunteer_shifts (shift_id, volunteer_id, shift_date, start_time, end_time, assigned_task, assigned_by) VALUES
(900501, 900003, CURRENT_DATE - 4, '09:00', '12:00', 'Distribute food hampers', 900001),
(900502, 900006, CURRENT_DATE - 1, '13:00', '16:00', 'Recipient pickup assistance', 900001),
(900503, 900003, CURRENT_DATE + 2, '09:00', '12:00', 'Inventory intake', 900001)
ON CONFLICT DO NOTHING;

INSERT INTO distributions (distribution_id, request_id, recipient_id, volunteer_id, shift_id, distribution_date, distribution_time, pickup_status) VALUES
(900601, 900401, 900004, 900003, 900501, CURRENT_DATE - 4, '10:00', 'Fulfilled'),
(900602, 900402, 900008, 900006, 900502, CURRENT_DATE - 1, '14:00', 'Missed'),
(900603, 900403, 900004, 900003, 900503, CURRENT_DATE + 2, '10:00', 'Scheduled')
ON CONFLICT DO NOTHING;

INSERT INTO distribution_items (distribution_item_id, distribution_id, inventory_id, quantity, unit) VALUES
(900701, 900601, 900301, 10, 'cans'),
(900702, 900601, 900302, 5, 'kg'),
(900703, 900601, 900303, 3, 'kg')
ON CONFLICT DO NOTHING;

INSERT INTO notifications (notification_id, type, risk_reason, distribution_id, inventory_id, category_id, recipient_email, message, sent_status) VALUES
(900801, 'PickupAlert', 'Missed', 900602, NULL, NULL, 'coordinator.demo@example.com', 'DEMO: Scheduled pickup was missed.', 'Pending'),
(900802, 'InventoryAtRisk', 'Expiry', NULL, 900304, NULL, 'coordinator.demo@example.com', 'DEMO: Milk cartons expire in two days.', 'Pending'),
(900803, 'InventoryAtRisk', 'StockDecline', NULL, NULL, 900102, 'coordinator.demo@example.com', 'DEMO: Produce category stock may decline.', 'Pending')
ON CONFLICT DO NOTHING;

COMMIT;