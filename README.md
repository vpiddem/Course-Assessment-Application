Course-Assessment-Application
=============================

A Comprehensive course assessment and ananlysis application developed using Java.

Pre-Requisites : 
1.	Install a Postgres Database Server application on the machine which aims to run our Gradience assessment application.
2.	Also make sure the default configuration values are maintained : 
The values we used for connecting to the Postgres Database are as below :
Database Server: Postgres 9.3 version.
Port No: 5432
Database Name: gradiencedb
Schema : public
3.	JAVA 6 or higher version of JRE must be installed on the target machine.


Additional Extra Features:
1.	Implemented Hot stand-by feature for scalable database services and enhancing data availability in case of database server maintenance and crash scenarios.
2.	We have successfully deployed Continuous Streaming replication deployed for a real-time on the fly data replication to a remote Backup server using postgres pg_hba.conf settings.
3.	A feature to search all the table components across the application using “Ctrl+F” keyboard command for content.
4.	Display of Last login time for a user every time he logs in, ensuring a layer of security and informing user of suspicious logins.
5.	Notifying the user if he tries to login with the old password that his password has been changed a few days ago. [e.g. password has been changed 10 days ago]
6.	Restricting the User from updating the password with the same old password.
7.	System tray Icon availability of the assessment application which makes minimizing, closing and opening the application seamlessly easy.
