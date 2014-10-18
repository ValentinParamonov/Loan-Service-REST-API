Loan Service REST API
=====================

A project created for evaluation by 4finance HR department

### Business Requirements

* User can apply for loan by passing amount and term to api.
* Loan application risk analysis is performed. Risk is considered too high if:
    -   the attempt to take loan is made after 00:00 with max possible amount.
    -   reached max applications (e.g. 3) per day from a single IP.
* Loan is issued if there are no risks associated with the application. If so, client gets response status ok. However, if risk is surrounding the application, client error with message.
* Client should be able to extend a loan. Loan term gets extended for one week, interest gets increased by a factor of 1.5.
* The whole history of loans is visible for clients, including loan extensions.
    
### Services
#####Add User                                       
```
Url:                /users?userName=:name&password=:password       
Authentication:     none                                           
Method:             POST                                           
Params:             userName=[*] Required                          
                    password=[*] Required                          
Success Response:   Status 201 (Created)                          
Error Response:     Status 403 (Forbidden) if :name exists already
                    Content: { error : "User name taken" }
```
#####Apply For Loan
```
Url:                /loans?amount=:amount&term=:term                    
Authentication:     Basic                                               
Method:             POST                                                
Params:             amount=[decimal] Required                           
                    term=[integer] Required                             
Success Response:   Status 2O0 (OK)                                     
Error Response:     Status 400 (Bad Request) if :amount is unacceptable 
                    Content: { error : "Invalid amount" } 
                  
                    Status 400 (Bad Request) if :term is unacceptable  
                    Content: { error : "Invalid term" }
                  
                    tatus 403 (Forbidden) if application was rejected  
                    Content: { error : "Application rejected" }    
```    
   
#####Extend Loan Term
```
Url:                /loans/:applicationId
Authentication:     Basic
Method:             POST
Params:             applicationId=[integer] Required
Success Response:   Status 2O0 (OK)
Error Response:     Status 400 (Bad Request) if :applicationId is invalid
                    Content: { error : "Invalid application id" }
                        
                    Status 403 (Forbidden) if application was rejected
                    Content: { error : "Extension application rejected" }
```
#####Get Loan History
```
Url:                /loans
Authentication:     Basic
Method:             GET
Params:             none
Success Response:   Status 2O0 (OK)
                    Content: [{
                        applicationId : [integer],
                        eventDate : [integer],
                        eventStatus : [string]
                    }]
Error Response:     Status 500 (Internal Server Error)
```
