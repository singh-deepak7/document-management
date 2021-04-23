# Document Management
### Resourse version *1.0.0*

### Objective: Build a document management solution on the cloud

Create the solution leveraging multiple managed services from AWS and writing custom code to stitch the services together to create the business process.

### Scenario: Document management

Enterprise information is primarily captured in either structured or unstructured manner. Databases have been very efficient in handling structured data and allowing the business users the flexibility to access the raw data along with capabilities to glean tactical and strategic insights. However, unstructured data storage and management will have less than desirable
outcome if the system used is optimized for structured data. The objective is to create a process and a document management system that allows the enterprises to organize, catalog and fetch data in an intuitive manner.

### Solution:

![Architecture Diagram](https://github.com/deepak-aws/documentmanagement/blob/master/documents/images/arch.png)

A Lambda will be triggered whenever a PDF document is uploaded to the S3 bucket through Java App. Lambda function will start a text extraction processing job. Once the AWS Textract completes the job, it will send a notification to the AWS Simple Notification Service which will trigger another Lambda. The triggered Lambda from AWS SNS Service will get the text extraction job result from the payload and write the results to a JSON file in the S3 bucket with the same name as the PDF. The PUT Event will be triggered from this S3 bucket which will send Message to SQS. A Java Program will be running every 20 seconds will read the message from SQS and if any messages are found it will send it to Cloud Search Domain and will delete the message from SQS. The Java search app will connect to Cloud Search Domain and will retrieve search result.
