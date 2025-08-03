INSERT INTO todo_items (title, description, completed, created_at, target_completion_date) VALUES
('Doctor Appointment','Schedule routine checkup', false, NOW(), DATEADD('DAY', 7, NOW())),
('Buy Groceries', 'Milk, Bread, Eggs, Vegetables', false, NOW(), DATEADD('DAY', 1, NOW())),
('Finish Assignments', 'Complete the technical documentation for the current project', false, NOW(), DATEADD('DAY', 3, NOW())),
('Yoga', 'Stay fit', true, DATEADD('DAY', -2, NOW()), DATEADD('DAY', -1, NOW()));