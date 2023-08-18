cms-service
=======================

Application Overview:

cms-service is a service layer created for front-end, CMS/AEM (primarily) sites to call back-end services and API's.


[How to setup locally](web-apps-advesignportal-zip\src\main\resources\env\mydomain\cfg\local-setup.md)
**Note:** Local setup instructions are for web-apps-advesignportal.  General instructions remain the same.  A specific instruction page for cms-service should be created in the future.

Please note that deployments to DEV, SIT and further environments of web-apps-cms-service are to be done via CDD.

**Jenkins** : https://jenkins-web.sunlifecorp.com/job/Web/job/ca.sunlife.web.apps/job/web-apps-cms-service/
    [web-apps-cms-service](https://jenkins-web.sunlifecorp.com/job/Web/job/ca.sunlife.web.apps/job/web-apps-cms-service/)

Log Path and Server Details :
------------------------------------------
All servers on port 2180 and port 2181

**DEV Server** :

  - Log Path    :  /logs/waslb/dev/ds/cmsapps/cmsapiservices11/cmsapiservice
  - Server      :  CL0A3127.sunlifecorp.com

**SIT Server** :

  - Log Path    :  /logs/waslb/sit/ds/cmsapps/cmsapiservices11/cmsapiservice
  - Server      :  CL0A3128.sunlifecorp.com
  
**Stage Server** :

  - Log Path    :  /logs/waslb/stage/ds/cmsapps/cmsapiservices11/cmsapiservice
  - Server      :  CL0A3503.sunlifecorp.com, C0A3504.sunlifecorp.com
  
**Prod Server** :

  - Log Path    :  /logs/waslb/prod/ds/cmsapps/cmsapiservices11/cmsapiservice
  - Server      :  CL0A3505.sunlifecorp.com, CL0A3506.sunlifecorp.com

**DR Server** :

  - Log Path    :  /logs/waslb/prod/ds/cmsapps/cmsapiservices11/cmsapiservice
  - Server      :  CL0A3507.sunlifecorp.com, CL0A3508.sunlifecorp.com  
     
Validation Steps :
------------------

Local Setup:

Try the below on your local browser.  Should receive a JSON response that says something like: "GET method not supported".
Note: Your port number may differ.
    http://localhost:8080/web-apps-cms-service/cms-service/submit

Try the below URL on your local browser. Should receive a JSON response that says something like: "GET method not supported".

**DEV and SIT:**
    Direct:
    http://cl0a3127.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit
    http://cl0a3128.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit

    VIP:
    https://dev-cmsapicservices.sunlifecorp.com/webappcms-microservice/cms-service/submit
    https://sit-cmsapicservices.sunlifecorp.com/webappcms-microservice/cms-service/submit

    Service and live domains (not available in DEV):
    https://sit-services.sunlife.com/services/submit
    https://sit-www.prosprsunlife.ca/services/submit

**STAGE**
    Direct (only via Postman or similar):
    http://cl0a3503.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit
    http://cl0a3504.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit

    VIP (only via Postman or similar):
    https://stage-cmsapicservices.sunlifecorp.com/webappcms-microservice/cms-service/submit

    Service and live domains:
    https://stage-services.sunlife.com/cmsservices/submit
    https://stage-www.prosprsunlife.ca/cmsservices/submit

**PROD and DR**
    Direct (only via Postman or similar):
    http://cl0a3505.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit
    http://cl0a3506.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit
    http://cl0a3507.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit
    http://cl0a3508.sunlifecorp.com:2180/webappcms-microservice/cms-service/submit


    VIP (only via Postman or similar):
    https://cmsapicservices.sunlifecorp.com/webappcms-microservice/cms-service/submit

    Service and live domains:
    https://services.sunlife.com/cmsservices/submit
    https://www.prosprsunlife.ca/cmsservices/submit

