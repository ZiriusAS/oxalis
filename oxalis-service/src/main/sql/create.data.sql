USE zapdb;

INSERT INTO `users` (`user_name`, `password`) VALUES ('superadmin@zirius.no', '6c817128e56418ff38249812af803c3b');
INSERT INTO `users` (`user_name`, `password`) VALUES ('guest@zirius.no', '084e0343a0486ff05530df6c705c8bb4');

INSERT INTO `user_role` (`user_name`, `role_name`) VALUES ('superadmin@zirius.no', 'AccessPointAdmin');
INSERT INTO `user_role` (`user_name`, `role_name`) VALUES ('guest@zirius.no', 'AccessPointUser');

INSERT INTO `participant` (`participant_id`, `participant_name`, `email_id`) 
VALUES ('9908:986559469', 'Zirius AS', 'karl.erik@zirius.no');

INSERT INTO `accesspoint_settings` (`accesspoint_id`, `last_sync_datetime`) VALUES ('zap', NOW());
