### Instructions to compile the program

####Prerequisites
- Jdk 8


#### How to run the code

As i have created a jar file of it run the application using the command

- java -jar BookSales.jar --books=/path/to/books.list --
sales=/path/to/sales.list --top_selling_books=3 --
top_customers=2 --sales_on_date=2018-02-01

where

--books=/path/to/books.list	(required)
--sales=/path/to/sales.list	(required)
--top_selling_books=<count>		(optional)	output	the	top	<count>	best	selling	books	in	terms	
of	sale	value
-- top_customers=<count>		(optional)	output	the	top	<count>	customers	in	terms	of	value	of	
purchases	they've	made.
--sales_on_date=<date>	(optional)	output	the	total	sales	amount	on	the	date