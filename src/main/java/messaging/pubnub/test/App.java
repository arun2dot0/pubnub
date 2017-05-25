package messaging.pubnub.test;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
	    PNConfiguration pnConfiguration = new PNConfiguration();
	    pnConfiguration.setSubscribeKey("sub-c-5c3f5f40-4190-11e7-86e2-02ee2ddab7fe");
	    pnConfiguration.setPublishKey("pub-c-5100138b-1286-477b-836e-55f23e3b0f8c");
	     
	    PubNub pubnub = new PubNub(pnConfiguration);
	 
	    

	    
	    pubnub.addListener(new SubscribeCallback() {
	        @Override
	        public void status(PubNub pubnub, PNStatus status) {
	            if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
	                // This event happens when radio / connectivity is lost
	            }
	 
	            else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
	 
	                // Connect event. You can do stuff like publish, and know you'll get it.
	                // Or just use the connected event to confirm you are subscribed for
	                // UI / internal notifications, etc
	             
	                if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
	                    sendLotOfMessages();
	                }
	            }
	            else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
	 
	                // Happens as part of our regular operation. This event happens when
	                // radio / connectivity is lost, then regained.
	            }
	            else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
	 
	                // Handle messsage decryption error. Probably client configured to
	                // encrypt messages and on live data feed it received plain text.
	            }
	        }
	 
	        private void sendLotOfMessages() {
	        	
	        	IntStream.range(1, 11).forEach(e->{
	        		pubnub.publish().channel("helloChannel").message("hello times "+e+"!!").async(new PNCallback<PNPublishResult>() {
	                    @Override
	                    public void onResponse(PNPublishResult result, PNStatus status) {
	                        // Check whether request successfully completed or not.
	                        if (!status.isError()) {
	                        	System.out.println("Message Sent");
	                            // Message successfully published to specified channel.
	                        }
	                        // Request processing failed.
	                        else {
	                        	System.out.println("Message Error !!");
	                            // Handle message publish error. Check 'category' property to find out possible issue
	                            // because of which request did fail.
	                            //
	                            // Request can be resent using: [status retry];
	                        }
	                    }
	                });
	        	});
	        	
				
			}

			@Override
	        public void message(PubNub pubnub, PNMessageResult message) {
	            // Handle new message stored in message.message
	            if (message.getChannel() != null) {
	                // Message has been received on channel group stored in
	                System.out.print("Message from Channel "+ message.getChannel());
	            }
	            else {
	                // Message has been received on channel stored in
	                // message.getSubscription()
	            }
	            System.out.print(" Message "+ message.getMessage());
	            System.out.println();
	            /*
	                log the following items with your favorite logger
	                    - message.getMessage()
	                    - message.getSubscription()
	                    - message.getTimetoken()
	            */
	        }

			@Override
			public void presence(PubNub arg0, PNPresenceEventResult arg1) {
				// TODO Auto-generated method stub
				
			}
	 
	       
	    });
	 
	    pubnub.subscribe().channels(Arrays.asList("helloChannel")).execute();
	}
}
