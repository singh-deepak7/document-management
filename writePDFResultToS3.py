import json
import boto3
import os

def getJobResults(jobId):

    pages = []
    # Extract Text using AWS Textract service
    textract = boto3.client('textract')
    response = textract.get_document_text_detection(JobId=jobId)
    
    pages.append(response)

    # Get request, use it with NextToken to get the next page of results
    nextToken = None
    if('NextToken' in response):
        nextToken = response['NextToken']

    while(nextToken):

        response = textract.get_document_text_detection(JobId=jobId, NextToken=nextToken)

        pages.append(response)
        nextToken = None
        if('NextToken' in response):
            nextToken = response['NextToken']

    return pages

def lambda_handler(event, context):
    # Get the SNS Notification JSON Object
    notificationMessage = json.loads(json.dumps(event))['Records'][0]['Sns']['Message']
    
    # read Data from SND JOSN Object
    pdfTextExtractionStatus = json.loads(notificationMessage)['Status']
    pdfTextExtractionJobTag = json.loads(notificationMessage)['JobTag']
    pdfTextExtractionJobId = json.loads(notificationMessage)['JobId']
    pdfTextExtractionDocumentLocation = json.loads(notificationMessage)['DocumentLocation']
    
    pdfTextExtractionS3ObjectName = json.loads(json.dumps(pdfTextExtractionDocumentLocation))['S3ObjectName']
    pdfTextExtractionS3Bucket = json.loads(json.dumps(pdfTextExtractionDocumentLocation))['S3Bucket']
    
    idName = os.path.splitext(pdfTextExtractionS3ObjectName)[0].split("-")[0]
    
    print(pdfTextExtractionJobTag + ' : ' + pdfTextExtractionStatus)
    
    pdfText = ''
    # Preparing JSON for Cloud search upload.
    pdfText += '[{ 	"type": "add", 	"id": '+ idName +', 	"fields": {	'
    
    # If the job status is “SUCCEEDED” and retrieve the job result using the “JobId”, process the result into a JSON file with the same name as the PDF file and write to the S3 bucket.
    if(pdfTextExtractionStatus == 'SUCCEEDED'):
        response = getJobResults(pdfTextExtractionJobId)
        
        for resultPage in response:
            for item in resultPage["Blocks"]:
            # read each line. 
                if item["BlockType"] == "LINE":
                # append text from each line
                    pdfText += item["Text"] + '\n'
        
        pdfText += '	} }]'          
        s3 = boto3.client('s3')
        
        # get JSON file name, same as PDF 
        outputTextFileName = os.path.splitext(pdfTextExtractionS3ObjectName)[0] + '.json'
        # upload to S3 bucket and make it public 
        s3.put_object(Body=pdfText, Bucket="<UPLOAD_BUCKET_NAME>", Key=outputTextFileName , ACL='public-read')