SET FOREIGN_KEY_CHECKS = 0;
-- 插入用户表(users)数据
INSERT INTO users (user_code, user_emial, user_password, user_name, user_age, user_sex, user_phone) 
VALUES ('#U1', 'user1@example.com', 'password1', 'Alice', 30, 'F', '12345678901'),
       ('#U2', 'user2@example.com', 'password2', 'Bob', 25, 'M', '12345678902'),
       ('#U3', 'user3@example.com', 'password3', 'Carol', 40, 'F', '12345678903'),
       ('#U4', 'user4@example.com', 'password4', 'David', 22, 'M', '12345678904'),
       ('#U5', 'user5@example.com', 'password5', 'Eve', 35, 'F', '12345678905');

-- 插入医生表(docuter)数据
INSERT INTO docuter (dc_code, hp_code, dc_emial, dc_password, dc_name, dc_ID, dc_IDcard, dc_age, dc_sex, dc_phone) 
VALUES ('#D1', '#H1', 'doctor1@example.com', 'dpassword1', 'Smith', 'D001', '123456789012345678', 35, 'M', '12345678911'),
       ('#D2', '#H1', 'doctor2@example.com', 'dpassword2', 'Johnson', 'D002', '123456789112345678', 40, 'M', '12345678912'),
       ('#D3', '#H2', 'doctor3@example.com', 'dpassword3', 'Lee', 'D003', '123456789212345678', 45, 'F', '12345678913'),
       ('#D4', '#H2', 'doctor4@example.com', 'dpassword4', 'Kim', 'D004', '123456789312345678', 38, 'M', '12345678914'),
       ('#D5', '#H3', 'doctor5@example.com', 'dpassword5', 'Brown', 'D005', '123456789412345678', 42, 'F', '12345678915');
       
-- 插入医院表(hospital)数据
INSERT INTO hospital (hp_code, hp_emial, hp_password, hp_name, hp_license, hp_busLi, hp_legal_person, hp_ID_legal) 
VALUES ('#H1', 'hospital1@example.com', 'hpassword1', 'ABC Hospital', 'License001', 'Business001', 'John', '123456789012345678'),
       ('#H2', 'hospital2@example.com', 'hpassword2', 'XYZ Hospital', 'License002', 'Business002', 'Jane', '123456789112345678'),
       ('#H3', 'hospital3@example.com', 'hpassword3', '123 Hospital', 'License003', 'Business003', 'Robert', '123456789212345678'),
       ('#H4', 'hospital4@example.com', 'hpassword4', '456 Hospital', 'License004', 'Business004', 'Lisa', '123456789312345678'),
       ('#H5', 'hospital5@example.com', 'hpassword5', '789 Hospital', 'License005', 'Business005', 'Steven', '123456789412345678');

-- 插入护士表(keeper)数据
INSERT INTO keeper (kp_code, hp_code, kp_password, kp_emial, kp_name, kp_IDcard, kp_age, kp_sex) 
VALUES ('#K1', '#H1', 'kpassword1', 'nurse1@example.com', 'Alice', '123456789012345679', 30, 'F'),
       ('#K2', '#H1', 'kpassword2', 'nurse2@example.com', 'Bob', '123456789112345679', 25, 'M'),
       ('#K3', '#H2', 'kpassword3', 'nurse3@example.com', 'Carol', '123456789212345679', 40, 'F'),
       ('#K4', '#H2', 'kpassword4', 'nurse4@example.com', 'David', '123456789312345679', 22, 'M'),
       ('#K5', '#H3', 'kpassword5', 'nurse5@example.com', 'Eve', '123456789412345679', 35, 'F');
 SET FOREIGN_KEY_CHECKS = 1;