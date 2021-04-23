/**
 * 
 * This program runs every 20 seconds to check if there is any message in AWS SQS
 * if any message is present it will push that to AWS Cloud Search 
 * and will delete the message from Queue.
 * 
 */
package com.amazonaws.samples;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClientBuilder;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsRequest;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

/**
 * 
 * Execute as $java -jar DocQueueManagement.jar
 * @param args
 *
 */
public class DocQueueManagement extends TimerTask {

	private static final String S3_BUCKET_URL = "https://emp-json-upload-bucket.s3-us-west-2.amazonaws.com/";
	private static final String QUEUE_ENDPOINT = "https://sqs.us-west-2.amazonaws.com/558855155213/emp-doc-upload-queue";
	public static final String ENDPOINT = "doc-employee-dept-search-cidlown7dcshwu2sro6c77ztyy.us-west-2.cloudsearch.amazonaws.com";

	public static void main(String[] args) {
		// initialize the Timer object and schedule it to run every 20 seconds
		Timer timer = new Timer();
		timer.schedule(new DocQueueManagement(), 0, 20000);
	}

	@Override
	public void run() {
		// this will be called every 20 seconds.
		recieveMessageFromQueue();

	}

	private void recieveMessageFromQueue() {
		// Connect to queue
		AmazonSQS sqs = getQueueConnection();
		System.out.println("Receiving messages from " + QUEUE_ENDPOINT);

		// Receive messages.
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QUEUE_ENDPOINT);
		final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		System.out.println("message size: " + messages.size());

		// if there are any messages it will parse the JSON object to get the file name
		if (messages.size() > 0) {
			for (final Message message : messages) {
				JSONObject obj = new JSONObject(message.getBody());
				JSONArray arr = obj.getJSONArray("Records");
				for (int i = 0; i < arr.length(); i++) {
					System.out.println(arr.getJSONObject(i).getString("awsRegion"));
					JSONObject obj1 = new JSONObject(arr.getJSONObject(i).get("s3").toString());
					String fileName = obj1.getJSONObject("object").getString("key");
					System.out.println("Json fileName= " + fileName);

					try {
						// Call method to send data to CLoud Search, will pass the JSON file name as
						// input parameters
						sendDataToCloudSearchDomain(fileName);

						// delete the message from queue.
						deleteMessage(sqs, messages);
					} catch (IOException e) {
						System.out.println("Error in uploading JSON to Cloud Search!");
						e.printStackTrace();
					}

				}
			}
		}

	}

	private void deleteMessage(AmazonSQS sqs, final List<Message> messages) {
		// Delete the message.
		System.out.println("Deleting a message.");
		final String messageReceiptHandle = messages.get(0).getReceiptHandle();
		sqs.deleteMessage(new DeleteMessageRequest(QUEUE_ENDPOINT, messageReceiptHandle));
	}

	private void sendDataToCloudSearchDomain(String fileName) throws IOException {
		// Get Cloud Search connection
		AmazonCloudSearchDomain domain = getCloudSearchDomainConnection();
		// Frame the S3 Bucket URL using the JSON file name received as input.
		URL url = new URL(S3_BUCKET_URL + fileName);
		URLConnection connection = url.openConnection();
		connection.connect();
		// get the file size
		long file_size = connection.getContentLength();
		// Read the JSON file as InputStream
		InputStream docAsStream = connection.getInputStream();
		// Upload the InputStream to Cloud Search
		UploadDocumentsRequest req = new UploadDocumentsRequest();
		// Its Mandatory to set Content Length else upload will fail.
		req.setContentLength(file_size);
		req.setContentType("application/json");
		req.setDocuments(docAsStream);
		UploadDocumentsResult result = domain.uploadDocuments(req);
		System.out.println(result.toString());
		// Close the Connection
		docAsStream.close();

	}

	private AmazonCloudSearchDomain getCloudSearchDomainConnection() {
		// Cloud Search Endpoint Configuration by providing service endpoint and signing
		// region
		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT, "us-west-2");
		// Establish Connection
		AmazonCloudSearchDomain domain = AmazonCloudSearchDomainClientBuilder.standard()
				.withCredentials(new InstanceProfileCredentialsProvider(false))
				.withEndpointConfiguration(endpointConfiguration).build();

		return domain;
	}

	private AmazonSQS getQueueConnection() {
		// Establish Connection with AWS SQS
		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(new InstanceProfileCredentialsProvider(false))
				.withRegion(Regions.US_WEST_2).build();
		return sqs;
	}

}
