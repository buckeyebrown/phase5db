package com.phasefive.backend;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


public class SQLConnect {

	public SQLConnect() {
		super();
	}

	public static ResultSet setUpConnection() {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		// Create a variable for the connection string.

		String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
				"databaseName=insurance;integratedSecurity=true;";

		String query = getQuery(ObtainQueryCode.getQueryCode());
				//Change this
		try {
			//Establish the connection
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(connectionUrl);
			//DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			//String dbURL = "jdbc:sqlserver://localhost;databaseName=insurance;integratedSecurity=true;";
			//Connection conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
			    Statement stat = conn.createStatement();
				System.out.println("Connected to SQL server.");
                if(!(ObtainQueryCode.getQueryCode() == 7)){
                    rs = stat.executeQuery(query);
                }
                else{
                    stat.executeUpdate(query);
                }
                System.out.println("Got the result set.");

			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (NullPointerException n) {
        }
        finally
         {

			return rs;
		}
	}

	private static String getQuery(int queryCode){
		String returnString = "";
		switch (queryCode){
            case 0:
                if(!(ObtainQueryCode.getQueryText() == "")){
                    returnString = ObtainQueryCode.getQueryText();
                }
                break;
			case 1:
				returnString = "SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"UNION\n" +
						"SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')";
				break;
			case 2:
				returnString = "SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"INTERSECT\n" +
						"SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')";
				break;
			case 3:
				returnString = "SELECT Account_ID FROM ACCOUNTS\n" +
						"EXCEPT\n" +
						"(SELECT ssn FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"INTERSECT\n" +
						"SELECT employee_ssn FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins'))";
				break;
			case 4:
				returnString = "SELECT vehicle_model FROM VEHICLES WHERE NOT EXISTS (SELECT Report_Number FROM ACCIDENT_HISTORY)";
				break;
			case 5:
				returnString = "SELECT SUM(salary) FROM EMPLOYEES WHERE salary > 40000";
				break;
			case 6:
				returnString = "SELECT * FROM (ACCOUNTS as a INNER JOIN CAR_INSURANCE_POLICY as c ON a.Account_ID = c.accountNumber) INNER JOIN BOAT_INSURANCE_POLICY as b ON b.accountNumber = a.Account_ID\n";
				break;
            case 7:
                returnString = "CREATE TABLE INSURANCE_COMPANY\n" +
                        "(\n" +
                        "\tIns_Company_Name varchar(50) not null,\n" +
                        "\tIns_Company_Address varchar(70) not null,\n" +
                        "\tWebsite_URL varchar(30),\n" +
                        "\tPRIMARY KEY(Ins_Company_Name)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE BROKERS\n" +
                        "(\n" +
                        "\tCompany_Name varchar(50) not null,\n" +
                        "\tCompany_Address varchar(70) not null,\n" +
                        "\tWebsite_URL varchar(30),\n" +
                        "\tPRIMARY KEY(Company_Name)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE EMPLOYEES\n" +
                        "(\n" +
                        "\temp_ssn char(9) UNIQUE not null,\n" +
                        "\twork_phone varchar(10),\n" +
                        "\tsalary int,\n" +
                        "\tfname varchar(30) not null,\n" +
                        "\tlname varchar(30) not null,\n" +
                        "\tbroker_company varchar(50),\n" +
                        "\tPRIMARY KEY(emp_ssn)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE CLIENT\n" +
                        "(\n" +
                        "\tfname varchar(30) not null,\n" +
                        "\tlname varchar(30) not null,\n" +
                        "\tssn char(9) UNIQUE not null,\n" +
                        "\tprimary_phone_number varchar(30),\n" +
                        "\tprimary_email varchar(30),\n" +
                        "\taddress varchar(70),\n" +
                        "\tdob datetime,\n" +
                        "\taccount_number int not null,\n" +
                        "\temployee_ssn char(9) not null,\n" +
                        "\tPRIMARY KEY(ssn),\n" +
                        "\tFOREIGN KEY(employee_ssn)\n" +
                        "\tREFERENCES EMPLOYEES(emp_ssn)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE DEPENDENTS\n" +
                        "(\n" +
                        "\tfname varchar(30) not null,\n" +
                        "\tlname varchar(30) not null,\n" +
                        "\tdependent_ssn char(9) UNIQUE not null,\n" +
                        "\tclient_ssn char(9) not null,\n" +
                        "\tdob datetime,\n" +
                        "\tPRIMARY KEY(dependent_ssn),\n" +
                        "\tFOREIGN KEY(client_ssn)\n" +
                        "\tREFERENCES CLIENT(ssn)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE LAWYERS\n" +
                        "(\n" +
                        "\tfname varchar(30) not null,\n" +
                        "\tlname varchar(30) not null,\n" +
                        "\tprimary_email varchar(30),\n" +
                        "\tprimary_phone varchar(10),\n" +
                        "\tlawyer_id int not null unique,\n" +
                        "\tinsurance_Company_Name varchar(50) not null,\n" +
                        "\tPRIMARY KEY(lawyer_id),\n" +
                        "\tFOREIGN KEY(insurance_Company_Name)\n" +
                        "\tREFERENCES INSURANCE_COMPANY(Ins_Company_Name)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE ACCOUNTS\n" +
                        "(\n" +
                        "\tAccount_ID int not null unique,\n" +
                        "\tDate_Created datetime,\n" +
                        "\tAccount_Age int,\n" +
                        "\tclient_ssn char(9) not null,\n" +
                        "\tPRIMARY KEY(Account_ID),\n" +
                        "\tFOREIGN KEY(client_ssn)\n" +
                        "\tREFERENCES CLIENT(ssn)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE PAYMENT\n" +
                        "(\n" +
                        "\ttransaction_id int not null unique,\n" +
                        "\tpayment_method varchar(15),\n" +
                        "\tpayment_date datetime,\n" +
                        "\tpayment_amount int,\n" +
                        "\tpaid_off varchar(3),\n" +
                        "\tbalance int,\n" +
                        "\taccount_no int not null,\n" +
                        "\tPRIMARY KEY(transaction_id),\n" +
                        "\tFOREIGN KEY(account_no)\n" +
                        "\tREFERENCES Accounts(Account_ID)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE HOME_INSURANCE_POLICY\n" +
                        "(\n" +
                        "\tPolicy_Num int not null unique,\n" +
                        "\tterm_price int,\n" +
                        "\tterm_length int,\n" +
                        "\tactive varchar(3),\n" +
                        "\tstartDate datetime,\n" +
                        "\tpolicyAge int,\n" +
                        "\taccountNumber int not null,\n" +
                        "\tPRIMARY KEY(Policy_Num),\n" +
                        "\tFOREIGN KEY(accountNumber)\n" +
                        "\tREFERENCES Accounts(Account_ID)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE CAR_INSURANCE_POLICY\n" +
                        "(\n" +
                        "\tPolicy_Num int not null unique,\n" +
                        "\tterm_price int,\n" +
                        "\tterm_length int,\n" +
                        "\tactive varchar(3),\n" +
                        "\tstartDate datetime,\n" +
                        "\tpolicyAge int,\n" +
                        "\taccountNumber int not null,\n" +
                        "\tPRIMARY KEY(Policy_Num),\n" +
                        "\tFOREIGN KEY(accountNumber)\n" +
                        "\tREFERENCES Accounts(Account_ID)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE BOAT_INSURANCE_POLICY\n" +
                        "(\n" +
                        "\tPolicy_Num int not null unique,\n" +
                        "\tterm_price int,\n" +
                        "\tterm_length int,\n" +
                        "\tactive varchar(3),\n" +
                        "\tstartDate datetime,\n" +
                        "\tpolicyAge int,\n" +
                        "\taccountNumber int not null,\n" +
                        "\tPRIMARY KEY(Policy_Num),\n" +
                        "\tFOREIGN KEY(accountNumber)\n" +
                        "\tREFERENCES Accounts(Account_ID)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE HOMES\n" +
                        "(\n" +
                        "\tHouse_ID int not null unique,\n" +
                        "\tfloors int,\n" +
                        "\toccupants int,\n" +
                        "\tyear_built int,\n" +
                        "\thouse_age int,\n" +
                        "\ttotal_cost int,\n" +
                        "\tliability_deductible int,\n" +
                        "\tliability_coverage int,\n" +
                        "\tfire_deductible int,\n" +
                        "\tfire_coverage int,\n" +
                        "\tflood_deductible int,\n" +
                        "\tflood_coverage int,\n" +
                        "\thome_insurance_policy_num int not null,\n" +
                        "\tPRIMARY KEY(House_ID),\n" +
                        "\tFOREIGN KEY(home_insurance_policy_num)\n" +
                        "\tREFERENCES Home_Insurance_Policy(Policy_Num)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE ACCIDENT_HISTORY\n" +
                        "(\n" +
                        "\tReport_Number int not null unique,\n" +
                        "\tAccident_Data varchar(100),\n" +
                        "\tAccident_Date datetime,\n" +
                        "\tPRIMARY KEY(Report_Number)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE VEHICLES\n" +
                        "(\n" +
                        "\tVin char(17) not null unique,\n" +
                        "\tTotal_cost int,\n" +
                        "\tvehicle_year int,\n" +
                        "\tvehicle_color varchar(30),\n" +
                        "\tvehicle_make varchar(30),\n" +
                        "\tvehicle_model varchar(30),\n" +
                        "\tvehicle_doors int,\n" +
                        "\tbodily_injury_deductible int,\n" +
                        "\tbodily_injury_coverage int,\n" +
                        "\tcomprehensive_injury_deductible int,\n" +
                        "\tcomprehensive_injury_coverage int,\n" +
                        "\tuninsured_motorist_deductible int,\n" +
                        "\tuninsured_motorist_coverage int,\n" +
                        "\tcollison_injury_deductible int,\n" +
                        "\tcollison_injury_coverage int,\n" +
                        "\taccident_report_number int not null,\n" +
                        "\tat_fault varchar(3),\n" +
                        "\tis_totaled varchar(3),\n" +
                        "\tdamage_cost int,\n" +
                        "\tvehicles_policy_number int not null,\n" +
                        "\tPRIMARY KEY(Vin),\n" +
                        "\tFOREIGN KEY(accident_report_number)\n" +
                        "\tREFERENCES ACCIDENT_HISTORY(Report_Number),\n" +
                        "\tFOREIGN KEY(vehicles_policy_number)\n" +
                        "\tREFERENCES CAR_INSURANCE_POLICY(Policy_Num)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE BOATS\n" +
                        "(\n" +
                        "\tHin char(12) not null unique,\n" +
                        "\tTotal_cost int,\n" +
                        "\tbodily_injury_deductible int,\n" +
                        "\tbodily_injury_coverage int,\n" +
                        "\tcomprehensive_injury_deductible int,\n" +
                        "\tcomprehensive_injury_coverage int,\n" +
                        "\tcollison_injury_deductible int,\n" +
                        "\tcollison_injury_coverage int,\n" +
                        "\taccident_report_number int not null,\n" +
                        "\tat_fault varchar(3),\n" +
                        "\tis_totaled varchar(3),\n" +
                        "\tdamage_cost int,\n" +
                        "\tboat_insurance_policy_number int not null,\n" +
                        "\tPRIMARY KEY(Hin),\n" +
                        "\tFOREIGN KEY(accident_report_number)\n" +
                        "\tREFERENCES ACCIDENT_HISTORY(Report_Number),\n" +
                        "\tFOREIGN KEY(boat_insurance_policy_number)\n" +
                        "\tREFERENCES BOAT_INSURANCE_POLICY(Policy_Num)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE ACCOUNTS_SOLD\n" +
                        "(\n" +
                        "\temp_ssn char(9) not null unique,\n" +
                        "\taccount_id int not null\n" +
                        "\tPRIMARY KEY(emp_ssn),\n" +
                        "\tFOREIGN KEY(account_id)\n" +
                        "\tREFERENCES ACCOUNTS(Account_ID)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE Policies_Written\n" +
                        "(\n" +
                        "\tLawyer_ID int not null unique,\n" +
                        "\tHome_Ins_Policy_Num int,\n" +
                        "\tCar_Ins_Policy_Num int,\n" +
                        "\tBoat_Ins_Policy_Num int,\n" +
                        "\tPRIMARY KEY(Lawyer_ID),\n" +
                        "\tFOREIGN KEY(Home_Ins_Policy_Num)\n" +
                        "\tREFERENCES HOME_INSURANCE_POLICY(Policy_Num),\n" +
                        "\tFOREIGN KEY(Car_Ins_Policy_Num)\n" +
                        "\tREFERENCES CAR_INSURANCE_POLICY(Policy_Num),\n" +
                        "\tFOREIGN KEY(Boat_Ins_Policy_Num)\n" +
                        "\tREFERENCES BOAT_INSURANCE_POLICY(Policy_Num)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE Companies_Brokers\n" +
                        "(\n" +
                        "\tIns_Company_Name varchar(50) not null,\n" +
                        "\tBroker_Company_Name varchar(50) not null,\n" +
                        "\tFOREIGN KEY(Ins_Company_Name)\n" +
                        "\tREFERENCES INSURANCE_COMPANY(Ins_Company_Name),\n" +
                        "\tFOREIGN KEY(Broker_Company_Name)\n" +
                        "\tREFERENCES BROKERS(Company_Name)\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE Client_Audit\n" +
                        "(\n" +
                        "\tclientFName varchar(30) not null,\n" +
                        "\tclientLName varchar(30) not null,\n" +
                        "\temp_ssn char(9) not null,\n" +
                        "\taudit_action varchar(100),\n" +
                        "\taudit_time datetime\n" +
                        ")\n" +
                        "\n" +
                        "CREATE TABLE Past_Clients\n" +
                        "(\n" +
                        "\tclientFname varchar(30) not null,\n" +
                        "\tclientLname varchar(30) not null,\n" +
                        "\tssn char(9) UNIQUE not null,\n" +
                        "\tprimary_phone_number int,\n" +
                        "\tprimary_email varchar(30),\n" +
                        "\tdate_deleted datetime\n" +
                        ")" + "CREATE INDEX EmpIndex\n" +
                        "ON EMPLOYEES (lname, fname)\n" +
                        "\n" +
                        "CREATE INDEX CliIndex\n" +
                        "ON CLIENT (lname, fname)\n" +
                        "\n" +
                        "CREATE INDEX DepIndex\n" +
                        "ON DEPENDENTS (fname, lname)\n" +
                        "\n" +
                        "CREATE INDEX LawIndex\n" +
                        "ON LAWYERS (lname, fname)\n" +
                        "\n" +
                        "CREATE INDEX PayIndex\n" +
                        "ON PAYMENT (Payment_date)\n";
                break;
            case 8:
                returnString = "/*\n" +
                        "* INSURANCE COMPANY INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO INSURANCE_COMPANY (Ins_Company_Name, Ins_Company_Address,Website_URL) \n" +
                        "values ('Humana', '123 Sunshine Street Columbus 43210', 'www.humana.com');\n" +
                        "\n" +
                        "INSERT INTO INSURANCE_COMPANY (Ins_Company_Name, Ins_Company_Address,Website_URL) \n" +
                        "values ('BestInsurance', '321 Happy Street Columbus 43210', 'www.bestinsurance.com');\n" +
                        "\n" +
                        "INSERT INTO INSURANCE_COMPANY (Ins_Company_Name, Ins_Company_Address,Website_URL) \n" +
                        "values ('RightInsurance', '321 Wonderful Avenue Columbus 43210', 'www.rightinsurance.com');\n" +
                        "\n" +
                        "/*\n" +
                        "* INSURANCE BROKER INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO BROKERS (Company_Name, Company_Address,Website_URL) \n" +
                        "values ('Tims brokerage', '123 Doughnut Street Columbus 43210', 'www.timsbrokerage');\n" +
                        "\n" +
                        "INSERT INTO BROKERS (Company_Name, Company_Address,Website_URL) \n" +
                        "values ('best brokers', '123 Cake Street Columbus 43210', 'www.bestbrokers');\n" +
                        "\n" +
                        "INSERT INTO BROKERS (Company_Name, Company_Address,Website_URL) \n" +
                        "values ('Sallys brokerage', '123 Petunia Avenue Columbus 43210', 'www.sallysbrokerage');\n" +
                        "\n" +
                        "/*\n" +
                        "* EMPLOYEE INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO EMPLOYEES (emp_ssn, work_phone, salary, fname, lname, broker_company) \n" +
                        "values ('234332243', '1234567891', '40000', 'hannah', 'higgins', 'Tims brokerage');\n" +
                        "\n" +
                        "INSERT INTO EMPLOYEES (emp_ssn, work_phone, salary, fname, lname, broker_company) \n" +
                        "values ('123343232', '3214567891', '50000', 'samantha', 'jones', 'best brokers');\n" +
                        "\n" +
                        "INSERT INTO EMPLOYEES (emp_ssn, work_phone, salary, fname, lname, broker_company) \n" +
                        "values ('313343232', '3217027891', '90000', 'alexa', 'newman', 'Sallys brokerage');\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* CLIENT INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO CLIENT (Fname, Lname, SSN, Primary_Phone_Number, primary_email, Address, DOB, employee_ssn, account_number) \n" +
                        "values ('Mike', 'Smith', '123111232', 233, 'mike.smith@gmail.com', '101 Mike Smith Way Columbus, OH 43201', '12/17/1970', '234332243', 55);\n" +
                        "\n" +
                        "INSERT INTO CLIENT (Fname, Lname, SSN, Primary_Phone_Number, Primary_Email, Address, DOB, employee_ssn, account_number) \n" +
                        "values ('Jeffrey', 'Brown', '123343232', 33, 'mrperson@gmail.com', '101 hello Way Columbus, OH 43201', '05/07/1971', '234332243', 56);\n" +
                        "\n" +
                        "INSERT INTO CLIENT (Fname, Lname, SSN, Primary_Phone_Number, Primary_Email, Address, DOB, employee_ssn, account_number) \n" +
                        "values ('Adam', 'Morakis', '313343232', 32, 'morakis@gmail.com', '123 Edison Street Columbus, OH 43201', '06/07/1971', '313343232', 57);\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* DEPENDENT INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO DEPENDENTS (Fname, Lname, Dependent_SSN, Client_SSN, DOB) \n" +
                        "values ('Sandy', 'Smith', '123118462', '123111232', '11/18/1970');\n" +
                        "\n" +
                        "INSERT INTO DEPENDENTS (Fname, Lname, Dependent_SSN, Client_SSN, DOB) \n" +
                        "values ('Mark', 'Edison', '953118462', '123111232', '11/18/1980');\n" +
                        "\n" +
                        "INSERT INTO DEPENDENTS (Fname, Lname, Dependent_SSN, Client_SSN, DOB) \n" +
                        "values ('Halle', 'Berry', '531118462', '123343232', '11/14/1990');\n" +
                        "\n" +
                        "INSERT INTO DEPENDENTS (Fname, Lname, Dependent_SSN, Client_SSN, DOB) \n" +
                        "values ('Brad', 'Pitt', '531118952', '123343232', '11/11/1990');\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* LAWYER INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO LAWYERS (Fname, Lname, Primary_Email, Primary_Phone, Lawyer_ID, Insurance_Company_Name) \n" +
                        "values ('Dwane', 'Carter', 'carter@gmail.com', '9195349624', '863563', 'Humana');\n" +
                        "\n" +
                        "INSERT INTO LAWYERS (Fname, Lname, Primary_Email, Primary_Phone, Lawyer_ID, Insurance_Company_Name) \n" +
                        "values ('Aubrey', 'Graham', 'graham@gmail.com', '9193549624', '103563', 'Humana');\n" +
                        "\n" +
                        "INSERT INTO LAWYERS (Fname, Lname, Primary_Email, Primary_Phone, Lawyer_ID, Insurance_Company_Name) \n" +
                        "values ('Michael', 'Jordan', 'jordan@gmail.com', '8621249624', '962563', 'Humana');\n" +
                        "\n" +
                        "INSERT INTO LAWYERS (Fname, Lname, Primary_Email, Primary_Phone, Lawyer_ID, Insurance_Company_Name) \n" +
                        "values ('Nicki', 'Minaj', 'minaj@gmail.com', '4259219224', '123663', 'Humana');\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* ACCOUNTS INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS (Account_ID, Date_Created, Account_Age, client_ssn) \n" +
                        "values ('326781', '01/10/1994', '22', '123111232');\n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS (Account_ID, Date_Created, Account_Age, client_ssn) \n" +
                        "values ('221781', '01/10/1995', '21', '123343232');\n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS (Account_ID, Date_Created, Account_Age, client_ssn) \n" +
                        "values ('117815', '02/10/1995', '21', '313343232');\n" +
                        "\n" +
                        "/*\n" +
                        "* PAYMENT INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO PAYMENT (Transaction_ID,Payment_Method, Payment_Date, Payment_Amount, Paid_Off, Balance, Account_no) \n" +
                        "values ('64224', 'Credit', '03/11/2000', '100', 'y', '600', '326781');\n" +
                        "\n" +
                        "INSERT INTO PAYMENT (Transaction_ID,Payment_Method, Payment_Date, Payment_Amount, Paid_Off, Balance, Account_no) \n" +
                        "values ('64225', 'Credit', '03/11/2001', '200', 'y', '800', '326781');\n" +
                        "\n" +
                        "INSERT INTO PAYMENT (Transaction_ID,Payment_Method, Payment_Date, Payment_Amount, Paid_Off, Balance, Account_no) \n" +
                        "values ('64226', 'Credit', '03/11/2002', '300', 'y', '900', '221781');\n" +
                        "\n" +
                        "INSERT INTO PAYMENT (Transaction_ID,Payment_Method, Payment_Date, Payment_Amount, Paid_Off, Balance, Account_no) \n" +
                        "values ('64227', 'Credit', '03/11/2003', '400', 'y', '100', '117815');\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* HOME INSURANCE POLICY INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO Home_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('1', '1000', '5', 'y', '03/08/2014', '3', '326781');\n" +
                        "\n" +
                        "INSERT INTO Home_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('2', '5000', '10', 'y', '03/01/2010', '7', '326781');\n" +
                        "\n" +
                        "INSERT INTO Home_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('3', '6000', '10', 'y', '03/10/2015', '2', '221781')\n" +
                        "\n" +
                        "INSERT INTO Home_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('4', '3000', '5', 'y', '03/10/2013', '4', '117815')\n" +
                        "\n" +
                        "/*\n" +
                        "* CAR INSURANCE POLICY INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO Car_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('1', '2000', '5', 'y', '03/08/2014', '3', '117815');\n" +
                        "\n" +
                        "INSERT INTO Car_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber)  \n" +
                        "values ('2', '3000', '10', 'y', '03/01/2010', '7', '117815');\n" +
                        "\n" +
                        "INSERT INTO Car_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('3', '4000', '10', 'y', '03/10/2015', '2', '221781')\n" +
                        "\n" +
                        "INSERT INTO Car_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('4', '5000', '5', 'y', '03/10/2013', '4', '326781')\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* Boat INSURANCE POLICY INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO Boat_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('1', '8000', '5', 'y', '03/08/2014', '3', '221781');\n" +
                        "\n" +
                        "INSERT INTO Boat_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber)  \n" +
                        "values ('2', '7000', '10', 'y', '03/01/2010', '7', '326781');\n" +
                        "\n" +
                        "INSERT INTO Boat_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('3', '6000', '10', 'y', '03/10/2015', '2', '326781')\n" +
                        "\n" +
                        "INSERT INTO Boat_Insurance_Policy (Policy_Num,Term_Price, Term_Length, Active, startDate, policyAge, accountNumber) \n" +
                        "values ('4', '2000', '5', 'y', '03/10/2013', '4', '117815')\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* HOME INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO HOMES(House_ID, floors, occupants, year_built, house_age, total_cost, liability_deductible, liability_coverage, fire_deductible, fire_coverage, flood_deductible, flood_coverage, home_insurance_policy_num)\n" +
                        "values ('55', '2', '2', '1920', '97', '800000', '10000', '500000', '10000', '500000', '10000', '500000', '1')\n" +
                        "\n" +
                        "INSERT INTO HOMES(House_ID, floors, occupants, year_built, house_age, total_cost, liability_deductible, liability_coverage, fire_deductible, fire_coverage, flood_deductible, flood_coverage, home_insurance_policy_num)\n" +
                        "values ('53', '5', '4', '1960', '57', '300000', '4000', '60000', '3000', '70000', '2000', '20000', '2')\n" +
                        "\n" +
                        "INSERT INTO HOMES(House_ID, floors, occupants, year_built, house_age, total_cost, liability_deductible, liability_coverage, fire_deductible, fire_coverage, flood_deductible, flood_coverage, home_insurance_policy_num)\n" +
                        "values ('50', '5', '3', '1970', '47', '500000', '4000', '60000', '3000', '70000', '2000', '20000', '3')\n" +
                        "\n" +
                        "/*\n" +
                        "* ACCIDENT HISTORY INSERTS\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('400', 'Owner injured another passenger with her truck.', '12/17/2016')\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('402', 'Non injury, owner ran into a flag pole.', '12/18/2016')\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('401', 'Non injury, owner got into a fender bender.', '12/31/2016')\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('500', 'Crashed into a harbor.', '06/17/2016')\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('502', 'DUI boat collision', '07/18/2016')\n" +
                        "\n" +
                        "INSERT INTO ACCIDENT_HISTORY(Report_Number, Accident_Data, Accident_Date)\n" +
                        "values ('501', 'Non injury, owner got into a fender bender with another boat', '08/31/2016')\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* Vehicles\n" +
                        "*/\n" +
                        "INSERT INTO VEHICLES(Vin, Total_cost, vehicle_year, vehicle_make, vehicle_model, vehicle_doors, bodily_injury_deductible, bodily_injury_coverage, comprehensive_injury_deductible, comprehensive_injury_coverage, uninsured_motorist_deductible, uninsured_motorist_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, vehicles_policy_number)\n" +
                        "values('11111222223333344', '13000', '1994', 'Mazda', 'Mazda6', '4', '10000', '5000000', '600', '30000', '500', '50000', '300', '30000', '400', 'yes', 'yes', '500000', '2')\n" +
                        "\n" +
                        "INSERT INTO VEHICLES(Vin, Total_cost, vehicle_year, vehicle_make, vehicle_model, vehicle_doors, bodily_injury_deductible, bodily_injury_coverage, comprehensive_injury_deductible, comprehensive_injury_coverage, uninsured_motorist_deductible, uninsured_motorist_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, vehicles_policy_number)\n" +
                        "values('77711222223333344', '13000', '1996', 'Honda', 'Accord', '4', '14000', '1000000', '500', '30000', '501', '50000', '300', '30000', NULL, 'yes', 'yes', '500000', '1')\n" +
                        "\n" +
                        "INSERT INTO VEHICLES(Vin, Total_cost, vehicle_year, vehicle_make, vehicle_model, vehicle_doors, bodily_injury_deductible, bodily_injury_coverage, comprehensive_injury_deductible, comprehensive_injury_coverage, uninsured_motorist_deductible, uninsured_motorist_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, vehicles_policy_number)\n" +
                        "values('7771122222333DD44', '13000', '1998', 'Honda', 'Civic', '4', '13000', '2000000', '500', '30000', '502', '50000', '300', '30000', '400', 'yes', 'yes', '500000', '3')\n" +
                        "\n" +
                        "\n" +
                        "/*\n" +
                        "* Boats\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO BOATS(Hin, Total_cost, bodily_injury_deductible, bodily_injury_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, boat_insurance_policy_number)\n" +
                        "values('98734453HH22', '500000', '2000', '70000', '100', '300000', '500', 'no', 'no', '0', '2')\n" +
                        "\n" +
                        "INSERT INTO BOATS(Hin, Total_cost, bodily_injury_deductible, bodily_injury_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, boat_insurance_policy_number)\n" +
                        "values('987FFFF3HH22', '50000', '200', '7000', '100', '300000', '501', 'no', 'no', '0', '4')\n" +
                        "\n" +
                        "INSERT INTO BOATS(Hin, Total_cost, bodily_injury_deductible, bodily_injury_coverage, collison_injury_deductible, collison_injury_coverage, accident_report_number, at_fault, is_totaled, damage_cost, boat_insurance_policy_number)\n" +
                        "values('987QQ453HH22', '230000', '1000', '80000', '500', '300000', '502', 'no', 'no', '0', '3')\n" +
                        "\n" +
                        "/*\n" +
                        "* Accounts Sold\n" +
                        "*/ \n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS_SOLD(emp_ssn, account_id)\n" +
                        "values('234332243', '326781')\n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS_SOLD(emp_ssn, account_id)\n" +
                        "values('313343232', '221781')\n" +
                        "\n" +
                        "INSERT INTO ACCOUNTS_SOLD(emp_ssn, account_id)\n" +
                        "values('123343232', '326781')\n" +
                        "\n" +
                        "/*\n" +
                        "* Policies Written\n" +
                        "*/\n" +
                        "\n" +
                        "INSERT INTO Policies_Written(Lawyer_ID, Home_Ins_Policy_Num, Car_Ins_Policy_Num, Boat_Ins_Policy_Num)\n" +
                        "values('863563', NULL, NULL, '2')\n" +
                        "\n" +
                        "INSERT INTO Policies_Written(Lawyer_ID, Home_Ins_Policy_Num, Car_Ins_Policy_Num, Boat_Ins_Policy_Num)\n" +
                        "values('962563', NULL, '3', NULL)\n" +
                        "\n" +
                        "INSERT INTO Policies_Written(Lawyer_ID, Home_Ins_Policy_Num, Car_Ins_Policy_Num, Boat_Ins_Policy_Num)\n" +
                        "values('103563', '1', '1', NULL)\n" +
                        "\n" +
                        "/*\n" +
                        "* Companies_Brokers\n" +
                        "*/\n" +
                        "INSERT INTO Companies_Brokers(Ins_Company_Name, Broker_Company_Name)\n" +
                        "values('Humana', 'best brokers')\n" +
                        "\n" +
                        "INSERT INTO Companies_Brokers(Ins_Company_Name, Broker_Company_Name)\n" +
                        "values('BestInsurance', 'Sallys brokerage')\n" +
                        "\n" +
                        "INSERT INTO Companies_Brokers(Ins_Company_Name, Broker_Company_Name)\n" +
                        "values('RightInsurance', 'Tims brokerage')";
                break;
		}
		return returnString;
	}

}
