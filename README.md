## For CSV -> commons-csv dependency

    Upload CSV file:
    
    curl --location --request POST 'localhost:8080/api/employee/upload' \
    --header 'Content-Type: application/csv' \
    --form 'file=@"/C:/Users/abcd/Desktop/book.csv"'
    --form 'type="csv"'

    
    Get all employees data:
    
    curl --location --request GET 'localhost:8080/api/employee' \
    --header 'Content-Type: application/json' \
    
    
    Download CSV file:

    curl --location --request GET 'localhost:8080/api/employee/download/csv' \
    --header 'Content-Type: application/csv' \
    
    
    
    
## For Excel -> poi dependency


    Upload Excel:
    
    curl --location --request POST 'localhost:8080/api/employee/upload/excel' \
    --header 'Content-Type:  application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml' \
    --form 'file=@"/C:/Users/abcd/Desktop/book1.xlsx"' \
    --form 'type="excel"'
    
    
    Download Excel File: if you use postman then response will be encoded , then you need to save the file to see the actual data
    
    curl --location --request GET 'localhost:8080/api/employee/download/excel' \
    --header 'Content-Type: application/vnd.ms-excel' \
    
    
    
- Check resource folder for Excel and CSV file to upload