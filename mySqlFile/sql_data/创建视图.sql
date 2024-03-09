DROP VIEW IF EXISTS id_code;
CREATE VIEW id_code AS
SELECT dc_emial AS emails,
       dc_code AS codes,
			 dc_password AS passwords
FROM docuter
UNION
SELECT hp_emial AS emails,
			 hp_code AS codes,
			 hp_password AS passwords
FROM hospital
UNION
SELECT kp_emial AS emails,
			 kp_code AS codes,
			 kp_password AS passwords
FROM keeper
UNION
SELECT user_emial AS emails,user_code AS codes,user_password AS passwords
FROM users
;

SELECT * FROM id_code;