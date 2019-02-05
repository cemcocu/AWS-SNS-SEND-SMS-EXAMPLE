package com.acc.awssns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    private Environment environment;

    public int sendSMS() {

           //AWS Account Credentials

                BasicAWSCredentials credentials =
                new BasicAWSCredentials(environment.getProperty("accessKey"),environment.getProperty("secretKey") );

             //Create a SNS client with the region

        AmazonSNS snsClient = AmazonSNSClient.builder()
                .withRegion(Regions.US_WEST_2)
                .withCredentials((new AWSStaticCredentialsProvider(credentials)))
                .build();


        // topic arn is necessary to send an sms
        String topicArn = "arn:aws:sns:us-west-2:737065854791:tpc";

         //notification sending request with ARN,Sending type,Country Code + GSM number
        SubscribeRequest subRequest = new SubscribeRequest(topicArn, "SMS","country code"+"your gsm number here");

        snsClient.subscribe(subRequest);

           //Content of the SMS
        String msg = "I am coming via AWS SNS";

                  //Send the SMS
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);

        PublishResult publishResult = snsClient.publish(publishRequest);

        int httpStatusCode = publishResult.getSdkHttpMetadata().getHttpStatusCode();

     return httpStatusCode;

    }
}
