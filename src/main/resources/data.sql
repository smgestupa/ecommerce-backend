
-- INITIALIZES A ROOT ACCOUNT & ITS PERMISSIONS
INSERT IGNORE INTO users ( username, password ) VALUES ( 'root', '1234' );
INSERT IGNORE INTO groups ( username, user_group ) VALUES ( 'root', 'ROOT' );